package sjcorbett.boggle;

import static org.testng.Assert.assertEquals;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.testng.annotations.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import sjcorbett.trie.Trie;

public class BogglerTest {

    private Set<String> wordsIn(Iterable<GridWord> gridWords) {
        return StreamSupport.stream(gridWords.spliterator(), false)
                .map(GridWord::getWord)
                .collect(Collectors.toSet());
    }

    @Test
    public void testNoSolutionsForEmptyGrid() {
        Trie trie = new Trie().add("a");
        Boggler boggler = new Boggler(trie);
        assertEquals(boggler.solve(new char[][]{}), Collections.emptySet());
    }

    @Test
    public void testSolveSimple() {
        Trie trie = new Trie().add("cat").add("tac");
        Boggler boggler = new Boggler(trie);
        Set<String> solutions = Sets.newHashSet("CAT", "TAC");
        assertEquals(wordsIn(boggler.solve(new char[][]{{'C', 'A', 'T'}})), solutions);
    }

    @Test
    public void testSolveTwoByTwo() {
        Set<String> words = ImmutableSet.of(
                "AB", "AC", "AD", "BA", "BC", "BD", "CA", "CB", "CD", "DA", "DB", "DC");
        Trie trie = new Trie().addAll(words);
        Boggler boggler = new Boggler(trie);
        char[][] grid = {{'A', 'B'}, {'C', 'D'}};
        assertEquals(wordsIn(boggler.solve(grid)), words);
    }

    @Test
    public void testNWSE() {
        Trie trie = new Trie().add("abc");
        char[][] grid = {
                {'A', 'x', 'x'},
                {'x', 'B', 'x'},
                {'x', 'x', 'C'},
        };
        Boggler boggler = new Boggler(trie);
        assertEquals(wordsIn(boggler.solve(grid)), ImmutableSet.of("ABC"));
    }

    @Test
    public void testSWNE() {
        Trie trie = new Trie().add("abc");
        char[][] grid = {
                {'x', 'x', 'A'},
                {'x', 'B', 'x'},
                {'C', 'x', 'x'},
        };
        Boggler boggler = new Boggler(trie);
        assertEquals(wordsIn(boggler.solve(grid)), ImmutableSet.of("ABC"));
    }

    @Test
    public void testSENW() {
        Trie trie = new Trie().add("abc");
        char[][] grid = {
                {'C', 'x', 'x'},
                {'x', 'B', 'x'},
                {'x', 'x', 'A'},
        };
        Boggler boggler = new Boggler(trie);
        assertEquals(wordsIn(boggler.solve(grid)), ImmutableSet.of("ABC"));
    }

    @Test
    public void testSnaking() {
        Trie trie = new Trie().add("snaking");
        char[][] grid = {
                {'x', 'N', 'A'},
                {'S', 'N', 'K'},
                {'x', 'I', 'G'},
        };
        Boggler boggler = new Boggler(trie);
        assertEquals(wordsIn(boggler.solve(grid)), ImmutableSet.of("SNAKING"));
    }

    @Test
    public void testSeveralAnswers() {
        Trie trie = new Trie().add("snaking").add("king").add("nan").add("nans").add("KXI");
        char[][] grid = {
                {'x', 'N', 'A'},
                {'S', 'N', 'K'},
                {'x', 'I', 'G'},
        };
        Boggler boggler = new Boggler(trie);
        assertEquals(wordsIn(boggler.solve(grid)), ImmutableSet.of(
                "SNAKING", "KING", "NAN", "NANS"));
    }

    @Test
    public void testIrregularGrid() {
        Trie trie = new Trie().add("valetudinarians").add("valets");
        char[][] grid = {
                {'V'},
                {'A', 'L'},
                {'E', 'T', 'U'},
                {'I', 'D', 'S', 'N'},
                {'N', 'A', 'R', 'I', 'A'}};
        Boggler boggler = new Boggler(trie);
        assertEquals(wordsIn(boggler.solve(grid)), ImmutableSet.of(
                "VALETS", "VALETUDINARIANS"));
    }

}
