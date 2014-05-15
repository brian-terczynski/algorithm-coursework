/**
 * Performs a Burrows-Wheeler transform to be used along with Huffman
 * coding compression.
 *
 * @author Brian Terczynski
 */
public class BurrowsWheeler {
    /**
     * Radix
     */
    private static final int RADIX = 256;

    /**
     * Applies the Burrows-Wheeler encoding, reading from standard input and
     * writing to standard output.
     */
    public static void encode() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
            }
        }
        for (int i = 0; i < csa.length(); i++) {
            int index = csa.index(i) + csa.length() - 1;
            if (index >= csa.length()) {
                index -= csa.length();
            }
            BinaryStdOut.write(input.charAt(index));
        }
        BinaryStdOut.flush();
    }

    /**
     * Applies the Burrows-Wheeler decoding, reading from standard input and
     * writing to standard output.
     */
    public static void decode() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        char [] sortedT = new char [t.length()];
        int [] origIndex = new int [t.length()];
        int[] count = new int[RADIX + 1];
        // Count the frequencies.
        for (int i = 0; i < sortedT.length; i++) {
          count[t.charAt(i) + 1]++;
        }
        // Now, go through the count array, and compute the cumulates
        for (int r = 0; r < RADIX; r++) {
          count[r + 1] += count[r];
        }
        // Move to the sortedT array, and keep track of the original indices
        // so we can perform the decoding properly.
        for (int i = 0; i < sortedT.length; i++) {
            int index = count[t.charAt(i)]++;
            sortedT[index] = t.charAt(i);
            origIndex[index] = i;
        }
        //  Compute the next array.
        int [] next = new int [sortedT.length];
        for (int i = 0; i < next.length; i++) {
            next[i] = origIndex[i];
        }
        int nextIndx = first;
        // Finally, walk to next array, starting from "first", and output
        // the original string.
        for (int i = 0; i < next.length; i++) {
            BinaryStdOut.write(sortedT[nextIndx]);
            nextIndx = next[nextIndx];
        }
        BinaryStdOut.flush();
    }

    /**
     * Test program for BurrowsWheeler.
     *
     * @param args
     *     If args[0] is "-", apply encoding.  If "+", apply decoding.
     */
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        }
    }
}