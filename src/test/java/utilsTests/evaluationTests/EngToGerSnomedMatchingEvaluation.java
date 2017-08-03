package utilsTests.evaluationTests;

import matching.Matcher;
import matching.SimpleMatcher;
import matching.distance.LevenshteinNormalized;
import matching.iterate.WordStrategy;
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
 * Created by sashbot on 31.07.17.
 */
public class EngToGerSnomedMatchingEvaluation {
    static Evaluator evaluator;
    static ArrayList<FileReader> fileReaders;
    static ArrayList<File> files;
    static String trainingPath1, trainingPath2;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        evaluator = new Evaluator("en","de",new MosesClient());

        // fileReaders = new ArrayList<>();
        // SpecialistReader sr = new SpecialistReader("/src/main/resources/ontologies/SpecialistLexicon/LEXICON", "en");
        //OwlReader owlr = new OwlReader("/src/main/resources/ontologies/NCI/NCI.owl", "de");
        // WordNetReader wnh = new WordNetReader("/src/main/resources/ontologies/WordNet/WordNet-3.0/dict", "en");
        // fileReaders.add(sr);
        //fileReaders.add(owlr);
        // fileReaders.add(wnh);
        files = new ArrayList<>();
        //for(int i =0; i<4; i++){
        //    files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion"+i+".txt"));
        //}
        files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/Snomed/SnomedCTCleaned"));


        System.out.println("init Complete");

    }

    @Test
    public void levenshtein(){
        trainingPath1 = "/src/main/resources/translation/Springer/Springer.enCleaned";
        trainingPath2 = "/src/main/resources/translation/Springer/Springer.deCleaned";
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("Springer",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
    }

    @Test
    public void levenshteinWordMatching(){
        trainingPath1 = "/src/main/resources/translation/Springer/Springer.enCleaned";
        trainingPath2 = "/src/main/resources/translation/Springer/Springer.deCleaned";
        Matcher matcher = new Matcher(new WordStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("ICD10WordStr",files,trainingPath1,trainingPath2);
        assertEquals(209, output3.size());
    }

    @Test
    public void simpleMatch(){
        trainingPath1 = "/src/main/resources/translation/Springer/Springer.enCleaned";
        trainingPath2 = "/src/main/resources/translation/Springer/Springer.deCleaned";
        Matcher matcher = new SimpleMatcher();
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("ICD10WordStr",files,trainingPath1,trainingPath2);
        assertEquals(209, output3.size());
    }
}
