package sjcorbett.boggle;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    final char[][] grid;

    public Grid(char[][] grid) {
        this.grid = grid;
    }

    public char letterAt(GridReference reference) {
        return letterAt(reference.x, reference.y);
    }

    char letterAt(int x, int y) {
        return this.grid[x][y];
    }

    boolean isInBounds(int x, int y) {
        return x >= 0 && y >= 0 &&
                x < this.grid.length &&
                y < this.grid[x].length;
    }

    Iterable<GridReference> surrounding(GridReference reference) {
        return surrounding(reference.x, reference.y);
    }

    public Iterable<GridReference> surrounding(int x, int y) {
        List<GridReference> s = new ArrayList<>(8);
        for (int a = x - 1; a <= x + 1; a++) {
            for (int b = y - 1; b <= y + 1; b++) {
                if (isInBounds(a, b) && !(a == x && b == y)) {
                    s.add(new GridReference(a, b));
                }
            }
        }

        return s;
    }

}
