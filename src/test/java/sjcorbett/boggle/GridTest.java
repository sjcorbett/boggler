package sjcorbett.boggle;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class GridTest {

    @Test
    public void testBoundsEmpty() {
        Grid grid = new Grid(new char[][]{});
        assertFalse(grid.isInBounds(0, 0));
    }

    @Test
    public void testSquareGrid() {
        Grid grid = new Grid(new char[][]{{'a', 'b'}, {'c', 'd'}});
        assertTrue(grid.isInBounds(0, 0));
        assertTrue(grid.isInBounds(0, 1));
        assertFalse(grid.isInBounds(0, 2));
        assertFalse(grid.isInBounds(-1, 0));
        assertTrue(grid.isInBounds(1, 0));
        assertTrue(grid.isInBounds(1, 1));
        assertFalse(grid.isInBounds(2, 1));
        assertEquals(grid.letterAt(0, 0), 'a');
        assertEquals(grid.letterAt(0, 1), 'b');
        assertEquals(grid.letterAt(1, 0), 'c');
        assertEquals(grid.letterAt(1, 1), 'd');
    }

    @Test
    public void testIrregularGrid() {
        Grid grid = new Grid(new char[][]{{'a', 'b'}, {'c'}, {'d', 'e'}});
        assertTrue(grid.isInBounds(0, 0));
        assertTrue(grid.isInBounds(0, 1));
        assertTrue(grid.isInBounds(1, 0));
        assertFalse(grid.isInBounds(1, 1));
        assertTrue(grid.isInBounds(2, 0));
        assertTrue(grid.isInBounds(2, 1));
    }

    @Test
    public void testSurrounding() {
        Grid grid = new Grid(new char[][]{{'a', 'b'}, {'c'}, {'d', 'e'}});
        Iterable<GridReference> surrounding = grid.surrounding(0, 1);
        assertEquals(Iterables.size(surrounding), 2, "surrounding=" + surrounding);
        assertEquals(Sets.newHashSet(surrounding), Sets.newHashSet(
                new GridReference(0, 0),
                new GridReference(1, 0)));

    }

}
