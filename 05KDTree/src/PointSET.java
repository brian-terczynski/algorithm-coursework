import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents a set of Points on a plane.
 *
 * @author Brian Terczynski
 */
public class PointSET {
    /**
     * Pen radius for plotting points.
     */
    private static final double PEN_RADIUS = 0.01;
    /**
     * Represents the set of points.
     */
    private Set<Point2D> pointSet = new TreeSet<Point2D>();

    /**
     * Constructs an empty set of points.
     */
    public PointSET() {
    }

    /**
     * Returns true if this set is empty, false if not.
     *
     * @return
     *     true if the set is empty, false if not
     */
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    /**
     * Returns the number of points in this set.
     *
     * @return
     *     The number of points in the set.
     */
    public int size() {
        return pointSet.size();
    }

    /**
     * Adds the point p to the set if it is not already in there.
     *
     * @param p
     *     The point to add.
     */
    public void insert(Point2D p) {
        pointSet.add(p);
    }

    /**
     * Returns true if this set contains the given point; false if not.
     *
     * @param p
     *     The point to check.
     *
     * @return
     *     True if this set contains the given point; false if not.
     */
    public boolean contains(Point2D p) {
        return pointSet.contains(p);
    }

    /**
     * Draws all of the points in the set to StdDraw.
     */
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(PEN_RADIUS);
        for (Point2D point : pointSet) {
            StdDraw.point(point.x(), point.y());
        }
    }

    /**
     * Returns all of the points in the set that are inside the given rectangle.
     *
     * @param rect
     *     The query rectangle.
     *
     * @return
     *     The points that are in the given rectangle, or an empty collection
     *     if there are none.
     */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> pointsInRange = new ArrayList<Point2D>();
        for (Point2D point : pointSet) {
            if (rect.contains(point)) {
                pointsInRange.add(point);
            }
        }
        return pointsInRange;
    }

    /**
     * Returns the point in this set that is closest to the given point.
     *
     * @param p
     *     The query point.
     *
     * @return
     *     The nearest point, or null if the set is empty.
     */
    public Point2D nearest(Point2D p) {
        Point2D winningPoint = null;
        double leastDist = Double.MAX_VALUE;
        for (Point2D point : pointSet) {
            double curDist = p.distanceSquaredTo(point);
            if (curDist < leastDist) {
                leastDist = curDist;
                winningPoint = point;
            }
        }
        return winningPoint;
    }
}
