import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class implements a Deque data structure.
 * <p>
 * Performance:  Each operation runs in constant worst-case time.
 * Space use is proportional to the number of items currently stored.
 *
 * @author Brian Terczynski
 *
 * @param <Item>
 *     The items to store in the deque.
 */
public class Deque<Item> implements Iterable<Item> {
    /**
     * Pointer to the head of the deque.
     */
    private Node<Item> first = null;
    /**
     * Pointer to the tail of the deque.
     */
    private Node<Item> last = null;
    /**
     * The current size of the deque.
     */
    private int size = 0;

    /**
     * Internal class representing a node in the deque data structure.
     *
     * @author Brian Terczynski
     *
     * @param <Item>
     *     The item stored in the payload of the Node.
     */
    private static class Node<Item> {
        /**
         * The item.
         */
        private Item item;
        /**
         * The previous node in the deque.
         */
        private Node<Item> prev;
        /**
         * The next node in the deque.
         */
        private Node<Item> next;
    }

    /**
     * An iterator for the deque.
     *
     * @author Brian Terczynski
     */
    private final class DequeIterator implements Iterator<Item>{
        /**
         * Pointer to the current item.
         */
        private Deque.Node<Item> pointer;

        /**
         * Constructs the DequeIterator.
         */
        private DequeIterator() {
            pointer = first;
        }

        /**
         * Returns true if there is still an item to be consumed
         * in this iterator.
         *
         * @return
         *     true if there is an item; false if not
         */
        @Override
        public boolean hasNext() {
            return (pointer != null);
        }

        /**
         * Returns the current item in the iterator, and advances the pointer.
         *
         * @return
         *     The current item in the iterator.
         */
        @Override
        public Item next() {
            if (pointer == null) {
                throw new NoSuchElementException();
            }
            Item itemToReturn = pointer.item;
            pointer = pointer.prev;
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
            throw new UnsupportedOperationException (
                    "remove() is not supported.");
        }
    }

    /**
     * Constructs an empty deque.
     */
    public Deque() {
    }

    /**
     * Returns true if this deque is empty.
     *
     * @return
     *     True if the deque is empty, false if not.
     */
    public boolean isEmpty() {
        return size <= 0;
    }

    /**
     * Returns the number of items in the deque.
     * 
     * @return
     *     The number of items in the deque.
     */
    public int size() {
        return size;
    }

    /**
     * Adds one item to the head of the deque.
     *
     * @param item
     *     The item to add.
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException("You cannot add a null item.");
        }
        Node<Item> newNode = new Node<Item>();
        newNode.item = item;
        newNode.prev = first;
        newNode.next = null;
        if (first != null) {
            first.next = newNode;
        }
        first = newNode;
        if (last == null) {
            last = first;
        }
        size++;
    }

    /**
     * Adds one item to the tail of the deque.
     *
     * @param item
     *     The item to add.
     */
    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException("You cannot add a null item.");
        }
        Node<Item> newNode = new Node<Item>();
        newNode.item = item;
        newNode.prev = null;
        newNode.next = last;
        if (last != null) {
            last.prev = newNode;
        }
        last = newNode;
        if (first == null) {
            first = last;
        }
        size++;
    }

    /**
     * Removes one item from the head of the deque, and returns the
     * removed item.
     *
     * @return
     *     The item that was removed.
     */
    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException("The Deque is empty.");
        }
        Item itemToReturn = first.item;
        Node<Item> newFirst = first.prev;
        first.prev = null;
        first.item = null;
        first = newFirst;
        if (first != null) {
            first.next = null;
        } else {
            last = null;
        }
        size--;
        return itemToReturn;
    }

    /**
     * Removes one item from the tail of the deque, and return the
     * removed item.
     *
     * @return
     *     The item that was removed.
     */
    public Item removeLast() {
        if (last == null) {
            throw new NoSuchElementException("The Deque is empty.");
        }
        Item itemToReturn = last.item;
        Node<Item> newLast = last.next;
        last.next = null;
        last.item = null;
        last = newLast;
        if (last != null) {
            last.prev = null;
        } else {
            first = null;
        }
        size--;
        return itemToReturn;
    }

    /**
     * Returns an iterator for this deque.
     * <p>
     * Performance: Construction, next() and hasNext()
     * run in constant worst-case time.  Space use is constant per iterator.
     *
     * @return
     *     The iterator for this deque.
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
}
