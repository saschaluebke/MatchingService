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

    @Test
    public void cleanAllCorpus(){
        paths = new ArrayList<>();
        paths.add("/src/main/resources/translation/Dict/dict");
        paths.add("/src/main/resources/translation/Emea/emea");
        paths.add("/src/main/resources/translation/ICD10/ICD10");
        paths.add("/src/main/resources/translation/News/News-Commentary11.de-en");
        paths.add("/src/main/resources/translation/Springer/Springer");
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
    public void cleanBigCorpus(){
        paths = new ArrayList<>();
        paths.add("/src/main/resources/translation/Wiki/Wikipedia");
        paths.add("/src/main/resources/translation/OpenSubtitles2016/OpenSubtitles2016.de-en");
        paths.add("/src/main/resources/translation/Euparl/Europarl.de-en");
        paths.add("/src/main/resources/translation/EUbookshop/EUbookshop.de-en");

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

    @Test
    public void cleanSnomed(){
        paths = new ArrayList<>();
        paths.add("/src/main/resources/evaluation/Snomed/SnomedCT");
        for(String path : paths){

            cc = new CorpusCleaner(path);
            int lines = cc.cleanCorpus(cc.braces());

            System.out.println(path+" cleaned. "+ lines+" lines.");

            assertEquals(true,true);
        }

    }

    @Test
    public void cleanBracesTest(){
        cc = new CorpusCleaner("/src/main/resources/translation/Wiki/Wikipedia","/src/main/resources/translation/Wiki/Wikipedia");
        String bracles = cc.cleanString(")(hallo)");
         assertEquals("",bracles);

        bracles = cc.cleanString("(hallo))");
        assertEquals("",bracles);

        bracles = cc.cleanString("(hal)()()(h)()())(()()((((()))((/)(/)))((())");
        assertEquals("",bracles);

        bracles = cc.cleanString("(hallo)(Bla blub normaler Text(");
        assertEquals("bla blub normaler text",bracles);

        bracles = cc.cleanString("(hallo]{bla blub normaler text([]}");
        assertEquals("hallo",bracles);
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
