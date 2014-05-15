import java.util.HashSet;
import java.util.Set;

/**
 * Class used to solve boggle game boards given a dictionary of words.
 *
 * @author Brian Terczynski
 */
public class BoggleSolver {
    /**
     * The trie used for checking valid words and prefixes.
     */
    private TrieSet trie;
    /**
     * The lookup hash used for checking valid words and scoring them.
     */
    private Set<String> dictionary;

    /**
     * Initializes the solver with the given dictionary.
     *
     * @param dictionary
     *     The dictionary.
     */
    public BoggleSolver(String[] dictionary) {
        trie = new TrieSet();
        this.dictionary = new HashSet<String>();
        for (int i = 0; i < dictionary.length; i++) {
            trie.put(dictionary[i]);
            this.dictionary.add(dictionary[i]);
        }
    }

    /**
     * Returns all valid words in the Boggle board.
     *
     * @param board
     *     The Boggle board.
     * @return
     *     The list of valid words, or an empty collection if there are none.
     */
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> solutions = new HashSet<String>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                new DFS(trie, board, solutions, i, j);
            }
        }
        return solutions;
    }

    /**
     * Returns the score for the given word.  The word must be in the
     * dictionary.
     *
     * @param word
     *     The word to check.
     * @return
     *     The score for the word, or 0 if the word is not in the dictionary.
     */
    public int scoreOf(String word) {
        if (dictionary.contains(word)) {
            switch (word.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
            }
        }
        return 0;
    }

    /**
     * Performs a depth-first search on the Boggle board, finding
     * all valid words.
     *
     * @author Brian Terczynski
     */
    private class DFS {
        /**
         * Indicates pieces that are marked during the traversal.
         */
        private boolean[] marked;
        /**
         * The currently formed word in the traversal.
         */
        private char[] currentPath;

        /**
         * Performs the DFS.
         *
         * @param trie
         *     The prefix trie of items.
         * @param board
         *     The Boggle board.
         * @param solutions
         *     The set of solutions being built.
         * @param row
         *     The current row.
         * @param col
         *     The current column.
         */
        public DFS(TrieSet trie, BoggleBoard board, Set<String> solutions,
                int row, int col) {
            marked = new boolean[board.cols() * board.rows()];
            // Times 2 to allow for Qu.
            currentPath = new char[board.cols() * board.rows() * 2];
            dfs(trie, board, solutions, row, col, 0);
        }

        /**
         * Recursive DFS.
         *
         * @param trie
         *     The prefix trie of items.
         * @param board
         *     The Boggle board.
         * @param solutions
         *     The set of solutions being built.
         * @param row
         *     The current row.
         * @param col
         *     The current column.
         * @param numChars
         *     The current number of characters in the current path.
         */
        private void dfs(TrieSet trie, BoggleBoard board,
                Set<String> solutions, int row, int col, int numChars) {
//System.out.println (row + " " + col);
            marked[row * board.cols() + col] = true;
            currentPath[numChars] = board.getLetter(row, col);
            if (currentPath[numChars] == 'Q') {
                numChars++;
                currentPath[numChars] = 'U';
            }
            for (int flattenedIndex : flattenedAdjIndices(board, row, col)) {
                if (!marked[flattenedIndex]
                        && trie.containsPrefix(currentPath, 0, numChars + 1)) {
                    dfs(trie, board, solutions, flattenedIndex / board.cols(),
                            flattenedIndex % board.cols(), numChars + 1);
                }
            }
            if (numChars >= 2 && trie.contains(currentPath, 0, numChars + 1)) {
                solutions.add(new String (currentPath, 0, numChars + 1));
            }
            marked[row * board.cols() + col] = false;
        }

        /**
         * Returns the adjacency list for the given position in the Boggle
         * Board.
         *
         * @param board
         *     The Boggle board.
         * @param row
         *     The row.
         * @param col
         *     The col.
         *
         * @return
         *     The list of adjacent vertices.
         */
        private int [] flattenedAdjIndices (BoggleBoard board, int row,
                int col) {
            ////////////////////////////////////////////////////
            //  There are 8 possible adjacent vertices.  Compute
            //  each one, but check if they are valid before
            //  adding to array.
            ////////////////////////////////////////////////////
            int candidateRows [] = new int [8];
            int candidateCols [] = new int [8];
            int count = 0;
            candidateRows[0] = row - 1;
            candidateCols[0] = col - 1;
            candidateRows[1] = row;
            candidateCols[1] = col - 1;
            candidateRows[2] = row + 1;
            candidateCols[2] = col - 1;
            candidateRows[3] = row - 1;
            candidateCols[3] = col;
            candidateRows[4] = row + 1;
            candidateCols[4] = col;
            candidateRows[5] = row - 1;
            candidateCols[5] = col + 1;
            candidateRows[6] = row;
            candidateCols[6] = col + 1;
            candidateRows[7] = row + 1;
            candidateCols[7] = col + 1;
            for (int i = 0; i < 8; i++) {
                if (candidateRows[i] >= 0
                        && candidateRows[i] < board.rows()
                        && candidateCols[i] >= 0
                        && candidateCols[i] < board.cols()) {
                    count++;
//System.out.println(candidateRows[i] + " " + candidateCols[i]);
                }
            }
            int [] returnVal = new int [count];
            count = 0;
            for (int i = 0; i < 8; i++) {
                if (candidateRows[i] >= 0
                        && candidateRows[i] < board.rows()
                        && candidateCols[i] >= 0
                        && candidateCols[i] < board.cols()) {
                    returnVal[count] = candidateRows[i] * board.cols()
                            + candidateCols[i];
                    count++;
//System.out.println(candidateRows[i] + " " + candidateCols[i] + " " + returnVal[count-1]);
                }
            }
            return returnVal;
        }
    }

    /**
     * The prefix trie.
     *
     * @author Brian Terczynski
     */
    private static class TrieSet {
        /**
         * The radix.  Only 26 characters being counted in this case.
         */
        private static final int RADIX = 26;
        /**
         * The root node.
         */
        private Node root = new Node();

        /**
         * The node in the prefix trie.
         *
         * @author Brian Terczynski
         *
         */
        private static class Node {
            /**
             * True if this node is the last character of a valid key.
             */
            private boolean isKey;
            /**
             * The child nodes.
             */
            private Node[] next = new Node[RADIX];
        }

        /**
         * Adds a key to the trie.
         *
         * @param key
         *     The key to add.
         */
        public void put (String key) {
            root = put(root, key, 0);
        }

        /**
         * Recursive put method.
         *
         * @param curNode
         *     The current node.
         * @param key
         *     The key to add.
         * @param pos
         *     The current position.
         *
         * @return
         *     The recursive node position.
         */
        private Node put(Node curNode, String key, int pos) {
            if (curNode == null) {
                curNode = new Node();
            }
            if (pos == key.length()) {
                curNode.isKey = true;
                return curNode;
            }
            byte c = (byte) (key.charAt(pos) - 'A');
            curNode.next[c] = put(curNode.next[c], key, pos + 1);
            return curNode;
        }

        /**
         * Returns whether the prefix tree contains the given prefix.
         *
         * @param prefix
         *     The prefix to check
         * @param startIndex
         *     The starting index in the prefix char array.
         * @param length
         *     The length in the prefix char array.
         *
         * @return
         *     True if the prefix exists, false if not.
         */
        public boolean containsPrefix(char [] prefix, int startIndex,
                int length) {
            return get(root, prefix, startIndex, length) != null;
        }

        /**
         * Returns true if the trie contains the given key, false if not.
         *
         * @param key
         *     The key to check.
         * @param startIndex
         *     The start index into the key array.
         * @param length
         *     The length.
         *
         * @return
         *     True if the key is in the tree, false if not.
         */
        public boolean contains(char [] key, int startIndex, int length) {
            Node x = get(root, key, startIndex, length);
            if (x == null) {
                return false;
            }
            return x.isKey;
        }

        /**
         * Recursive get function.
         *
         * @param curNode
         *     The current node in the recursion.
         * @param key
         *     The key to check.
         * @param startIndex
         *     The start index into the key array.
         * @param length
         *     The length.
         *
         * @return
         *     True if the key is in the tree, false if not.
         */
        private Node get(Node curNode, char [] key, int startIndex,
                int length) {
            if (curNode == null) {
                return null;
            }
            if (startIndex == length) {
                return curNode;
            }
            byte c = (byte) (key[startIndex] - 'A');
            return get(curNode.next[c], key, startIndex + 1, length);
        }
    }

    /**
     * Test driver.
     *
     * @param args
     *     The dictionary and the game board.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
