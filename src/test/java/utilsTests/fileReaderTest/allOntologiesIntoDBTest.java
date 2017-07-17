package utilsTests.fileReaderTest;

import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import databaseTests.DBQueryTest;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.ontology.*;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 15.07.17.
 */
public class allOntologiesIntoDBTest {
    static OpenThesaurusReader openThesaurusReader;
    static OwlReader owlReader;
    static SpecialistReader specialistReader;
    static WordNetReader wordNetReader;
    static MySQLQuery dbq;
    static DBHelper dbh;
    static ArrayList<FileReader> frList;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh = new DBHelper(new SynonymStrategy());
        openThesaurusReader = new OpenThesaurusReader("/src/main/resources/ontologies/openThesaurus/openthesaurus.txt","de");
        owlReader = new OwlReader("/src/main/resources/ontologies/NCI/NCI.owl","de");
        specialistReader = new SpecialistReader("/src/main/resources/ontologies/SpecialistLexicon/LEXICON","en");
        wordNetReader = new WordNetReader("/src/main/resources/ontologies/WordNet/WordNet-3.0/dict","en");
        frList = new ArrayList<>();
        frList.add(openThesaurusReader);
        frList.add(owlReader);
        frList.add(specialistReader);
        frList.add(wordNetReader);
    }

    @Test
    public void getAllOntologiesInDatabase(){
        dbh.newLanguage("en");
        dbh.newLanguage("de");
        for(FileReader fr : frList){
            int tmp = 0;
            int linesCount = fr.getAllLinesCount();
            for(int i=2000 ; i<linesCount; i=i+2000){
                fr.setFromEntry(tmp);
                fr.setToEntry(i);
                dbh.storeFromFile(fr);
                tmp = i;
            }
            if (tmp+2000>linesCount){
                fr.setFromEntry(tmp);
                fr.setToEntry(linesCount);
            }
            System.out.println("Finish "+fr.getClass().getSimpleName()+" with "+linesCount+" lines.");
        }
        assertEquals(736505,dbh.getAllWords("en").size());
    }
}
