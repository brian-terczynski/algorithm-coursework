import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game board.
 *
 * @author Brian Terczynski
 */
public class Board {
    /**
     * The blocks on the board.
     */
    private char [][] blocks;
    /**
     * The board dimension.
     */
    private int dimension;

    /**
     * Constructs an N-N game board.
     *
     * @param blocks
     *     The blocks on the board.
     */
    public Board(int[][] blocks) {
        this.dimension = blocks[0].length;
        this.blocks = new char[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.blocks[i][j] = (char) blocks[i][j];
            }
        }
    }

    /**
     * Returns the dimension N of the board.
     *
     * @return
     *     The board dimension N.
     */
    public int dimension() {
        return dimension;
    }

    /**
     * Returns the number of blocks that are out of place.
     *
     * @return
     *     The number of blocks that are out of place.
     */
    public int hamming() {
        int expectedValue = 1;
        int outOfPlaceCounter = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (i == dimension - 1 && j == dimension - 1) {
                    expectedValue = 0;
                }
                if (blocks[i][j] > 0 && blocks[i][j] != expectedValue) {
                    outOfPlaceCounter++;
                }
                expectedValue++;
            }
        }
        return outOfPlaceCounter;
    }

    /**
     * Returns the sum of the Manhattan distances between the blocks on the
     * board and the goal.
     *
     * @return
     *     The sum of the Manhattan distances.
     */
    public int manhattan() {
        int manhattanSum = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int expectedRow;
                int expectedCol;
                if (blocks[i][j] > 0) {
                    expectedRow = (blocks[i][j] - 1) / dimension;
                    expectedCol = (blocks[i][j] - 1) % dimension;
                    manhattanSum += Math.abs(expectedRow - i) +
                            Math.abs(expectedCol - j);
                }
            }
        }

        return manhattanSum;
    }

    /**
     * Returns true if this board is a goal board; in other words, all of the
     * blocks are in place.
     *
     * @return
     *     true if this is a goal board; false if not
     */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * This returns a board with two adjacent blocks exchanged in the
     * same row.
     *
     * @return
     *     The twin Board.
     */
    public Board twin() {
        int [][] newBlocks = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                newBlocks[i][j] = blocks[i][j];
            }
        }
        if (dimension > 1) {
            if (newBlocks[0][0] == 0 || newBlocks[0][1] == 0) {
                int swap = newBlocks[1][1];
                newBlocks[1][1] = newBlocks[1][0];
                newBlocks[1][0] = swap;
            } else {
                int swap = newBlocks[0][1];
                newBlocks[0][1] = newBlocks[0][0];
                newBlocks[0][0] = swap;
            }
        }
        return new Board(newBlocks);
    }

    /**
     * Returns true if this board equals the specified board object.
     *
     * @return
     *     true if they are equal, false if not.
     */
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (y instanceof Board) {
            Board other = (Board) y;
            if (other.dimension != this.dimension) {
                return false;
            }
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (other.blocks[i][j] != this.blocks[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns all neighboring boards to this board.
     *
     * @return
     *     All neighboring boards.
     */
    public Iterable<Board> neighbors() {
        int zeroRow = -1;
        int zeroCol = -1;
        for (int i = 0; i < dimension && zeroRow < 0; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
        }
        List<Board> neighbors = new ArrayList<Board>();
        createNeighbor(zeroRow, zeroCol, zeroRow - 1, zeroCol, neighbors);
        createNeighbor(zeroRow, zeroCol, zeroRow + 1, zeroCol, neighbors);
        createNeighbor(zeroRow, zeroCol, zeroRow, zeroCol - 1, neighbors);
        createNeighbor(zeroRow, zeroCol, zeroRow, zeroCol + 1, neighbors);
        return neighbors;
    }

    /**
     * Returns a string representation of the board.
     *
     * @return
     *     The string representation of this board.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension).append("\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                s.append(String.format("%2d ", (short) blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Creates a neighbor board, given the position of the missing piece
     * and the position with which to exchange it, and adds it to the list
     * of neighbors passed in.
     *
     * @param zeroRow
     *     The row of the missing piece.
     * @param zeroCol
     *     The column of the missing piece.
     * @param exchangeRow
     *     The row of the piece to exchange.
     * @param exchangeCol
     *     The column of the piece to exchange.
     * @param neighbors
     *     The list of neighbors being built.
     */
    private void createNeighbor(int zeroRow, int zeroCol, int exchangeRow,
            int exchangeCol, List<Board> neighbors) {
        if (exchangeRow >= 0 && exchangeRow < dimension
                && exchangeCol >= 0 && exchangeCol < dimension) {
            int [][] newBlocks = new int[dimension][dimension];
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    newBlocks[i][j] = blocks[i][j];
                }
            }
            int swap = newBlocks[exchangeRow][exchangeCol];
            newBlocks[exchangeRow][exchangeCol] = newBlocks[zeroRow][zeroCol];
            newBlocks[zeroRow][zeroCol] = swap;
            neighbors.add(new Board(newBlocks));
        }
    }
}
