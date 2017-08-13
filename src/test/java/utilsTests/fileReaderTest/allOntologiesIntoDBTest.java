package utilsTests.fileReaderTest;

import components.Word;
import utils.ontology.OntologyAnalysis;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.ontology.*;

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
        //owlReader = new OwlReader("/src/main/resources/ontologies/NCI/NCI.owl","en");
        //specialistReader = new SpecialistReader("/src/main/resources/ontologies/SpecialistLexicon/LEXICON","en");
        wordNetReader = new WordNetReader("/src/main/resources/ontologies/WordNet/WordNet-3.0/dict","en");
        frList = new ArrayList<>();
        frList.add(openThesaurusReader);
        //frList.add(owlReader);
        //frList.add(specialistReader);
        frList.add(wordNetReader);
    }

    @Test
    public void test(){
        dbh.newLanguage("de");
        TestReader tr = new TestReader();
        dbh.storeFromFile(tr);
        assertEquals(true,true);
    }

    @Test
    public void justOpenThesarus(){
        frList = new ArrayList<>();
        frList.add(openThesaurusReader);
        int wordCount=0,synCount=0,wordWithoutSyn=0;
        double varianz=0,average=0;
        dbh.newLanguage("en");
        dbh.newLanguage("de");
        for(FileReader fr : frList){
            int tmp = 0;
            int linesCount = fr.getAllLinesCount();
            for(int i=1000 ; i<linesCount; i=i+1000){

                System.out.println(i +" von "+linesCount+" erreicht.");
                fr.setFromEntry(tmp);
                fr.setToEntry(i);
                dbh.storeFromFile(fr);
                tmp = i;
            }

            System.out.println("Finish "+fr.getClass().getSimpleName()+" with "+linesCount+" lines.");
        }

        //TODO: Datenbank analyisieren.
        System.out.println("Wörter: "+wordCount+" Synonyme: "+synCount+" WörterOhneSyn: "+wordWithoutSyn);
        System.out.println("Mittel: "+average+" Varianz: "+varianz);
        assertEquals(0,dbh.getAllWords("en").size());
    }


    @Test
    public void onlyWordNet(){
        frList = new ArrayList<>();
        frList.add(wordNetReader);
        int wordCount=0,synCount=0,wordWithoutSyn=0;
        double varianz=0,average=0;
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
            System.out.println("Finish "+fr.getClass().getSimpleName()+" with "+linesCount+" lines.");
        }
        System.out.println("Wörter: "+wordCount+" Synonyme: "+synCount+" WörterOhneSyn: "+wordWithoutSyn);
        System.out.println("Mittel: "+average+" Varianz: "+varianz);
        assertEquals(87296,dbh.getAllWords("en").size());
    }

    @Test
    public void onlyNCI(){
        frList = new ArrayList<>();
        frList.add(owlReader);
        int wordCount=0,synCount=0,wordWithoutSyn=0;
        double varianz=0,average=0;
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
            System.out.println("Finish "+fr.getClass().getSimpleName()+" with "+linesCount+" lines.");
        }
        System.out.println("Wörter: "+wordCount+" Synonyme: "+synCount+" WörterOhneSyn: "+wordWithoutSyn);
        System.out.println("Mittel: "+average+" Varianz: "+varianz);
        assertEquals(87296,dbh.getAllWords("en").size());
    }

    @Test
    public void onlySpecialist(){
        frList = new ArrayList<>();
        frList.add(specialistReader);
        int wordCount=0,synCount=0,wordWithoutSyn=0;
        double varianz=0,average=0;
        dbh.newLanguage("en");
        dbh.newLanguage("de");
        for(FileReader fr : frList){
            int tmp = 0;
            int linesCount = fr.getAllLinesCount();
            for(int i=2000 ; i<linesCount-10000; i=i+2000){
                fr.setFromEntry(tmp);
                fr.setToEntry(i);
                dbh.storeFromFile(fr);
                tmp = i;
                System.out.println(i+" from "+linesCount);
            }
            System.out.println("Finish "+fr.getClass().getSimpleName()+" with "+linesCount+" lines.");
        }
        System.out.println("Wörter: "+wordCount+" Synonyme: "+synCount+" WörterOhneSyn: "+wordWithoutSyn);
        System.out.println("Mittel: "+average+" Varianz: "+varianz);
        assertEquals(498084,dbh.getAllWords("en").size());
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
        assertEquals(388772,dbh.getAllWords("en").size());
    }
}
