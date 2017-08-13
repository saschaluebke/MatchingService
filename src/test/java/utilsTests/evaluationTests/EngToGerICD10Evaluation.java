package utilsTests.evaluationTests;

import matching.Matcher;
import matching.distance.Levenshtein;
import matching.distance.LevenshteinNormalized;
import matching.iterate.SimpleStrategy;
import matching.iterate.WordPerformanceStrategy;
import matching.iterate.WordStrategy;
import matching.sorting.ScoreSort;
import org.junit.BeforeClass;
import org.junit.Test;
import translators.MosesClient;
import utils.evaluation.Evaluator;
import utils.ontology.FileReader;
import utils.ontology.SpecialistReader;
import utils.ontology.WordNetReader;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Translation Evaluation from Englisch to German with Moses
 */
public class EngToGerICD10Evaluation {
    static Evaluator evaluator;
    static ArrayList<FileReader> fileReaders;
    static ArrayList<File> files;
    static String trainingPath1, trainingPath2;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        evaluator = new Evaluator("en","de",new MosesClient());
        files = new ArrayList<>();
        files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion1.txtCleaned"));
        //files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion2.txtCleaned"));
        //files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion3.txtCleaned"));

        System.out.println("init Complete");

    }


    @Test
    public void evaluationICD10Translate(){
        trainingPath1 = "/src/main/resources/translation/ICD10/ICD10.enCleaned";
        trainingPath2 = "/src/main/resources/translation/ICD10/ICD10.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("ICD10",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinSimple(){
        Matcher matcher = new Matcher(new SimpleStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output = evaluator.synonymTranslate("AllMin",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void evaluationAllMinWordPerformance(){
        Matcher matcher = new Matcher(new WordPerformanceStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output = evaluator.synonymTranslate("AllMin_WordPerformance",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

}
