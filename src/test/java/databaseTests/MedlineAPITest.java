package databaseTests;
import ontology.MedlineAPI;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Saschbot on 21.03.2017.
 */
public class MedlineAPITest {

        static ontology.MedlineAPI medline;

        @BeforeClass
        public static void onceExecutedBeforeAll() {
            medline = new MedlineAPI();
        }

        @Test
        public void allWordsin() {
            medline.init();
            assertEquals("","");
            //assertEquals("dog",dbh.getWordList("Dog","en").get(0).getName());
        }
}
