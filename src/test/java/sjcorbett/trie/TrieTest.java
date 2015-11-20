package sjcorbett.trie;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import org.testng.annotations.Test;

public class TrieTest {

    @Test
    public void testAddOne() {
        Trie trie = new Trie();
        assertFalse(trie.isWord("cat"));
        trie.add("cat");
        assertTrue(trie.isWord("cat"));
        assertTrue(trie.isWord("CAT"));
        assertTrue(trie.isWord("cAt"));
        assertFalse(trie.isWord("d"));
    }

    @Test
    public void testAddAFew() {
        Trie trie = new Trie();
        trie.add("cat");
        trie.add("cater");
        trie.add("mister");
        trie.add("miser");
        trie.add("boxer");
        assertTrue(trie.isWord("cat"));
        assertTrue(trie.isWord("cater"));
        assertFalse(trie.isWord("cate"));
    }

    @Test
    public void testLoadUsrShareDict() throws Exception {
        File file = new File("/usr/share/dict/words");
        Trie trie = Tries.from(file.toURI().toURL());
        assertTrue(trie.isWord("zygotaxis"));
        assertFalse(trie.isWord("sixatogyz"));
    }

    @Test
    public void testLoadSowpods() throws Exception {
        URL file = getClass().getResource("/sowpods.txt");
        Trie trie = Tries.from(file);
        assertTrue(trie.isWord("REAPPRAISED"));
        assertFalse(trie.isWord("q"));
    }

}
