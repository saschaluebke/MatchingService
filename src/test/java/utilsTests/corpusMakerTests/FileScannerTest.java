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
    public void scanICD10(){
        fs = new FileScanner("/src/main/resources/translation/ICD10/ICD10.deCleaned");

        assertEquals(58726,fs.getWordCount());
        assertEquals(10793,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/ICD10/ICD10.enCleaned");

        assertEquals(57638,fs.getWordCount());
        assertEquals(10793,fs.getLines());
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
    public void scanEuroparl(){
        fs = new FileScanner("/src/main/resources/translation/Euparl/Europarl.de-en.deCleaned");

        assertEquals(45348994,fs.getWordCount());
        assertEquals(1940172,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/Euparl/Europarl.de-en.enCleaned");

        assertEquals(48718438,fs.getWordCount());
        assertEquals(1940172,fs.getLines());
    }

    @Test
    public void scanSubtitle(){
        fs = new FileScanner("/src/main/resources/translation/OpenSubtitles2016/OpenSubtitles2016.de-en.deCleaned");

        assertEquals(83452654,fs.getWordCount());
        assertEquals(13209409,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/OpenSubtitles2016/OpenSubtitles2016.de-en.enCleaned");

        assertEquals(87051274,fs.getWordCount());
        assertEquals(13209409,fs.getLines());
    }

    @Test
    public void euBookshop(){
        fs = new FileScanner("/src/main/resources/translation/EUbookshop/EUbookshop.de-en.deCleaned");

        assertEquals(164222141,fs.getWordCount());
        assertEquals(9141225,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/EUbookshop/EUbookshop.de-en.deCleaned");

        assertEquals(164222141,fs.getWordCount());
        assertEquals(9141225,fs.getLines());
    }

    @Test
    public void News(){
        fs = new FileScanner("/src/main/resources/translation/News/News-Commentary11.de-en.de");

        assertEquals(5019086,fs.getWordCount());
        assertEquals(223153,fs.getLines());

        fs = new FileScanner("/src/main/resources/translation/News/News-Commentary11.de-en.en");

        assertEquals(4923246,fs.getWordCount());
        assertEquals(223153,fs.getLines());
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
