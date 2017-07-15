package utilsTests.corpusMakerTests;

import org.junit.Test;
import utils.corpus.WikiReducer;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 11.07.17.
 */
public class WikiReducerTest {
    WikiReducer wr;
    @Test
    public void reduceTest(){
        wr = new WikiReducer("/src/main/resources/Wiki/Wikipedia.de","/src/main/resources/Wiki/Wikipedia.en");
        wr.reduce(200000);
        assertEquals(0, 0);
    }
}
