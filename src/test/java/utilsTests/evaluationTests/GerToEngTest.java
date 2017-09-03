package utilsTests.evaluationTests;

import matching.Matcher;
import matching.distance.JaroWinkler;
import matching.distance.SubstringDistance;
import matching.iterate.WordSimpleStrategy;
import matching.iterate.WordStrategy;
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
 * Created by sashbot on 13.08.17.
 */
public class GerToEngTest {
    static Evaluator evaluator;
    static ArrayList<FileReader> fileReaders;
    static ArrayList<File> files;
    static String trainingPath1, trainingPath2;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        evaluator = new Evaluator("de","en",new MosesClient());
        files = new ArrayList<>();
        files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/Test/testGerman"));


        System.out.println("init Complete");

    }

    @Test
    public void evaluationSpringerPerformanceLevenshtein() {
        Matcher matcher = new Matcher(new WordSimpleStrategy(), new JaroWinkler(), new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output = evaluator.synonymTranslate("Test_WordPerformanceJaroWinkler", files, trainingPath1, trainingPath2);
        assertEquals(true, true);
    }


    @Test
    public void evaluationSpringerSubstring() {
        Matcher matcher = new Matcher(new WordStrategy(), new SubstringDistance(), new IntervalSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output = evaluator.synonymTranslate("Test_WordSubstring", files, trainingPath1, trainingPath2);
        assertEquals(true, true);
    }
}
