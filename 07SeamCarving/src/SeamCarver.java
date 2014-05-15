import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Seam Carver
 *
 * @author Brian Terczynski
 */
public class SeamCarver {
    /**
     * Border pixel energy.
     */
    private static final int BORDER_PIXEL_ENERGY = 195075;
    /**
     * Stores the current picture.
     */
    private Color [][] currentPicture;
    /**
     * Stores the current, flattened energy matrix.
     */
    private double [] energy;
    /**
     * The current energy matrix width.
     */
    private int energyWidth;
    /**
     * The current energy matrix height.
     */
    private int energyHeight;

    /**
     * Creates a seam carver for the given picture.
     *
     * @param picture
     *     The picture.
     */
    public SeamCarver(Picture picture) {
        currentPicture = new Color[picture.height()][picture.width()];
        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                currentPicture[y][x] = picture.get(x, y);
            }
        }
        recomputeEnergy();
    }

    /**
     * Returns the currently carved picture.
     *
     * @return
     *     The currently carved picture.
     */
    public Picture picture() {
        Picture newPicture = new Picture(width(), height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                newPicture.set(x, y, currentPicture[y][x]);
            }
        }
        return newPicture;
    }

    /**
     * The width of the current picture.
     *
     * @return
     *     The width of the current picture.
     */
    public int width() {
        return currentPicture[0].length;
    }

    /**
     * The height of the current picture.
     *
     * @return
     *     The height of the current picture.
     */
    public int height() {
        return currentPicture.length;
    }

    /**
     * Returns the energy of the specified pixel.
     *
     * @param x
     *     The x-coordinate.
     * @param y
     *     The y-coordinate.
     *
     * @return
     *     The energy of the pixel.
     */
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IndexOutOfBoundsException();
        }
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return BORDER_PIXEL_ENERGY;
        }

        return xGradientSquare(x, y) + yGradientSquare(x, y);
    }

    /**
     * Returns a horizontal seam that is a candidate for carving in the
     * current picture.
     *
     * @return
     *     A candidate horizontal seam.
     */
    public int[] findHorizontalSeam() {
        transpose();
        AcyclicSP sp = new AcyclicSP(0);
        int [] path = sp.getPath();
        transpose();
        return path;
    }

    /**
     * Returns a vertical seam that is a candidate for carving in the
     * current picture.
     *
     * @return
     *     A candidate vertical seam.
     */
    public int[] findVerticalSeam() {
        AcyclicSP sp = new AcyclicSP(0);
        return sp.getPath();
    }

    /**
     * Removes the given horizontal seam.
     *
     * @param a
     *     The seam to remove.
     */
    public void removeHorizontalSeam(int[] a) {
        transposePicture();
        internalRemoveVerticalSeam(a);
        transposePicture();
        recomputeEnergy();
    }

    /**
     * Removes the given vertical seam.
     *
     * @param a
     *     The seam to remove.
     */
    public void removeVerticalSeam(int[] a) {
        internalRemoveVerticalSeam(a);
        recomputeEnergy();
    }

    /**
     * Removes the specified vertical seam.
     *
     * @param a
     *     The seam to remove.
     */
    private void internalRemoveVerticalSeam(int [] a) {
        ///////////
        //  Verify.
        ///////////
        if (width() <= 1 || a.length != height()) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] < 0 || a[i] >= width()) {
                throw new IllegalArgumentException();
            }
            if (i > 0 && (a[i] - a[i - 1] > 1
                    || a[i] - a[i - 1] < -1)) {
                throw new IllegalArgumentException();
            }
        }

        int newWidth = width() - 1;
        for (int i = 0; i < a.length; i++) {
            Color [] newColorArray = new Color [newWidth];
            int k = 0;
            for (int j = 0; j < currentPicture[i].length; j++) {
                if (j != a[i]) {
                    newColorArray[k] = currentPicture[i][j];
                    k++;
                }
            }
/*            if (a[i] > 0) {
                System.arraycopy(currentPicture[i], 0, newColorArray, 0, a[i]);
            }
            if (a[i] < width() - 1) {
                System.arraycopy(currentPicture[i], a[i] + 1, newColorArray,
                a[i], (newWidth) - (a[i]));
            } */
            currentPicture[i] = newColorArray;
        }
    }

    /**
     * Transposes the picture; used in the horizontal operations.
     */
    private void transposePicture() {
        Color [][] newPicture = new Color [width()][height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                newPicture[x][y] = currentPicture[y][x];
            }
        }
        currentPicture = newPicture;
    }

    /**
     * Returns the x gradient square.
     *
     * @param x
     *     The x coordinate.
     * @param y
     *     The y coordinate.
     * @return
     *     The x gradient square.
     */
    private int xGradientSquare(int x, int y) {
        return computeGradient(currentPicture[y][x - 1],
                currentPicture[y][x + 1]);
    }

    /**
     * Returns the y gradient square.
     *
     * @param x
     *     The x coordinate.
     * @param y
     *     The y coordinate.
     * @return
     *     The y gradient square.
     */
    private int yGradientSquare(int x, int y) {
        return computeGradient(currentPicture[y - 1][x],
                currentPicture[y + 1][x]);
    }

    /**
     * Computes the gradient square between the two pixels.
     *
     * @param priorPixel
     *     The prior pixel.
     * @param nextPixel
     *     The next pixel.
     * @return
     *     The gradient square.
     */
    private int computeGradient(Color priorPixel, Color nextPixel) {
        int redDiff = nextPixel.getRed() - priorPixel.getRed();
        int greenDiff = nextPixel.getGreen() - priorPixel.getGreen();
        int blueDiff = nextPixel.getBlue() - priorPixel.getBlue();
        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff;
    }

    /**
     * Converts an index in the flattened energy matrix to its corresponding
     * x coordinate.
     *
     * @param energyIndex
     *     The energy matrix index.
     * @return
     *     The x coordinate.
     */
    private int energyIndexToX(int energyIndex) {
        return (energyIndex - 1) % energyWidth;
    }

    /**
     * Converts x and y coordinates into the energy matrix coordinate.
     *
     * @param x
     *     The x coordinate.
     * @param y
     *     The y coordinate.
     * @return
     *     The energy index.
     */
    private int xyToEnergyIndex(int x, int y) {
        return (energyWidth * y) + x + 1;
    }

    /**
     * Converts x and y coordinates into the transpose energy matrix coordinate.
     *
     * @param x
     *     The x coordinate.
     * @param y
     *     The y coordinate.
     *
     * @return
     *     The energy index.
     */
    private int xyToEnergyIndexTranspose(int x, int y) {
        return (energyHeight * x) + y + 1;
    }

    /**
     * Returns the array of adjacent vertices to the given one.
     *
     * @param v
     *     The vertex.
     * @return
     *     The adjacent vertices.
     */
    private int [] getAdjacent(int v) {
        int [] adj;
        if (v == 0) {  // Source node.
            adj = new int [energyWidth];
            for (int i = 0; i < energyWidth; i++) {
                adj[i] = i;
            }
        } else if (v == energy.length - 1) {  // Sink node.
            adj = new int [0];
        } else if (v + energyWidth >= energy.length - 1) {  // Bottom.
            adj = new int [1];
            adj[0] = energy.length - 1;
        } else if (energyWidth == 1) {
            adj = new int [1];
            adj[0] = v + 1;
        } else if  ((v - 1) % energyWidth == 0) {
            adj = new int [2];
            adj[0] = v + energyWidth;
            adj[1] = v + energyWidth + 1;
        } else if ((v % energyWidth) == 0) {
            adj = new int [2];
            adj[0] = v + energyWidth - 1;
            adj[1] = v + energyWidth;
        } else {
            adj = new int [3];
            adj[0] = v + energyWidth - 1;
            adj[1] = v + energyWidth;
            adj[2] = v + energyWidth + 1;
        }
        return adj;
    }

    /**
     * Transposes the energy matrix.
     */
    private void transpose() {
        double [] newEnergy = new double [energy.length];
        newEnergy[0] = 0.0;
        for (int y = 0; y < energyHeight; y++) {
            for (int x = 0; x < energyWidth; x++) {
                newEnergy[xyToEnergyIndexTranspose(x, y)] =
                        energy[xyToEnergyIndex(x, y)];
            }
        }
        newEnergy[energy.length - 1] = 0.0;

        int temp = energyWidth;
        energyWidth = energyHeight;
        energyHeight = temp;
        energy = newEnergy;
    }

    /**
     * Recomputes the energy matrix.
     */
    private void recomputeEnergy() {
        int k = 1;
        energy = new double [width() * height() + 2];
        energy[0] = 0.0;
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energy[k] = energy(x, y);
                k++;
            }
        }
        energy[k] = 0.0;
        energyWidth = width();
        energyHeight = height();
    }

    /**
     * Performs a topological sort.
     *
     * @author Brian Terczynski
     */
    private class TopologicalSort {
        /**
         * Maintains the visited vertices.
         */
        private boolean [] visited;
        /**
         * The post order list.
         */
        private List<Integer> postOrderList = new ArrayList<Integer>();

        /**
         * Constructs the TopologicalSort object.
         *
         * @param energyArray
         *     The energy array.
         * @param source
         *     The source vertex.
         */
        private TopologicalSort(double [] energyArray, int source) {
            visited = new boolean[energyArray.length];
            depthFirstSearch(energyArray, source);
        }

        /**
         * Recursively performs a DFS on the given vertex.
         *
         * @param energyArray
         *     The energy array.
         * @param v
         *     The source vertex.
         */
        private void depthFirstSearch(double [] energyArray, int v) {
            visited[v] = true;
            for (int adj : getAdjacent(v)) {
                if (!visited[adj]) {
                    depthFirstSearch(energyArray, adj);
                }
            }
            postOrderList.add(v);
        }

        /**
         * Returns the post order list.
         *
         * @return
         *     The post order list.
         */
        private List<Integer> postOrder() {
            return postOrderList;
        }
    }

    /**
     * Performs an acyclic Shortest Paths search on the energy matrix.
     *
     * @author Brian Terczynski
     */
    private class AcyclicSP {
        /**
         * The distTo values.
         */
        private double [] distTo;
        /**
         * The edgeTo values.
         */
        private int [] edgeTo;

        /**
         * Sets up the Acyclic Shortest Path.
         *
         * @param s
         *     The source vertex.
         */
        private AcyclicSP(int s) {
            distTo = new double[energy.length];
            edgeTo = new int[energy.length];
            for (int i = 0; i < distTo.length; i++) {
              distTo[i] = Double.POSITIVE_INFINITY;
              edgeTo[i] = i;
            }
            distTo[s] = 0.0;
            TopologicalSort topo = new TopologicalSort(energy, s);
            for (int i = topo.postOrder().size() - 1; i >= 0; i--) {
                int v = topo.postOrder().get(i);
                for (int w : getAdjacent(v)) {
                    relax(v, w, energy[w]);
                }
            }
        }

        /**
         * Returns the shortest path.
         *
         * @return
         *     The shortest path.
         */
        public int [] getPath() {
            Stack<Integer> stack = new Stack<Integer>();
            int nextVertex = energy.length - 1;
            while ((nextVertex = edgeTo[nextVertex]) != 0) {
                stack.push(nextVertex);
            }
            int [] theArray = new int [stack.size()];
            int i = 0;
            while (!stack.isEmpty()) {
                theArray[i] = energyIndexToX(stack.pop());
                i++;
            }
            return theArray;
        }

        /**
         * Relaxes the edge between v and w.
         *
         * @param v
         *     The from vertex.
         * @param w
         *     The to vertex.
         * @param weight
         *     The weight of the edge.
         */
        private void relax(int v, int w, double weight) {
            if (distTo[w] > distTo[v] + weight) {
                distTo[w] = distTo[v] + weight;
                edgeTo[w] = v;
            }
        }
    }
}