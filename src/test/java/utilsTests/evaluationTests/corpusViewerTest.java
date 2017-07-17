package utilsTests.evaluationTests;

import org.junit.Test;
import utils.evaluation.CorpusViewer;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 17.07.17.
 */
public class corpusViewerTest {
    static CorpusViewer cv;

    @Test
    public void hausTest(){
        cv = new CorpusViewer("/src/main/resources/translation/Wiki/Wikipedia.de","/src/main/resources/translation/Wiki/Wikipedia.en");

        ArrayList<String> lines = cv.getLinesWithWord("Haus");
        assertEquals(44386, lines.size());
        cv.printWordFromCorpus("Haus");
    }
}
