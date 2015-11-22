package sjcorbett;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterables;

import io.airlift.airline.Command;
import io.airlift.airline.HelpOption;
import io.airlift.airline.SingleCommand;
import sjcorbett.boggle.Boggler;
import sjcorbett.boggle.Grid;
import sjcorbett.boggle.GridReference;
import sjcorbett.boggle.GridWord;
import sjcorbett.boggle.Stats;
import sjcorbett.trie.Trie;
import sjcorbett.trie.Tries;

@Command(name = "boggle", description = "Solves Boggle puzzles")
public class Main {

    @Inject
    public HelpOption helpOption;

    //@Option(name = {"--grid"}, description = "File containing grid")
    //public File gridFile;

    public static final Grid GRID = new Grid(new char[][]{
            {'C', 'U', 'B', 'S', 'I', 'D', 'S', 'E', 'V'},
            {'L', 'R', 'A', 'T', 'E', 'H', 'L', 'Y', 'A'},
            {'S', 'C', 'A', 'B', 'D', 'I', 'A', 'L', 'L'},
            {'U', 'K', 'G', 'O', 'O', 'S', 'E', 'D', 'I'},
            {'M', 'A', 'I', 'N', 'H', 'T', 'Y', 'A', 'M'},
            {'T', 'O', 'A', 'M', 'U', 'O', 'D', 'N', 'U'},
            {'O', 'I', 'A', 'D', 'E', 'R', 'R', 'O', 'T'},
            {'R', 'K', 'I', 'R', 'T', 'Y', 'A', 'L', 'E'},
            {'A', 'N', 'V', 'I', 'L', 'V', 'T', 'L', 'D'}});


    public static void main(String[] args) throws Exception {
        Main main = SingleCommand.singleCommand(Main.class).parse(args);
        if (!main.helpOption.showHelpIfRequested()) {
            main.run();
        }
    }

    public void run() throws Exception {
        URL file = Main.class.getResource("/sowpods.txt");
        Stopwatch stopwatch = Stopwatch.createStarted();
        Trie trie = Tries.from(file);
        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("Built trie in " + elapsed + "ms.");
        Boggler boggler = new Boggler(trie);

        stopwatch = Stopwatch.createStarted();

        Iterable<GridWord> solutions = boggler.solve(GRID);
        System.out.println("Found " + Iterables.size(solutions) + " words in " + (stopwatch.elapsed(TimeUnit.MILLISECONDS) - elapsed) + "ms.");
        elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);

        Stats stats = new Stats(solutions, 3);
        int longestWordLength = stats.getLongestWord().length();

        for (GridWord word : stats.getWords()) {
            System.out.println(String.format("%1$-" + longestWordLength + "s", word.word) + " - " + word.getPathString());
        }

        if (Iterables.size(solutions) == 0) return;
        System.out.println("Found " + stats.getTotalWords() + " words of three letters or more.");
        if (longestWordLength != -1) {
            System.out.println("The longest word is " + longestWordLength + " characters long: " + stats.getLongestWord());
        }

        GridReference mostUsefulPoint = stats.getBestPoint();
        GridReference mostUsefulStarter = stats.getBestStart();
        System.out.println(String.format("The most useful point in the grid is the %s at %s. It is used in %d words.",
                Main.GRID.letterAt(mostUsefulPoint), mostUsefulPoint.toDisplayString(), stats.getBestPointCount()));
        System.out.println(String.format("The most useful point to start words with is the %s at %s. It is used at the start of %d words.",
                Main.GRID.letterAt(mostUsefulStarter), mostUsefulStarter.toDisplayString(), stats.getBestStartCount()));

        System.out.println("Reporting complete in " + (stopwatch.elapsed(TimeUnit.MILLISECONDS) - elapsed) + "ms.");
    }

}
