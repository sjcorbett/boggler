package sjcorbett.trie;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class Tries {

    private Tries() {}

    public static Trie from(URL url) throws IOException {
        List<String> lines = Resources.readLines(url, Charsets.UTF_8);
        return new Trie().addAll(lines);
    }

}
