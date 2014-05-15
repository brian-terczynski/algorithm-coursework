/**
 * Applies a move-to-front encoding on an input.
 *
 * @author Brian Terczynski
 */
public class MoveToFront {
    /**
     * Radix
     */
    private static final int RADIX = 256;

    /**
     * Applies a move-to-front encoding, reading from the standard input
     * and writing to the standard output.
     */
    public static void encode() {
        char [] charToIndexMap = new char [RADIX];
        char [] indexToCharMap = new char [RADIX];
        for (int i = 0; i < charToIndexMap.length; i++) {
            charToIndexMap[i] = (char) i;
            indexToCharMap[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int maxIndex = charToIndexMap[c];
            BinaryStdOut.write(charToIndexMap[c]);
            charToIndexMap[c] = 0;
            for (int i = maxIndex; i >= 1; i--) {
                indexToCharMap[i] = indexToCharMap[i - 1];
                char theChar = indexToCharMap[i];
                charToIndexMap[theChar] = (char) i;
            }
            indexToCharMap[0] = c;
        }
        BinaryStdOut.flush();
    }

    /**
     * Applies a move-to-front decoding, reading from the standard input and
     * writing to the standard output.
     */
    public static void decode() {
        char [] indexToCharMap = new char [RADIX];
        for (int i = 0; i < indexToCharMap.length; i++) {
            indexToCharMap[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int maxIndex = c;
            BinaryStdOut.write(indexToCharMap[c]);
            char theChar = indexToCharMap[c];
            for (int i = maxIndex; i >= 1; i--) {
                indexToCharMap[i] = indexToCharMap[i - 1];
            }
            indexToCharMap[0] = theChar;
        }
        BinaryStdOut.flush();
    }

    /**
     * Test program for MoveToFront.
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
