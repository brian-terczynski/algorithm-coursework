import java.util.Arrays;

/**
 * Checks which points are collinear using a "brute force" algorithm.
 *
 * @author Brian Terczynski
 */
public class Brute {
    /**
     * The maximum value a point can have in either the x or y direction.
     */
    private static final int MAX_COORDINATE = 32767;

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
        Arrays.sort(points);
        StdDraw.setXscale(0, MAX_COORDINATE);
        StdDraw.setYscale(0, MAX_COORDINATE);
        for (i = 0; i < points.length; i++) {
            points[i].draw();
        }

        for (i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double slope1 = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length; k++) {
                    double slope2 = points[i].slopeTo(points[k]);
                    if (slope1 == slope2) {
                        for (int l = k + 1; l < points.length; l++) {
                            double slope3 = points[i].slopeTo(points[l]);
                            if (slope1 == slope3) {
                                points[i].drawTo(points[l]);
                                StdOut.println(points[i] + " -> "
                                + points[j]
                                        + " -> "
                                + points[k]
                                        + " -> "
                                + points[l]);
                            }
                        }
                    }
                }
            }
        }
    }

}
