import java.util.LinkedList;
import java.util.List;

/**
 * Represents a K-d tree of points on a plane.
 *
 * @author Brian Terczynski
 */
public class KdTree {
    /**
     * Pen radius for drawing points.
     */
    private static final double PEN_RADIUS = 0.01;
    /**
     * The root of the tree.
     */
    private Node root;

    /**
     * The number of nodes in the tree.
     */
    private int size;

    /**
     * Represents a node in the K-d tree.
     */
    private final static class Node {
        /**
         * Creates a K-d tree node.
         *
         * @param point
         *     The point.
         * @param rect
         *     The rect subdivision associated with this point.
         * @param left
         *     The left subtree.
         * @param right
         *     The right subtree.
         */
        private Node(final Point2D point, final RectHV rect,
                final Node left, final Node right) {
            this.point = point;
            this.rect = rect;
            this.left = left;
            this.right = right;
        }
        /**
         * The point at the node.
         */
        private Point2D point;
        /**
         * The axis-aligned rectangle.
         */
        private RectHV rect;
        /**
         * The left subtree.
         */
        private Node left;
        /**
         * The right subtree.
         */
        private Node right;
    }

    /**
     * Constructs an empty tree of points.
     */
    public KdTree() {
        root = null;
        size = 0;
    }

    /**
     * Returns true if this tree is empty, false if not.
     *
     * @return
     *     true if the tree is empty, false if not
     */
    public boolean isEmpty() {
        return (root == null);
    }

    /**
     * Returns the number of points in the tree.
     *
     * @return
     *     The number of points in the tree.
     */
    public int size() {
        return size;
    }

    /**
     * Adds the point p to the tree if it is not already in there.
     *
     * @param p
     *     The point to add.
     */
    public void insert(Point2D p) {
        root = insert(root, p, 0, new RectHV(0.0, 0.0, 1.0, 1.0));
    }

    /**
     * Recursive insert method.
     *
     * @param subtreeRoot
     *     The subtree.
     * @param p
     *     The query point.
     * @param level
     *     The level in the tree.
     * @param enclosingRect
     *     The enclosing rect.
     *
     * @return
     *     The inserted node.
     */
    private Node insert(final Node subtreeRoot, final Point2D p,
            final int level, final RectHV enclosingRect) {
        if (subtreeRoot == null) {
            size++;
            return new Node(p, enclosingRect, null, null);
        }
        if (p.equals(subtreeRoot.point)) {
            return subtreeRoot;
        }
        double cmp;
        RectHV newEnclosingRect;
        boolean usingX = false;
        if (level % 2 == 0) {
            cmp = p.x() - subtreeRoot.point.x();
            usingX = true;
        } else {
            cmp = p.y() - subtreeRoot.point.y();
        }
        if (cmp < 0.0) {
            if (usingX) {
                newEnclosingRect = new RectHV(enclosingRect.xmin(),
                        enclosingRect.ymin(),
                        subtreeRoot.point.x(), enclosingRect.ymax());
            } else {
                newEnclosingRect = new RectHV(enclosingRect.xmin(),
                        enclosingRect.ymin(),
                        enclosingRect.xmax(), subtreeRoot.point.y());
            }
            subtreeRoot.left = insert(subtreeRoot.left, p, level + 1,
                    newEnclosingRect);
        } else {
            if (usingX) {
                newEnclosingRect = new RectHV(subtreeRoot.point.x(),
                        enclosingRect.ymin(),
                        enclosingRect.xmax(), enclosingRect.ymax());
            } else {
                newEnclosingRect = new RectHV(enclosingRect.xmin(),
                        subtreeRoot.point.y(),
                        enclosingRect.xmax(), enclosingRect.ymax());
            }
            subtreeRoot.right = insert(subtreeRoot.right, p, level + 1,
                    newEnclosingRect);
        }
        return subtreeRoot;
    }

    /**
     * Returns true if this tree contains the given point; false if not.
     *
     * @param p
     *     The point to check.
     *
     * @return
     *     True if this tree contains the given point; false if not.
     */
    public boolean contains(Point2D p) {
        return contains(root, p, 0);
    }

    /**
     * Recursive contains method.
     *
     * @param subtreeRoot
     *     The subtree root.
     * @param point
     *     The query point.
     * @param level
     *     The current level in the tree.
     *
     * @return
     *     true if the point was found in this subtree; false if not.
     */
    private boolean contains(final Node subtreeRoot, final Point2D point,
            final int level) {
        if (subtreeRoot == null) {
            return false;
        }
        double cmp;
        if (level % 2 == 0) {
            cmp = point.x() - subtreeRoot.point.x();
        } else {
            cmp = point.y() - subtreeRoot.point.y();
        }
        if (point.equals(subtreeRoot.point)) {
            return true;
        }
        if (cmp < 0.0) {
            return contains(subtreeRoot.left, point, level + 1);
        } else {
            return contains(subtreeRoot.right, point, level + 1);
        }
    }

