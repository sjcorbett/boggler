package sjcorbett;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

import com.google.common.base.Stopwatch;

import io.airlift.airline.Command;
import io.airlift.airline.HelpOption;
import io.airlift.airline.SingleCommand;
import sjcorbett.boggle.Boggler;
import sjcorbett.boggle.Grid;
import sjcorbett.boggle.GridWord;
import sjcorbett.boggle.Reporter;
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
        long trieTime = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        Boggler boggler = new Boggler(trie);

        stopwatch = Stopwatch.createStarted();

        Iterable<GridWord> solutions = boggler.solve(GRID);
        Reporter.reportResults(solutions, stopwatch);
        System.out.println("Built trie in " + trieTime + "ms.");

    }

}
