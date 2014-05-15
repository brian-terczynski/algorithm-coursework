import java.util.Iterator;

/**
 * This program reads a list of strings from standard input, and returns
 * a random subset of those numbers where the size of that subset is specified
 * on the command line.
 * 
 * @author Brian Terczynski
 */
public class Subset {
    /**
     * The main entry point for the program.
     * 
     * @param args
     *     The size of the random subset to return.
     */
    public static void main(String[] args) {
        int numItemsToPrint = 0;
        if (args.length != 1) {
            System.err.println("Usage: Subset <num_items_to_print>");
        }
        numItemsToPrint = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }
        Iterator <String> iter = rq.iterator();
        for (int i = 0; i < numItemsToPrint; i++) {
            StdOut.println(iter.next());
        }
    }
}
