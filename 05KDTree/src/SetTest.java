import static org.junit.Assert.*;

import org.junit.Test;


public class SetTest {

    @Test
    public void testPointSET() {
        PointSET set = new PointSET();
        assertTrue (set.isEmpty());
        assertEquals (0, set.size());
        assertFalse (set.contains(new Point2D(0.1,0.3)));
        set.insert(new Point2D(0.1,0.3));
        set.insert(new Point2D(1.0,0.0));
        set.insert(new Point2D(0.4,0.4));
        assertEquals (3, set.size());
        assertFalse (set.isEmpty());
        assertTrue (set.contains(new Point2D(0.1,0.3)));
        assertTrue (set.contains(new Point2D(0.4,0.4)));
        assertTrue (set.contains(new Point2D(1.0,0.0)));
    }

    @Test
    public void testKdTree() {
        KdTree set = new KdTree();
        assertTrue (set.isEmpty());
        assertEquals (0, set.size());
        assertFalse (set.contains(new Point2D(0.1,0.3)));
        set.insert(new Point2D(0.1,0.3));
        set.insert(new Point2D(1.0,0.0));
        set.insert(new Point2D(0.4,0.4));
        assertEquals (3, set.size());
        assertFalse (set.isEmpty());
        assertTrue (set.contains(new Point2D(0.1,0.3)));
        assertTrue (set.contains(new Point2D(0.4,0.4)));
        assertTrue (set.contains(new Point2D(1.0,0.0)));
        set.insert(new Point2D(1.0,0.0));
        assertEquals (3, set.size());
    }

}
