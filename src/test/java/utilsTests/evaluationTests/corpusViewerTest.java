package utilsTests.evaluationTests;

import org.junit.Test;
import utils.evaluation.CorpusViewer;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class corpusViewerTest {
    static CorpusViewer cv;

    @Test
    public void hausTest(){
        cv = new CorpusViewer("/src/main/resources/translation/Wiki/Wikipedia.de","/src/main/resources/translation/Wiki/Wikipedia.en");

        ArrayList<ArrayList<String>> lines = cv.getLinesWithWord("begriffe ohne gruppenzugehörigkeit");
        assertEquals(2, lines.size()); //two words stationen and automobil
        assertEquals(472,lines.get(0).size()); //stationen is very often in the wikicorpus
        assertEquals(0,lines.get(1).size()); // automobil is not in wikicorpus
    }
}
