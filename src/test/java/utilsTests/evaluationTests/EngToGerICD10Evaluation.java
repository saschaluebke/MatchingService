package utilsTests.evaluationTests;

import matching.Matcher;
import matching.distance.Levenshtein;
import matching.distance.LevenshteinNormalized;
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

       // fileReaders = new ArrayList<>();
       // SpecialistReader sr = new SpecialistReader("/src/main/resources/ontologies/SpecialistLexicon/LEXICON", "en");
        //OwlReader owlr = new OwlReader("/src/main/resources/ontologies/NCI/NCI.owl", "de");
       // WordNetReader wnh = new WordNetReader("/src/main/resources/ontologies/WordNet/WordNet-3.0/dict", "en");
       // fileReaders.add(sr);
        //fileReaders.add(owlr);
       // fileReaders.add(wnh);
        files = new ArrayList<>();
        for(int i =0; i<2; i++){
            files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion"+i+".txt"));
        }
        trainingPath1 = "/src/main/resources/translation/ICD10/ICD10.enCleaned";
        trainingPath2 = "/src/main/resources/translation/ICD10/ICD10.deCleaned";
        System.out.println("init Complete");

    }

    @Test
    public void evaluation(){
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("DictEn",files,trainingPath1,trainingPath2);
        assertEquals(179, output.get(0).size());
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("ICD10Performance",files,trainingPath1,trainingPath2);
        assertEquals(179, output2.size());
        /*Matcher matcher = new Matcher(new WordStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("ICD10WordStr",files);
        assertEquals(179, output3.size());*/
    }
}
