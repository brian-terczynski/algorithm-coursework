import java.util.Arrays;

/**
 * Checks which points are collinear using the "fast" algorithm that
 * sorts points according to their slope with an origin point.
 *
 * @author Brian Terczynski
 */
public class Fast {
    /**
     * The maximum value a point can have in either the x or y direction.
     */
    private static final int MAX_COORDINATE = 32767;

    /**
     * Number of additional points needed to an origin point to consider
     * the points collinear.
     */
    private static final int COLLINEARITY_FACTOR = 3;

    /**
     * The main method; takes an input file that has the number of
     * points, followed by the list of points to check for collinearity.
     *
     * @param args
     *     The name of the input file.
     */
    public static void main(String[] args) {
        String file = args[0];
        In in = new In(file);
        int numPoints = in.readInt();
        Point [] points = new Point [numPoints];
        int i = 0;
        while (!in.isEmpty()) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
            i++;
        }
        StdDraw.setXscale(0, MAX_COORDINATE);
        StdDraw.setYscale(0, MAX_COORDINATE);
        for (i = 0; i < points.length; i++) {
            points[i].draw();
        }

        for (i = 0; i < points.length; i++) {
            Arrays.sort(points, i + 1, points.length, points[i].SLOPE_ORDER);
            double curSlope = 0.0;
            int startIndex = -1;
            int endIndex = -1;
            for (int j = i + 1; j < points.length; j++) {
                if (startIndex == -1 && endIndex == -1) {
                    curSlope = points[i].slopeTo(points[j]);
                    startIndex = j;
                    endIndex = j;
                } else {
                    if (points[i].slopeTo(points[j]) != curSlope) {
                        checkForCollinearity(points, i, startIndex, endIndex);
                        startIndex = j;
                        endIndex = j;
                        curSlope = points[i].slopeTo(points[j]);
                    } else {
                        endIndex++;
                    }
                }
            }
            checkForCollinearity(points, i, startIndex, endIndex);
        }
    }

    /**
     * Checks whether the points between startIndex and endIndex
     * form a line segment of 4 or more points, and if so, plots the
     * line and prints the line to stdout.
     *
     * @param points
     *     The points array.
     * @param refIndex
     *     The index to the reference point.
     * @param startIndex
     *     The start index.
     * @param endIndex
     *     The end index.
     */
    private static void checkForCollinearity(final Point[] points,
            final int refIndex, final int startIndex, final int endIndex) {
        if (endIndex - startIndex + 1 >= COLLINEARITY_FACTOR) {
            Point [] lineArray = new Point [endIndex - startIndex + 2];
            for (int k = 0; k < lineArray.length - 1; k++) {
                lineArray[k] = points[k + startIndex];
            }
            lineArray[lineArray.length - 1] = points[refIndex];
            Arrays.sort(lineArray);
            lineArray[0].drawTo(lineArray[lineArray.length - 1]);
            for (int k = 0; k < lineArray.length; k++) {
                if (k > 0) {
                    StdOut.print(" -> ");
                }
                StdOut.print(lineArray[k]);
            }
            StdOut.println();
        }
    }

}
