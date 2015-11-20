package sjcorbett.boggle;

import com.google.common.base.MoreObjects;

public class GridReference {
    final int x;
    final int y;

    public GridReference(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridReference that = (GridReference) o;
        if (x != that.x) return false;
        if (y != that.y) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return (31 * x) + y;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("x", x)
                .add("y", y)
                .toString();
    }

    public String toDisplayString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
