package sjcorbett.boggle;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;

import sjcorbett.Main;

public class Reporter {

    private Reporter() {}

    public static void reportResults(Iterable<GridWord> solutions, Stopwatch stopwatch) {
            long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            SortedSet<GridWord> threeLettersOrMore = new TreeSet<>();
            int longestWordLength = -1;
            String longestWord = "";
            for (GridWord gridWord : solutions) {
                String word = gridWord.word;
                if (word.length() >= 3) {
                    if (word.length() > longestWordLength) {
                        longestWordLength = word.length();
                        longestWord = word;
                    }
                    threeLettersOrMore.add(gridWord);
                }
            }

            for (GridWord word : threeLettersOrMore) {
                System.out.println(String.format("%1$-" + longestWordLength + "s", word.word) + " - " + word.getPathString());
            }
            System.out.println("Found " + Iterables.size(solutions) + " words in " + elapsed + "ms.");
            if (Iterables.size(solutions) == 0) return;
            System.out.println("Found " + Iterables.size(threeLettersOrMore) + " words of three letters or more.");
            if (longestWordLength != -1) {
                System.out.println("The longest word is " + longestWordLength + " characters long: " + longestWord);
            }

            Multiset<GridReference> firstLetters = HashMultiset.create();
            Multiset<GridReference> allLetters = HashMultiset.create();
            for (GridWord gridWord : threeLettersOrMore) {
                firstLetters.add(gridWord.path.get(0));
                allLetters.addAll(gridWord.path);
            }

            allLetters = Multisets.copyHighestCountFirst(allLetters);
            firstLetters = Multisets.copyHighestCountFirst(firstLetters);
            GridReference mostUsefulPoint = allLetters.iterator().next();
            System.out.println(String.format("The most useful point in the grid is the %s at %s. It is used in %d words.",
                    Main.GRID.letterAt(mostUsefulPoint), mostUsefulPoint.toDisplayString(), allLetters.count(mostUsefulPoint)));
            GridReference mostUsefulStarter = firstLetters.iterator().next();
            System.out.println(String.format("The most useful point to start words with is the %s at %s. It is used at the start of %d words.",
                    Main.GRID.letterAt(mostUsefulStarter), mostUsefulStarter.toDisplayString(), firstLetters.count(mostUsefulStarter)));

            System.out.println("Reporting complete in " + (stopwatch.elapsed(TimeUnit.MILLISECONDS) - elapsed) + "ms.");
        }

}
