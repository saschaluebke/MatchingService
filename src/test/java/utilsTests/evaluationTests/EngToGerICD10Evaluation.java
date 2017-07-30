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
        //for(int i =0; i<4; i++){
        //    files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion"+i+".txt"));
        //}
        //TODO: for debugging a smaller Input!
        files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion1.txtCleaned"));
        files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion2.txtCleaned"));
        files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion3.txtCleaned"));

        System.out.println("init Complete");

    }

    @Test
    public void evaluationEmea(){
        trainingPath1 = "/src/main/resources/translation/Emea/emea.enCleaned";
        trainingPath2 = "/src/main/resources/translation/Emea/emea.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("Emea",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("Emea",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
        /*Matcher matcher = new Matcher(new WordStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("ICD10WordStr",files);
        assertEquals(179, output3.size());*/
    }

    @Test
    public void evaluationICD10(){
        trainingPath1 = "/src/main/resources/translation/ICD10/ICD10.enCleaned";
        trainingPath2 = "/src/main/resources/translation/ICD10/ICD10.deCleaned";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("ICD10",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("ICD10",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
        /*Matcher matcher = new Matcher(new WordStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("ICD10WordStr",files);
        assertEquals(179, output3.size());*/
    }


    @Test
    public void evaluationAll(){
        trainingPath1 = "/src/main/resources/translation/AllMin/allCleaned.en";
        trainingPath2 = "/src/main/resources/translation/AllMin/allCleaned.de";
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("All",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
        ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("All",files,trainingPath1,trainingPath2);
        assertEquals(true, true);
        /*Matcher matcher = new Matcher(new WordStrategy(),new LevenshteinNormalized(),new ScoreSort());
        evaluator.setMatcher(matcher);
        ArrayList<ArrayList<String>> output3 = evaluator.synonymTranslate("ICD10WordStr",files);
        assertEquals(179, output3.size());*/
    }
}
