import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;

// -------------------------------------------------------------------------
/**
 *
 *  Movie theater seating allocation
 *
 *  @author Jiayue Zhou
 *  @version 2022-2-20
 */
public class MovieTheaterSeating
{
    private static final int ROW = 10;
    private static final int COL = 20;
    //0 means seats available, 1 means seats not available.
    HashMap<Integer, Character> map = new HashMap<Integer, Character>();
    private int [][]seats = new int[ROW][COL];
    //All available seats number
    private int allAvaSeatsNum = ROW * COL;
    private int [] rowAvaSeats = new int[ROW];
    //Begin from center row
    private int beginRow = ROW / 2;
    RandomAccessFile randomFile = new RandomAccessFile("output.txt", "rw");
    // ----------------------------------------------------------
    /**
     * Create a new MovieTheaterSeating object.
     * @throws IOException
     */
    public MovieTheaterSeating() throws IOException
    {
        initSeats();
        randomFile.setLength(0);
    }
    // ----------------------------------------------------------
    /**
     * Initilize the seats and map
     */
    private void initSeats()
    {
        for (int i = 0; i < ROW; ++i) {
            rowAvaSeats[i] = COL;
            for (int j = 0; j < COL; ++j) {
                seats[i][j] = 0;
            }
        }
        for (int i = 0; i < ROW; ++i) {
            char c = (char)(65 + i);
            map.put(i, c);
        }
    }
    /**
     * Allocate function, it processes one line each time.
     * @param trim one line command
     * @throws IOException
     */
    public String allocate(String trim) throws IOException
    {
        String[] temp = trim.split(" ");
        int requireNum = Integer.parseInt(temp[1]);
        if (requireNum <= 0) {
            randomFile.seek(randomFile.length());
            String ans = temp[0]+ " Please enter a valid number.\r\n";
            randomFile.writeBytes(ans);
            return ans;
        }
        //Find a available space for a specific number
        ArrayList<Integer> arr1 = findAvailableRows(requireNum);
        //arr1 get the row we need allocate for this iteration
        /* If size of arr1 is zero,
         * we do not have enough seats for the requirement.
         * */
        if (arr1.size() == 0) {
            randomFile.seek(randomFile.length());
            String ans = temp[0] +
                " Sorry we don't have enough available seats left.\r\n";
            randomFile.writeBytes(ans);
            return ans;
        }
        /* If size of arr1 is one, we only need one row
         * to allocate the requirement.
         * */
        if (arr1.size() == 1) {
            int curRow = arr1.get(0);
            ArrayList<Integer> arr2 = allocateForRow(curRow, requireNum);
            String ans = writeOutput(temp[0], arr2);
            return ans;
        }
        /* If size of arr1 is more than one, we need mutiply rows
         * to allocate this requirement
         * */
        ArrayList<Integer> arr2 = separateAllocate(arr1, requireNum);
        String ans = writeOutput(temp[0], arr2);
        return ans;
    }

    // ----------------------------------------------------------
    /**
     * WriteOutput is for writing the output file.
     * @param arr2 the seats reservation result
     * @throws IOException
     */
    private String writeOutput(String id, ArrayList<Integer> arr2)
        throws IOException
    {
        String ans = "";
        try {
        String assignedSeats = "";
        for (int i = 0; i < arr2.size(); i += 2) {
            char rowLetter = map.get(arr2.get(i));
            int colNum = arr2.get(i + 1);
            assignedSeats = assignedSeats + "," +
                rowLetter + String.valueOf(colNum + 1);
        }
        randomFile.seek(randomFile.length());
        ans = id + " " + assignedSeats.substring(1)+"\r\n";
        randomFile.writeBytes(ans);
        return ans;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return ans;
    }

    // ----------------------------------------------------------
    /**
     * SeparateAllocate is for processing mutiply rows.
     * @param arr
     * @param requireNum
     * @return
     */
    private ArrayList<Integer> separateAllocate(ArrayList<Integer> arr,
        int requireNum)
    {
        ArrayList<Integer> arr2 = new ArrayList<Integer>();
        for (int i = 0; i < arr.size(); ++i) {
            int curRow = arr.get(i);
            for (int j = 0; j < COL; ++j) {
                if (requireNum <= 0) {
                    break;
                }
                if (seats[curRow][j] == 0) {
                    requireNum--;
                    arr2.add(curRow);
                    arr2.add(j);
                }
            }
        }
        eliminateUnsafeSeats(arr2);
        return arr2;
    }
    // ----------------------------------------------------------
    /**
     * AllocateForRow is for processing only one row.
     * @param curRow current row
     * @param requireNum requirement seats on current row
     */
    private ArrayList<Integer> allocateForRow(int curRow, int requireNum)
    {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        for (int i = 0; i < COL; ++i) {
            //Available
            if (requireNum <= 0) {
                break;
            }
            if (seats[curRow][i] == 0) {
                requireNum--;
                arr.add(curRow);
                arr.add(i);
            }
        }
        eliminateUnsafeSeats(arr);
        return arr;
    }
    // ----------------------------------------------------------
    /**
     * eliminateUnsafeSeats is for eliminating all the unavailable seats
     * from the available seats array.
     * @param arr arr is the array of seats requirement waiting processing
     */
    private void eliminateUnsafeSeats(ArrayList<Integer> arr)
    {
        //System.out.println("#");
        for (int i = 0; i < arr.size(); i += 2) {
            //System.out.println("#");
            int curRow = arr.get(i);
            int col = arr.get(i + 1);
            seats[curRow][col] = 1;
            rowAvaSeats[curRow]--;
            for (int j = 1; j <= 3; ++j) {
                if (col + j < COL && seats[curRow][col + j] == 0) {
                    seats[curRow][col + j] = 1;
                    rowAvaSeats[curRow]--;
                }
            }
            for (int j = 1; j <= 3; ++j) {
                if (col - j >= 0 && seats[curRow][col - j] == 0) {
                    seats[curRow][col - j] = 1;
                    rowAvaSeats[curRow]--;
                }
            }
            if (curRow + 1 < ROW && seats[curRow + 1][col] == 0) {
                seats[curRow + 1][col] = 1;
                rowAvaSeats[curRow + 1]--;
            }
            if (curRow - 1 >= 0 && seats[curRow - 1][col] == 0) {
                seats[curRow - 1][col] = 1;
                rowAvaSeats[curRow - 1]--;
            }
        }
    }

    // ----------------------------------------------------------
    /**
     * find available rows for a requirement
     * @param requireNum requirement number
     */
    private ArrayList<Integer> findAvailableRows(int requireNum)
    {
        ArrayList<Integer> arr = new ArrayList<Integer>();
        int flag;
        for (int i = 0; i < ROW / 2 + 1; ++i) {
            flag = 1;
            for (int j = 0; j < 2; ++j) {
                if (j == 1) {
                    if (i == 0) {
                        continue;
                    }
                    flag = -1;
                }
                int curRow = beginRow + flag * i;
                if (curRow < 0 || curRow >= ROW) {
                    continue;
                }
                if (rowAvaSeats[curRow] >= requireNum) {
                    arr.add(curRow);
                    return arr;
                }
                else if (rowAvaSeats[curRow] >= 0) {
                    requireNum = requireNum - rowAvaSeats[curRow];
                    arr.add(curRow);
                }
            }
        }
        arr = new ArrayList<Integer>();
        return arr;
    }
}
