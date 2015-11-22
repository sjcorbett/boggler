package sjcorbett.boggle;

import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

public class Stats {

    private final SortedSet<GridWord> words;
    private final GridReference bestPoint;
    private final int bestPointCount;
    private final GridReference bestStart;
    private final int bestStartCount;
    private final String longestWord;
    private final int minWordLength;

    public Stats(Iterable<GridWord> allSolutions, int minWordLength) {
        this.minWordLength = minWordLength;
        this.words = new TreeSet<>();
        int longestWordLength = -1;
        String longestWord = null;
        for (GridWord gridWord : allSolutions) {
            String word = gridWord.word;
            if (word.length() >= minWordLength) {
                if (word.length() > longestWordLength) {
                    longestWordLength = word.length();
                    longestWord = word;
                }
                words.add(gridWord);
            }
        }
        this.longestWord = longestWord;

        Multiset<GridReference> firstLetters = HashMultiset.create();
        Multiset<GridReference> allLetters = HashMultiset.create();
        for (GridWord gridWord : words) {
            firstLetters.add(gridWord.path.get(0));
            allLetters.addAll(gridWord.path);
        }

        allLetters = Multisets.copyHighestCountFirst(allLetters);
        firstLetters = Multisets.copyHighestCountFirst(firstLetters);
        this.bestPoint = allLetters.iterator().next();
        this.bestPointCount = allLetters.count(bestPoint);
        this.bestStart = firstLetters.iterator().next();
        this.bestStartCount = firstLetters.count(bestStart);
    }

    public GridReference getBestPoint() {
        return bestPoint;
    }

    public GridReference getBestStart() {
        return bestStart;
    }

    public int getBestPointCount() {
        return bestPointCount;
    }

    public int getBestStartCount() {
        return bestStartCount;
    }

    public String getLongestWord() {
        return longestWord;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public SortedSet<GridWord> getWords() {
        return words;
    }

    public int getTotalWords() {
        return words.size();
    }

}