    /**
     * Draws all of the points in the tree to StdDraw.
     */
    public void draw() {
        traverse(root, 0);
    }

    /**
     * Recursive traverse method to draw the points and their subdividing lines.
     *
     * @param subtreeRoot
     *     The root of the subtree.
     * @param level
     *     The current level in the tree.
     */
    private void traverse(final Node subtreeRoot, final int level) {
        if (subtreeRoot == null) {
            return;
        }
        traverse(subtreeRoot.left, level + 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(PEN_RADIUS);
        StdDraw.point(subtreeRoot.point.x(), subtreeRoot.point.y());
        if (level % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.0);
            StdDraw.line(subtreeRoot.point.x(), subtreeRoot.rect.ymin(),
                    subtreeRoot.point.x(), subtreeRoot.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.0);
            StdDraw.line(subtreeRoot.rect.xmin(), subtreeRoot.point.y(),
                    subtreeRoot.rect.xmax(), subtreeRoot.point.y());
        }
        traverse(subtreeRoot.right, level + 1);
    }

    /**
     * Returns all of the points in the tree that are inside the given
     * rectangle.
     *
     * @param rect
     *     The query rectangle.
     *
     * @return
     *     The points that are in the given rectangle, or an empty collection
     *     if there are none.
     */
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> points = new LinkedList<Point2D>();
        range(root, rect, points);
        return points;
    }

    /**
     * Recursive range method.
     *
     * @param subtreeRoot
     *     The subtree root.
     * @param rect
     *     The query rectangle.
     * @param points
     *     The points found so far.
     */
    private void range(final Node subtreeRoot, final RectHV rect,
            final List<Point2D> points) {
        if (subtreeRoot == null) {
            return;
        }
        if (rect.contains(subtreeRoot.point)) {
            points.add(subtreeRoot.point);
        }
        if (rect.intersects(subtreeRoot.rect)) {
            range(subtreeRoot.left, rect, points);
            range(subtreeRoot.right, rect, points);
        }
    }

    /**
     * Returns the point in this tree that is closest to the given point.
     *
     * @param p
     *     The query point.
     *
     * @return
     *     The nearest point, or null if the set is empty.
     */
    public Point2D nearest(Point2D p) {
        return nearest(root, p, 0, Double.MAX_VALUE);
    }

    /**
     * Recursive helper function for "nearest".
     *
     * @param subtreeRoot
     *     The current subtree.
     * @param p
     *     The query point.
     * @param level
     *     The current level in the tree.
     * @param bestDistance
     *     The current best distance
     *
     * @return
     *     The new nearest point, or null if no better one was found in this
     *     subtree.
     */
    private Point2D nearest(final Node subtreeRoot, final Point2D p,
            final int level, double bestDistance) {
        Point2D pointToReturn = null;
        if (subtreeRoot == null) {
            return null;
        }
        double rectDist = subtreeRoot.rect.distanceSquaredTo(p);
        if (rectDist < bestDistance) {
            Point2D candidate = null;
            double curDist = subtreeRoot.point.distanceSquaredTo(p);
            if (curDist < bestDistance) {
                pointToReturn = subtreeRoot.point;
                bestDistance = curDist;
            }
            Node subtree1 = null;
            Node subtree2 = null;
            if (level % 2 == 0) {
                if (p.x() < subtreeRoot.point.x()) {
                    subtree1 = subtreeRoot.left;
                    subtree2 = subtreeRoot.right;
                } else {
                    subtree1 = subtreeRoot.right;
                    subtree2 = subtreeRoot.left;
                }
            } else {
                if (p.y() < subtreeRoot.point.y()) {
                    subtree1 = subtreeRoot.left;
                    subtree2 = subtreeRoot.right;
                } else {
                    subtree1 = subtreeRoot.right;
                    subtree2 = subtreeRoot.left;
                }
            }
            candidate = nearest(subtree1, p, level + 1, bestDistance);
            if (candidate != null) {
                pointToReturn = candidate;
                bestDistance = pointToReturn.distanceSquaredTo(p);
            }
            candidate = nearest(subtree2, p, level + 1, bestDistance);
            if (candidate != null) {
                pointToReturn = candidate;
            }
        }
        return pointToReturn;
    }
}
