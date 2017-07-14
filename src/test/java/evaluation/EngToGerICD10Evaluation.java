package evaluation;

import database.dbStrategy.simpleStrategy.SimpleStrategy;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import translators.MosesClient;
import utils.*;

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

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        evaluator = new Evaluator("en","de",new MosesClient());
        fileReaders = new ArrayList<>();
        SpecialistReader sr = new SpecialistReader("/src/main/resources/ontologies/SpecialistLexicon/LEXICON", "en");
        //OwlReader owlr = new OwlReader("/src/main/resources/ontologies/NCI/NCI.owl", "de");
        WordNetReader wnh = new WordNetReader("/src/main/resources/ontologies/WordNet/WordNet-3.0/dict", "en");
        fileReaders.add(sr);
        //fileReaders.add(owlr);
        fileReaders.add(wnh);
        files = new ArrayList<>();
        for(int i =0; i<2; i++){
            files.add(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion"+i+".txt"));
        }
        System.out.println("init Complete");
    }

    @Test
    public void evaluation(){
        ArrayList<ArrayList<String>> output = evaluator.simpleTranslate("DictEn",files,true);
        assertEquals(179, output.get(0).size());
        //ArrayList<ArrayList<String>> output2 = evaluator.synonymTranslate("Springer",new SynonymStrategy(),fileReaders,files,false);
        //assertEquals(179, output2.size());
    }
}
