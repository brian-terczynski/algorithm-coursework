/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

/**
 * Represents a point in a plane.
 *
 * @author Brian Terczynski
 */
public class Point implements Comparable<Point> {
    /**
     * Class used to compare two points by slope with "this" third
     * point.
     */
    private class SlopeComparator implements Comparator<Point> {
        @Override
        public int compare(Point point1, Point point2) {
            double slope1 = Point.this.slopeTo(point1);
            double slope2 = Point.this.slopeTo(point2);
            if (slope1 == slope2) {
                return 0;
            } else if (slope1 < slope2) {
                return -1;
            } else {
                return 1;
            }
        }
    }

    /**
     * Compares points by slope.
     */
    public final Comparator<Point> SLOPE_ORDER = new SlopeComparator();

    /**
     * The x coordinate.
     */
    private final int x;

    /**
     * The y coordinate.
     */
    private final int y;

    /**
     * Creates the point (x, y).
     *
     * @param x
     *     The x coordinate.
     *
     * @param y
     *     The y coordinate.
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Plots this point on StdDraw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws a line between this point and that point on StdDraw.
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and that point.  A horizontal
     * line segment has a slope of zero.  A veritcal line segment (either
     * up or down) has a slope of positive infinity.  The slope of a line
     * segment with the same start and end point (slope of a point to itself)
     * is negative infinity.
     *
     * @param that
     *     The other point.
     *
     * @return
     *     The slope.
     */
    public double slopeTo(Point that) {
        if (that.x == this.x) {
            if (that.y == this.y) {
                return Double.NEGATIVE_INFINITY;
            } else {
                return Double.POSITIVE_INFINITY;
            }
        } else if (that.y == this.y) {
            return +0.0;
        }
        return (double) (that.y - this.y) / (double) (that.x - this.x);
    }

    /**
     * Compare points by y-coordinates and break ties by x-coordinates.
     */
    public int compareTo(Point that) {
        if (that.y == this.y) {
            return this.x - that.x;
        } else {
            return this.y - that.y;
        }
    }

    /**
     * Returns the string representation of this point.
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit test main method; I'm actually going to use JUnit classes to
     * do the unit testing, so this method is unimplemented.
     *
     * @param args
     *     Not implemented.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}
