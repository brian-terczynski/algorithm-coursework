import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Test;


public class DequeTest {

    @Test
    public void testDeque() {
        Deque <String> deque = new Deque <String> ();
        assertEquals(true, deque.isEmpty());
        deque.addFirst("One");
        deque.addFirst("Two");
        assertEquals(2, deque.size());
        assertEquals(false, deque.isEmpty());
        assertEquals ("Two", deque.removeFirst());
        assertEquals ("One", deque.removeFirst());
        assertEquals(0, deque.size());
        assertEquals(true, deque.isEmpty());
        
        deque.addFirst("Three");
        deque.addLast("Four");
        deque.addLast("Five");
        assertEquals(3, deque.size());
        assertEquals(false, deque.isEmpty());
        assertEquals("Five", deque.removeLast());
        assertEquals("Four", deque.removeLast());
        assertEquals("Three", deque.removeLast());
        assertEquals(0, deque.size());
        assertEquals(true, deque.isEmpty());
        
        deque.addLast("A");
        deque.addFirst("B");
        assertEquals(2, deque.size());
        assertEquals ("A", deque.removeLast());
        assertEquals ("B", deque.removeFirst());
        assertEquals(0, deque.size());
        assertEquals(true, deque.isEmpty());
    }

    @Test
    public void testNegativeDeque() {
        Deque <String> deque = new Deque <String> ();
        try {
            deque.addFirst(null);
            fail("No exception thrown");
        } catch (NullPointerException e) {
        }
        try {
            deque.addLast(null);
            fail("No exception thrown");
        } catch (NullPointerException e) {
        }
        try {
            deque.removeFirst();
            fail("No exception thrown");
        } catch (NoSuchElementException e) {
        }
        try {
            deque.removeLast();
            fail("No exception thrown");
        } catch (NoSuchElementException e) {
        }
    }

    @Test
    public void testIterate() {
        Deque <String> deque = new Deque <String> ();
        assertEquals(true, deque.isEmpty());
        deque.addFirst("One");
        deque.addFirst("Two");
        deque.addLast("Three");
        deque.addLast("Four");
        deque.addLast("Five");
        deque.addFirst("Six");
        Iterator<String> iter;
        iter = deque.iterator();
        
        assertEquals(true, iter.hasNext());
        assertEquals("Six", iter.next());
        assertEquals(true, iter.hasNext());
        assertEquals("Two", iter.next());
        assertEquals(true, iter.hasNext());
        assertEquals("One", iter.next());
        assertEquals(true, iter.hasNext());
        assertEquals("Three", iter.next());
        assertEquals(true, iter.hasNext());
        assertEquals("Four", iter.next());
        assertEquals(true, iter.hasNext());
        assertEquals("Five", iter.next());
        assertEquals(false, iter.hasNext());
    }

    @Test
    public void testIterateNegative() {
        Deque <String> deque = new Deque <String> ();
        Iterator<String> iter;
        iter = deque.iterator();
        assertEquals(false, iter.hasNext());
        try {
            iter.next();
            fail ("No exception thrown.");
        } catch (NoSuchElementException e) {
        }
        deque.addFirst("One");
        deque.addFirst("Two");
        deque.addLast("Three");
        deque.addLast("Four");
        deque.addLast("Five");
        deque.addFirst("Six");
        deque.removeFirst();
        deque.removeLast();
        deque.removeFirst();
        deque.removeLast();
        deque.removeFirst();
        deque.removeLast();

        iter = deque.iterator();
        assertEquals(false, iter.hasNext());
        assertEquals(0, deque.size());
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
