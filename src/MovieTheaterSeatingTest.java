import java.io.IOException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author zjy
 *  @version 2022-2-20
 */
public class MovieTheaterSeatingTest
{
    // ----------------------------------------------------------
    /**
     * Create a new MovieTheaterSeatingTest object.
     */
    private MovieTheaterSeating seating;

    @Before
    public void setUp() throws IOException {
        this.seating = new MovieTheaterSeating();
    }
    @Test
    public void testNormal1() throws IOException {
        Assert.assertEquals(seating.allocate("R001 2"), "R001 F1,F2\r\n");
        Assert.assertEquals(seating.allocate("R002 4"), "R002 F6,F7,F8,F9\r\n");
        Assert.assertEquals(seating.allocate("R003 4"), "R003 F13,F14,F15,F16\r\n");
        Assert.assertEquals(seating.allocate("R004 3"), "R004 G3,G4,G5\r\n");
    }
    @Test
    public void testBig() throws IOException {
        Assert.assertEquals(seating.allocate("R001 201"),
            "R001 Sorry we don't have enough available seats left.\r\n");
    }
    @Test
    public void testInvalidInput() throws IOException {
        Assert.assertEquals(seating.allocate("R001 -1"),
            "R001 Please enter a valid number.\r\n");
        Assert.assertEquals(seating.allocate("R002 0"),
            "R002 Please enter a valid number.\r\n");
    }
    @Test
    public void testSafeDistance() throws IOException {
        Assert.assertEquals(seating.allocate("R001 21"), "R001 F1,F2,F3,F4,F5,F6"
            + ",F7,F8,F9,F10,F11,F12,F13,F14,F15,F16,F17,F18,F19,F20,G1\r\n");
        Assert.assertEquals(seating.allocate("R002 1"), "R002 H2\r\n");
        Assert.assertEquals(seating.allocate("R003 4"), "R003 H6,H7,H8,H9\r\n");
    }
    public void testCenter() throws IOException {
        Assert.assertEquals(seating.allocate("R001 11"),
            "F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,F11\r\n");
        Assert.assertEquals(seating.allocate("R002 11"),
            "G12,G13,G14,G15,G16,G17,G18,G19,G20,E12,E13\r\n");
        Assert.assertEquals(seating.allocate("R003 11"),
            "E17,E18,E19,E20,H1,H2,H3,H4,H5,H6,H7\r\n");
    }
}
