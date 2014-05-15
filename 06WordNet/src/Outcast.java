/**
 * The Outcast data type, used for determining the least-related noun from
 * a set of nouns, using Wordnet.
 *
 * @author Brian Terczynski
 *
 */
public class Outcast {
    /**
     * The Wordnet data structure.
     */
    private WordNet wordnet;

    /**
     * Creates the outcast object, with the given Wordnet structure.
     *
     * @param wordnet
     *     The Wordnet structure.
     */
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    /**
     * Given an array of WordNet nouns, return the outcast, which is the
     * least related noun.
     *
     * @param nouns
     *     The nouns to check.
     *
     * @return
     *     The least related noun.
     */
    public String outcast(String[] nouns) {
        int maxDistance = 0;
        String outcastNoun = null;
        for (int i = 0; i < nouns.length; i++) {
            int curDistance = 0;
            for (int j = 0; j < nouns.length; j++) {
                curDistance += wordnet.distance(nouns[i], nouns[j]);
            }
            if (curDistance > maxDistance) {
                outcastNoun = nouns[i];
                maxDistance = curDistance;
            }
        }
        return outcastNoun;
    }

    /**
     * Used for unit testing Outcast.
     *
     * @param args
     *     Arguments used for testing.
     */
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            String[] nouns = In.readStrings(args[t]);
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
