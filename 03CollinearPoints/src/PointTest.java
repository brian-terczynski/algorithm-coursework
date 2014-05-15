import static org.junit.Assert.*;

import org.junit.Test;


public class PointTest {
    private static final double DOUBLE_COMPARE_DELTA = 0.000001;

    @Test
    public void testSlope() {
        // Slope of 1.
        Point p1 = new Point (1, 1);
        Point p2 = new Point (3, 3);
        assertEquals (1.0, p1.slopeTo(p2), DOUBLE_COMPARE_DELTA);
        
        // Slope of 1.
        Point p3 = new Point (7, 5);
        Point p4 = new Point (4, 2);
        assertEquals (1.0, p3.slopeTo(p4), DOUBLE_COMPARE_DELTA);

        // Slope of zero.
        Point p5 = new Point (5, 6);
        Point p6 = new Point (10, 6);
        assertTrue (p5.slopeTo(p6) == 0.0);
        assertTrue (p6.slopeTo(p5) == -0.0);

        // Positive infinity.
        Point p7 = new Point (1, 1);
        Point p8 = new Point (1, 3);
        assertEquals (Double.POSITIVE_INFINITY, p7.slopeTo(p8), DOUBLE_COMPARE_DELTA);
        assertEquals (Double.POSITIVE_INFINITY, p8.slopeTo(p7), DOUBLE_COMPARE_DELTA);

        // Negative infinity.
        Point p9 = new Point (-12, 3);
        Point p10 = new Point (-12, 3);
        assertEquals (Double.NEGATIVE_INFINITY, p9.slopeTo(p10), DOUBLE_COMPARE_DELTA);
        assertEquals (Double.NEGATIVE_INFINITY, p10.slopeTo(p9), DOUBLE_COMPARE_DELTA);
        
        // Slope of -1.
        Point p11 = new Point (1, 1);
        Point p12 = new Point (3, -1);
        assertEquals (-1.0, p11.slopeTo(p12), DOUBLE_COMPARE_DELTA);
        
        try {
            p11.slopeTo(null);
            fail ("No exception thrown.");
        } catch (NullPointerException e) {
            
        }
    }

    @Test
    public void testCompareTo() {
        // Slope of 1.
        Point p1 = new Point (1, 1);
        Point p2 = new Point (3, 3);
        assertTrue(p1.compareTo(p2) < 0);
        assertTrue(p2.compareTo(p1) > 0);
        assertTrue(p1.compareTo(p1) == 0);
        try {
            p1.compareTo(null);
            fail ("No exception thrown.");
        } catch (NullPointerException e) {
            
        }
    }

}
