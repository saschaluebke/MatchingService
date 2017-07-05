package translatorsTests;

import org.junit.BeforeClass;
import org.junit.Test;
import translators.Transltr;

import static org.junit.Assert.assertEquals;

/**
 * Created by Saschbot on 12.03.2017.
 */
public class TransltrTest {
    static Transltr transltr;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        transltr = new Transltr();
    }


    //Not stable...
    @Test
    public void singleTranslationTest() {
        String translation = transltr.translate("dog");
        assertEquals("Hund",translation);
    }

/*
    @Test
    public void multiTranslationTest() {
        String translation = transltr.translate("dog cat");
        assertEquals("Hund Katze",translation);
    }
*/
}
