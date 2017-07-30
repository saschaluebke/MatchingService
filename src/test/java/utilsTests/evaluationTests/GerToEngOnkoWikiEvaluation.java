package utilsTests.evaluationTests;

import org.junit.BeforeClass;
import org.junit.Test;
import translators.MosesClient;
import utils.evaluation.Evaluator;
import utils.evaluation.OnkoWikiReader;
import utils.ontology.FileReader;
import utils.ontology.OpenThesaurusReader;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 14.07.17.
 */
public class GerToEngOnkoWikiEvaluation {
    static Evaluator evaluator;
    static ArrayList<FileReader> fileReaders;
    static ArrayList<File> files;
    static String trainingPath1,trainingPath2;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        evaluator = new Evaluator("de","en",new MosesClient());
        fileReaders = new ArrayList<>();
        OpenThesaurusReader otr = new OpenThesaurusReader("/src/main/resources/ontologies/openThesaurus/openthesaurus.txt","de");
        fileReaders.add(otr);
        files = new ArrayList<>();
        for(int i =0; i<2; i++){
            OnkoWikiReader owr = new OnkoWikiReader();
            files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/OnkoWiki/OnkoWikiDaten.txt"));
        }

        System.out.println("init Complete");
    }

    @Test
    public void evaluation(){
        trainingPath2 = "/src/main/resources/translation/ICD10/ICD10.enCleaned";
        trainingPath1 = "/src/main/resources/translation/ICD10/ICD10.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("ICD10Test",files,trainingPath1,trainingPath2);
        assertEquals(143, output.get(0).size());
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("ICD10Test",files,trainingPath1,trainingPath2);
        assertEquals(143, output2.size());
    }

    @Test
    public void evaluationAl(){
        trainingPath2 = "/src/main/resources/translation/AllMin/allCleaned.en";
        trainingPath1 = "/src/main/resources/translation/AllMin/allCleaned.de";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("All",files,trainingPath1,trainingPath2);
        assertEquals(143, output.get(0).size());
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("All",files,trainingPath1,trainingPath2);
        assertEquals(143, output2.size());
    }
}
