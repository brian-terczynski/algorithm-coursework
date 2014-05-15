import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The Wordnet data structure.
 *
 * @author Brian Terczynski
 *
 */
public class WordNet {
    /**
     * The map of nouns to synsets.
     */
    private Map<String, List<Integer>> nounToSynsetMap;
    /**
     * The list of synsets.
     */
    private List<String> synsetList;
    /**
     * The digraph of the Wordnet.
     */
    private Digraph graph;
    /**
     * The SAP structure for the Wordnet.
     */
    private SAP sap;

    /**
     * Builds the Wordnet, given the names of the synset and
     * hypernym files.
     *
     * @param synsets
     *     The name of the synset file.
     * @param hypernyms
     *     The name of the hypernym file.
     */
    public WordNet(String synsets, String hypernyms) {
        synsetList = new ArrayList<String>();
        In in = new In(synsets);
        String line;
        int i = 0;
        while ((line = in.readLine()) != null) {
            String [] split = line.split(",");
            if (split.length < 3) {
                throw new IllegalArgumentException();
            }
            try {
                if (i != Integer.parseInt(split[0])) {
                    throw new IllegalArgumentException();
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
            synsetList.add(split[1]);
            i++;
        }
        nounToSynsetMap = new HashMap<String, List<Integer>>();
        for (i = 0; i < synsetList.size(); i++) {
            String [] split = synsetList.get(i).split(" ");
            for (int j = 0; j < split.length; j++) {
                if (!nounToSynsetMap.containsKey(split[j])) {
                    nounToSynsetMap.put(split[j], new LinkedList<Integer>());
                }
                nounToSynsetMap.get(split[j]).add(i);
            }
        }
        in.close();
        graph = new Digraph(synsetList.size());
        in = new In(hypernyms);
        try {
            while ((line = in.readLine()) != null) {
                String [] split = line.split(",");
                int sourceVertex = Integer.parseInt(split[0]);
                for (i = 1; i < split.length; i++) {
                    graph.addEdge(sourceVertex, Integer.parseInt(split[i]));
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        in.close();
        sap = new SAP(graph);
    }

    /**
     * The set of nouns in the synset (duplicates removed).
     *
     * @return
     *     The set of nouns.
     */
    public Iterable<String> nouns() {
        return nounToSynsetMap.keySet();
    }

    /**
     * Returns true if the parameter is a Wordnet noun.
     *
     * @param word
     *     The word to check.
     *
     * @return
     *     True if this is a noun in the Wordnet; false if not.
     */
    public boolean isNoun(String word) {
        return nounToSynsetMap.containsKey(word);
    }

    /**
     * Returns the Shortest Ancestral Path distance between two nouns, or
     * -1 if they are not found or have no ancestral path.
     *
     * @param nounA
     *     Noun 1.
     * @param nounB
     *     Noun 2.
     *
     * @return
     *     The SAP distance, or -1 if any of them are not in the Wordnet or
     *     there is not ancestral path.
     */
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return sap.length(nounToSynsetMap.get(nounA),
                nounToSynsetMap.get(nounB));
    }

    /**
     * Returns the synset (space-separated nouns) that is the common ancestor
     * of nounA and nounB in the Shortest Ancestral Path.
     *
     * @param nounA
     *     Noun 1.
     * @param nounB
     *     Noun 2.
     *
     * @return
     *     The synset, or null if a SAP does not exist for them.
     */
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return synsetList.get(sap.ancestor(nounToSynsetMap.get(nounA),
                nounToSynsetMap.get(nounB)));
    }
}
