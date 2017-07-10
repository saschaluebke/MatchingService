package utilsTests;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by SashBot on 09.07.2017.
 */
public class SpringerReaderTest {
    SpringerReader sr = new SpringerReader("home/sascha/IdeaProjects/Springer/.idea/resources/SpringerGerman.xml");
    ArrayList<String> output = new ArrayList<>();

    @Test
    public void printTest() {
        assertEquals(194, output.size());//TODO richtige zahl angeben
    }
}
