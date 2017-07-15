package utilsTests.corpusMakerTests;

import org.junit.Test;
import utils.corpus.SpringerCorpusMaker;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 10.07.17.
 */
public class SpringerCorpusMakerTest {
    String englishPath = System.getProperty("user.dir")+"/src/main/resources/Springer/Englisch";
    String germanPath = System.getProperty("user.dir")+"/src/main/resources/Springer/Deutsch";
    SpringerCorpusMaker sr = new SpringerCorpusMaker(englishPath, germanPath);
/*
    @Test
    public void chunksTest() {
        sr.getFileContent();
        assertEquals(0, 0);
    }
    */
    @Test
    public void annotetedDataTest() {
        int lines =sr.getFileContentByLine();
        assertEquals(10051, lines);
        sr.getFileContent();
    }

}
