package utilsTests.evaluationTests;

import database.dbStrategy.simpleStrategy.SynonymStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import translators.MosesClient;
import utils.evaluation.Evaluator;
import utils.ontology.FileReader;
import utils.ontology.OpenThesaurusReader;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 13.07.17.
 */
public class EvaluatorTest {
    static Evaluator evaluator;
    static ArrayList<FileReader> fileReaders;
    static ArrayList<File> files;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        evaluator = new Evaluator("en","de",new MosesClient());
        fileReaders = new ArrayList<>();
        OpenThesaurusReader otr = new OpenThesaurusReader("/src/main/resources/ontologies/Test/TestOntology", "en");
        //OwlReader owlr = new OwlReader("/src/main/resources/ontologies/NCI/Thesaurus-byName.owl", "de");
        //WordNetReader wnh = new WordNetReader("/src/main/resources/ontologies/WordNet/WordNet-3.0/dict", "en");
        fileReaders.add(otr);
        //fileReaders.add(owlr);
        //fileReaders.add(wnh);
        files = new ArrayList<>();
        for(int i =0; i<2; i++){
            files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/Test/TestFile"+i+".en.txt"));
        }
        System.out.println("init Complete");
    }


    /**
     * simpleTranslateTest: Moses has only translation for "cat" not "Cat", even if they have the same translation Katze
     * synonymTranslateTest: Now The DB tells Moses that he has to translate "Cat" as "cat"
     */

    @Test
    public void simpleTranslateTest(){
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("Springer",files,false);
        String outString = output.get(0).get(0);
        outString = outString.trim();
        assertEquals("Hallo", outString);
        outString = output.get(0).get(1);
        outString = outString.trim();
        assertEquals("Cat", outString);
        outString = output.get(0).get(2);
        outString = outString.trim();
        assertEquals("Katze", outString);

        //ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate(new SynonymStrategy(),fileReaders,files,false);
        //assertEquals(178, output2.get(2).size());
    }

    @Test
    public void synonymTranslateTest(){
        ArrayList<ArrayList<String>> output = evaluator.synonymTranslate("Springer",fileReaders,files,false);
        String outString = output.get(0).get(0);
        outString = outString.trim();
        assertEquals("Hallo", outString);
        outString = output.get(1).get(0);
        outString = outString.trim();
        assertEquals("Katze", outString);
        outString = output.get(2).get(1);
        outString = outString.trim();
        assertEquals("Katze", outString);

        //ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate(new SynonymStrategy(),fileReaders,files,false);
        //assertEquals(178, output2.get(2).size());
    }

}
