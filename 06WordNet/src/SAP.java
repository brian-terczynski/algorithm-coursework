/**
 * Data structure used for determining the Shortest Ancestral Path for
 * two vertices within a graph.
 *
 * @author Brian Terczynski
 */
public class SAP {
    /**
     * The digraph structure.
     */
    private Digraph graph;

    /**
     * Constructs a Shortest Ancestral Path object given a Digraph.
     *
     * @param G
     *     The digraph (not necessarily a DAG).
     */
    public SAP(Digraph G) {
        this.graph = new Digraph(G);
    }

    /**
     * Returns the shortest ancestral path length between vertices v and w
     * in the digraph.
     *
     * @param v
     *     Vertex 1.
     * @param w
     *     Vertex 2.
     *
     * @return
     *     The shortest ancestral path, or -1 if there is no such path.
     */
    public int length(int v, int w) {
        return ancestorHelper(v, w, true);
    }

    /**
     * Returns the common ancestor in the shortest ancestral path
     * between vertices v and w in the digraph.
     *
     * @param v
     *     Vertex 1.
     * @param w
     *     Vertex 2.
     *
     * @return
     *     The common ancestor in SAP, or -1 if there is no SAP.
     */
    public int ancestor(int v, int w) {
        return ancestorHelper(v, w, false);
    }

    /**
     * Helper method for returning either the common ancestor or the distance
     * of the shortest ancestral path between vertex v and
     * vertex w in the digraph.
     *
     * @param v
     *     Vertex 1.
     * @param w
     *     Vertex 2.
     * @param returnAsDistance
     *     True if the return value will be the distance; false if the return
     *     value will be the common ancestor.
     *
     * @return
     *     The common ancestor or distance in SAP, or -1 if there is no SAP.
     */
    private int ancestorHelper(int v, int w, boolean returnAsDistance) {
        if (v < 0 || v >= graph.V()) {
            throw new IndexOutOfBoundsException();
        }
        if (w < 0 || w >= graph.V()) {
            throw new IndexOutOfBoundsException();
        }
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph,
                v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph,
                w);
        int minDist = -1;
        int commonAncestor = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (minDist == -1
                        || bfsV.distTo(i) + bfsW.distTo(i) < minDist) {
                    minDist = bfsV.distTo(i) + bfsW.distTo(i);
                    commonAncestor = i;
                }
            }
        }
        return returnAsDistance ? minDist : commonAncestor;
    }

    /**
     * Returns the shortest ancestral path length between set of vertices v
     * and set of vertices w in the digraph.
     *
     * @param v
     *     Vertex Set 1.
     * @param w
     *     Vertex Set 2.
     *
     * @return
     *     The shortest ancestral path, or -1 if there is no such path.
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return ancestorHelper(v, w, true);
    }

    /**
     * Returns the common ancestor in the shortest ancestral path
     * between set of vertices v and set of vertices w in the digraph.
     *
     * @param v
     *     Vertex Set 1.
     * @param w
     *     Vertex Set 2.
     *
     * @return
     *     The common ancestor in SAP, or -1 if there is no SAP.
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return ancestorHelper(v, w, false);
    }

    /**
     * Helper method for returning either the common ancestor or the distance
     * of the shortest ancestral path between set of vertices v and set of
     * vertices w in the digraph.
     *
     * @param v
     *     Vertex Set 1.
     * @param w
     *     Vertex Set 2.
     * @param returnAsDistance
     *     True if the return value will be the distance; false if the return
     *     value will be the common ancestor.
     *
     * @return
     *     The common ancestor or distance in SAP, or -1 if there is no SAP.
     */
    private int ancestorHelper (Iterable<Integer> v, Iterable<Integer> w,
            boolean returnAsDistance) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph,
                v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph,
                w);
        int minDist = -1;
        int commonAncestor = -1;
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (minDist == -1
                        || bfsV.distTo(i) + bfsW.distTo(i) < minDist) {
                    minDist = bfsV.distTo(i) + bfsW.distTo(i);
                    commonAncestor = i;
                }
            }
        }
        return returnAsDistance ? minDist : commonAncestor;
    }

    /**
     * Used for unit testing Outcast.
     *
     * @param args
     *     Arguments used for testing.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
