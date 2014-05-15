import java.util.Arrays;
import java.util.Comparator;

/**
 * Stores a sorted Circular Suffix Array, used for Burrows-Wheeler transforms.
 *
 * @author Brian Terczynski
 */
public class CircularSuffixArray {
    /**
     * The length of the original string.
     */
    private int length;
    /**
     * The internalized string buffer, which is the original string
     * concatenated with itself.
     */
    private String suffixBuffer;
    /**
     * The array of indices.
     */
    private Integer [] index;

    /**
     * Creates the Circular Suffix Array.
     *
     * @param s
     *     The reference string.
     */
    public CircularSuffixArray(String s) {
        suffixBuffer = s + s;
        length = s.length();
        index = new Integer [length];
        for (int i = 0; i < length; i++) {
            index[i] = i;
        }
        Arrays.sort(index, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                for (int i = 0; i < length; i++) {
                    int diff =
                            suffixBuffer.charAt(o1 + i)
                            - suffixBuffer.charAt(o2 + i);
                    if (diff != 0) {
                        return diff;
                    }
                }
                return 0;
            }
        });
    }

    /**
     * Returns the length of the original string.
     *
     * @return
     *     The original string length.
     */
    public int length() {
        return length;
    }

    /**
     * The index value at the given i.
     *
     * @param i
     *     The given i.
     * @return
     *     The index value.
     */
    public int index(int i) {
        return index[i];
    }
}
