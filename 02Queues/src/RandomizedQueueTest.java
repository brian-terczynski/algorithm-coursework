import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;


public class RandomizedQueueTest {

    @Test
    public void testRQ() {
        RandomizedQueue<String> rq = new RandomizedQueue<String> ();
        assertTrue (rq.isEmpty());
        rq.enqueue("One");
        rq.enqueue("Two");
        rq.enqueue("Three");

        assertEquals (3, rq.size());
        assertFalse (rq.isEmpty());

        System.out.println(rq.sample());
        System.out.println(rq.sample());
        System.out.println(rq.sample());
        System.out.println(rq.sample());
        
        System.out.println(rq.dequeue());
        assertEquals (2, rq.size());
        System.out.println(rq.dequeue());
        System.out.println(rq.dequeue());
        assertEquals (0, rq.size());
        assertTrue (rq.isEmpty());
        
        rq.enqueue("test");
    }

    @Test
    public void testRQNegative() {
        RandomizedQueue<String> rq = new RandomizedQueue<String> ();
        assertTrue (rq.isEmpty());
        try {
            rq.enqueue(null);
            fail("No exception thrown.");
        } catch (NullPointerException e) {
        }
        try {
            rq.dequeue();
            fail("No exception thrown.");
        } catch (NoSuchElementException e) {
        }
        try {
            rq.sample();
            fail("No exception thrown.");
        } catch (NoSuchElementException e) {
        }
    }

    @Test
    public void testRQIterator() {
        RandomizedQueue<String> rq = new RandomizedQueue<String> ();
        assertTrue (rq.isEmpty());
        rq.enqueue("One");
        rq.enqueue("Two");
        rq.enqueue("Three");
        rq.enqueue("Four");

        assertEquals (4, rq.size());
        assertFalse (rq.isEmpty());

        Iterator<String> iter;
        iter = rq.iterator();

        assertTrue(iter.hasNext());
        System.out.println("Iter:" + iter.next());
        assertTrue(iter.hasNext());
        System.out.println("Iter:" + iter.next());
        assertTrue(iter.hasNext());
        System.out.println("Iter:" + iter.next());
        assertTrue(iter.hasNext());
        System.out.println("Iter:" + iter.next());
        assertFalse(iter.hasNext());
    }

    @Test
    public void testRQIteratorNegative() {
        RandomizedQueue<String> rq = new RandomizedQueue<String> ();
        Iterator<String> iter;
        iter = rq.iterator();
        assertEquals(false, iter.hasNext());
        try {
            iter.next();
            fail ("No exception thrown.");
        } catch (NoSuchElementException e) {
        }
        rq.enqueue("One");
        rq.enqueue("Two");
        rq.enqueue("Three");
        rq.dequeue();
        rq.dequeue();
        rq.dequeue();

        iter = rq.iterator();
        assertEquals(false, iter.hasNext());
        assertEquals(0, rq.size());
        try {
            iter.next();
            fail ("No exception thrown.");
        } catch (NoSuchElementException e) {
        }
        try {
            iter.remove();
            fail ("No exception thrown.");
        } catch (UnsupportedOperationException e) {
        }
    }

}
