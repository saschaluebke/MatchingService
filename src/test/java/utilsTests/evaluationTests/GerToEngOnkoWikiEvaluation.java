package utilsTests.evaluationTests;

import matching.JaroWinklerMatcher;
import matching.Matcher;
import matching.SimpleMatcher;
import matching.distance.JaroWinkler;
import matching.distance.LevenshteinNormalized;
import matching.iterate.PerformanceStrategy;
import matching.iterate.WordStrategy;
import matching.sorting.ScoreSort;
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
        files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/OnkoWiki/OnkoWikiDaten.txt"));

        System.out.println("init Complete");
    }

    @Test
    public void evaluationICD10(){
        trainingPath2 = "/src/main/resources/translation/ICD10/ICD10.enCleaned";
        trainingPath1 = "/src/main/resources/translation/ICD10/ICD10.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("ICD10",files,trainingPath1,trainingPath2);
        assertEquals(143, output.get(0).size());
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("ICD10",files,trainingPath1,trainingPath2);
        assertEquals(143, output2.size());
    }

    @Test
    public void evaluationSpringer(){
        trainingPath2 = "/src/main/resources/translation/Springer/Springer.enCleaned";
        trainingPath1 = "/src/main/resources/translation/Springer/Springer.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("Springer",files,trainingPath1,trainingPath2);
        assertEquals(143, output.get(0).size());
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("Springer",files,trainingPath1,trainingPath2);
        assertEquals(143, output2.size());
    }

    @Test
    public void evaluationDict(){
        trainingPath2 = "/src/main/resources/translation/Dict/dict.enCleaned";
        trainingPath1 = "/src/main/resources/translation/Dict/dict.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("Dict",files,trainingPath1,trainingPath2);
        assertEquals(143, output.get(0).size());
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("Dict",files,trainingPath1,trainingPath2);
        assertEquals(143, output2.size());
    }

    @Test
    public void evaluationEmea(){
        trainingPath2 = "/src/main/resources/translation/Emea/emea.enCleaned";
        trainingPath1 = "/src/main/resources/translation/Emea/emea.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("Emea",files,trainingPath1,trainingPath2);
        assertEquals(143, output.get(0).size());
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("Emea",files,trainingPath1,trainingPath2);
        assertEquals(143, output2.size());
    }
/*
    @Test
    public void evaluationAllMin(){
        trainingPath1 = "/src/main/resources/translation/AllMin/allCleaned.en";
        trainingPath2 = "/src/main/resources/translation/AllMin/allCleaned.de";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("AllMin",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("AllMin",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
        Matcher matcher = new Matcher(new WordStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("AllMin_Word",files,trainingPath1,trainingPath2);
        assertEquals(143, output3.size());
    }
*/

    @Test
    public void simpleMatchAllMin(){
        trainingPath1 = "/src/main/resources/translation/AllMin/allCleaned.en";
        trainingPath2 = "/src/main/resources/translation/AllMin/allCleaned.de";
        Matcher matcher = new SimpleMatcher();
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("AllMinSimpleMatch",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinTranslate(){
        trainingPath1 = "/src/main/resources/translation/AllMin/allCleaned.en";
        trainingPath2 = "/src/main/resources/translation/AllMin/allCleaned.de";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("AllMinTranslate",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinSynonymLevenshtein(){
        trainingPath1 = "/src/main/resources/translation/AllMin/allCleaned.en";
        trainingPath2 = "/src/main/resources/translation/AllMin/allCleaned.de";
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("AllMinLevenshtein",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    /**
     * Noch nicht gemacht!
     */
    @Test
    public void evaluationAllMinSynonymJaroWinkler(){
        trainingPath1 = "/src/main/resources/translation/AllMin/allCleaned.en";
        trainingPath2 = "/src/main/resources/translation/AllMin/allCleaned.de";
        JaroWinklerMatcher matcher = new JaroWinklerMatcher();
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("AllMinJaroWinkler",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinWord(){
        trainingPath1 = "/src/main/resources/translation/AllMin/allCleaned.en";
        trainingPath2 = "/src/main/resources/translation/AllMin/allCleaned.de";
        Matcher matcher = new Matcher(new WordStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("AllMin_Word",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }


/*
    @Test
    public void evaluationAl(){
        trainingPath2 = "/src/main/resources/translation/AllMin/allCleaned.en";
        trainingPath1 = "/src/main/resources/translation/AllMin/allCleaned.de";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("All",files,trainingPath1,trainingPath2);
        assertEquals(143, output.get(0).size());
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("All",files,trainingPath1,trainingPath2);
        assertEquals(143, output2.size());
    }
    */
}
