import java.util.Comparator;

/**
 * Class used to solve the 8-puzzle problem using the A* Algorithm.
 *
 * @author Brian Terczynski
 */
public class Solver {
    /**
     * Represents a Search Node in the A* Algorithm.
     *
     * @author Brian Terczynski
     */
    private static final class SearchNode {
        /**
         * The current state of the game board.
         */
        private Board board;
        /**
         * The number of moves to get to this particular board state from
         * the initial board.
         */
        private int numMoves;
        /**
         * The previous search node, or null if this is the initial board.
         */
        private SearchNode prev;

        /**
         * Constructs the Search Node.
         *
         * @param board
         *     The current state of the board.
         * @param moves
         *     The number of moves to get to this board state.
         * @param prev
         *     The previous board.
         */
        private SearchNode(final Board board, final int moves,
                final SearchNode prev) {
            this.board = board;
            this.numMoves = moves;
            this.prev = prev;
        }

        /**
         * Returns the current state of the board for this search node.
         *
         * @return
         *     The current state of the board for this search node.
         */
        public Board getBoard() {
            return board;
        }

        /**
         * Returns the number of moves to reach this board state.
         *
         * @return
         *     The number of moves.
         */
        public int getMoves() {
            return numMoves;
        }

        /**
         * The previous search node, or null if this is the initial board.
         *
         * @return
         *     The previous search node.
         */
        public SearchNode getPrev() {
            return prev;
        }
    }

    /**
     * The Manhattan Priority Function to use for the priority queues.
     *
     * @author Brian Terczynski
     */
    private class SearchNodeComparator implements Comparator<SearchNode> {
        @Override
        public int compare(SearchNode left, SearchNode right) {
            return (left.getBoard().manhattan() + left.getMoves())
                    - (right.getBoard().manhattan() + right.getMoves());
        }
    }

    /**
     * The number of moves to arrive at the solution.
     */
    private int moves = -1;
    /**
     * The path of boards to reach the solution.
     */
    private Stack<Board> solutionPath = null;
    /**
     * True if the initial board is unsolvable; false if it is solvable.
     */
    private boolean isUnsolvable = false;

    // find a solution to the initial board (using the A* algorithm)
    /**
     * Finds a solution to the initial board, using the A* Algorithm.
     *
     * @param initial
     *     The initial game board.
     */
    public Solver(Board initial) {
        MinPQ<SearchNode> priorityQueue =
                new MinPQ<SearchNode>(new SearchNodeComparator());
        MinPQ<SearchNode> twinPriorityQueue =
                new MinPQ<SearchNode>(new SearchNodeComparator());
        priorityQueue.insert(new SearchNode(initial, 0, null));
        twinPriorityQueue.insert(new SearchNode(initial.twin(), 0, null));
        SearchNode curNode = null;
        SearchNode twinCurNode = null;
        while ((curNode = priorityQueue.delMin()) != null) {
            twinCurNode = twinPriorityQueue.delMin();
//            System.out.println("CurNode:" + curNode.getBoard() + " " +
            //curNode.getBoard().manhattan() + " " + curNode.getMoves());
            if (curNode.getBoard().isGoal()) {
                moves = curNode.getMoves();
                solutionPath = new Stack<Board>();
                while (curNode != null) {
                    solutionPath.push(curNode.getBoard());
                    curNode = curNode.getPrev();
                }
                return;
            } else {
                if (twinCurNode.getBoard().isGoal()) {
                    isUnsolvable = true;
                    return;
                }
                Iterable<Board> neighbors = curNode.getBoard().neighbors();
                for (Board neighbor : neighbors) {
                    if (curNode.getPrev() == null
                            || !curNode.getPrev().getBoard().equals(neighbor)) {
//                        System.out.println("Neighbor:" + neighbor + " " +
                        //neighbor.manhattan() + " " + curNode.getMoves()+1);
                        priorityQueue.insert(
                                new SearchNode(neighbor,
                                        curNode.getMoves() + 1, curNode));
                    }
                }
                Iterable<Board> twinNeighbors =
                        twinCurNode.getBoard().neighbors();
                for (Board twinNeighbor : twinNeighbors) {
                    if (twinCurNode.getPrev() == null
                            || !twinCurNode.getPrev().getBoard().
                            equals(twinNeighbor)) {
                        twinPriorityQueue.insert(
                                new SearchNode(twinNeighbor,
                                        twinCurNode.getMoves() + 1,
                                        twinCurNode));
                    }
                }
            }
        }
    }

    /**
     * Returns true if this board is solvable; false if not.
     *
     * @return
     *     True if solvable; false if not.
     */
    public boolean isSolvable() {
        return !isUnsolvable;
    }

    /**
     * The number of moves to reach the solution; -1 if the board is
     * unsolvable.
     *
     * @return
     *     The number of moves.
     */
    public int moves() {
        return moves;
    }

    /**
     * Returns the shortest sequence of boards to reach the solution, or
     * null if there is no solution.
     *
     * @return
     *     The shortest sequence of boards, or null if there is no solution.
     */
    public Iterable<Board> solution() {
        return solutionPath;
    }

    /**
     * Main entry point for the program; takes the input file as an argument.
     *
     * @param args
     *     The input board file.
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
