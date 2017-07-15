package utilsTests.corpusMakerTests;

import org.junit.BeforeClass;
import org.junit.Test;
import utils.corpus.CorpusCleaner;
import utils.corpus.FileScanner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 15.07.17.
 */
public class CorpusCleanerTest {
    static CorpusCleaner cc;
    static FileScanner fs;
    static ArrayList<String> paths;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        paths = new ArrayList<>();
        paths.add("/src/main/resources/translation/Dict/dict");
        paths.add("/src/main/resources/translation/Emea/emea");
        paths.add("/src/main/resources/translation/ICD10/ICD10");
        paths.add("/src/main/resources/translation/News/News-Commentary11.de-en");
        paths.add("/src/main/resources/translation/Springer/Springer");
        paths.add("/src/main/resources/translation/Wiki/Wikipedia");
    }

    @Test
    public void cleanAllCorpus(){
        String truePath;
        int lineCountDE, newLineCountDE, lineCountEN, newLineCountEN;
        for(String path : paths){
            truePath = path+".de";
            fs = new FileScanner(truePath);
            lineCountDE = fs.getLines();
            cc = new CorpusCleaner(truePath);
            cc.clean(cc.braces());
            truePath = path+".deCleaned";
            fs = new FileScanner(truePath);
            newLineCountDE = fs.getLines();
            assertEquals(lineCountDE,newLineCountDE);

            System.out.println(truePath+" cleaned. "+ lineCountDE+" lines.");

            truePath = path+".en";
            fs = new FileScanner(truePath);
            lineCountEN = fs.getLines();
            cc = new CorpusCleaner(truePath);
            cc.clean(cc.braces());
            truePath = path+".enCleaned";
            fs = new FileScanner(truePath);
            newLineCountEN = fs.getLines();
            assertEquals(lineCountEN,newLineCountEN);

            System.out.println(truePath+" cleaned. "+lineCountEN+" lines.");

            assertEquals(newLineCountDE,newLineCountEN);
        }

    }

    @Test
    public void justLowerCaseForBigCorpra(){
        String path = "/src/main/resources/translation/Wiki/Wikipedia";

        String truePath;
        int lineCountDE, newLineCountDE, lineCountEN, newLineCountEN;
        truePath = path+".de";
        cc = new CorpusCleaner(truePath);
        lineCountDE = cc.clean(cc.specialSigns());
        truePath = path+".deCleaned";

        System.out.println(truePath+" cleaned. "+ lineCountDE+" lines.");

        truePath = path+".en";
        cc = new CorpusCleaner(truePath);
        lineCountEN = cc.clean(cc.specialSigns());
        truePath = path+".enCleaned";

        System.out.println(truePath+" cleaned. "+lineCountEN+" lines.");

        assertEquals(lineCountDE,lineCountEN);
    }

}
