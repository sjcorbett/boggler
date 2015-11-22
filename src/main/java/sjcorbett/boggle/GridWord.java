package sjcorbett.boggle;

import java.util.Iterator;
import java.util.List;

public class GridWord implements Comparable<GridWord> {

    public final String word;
    public final List<GridReference> path;

    public GridWord(String word, List<GridReference> path) {
        this.word = word;
        this.path = path;
    }

    public String getPathString() {
        StringBuilder builder = new StringBuilder("[");
        Iterator<GridReference> it = path.iterator();
        while (it.hasNext()) {
            GridReference ref = it.next();
            builder.append(ref.toDisplayString());
            if (it.hasNext()) {
                builder.append(", ");
            }
        }
        return builder.append("]").toString();
    }

    public String getWord() {
        return word;
    }

    public List<GridReference> getPath() {
        return path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridWord gridWord = (GridWord) o;
        if (!word.equals(gridWord.word)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public int compareTo(GridWord other) {
        return word.compareTo(other.word);
    }
}
