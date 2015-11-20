package sjcorbett.boggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;

import sjcorbett.trie.Trie;

public class Boggler {

    private final Trie trie;

    public Boggler(Trie trie) {
        this.trie = trie;
    }

    public Iterable<GridWord> solve(char[][] grid) {
        return solve(new Grid(grid));
    }

    public Iterable<GridWord> solve(Grid grid) {
        Set<GridWord> words = Sets.newHashSet();
        for (int i = 0; i < grid.grid.length; i++) {
            char[] row = grid.grid[i];
            for (int j = 0; j < row.length; j++) {
                words.addAll(solveFrom(grid, i, j));
            }
        }
        return words;
    }

    public Set<GridWord> solveFrom(Grid grid, int x, int y) {
        GridReference startingPoint = new GridReference(x, y);
        Trie.Node currentNode = trie.getRootNode();
        if (currentNode == null) return Collections.emptySet();

        Set<GridWord> words = Sets.newHashSet();
        StringBuilder currentWord = new StringBuilder();
        Deque<Trie.Node> nodes = new LinkedList<>();
        Deque<Deque<GridReference>> path = new LinkedList<>();

        nodes.add(currentNode);
        path.add(new LinkedList<>(ImmutableList.of(startingPoint)));

        // Head of head of path is the next grid reference to examine.
        while (!path.isEmpty()) {
            Deque<GridReference> head = path.peek();

            // if the head is empty then remove it and roll back a letter.
            if (head.isEmpty()) {
                path.pop();
                if (!path.isEmpty()) {
                    path.peek().pop();
                    currentWord.deleteCharAt(currentWord.length() - 1);
                }
                nodes.pop();
            } else {
                // Otherwise, there's a new grid reference to check.
                GridReference ref = head.peek();
                char nextLetter = grid.letterAt(ref);
                // if the current node has no child with the next letter then there's no point continuing. pop the current head.
                Trie.Node child = nodes.peek().getChild(nextLetter);
                if (child == null) {
                    head.pop();
                } else {
                    nodes.push(child);
                    currentWord.append(nextLetter);
                    // Does this character make a word?
                    if (child.isTerminal()) {
                        words.add(new GridWord(currentWord.toString(), currentPath(path)));
                    }
                    // Investigate all of the surrounding nodes that aren't on the current path.
                    Deque<GridReference> nextSteps = new LinkedList<>();
                    for (GridReference candidate : grid.surrounding(ref)) {
                        if (!hasAlreadyVisited(candidate, path)) {
                            nextSteps.push(candidate);
                        }
                    }
                    path.push(nextSteps);

                }
            }
        }

        return words;
    }

    private boolean hasAlreadyVisited(GridReference reference, Deque<Deque<GridReference>> path) {
        for (Deque<GridReference> previous : path) {
            if (previous.peek().equals(reference)) {
                return true;
            }
        }
        return false;
    }

    private List<GridReference> currentPath(Deque<Deque<GridReference>> path) {
        List<GridReference> route = new ArrayList<>(path.size());
        Iterator<Deque<GridReference>> it = path.descendingIterator();
        while (it.hasNext()) {
            route.add(it.next().peek());
        }
        return route;
    }

}
