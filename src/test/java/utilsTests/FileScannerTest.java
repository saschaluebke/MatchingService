package utilsTests;

import org.junit.Test;
import utils.corpus.FileScanner;

import static org.junit.Assert.assertEquals;

public class FileScannerTest {
    static FileScanner fs;

    @Test
    public void scanEmea(){
       fs = new FileScanner("/src/main/resources/translation/Emea/emea.de");
       fs.loadData();

        assertEquals(10979502,fs.getWordCount());
        assertEquals(1108752,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/Emea/emea.en");
        fs.loadData();

        assertEquals(12322425,fs.getWordCount());
        assertEquals(1108752,fs.getLines());
    }

    @Test
    public void scanDict(){
        fs = new FileScanner("/src/main/resources/translation/Dict/dict.de");
        fs.loadData();

        assertEquals(3015079,fs.getWordCount());
        assertEquals(1136891,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/Dict/dict.en");
        fs.loadData();

        assertEquals(3497015,fs.getWordCount());
        assertEquals(1136891,fs.getLines());
    }

    @Test
    public void scanSpringer(){
        fs = new FileScanner("/src/main/resources/translation/Springer/Springer.de");
        fs.loadData();

        assertEquals(184565,fs.getWordCount());
        assertEquals(10051,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/Springer/Springer.en");
        fs.loadData();

        assertEquals(199328,fs.getWordCount());
        assertEquals(10051,fs.getLines());
    }

    @Test
    public void scanWiki(){
        fs = new FileScanner("/src/main/resources/translation/Wiki/Wikipedia.de");
        fs.loadData();

        assertEquals(38923205,fs.getWordCount());
        assertEquals(2459662,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/Wiki/Wikipedia.en");
        fs.loadData();

        assertEquals(45456106,fs.getWordCount());
        assertEquals(2459662,fs.getLines());
    }
}
