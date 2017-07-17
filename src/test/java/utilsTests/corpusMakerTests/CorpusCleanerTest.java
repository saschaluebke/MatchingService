package utilsTests.corpusMakerTests;

import org.junit.BeforeClass;
import org.junit.Test;
import utils.corpus.CorpusCleaner;
import utils.corpus.FileScanner;
import utils.ontology.FileReader;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 15.07.17.
 */
public class CorpusCleanerTest {
    static CorpusCleaner cc;
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
        FileScanner fsDE,fsEN;
        String truePathDE,truePathEN;
        int lineCountDE, newLineCountDE, lineCountEN, newLineCountEN;
        for(String path : paths){
            truePathDE = path+".de";
            truePathEN = path+".en";
            fsDE = new FileScanner(truePathDE);
            fsEN = new FileScanner(truePathEN);
            lineCountDE = fsDE.getLines();
            lineCountEN = fsEN.getLines();
            cc = new CorpusCleaner(truePathDE,truePathEN);
            cc.cleanCorpus(cc.braces());
            truePathDE = path+".deCleaned";
            truePathEN = path+".enCleaned";
            fsDE = new FileScanner(truePathDE);
            fsEN = new FileScanner(truePathEN);
            newLineCountDE = fsDE.getLines();
            newLineCountEN = fsEN.getLines();

            System.out.println(truePathDE+" cleaned. "+ lineCountDE+" lines.");
            System.out.println(truePathEN+" cleaned. "+lineCountEN+" lines.");

            assertEquals(newLineCountDE,newLineCountEN);
        }

    }

    /**
     * This takes very long!
     */
    @Test
    public void cleanWikiCorpus(){
        FileScanner fsDE,fsEN;
        String truePathDE,truePathEN;
        int lineCountDE, newLineCountDE, lineCountEN, newLineCountEN;
        String path = "/src/main/resources/translation/Wiki/Wikipedia";

        truePathDE = path+".de";
        truePathEN = path+".en";
        fsDE = new FileScanner(truePathDE);
        fsEN = new FileScanner(truePathEN);
        lineCountDE = fsDE.getLines();
        lineCountEN = fsEN.getLines();
        cc = new CorpusCleaner(truePathDE,truePathEN);
        cc.cleanCorpus(cc.braces());
        truePathDE = path+".deCleaned";
        truePathEN = path+".enCleaned";
        fsDE = new FileScanner(truePathDE);
        fsEN = new FileScanner(truePathEN);
        newLineCountDE = fsDE.getLines();
        newLineCountEN = fsEN.getLines();

        System.out.println(truePathDE+" cleaned. "+ lineCountDE+" lines.");
        System.out.println(truePathEN+" cleaned. "+lineCountEN+" lines.");

        assertEquals(newLineCountDE,newLineCountEN);

    }
/*
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
*/
}
