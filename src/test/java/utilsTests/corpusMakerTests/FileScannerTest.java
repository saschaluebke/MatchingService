package utilsTests.corpusMakerTests;

import org.junit.Test;
import utils.corpus.FileScanner;

import static org.junit.Assert.assertEquals;

public class FileScannerTest {
    static FileScanner fs;

    @Test
    public void scanEmea(){
       fs = new FileScanner("/src/main/resources/translation/Emea/emea.de");

        assertEquals(10979502,fs.getWordCount());
        assertEquals(1108752,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/Emea/emea.en");

        assertEquals(12322425,fs.getWordCount());
        assertEquals(1108752,fs.getLines());
    }

    @Test
    public void scanDict(){
        fs = new FileScanner("/src/main/resources/translation/Dict/dict.de");

        assertEquals(3015079,fs.getWordCount());
        assertEquals(1136891,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/Dict/dict.en");

        assertEquals(3497015,fs.getWordCount());
        assertEquals(1136891,fs.getLines());
    }

    @Test
    public void scanSpringer(){
        fs = new FileScanner("/src/main/resources/translation/Springer/Springer.de");

        assertEquals(184565,fs.getWordCount());
        assertEquals(10051,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/Springer/Springer.en");

        assertEquals(199328,fs.getWordCount());
        assertEquals(10051,fs.getLines());
    }

    @Test
    public void scanWiki(){
        fs = new FileScanner("/src/main/resources/translation/Wiki/Wikipedia.de");

        assertEquals(38923205,fs.getWordCount());
        assertEquals(2459662,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/Wiki/Wikipedia.en");

        assertEquals(45456106,fs.getWordCount());
        assertEquals(2459662,fs.getLines());
    }

    @Test
    public void scanAllMax(){
        fs = new FileScanner("/src/main/resources/translation/AllMax/training_language.de");
        int deLines = fs.getLines();
        int deWords = fs.getWordCount();
        assertEquals(28946569,fs.getLines());
        assertEquals(346202197,fs.getWordCount());
        fs = new FileScanner("/src/main/resources/translation/AllMax/training_language.en");
        int enLines = fs.getLines();
        int enWords = fs.getWordCount();
        assertEquals(38923205,fs.getWordCount());
        assertEquals(28946569,fs.getLines());
        System.out.println(deLines+"="+enLines+" & "+deWords+"="+enWords);

    }
}
