package utilsTests.evaluationTests;

import matching.Matcher;
import matching.distance.LevenshteinNormalized;
import matching.iterate.PerformanceStrategy;
import matching.iterate.WordPerformanceStrategy;
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
 * Created by sashbot on 12.08.17.
 */
public class EngToGerTest {
    static Evaluator evaluator;
    static ArrayList<FileReader> fileReaders;
    static ArrayList<File> files;
    static String trainingPath1, trainingPath2;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        evaluator = new Evaluator("en","de",new MosesClient());
        files = new ArrayList<>();
        files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/Test/test"));


        System.out.println("init Complete");

    }

    @Test
    public void evaluationSpringerPerformanceLevenshtein() {
        Matcher matcher = new Matcher(new WordPerformanceStrategy(), new LevenshteinNormalized(), new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output = evaluator.synonymTranslate("Test_PerformanceLevenshtein", files, trainingPath1, trainingPath2);
        assertEquals(true, true);
    }


    @Test
    public void evaluationSimple() {
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("Test_SimpleSnomed", files, trainingPath1, trainingPath2);
        assertEquals(true, true);
    }
}
