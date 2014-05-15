import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class implements a randomized queue, meaning that (1) iterators
 * will iterate over the queue in their own, independent, randomized orders;
 * (2) the sample() operation returns a random item; and (3) the dequeue()
 * operation removes an item at random.
 * <p>
 * PERFORMANCE: All operations are in constant amortized time, with space
 * proportional to the number of items (sequence of N operations take
 * cN in the worst case).
 *
 * @author Brian Terczynski
 *
 * @param <Item>
 *     The items that this randomized queue holds.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    /**
     * The initial capacity for the underlying array.
     */
    private static final int INITIAL_CAPACITY = 1;
    /**
     * Threshold at which to increase the array.
     */
    private static final int INCREASE_THRESHOLD = 2;
    /**
     * Threshold at which to decrease the array.
     */
    private static final int DECREASE_THRESHOLD = 4;
    /**
     * The array holding the items.
     */
    private Item [] items;
    /**
     * The current number of items in the array.
     */
    private int size;

    /**
     * The iterator for the RandomizedQueue.
     *
     * @author Brian Terczynski
     */
    private final class RandomizedQueueIterator implements Iterator<Item> {
        /**
         * Holds the shuffled copy of the items.
         */
        private Item [] randomizedArray;
        /**
         * The current index of the iterator.
         */
        private int curIndex;

        /**
         * Constructs the iterator.
         */
        private RandomizedQueueIterator() {
            randomizedArray = (Item []) new Object [size];
            for (int i = 0; i < size; i++) {
                randomizedArray[i] = items[i];
            }
            StdRandom.shuffle(randomizedArray);
            curIndex = 0;
        }

        /**
         * Returns if there are more items over which to iterate.
         *
         * @return
         *     true if there are more items over which to iterate, false if not
         */
        @Override
        public boolean hasNext() {
            return (curIndex < randomizedArray.length);
        }

        /**
         * Returns the next item in the iteration, and advances the iterator
         * pointer.
         *
         * @return
         *     The next item in the iteration.
         */
        @Override
        public Item next() {
            if (curIndex >= randomizedArray.length) {
                throw new NoSuchElementException();
            }
            Item itemToReturn = randomizedArray[curIndex];
            curIndex++;
            return itemToReturn;
        }

        /**
         * Do not use.  Not supported.
         *
         * @throws
         *     UnsupportedOperationException
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                    "remove() is not supported.");
        }
    }

    /**
     * Constructs a randomized queue.
     */
    public RandomizedQueue() {
        items = (Item []) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns whether the queue is empty or not.
     *
     * @return
     *     true if the queue is empty, false if not
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Returns the number of items in the randomized queue.
     *
     * @return
     *     The number of items in the queue.
     */
    public int size() {
        return size;
    }

    /**
     * Adds an item to the randomized queue.
     *
     * @param item
     *     The item to add to the queue.  null is not allowed.
     *
     * @throws
     *     NullPointerException if null is passed in.
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException("null items are not permitted");
        }
        size++;
        resizeIfNeeded();
        items[size - 1] = item;
    }

    /**
     * Removes a random item from the randomized queue.
     *
     * @return
     *     The item removed from the queue.
     *
     * @throws
     *     NoSuchElementException if the queue is empty.
     */
    public Item dequeue() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(size);
        Item itemToReturn = items[index];
        items[index] = items[size - 1];
        items[size - 1] = null;
        size--;
        resizeIfNeeded();
        return itemToReturn;
    }

    /**
     * Returns a random item from the queue.
     *
     * @return
     *     The item sampled from the queue.
     *
     * @throws
     *     NoSuchElementException if the queue is empty.
     */
    public Item sample() {
        if (size <= 0) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniform(size)];
    }

    /**
     * With the size updated, this checks to see if the internal
     * array needs to be resized (either grown or shrunk).
     */
    private void resizeIfNeeded() {
        if (size > items.length) {
            Item[] newArray = (Item[]) new Object [items.length
                                                   * INCREASE_THRESHOLD];
            for (int i = 0; i < items.length; i++) {
                newArray[i] = items[i];
            }
            items = newArray;
        } else if (size > 0 && size <= items.length / DECREASE_THRESHOLD) {
            Item[] newArray = (Item[]) new Object [size];
            for (int i = 0; i < size; i++) {
                newArray[i] = items[i];
            }
            items = newArray;
        }
    }

    /**
     * Returns an iterator for the RandomizedQueue.  Each iterator is
     * independent and likely will return items in different
     * orders.
     * <p>
     * PERFORMANCE: Construction in time linear to the number of items,
     * next() and hasNext() are constant worst-case time.  Up to a linear
     * amount of memory may be used.
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
}
