package utilsTests.evaluationTests;

import matching.Matcher;
import matching.distance.EqualDistance;
import matching.distance.JaroWinkler;
import matching.distance.LevenshteinNormalized;
import matching.iterate.*;
import matching.sorting.IntervalSort;
import matching.sorting.ScoreSort;
import org.junit.BeforeClass;
import org.junit.Test;
import translators.MosesClient;
import utils.evaluation.Evaluator;
import utils.ontology.FileReader;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 27.07.17.
 */
public class EngToGerSnomedEvaluation {
    static Evaluator evaluator;
    static ArrayList<FileReader> fileReaders;
    static ArrayList<File> files;
    static String trainingPath1, trainingPath2;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        evaluator = new Evaluator("en","de",new MosesClient());
        files = new ArrayList<>();
        files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/Snomed/SnomedCTCleaned"));


        System.out.println("init Complete");

    }

    /**
     * AllMin Evaluations
     */
/*
    @Test
    public void evaluationAllMinSimple(){
        trainingPath1 = "/src/main/resources/translation/AllMin/allCleaned.en";
        trainingPath2 = "/src/main/resources/translation/AllMin/allCleaned.de";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("AllMin",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }
*/
    @Test
    public void evaluationSpringerPerformanceLevenshtein() {
        Matcher matcher = new Matcher(new PerformanceStrategy(), new LevenshteinNormalized(), new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output = evaluator.synonymTranslate("AllMin_PerformanceLevenshtein", files, trainingPath1, trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinSynonymWordLevenshtein(){
        Matcher matcher = new Matcher(new WordStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("AllMin_WordLevenshtein",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinSynonymWordPerformanceLevenshtein(){
        Matcher matcher = new Matcher(new WordPerformanceStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("AllMin_WordPerformanceLevenshtein",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinSynonymSimpleLevenshtein(){
        Matcher matcher = new Matcher(new SimpleStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("AllMin_SimpleLevenshtein",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinSynonymWordJW(){
        Matcher matcher = new Matcher(new WordStrategy(),new JaroWinkler(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("AllMin_WordJW",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinSynonymWordPerformanceJW(){
        Matcher matcher = new Matcher(new WordPerformanceStrategy(),new JaroWinkler(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("AllMin_WordPerformanceJW",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinSynonymWordSubstring(){
        Matcher matcher = new Matcher(new WordStrategy(),new SubstringDistance(),new IntervalSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("AllMin_WordSubstring",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinSynonymWordEqual(){
        Matcher matcher = new Matcher(new WordStrategy(),new EqualDistance(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("AllMin_WordEqual",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    /**
     * AllMinEvaluations End

     * Other Evaluations Begin (Every Test from now on need a specific Moses Server!
     */

    @Test
    public void evaluationSpringerTranslate(){
        trainingPath1 = "/src/main/resources/translation/Springer/Springer.enCleaned";
        trainingPath2 = "/src/main/resources/translation/Springer/Springer.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("Springer",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationDictTranslate(){
        trainingPath1 = "/src/main/resources/translation/Dict/dict.enCleaned";
        trainingPath2 = "/src/main/resources/translation/Dict/dict.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("Dict",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationICD10Translate(){
        trainingPath1 = "/src/main/resources/translation/ICD10/ICD10.enCleaned";
        trainingPath2 = "/src/main/resources/translation/ICD10/ICD10.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("ICD10",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationEmeaTranslate(){
        trainingPath1 = "/src/main/resources/translation/Emea/emea.enCleaned";
        trainingPath2 = "/src/main/resources/translation/Emea/emea.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("Emea",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationNewsTranslate(){
        trainingPath1 = "/src/main/resources/translation/News/News-Commentary11.de-en.enCleaned";
        trainingPath2 = "/src/main/resources/translation/News/News-Commentary11.de-en.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("News",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

}
