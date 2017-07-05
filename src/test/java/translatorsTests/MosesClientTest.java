package translatorsTests;

import org.junit.BeforeClass;
import org.junit.Test;
import translators.MosesClient;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 04.07.17.
 */
public class MosesClientTest {
   static MosesClient mc;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
       mc = new MosesClient("http://localhost:8080/RPC2");
    }

    @Test
    public void getSmallFileContentTest() {
       /* mc.runClient("Er geht in das Haus");
        String result = mc.runClient("Hypotonie hat etwas mit Blut zu tun.");

        assertEquals("Hallo",result);
        */

       String result = mc.translation("Hypotonie|UNK|UNK|UNK |0-0| has something |1-2| to |5-5| tun.|UNK|UNK|UNK |6-6| with blood");
        assertEquals("Hallo",result);
    }

}
