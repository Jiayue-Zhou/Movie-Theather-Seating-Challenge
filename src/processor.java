import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// -------------------------------------------------------------------------
/**
 *  For movie theater seating allocation
 *  This is the entry for the program.
 *
 *  @author Jiayue Zhou
 *  @version 2022-2-20
 */
public class processor
{

    // ----------------------------------------------------------
    /**
     * Create a new processor object.
     */
    public processor()
    {

    }
    /**
     * Main function, accepting file name as args
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        // the file object
        File file = null;

        // Attempts to open the file and scan through it
        try {
            // takes the first command line argument and opens that file
            file = new File(args[0]);

            // creates a scanner object
            Scanner scanner = new Scanner(file);

            // creates a command processor object
            MovieTheaterSeating seating = new MovieTheaterSeating();
            // reads the entire file and processes the commands
            // line by line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // determines if the file has more lines to read
                if (!line.trim().isEmpty()) {
                    seating.allocate(line.trim());
                }
            }
            // closes the scanner
            scanner.close();
            System.out.println("output.txt has been created in");
            System.out.println(System.getProperty("user.dir"));
        }
        // catches the exception if the file cannot be found
        // and outputs the correct information to the console
        catch (FileNotFoundException e) {
            System.out.println("Invalid file");
            e.printStackTrace();
        }
    }
}
