package utilsTests.fileReaderTest;

import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.ontology.SpecialistReader;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 02.07.17.
 */
public class SpecialistTest {
    static MySQLQuery dbq;
    static DBHelper dbh;
    static SpecialistReader sr;
    static int lines;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh = new DBHelper(new SynonymStrategy());

    }
/*
    @Test
    public void getSmallFileContentTest() {
        dbh.newLanguage("en");

        sr.setFromEntry(0);
        sr.setToEntry(50000);
        dbh.takeFromFileReader(sr);

        assertEquals(51951,sr.getWordList().size());
System.out.println("----First Round-----");
        sr.setFromEntry(50001);
        sr.setToEntry(100000);
        dbh.takeFromFileReader(sr);

        assertEquals(51367,sr.getWordList().size());
        //TODO: Noch mehr testen sind sie wirklich drinne sind die translations richtig?
    }

    */

    @Test
    public void simpleTest() {
        sr = new SpecialistReader("/src/main/resources/ontologies/SpecialistLexicon/TestOntology","en");
        dbh.newLanguage("en");
        sr.setFromEntry(0);
        sr.setToEntry(2);
        dbh.storeFromFile(sr);
        assertEquals(5,dbh.getAllWords("en").size());
        assertEquals(8,dbh.getAllRelations("en","en").size());
    }

@Test
public void getAllLinesTest() {
    sr = new SpecialistReader("/src/main/resources/ontologies/SpecialistLexicon/LEXICON","en");
    ArrayList<String> allLines = sr.getAllLines();
    lines = allLines.size();
    assertEquals(498432,allLines.size());
}


    @Test
    public void synonymsTest() {
        sr = new SpecialistReader("/src/main/resources/ontologies/SpecialistLexicon/LEXICON","en");
        //ArrayList<String> allLines = sr.getAllLines();
       // lines = allLines.size();
        lines = 498432;
    dbh.newLanguage("en");
    int tmp=20000;
    for(int i = 20100; i<lines+1000; i=i+100){
System.out.println("Read "+i);
        sr.setFromEntry(tmp);
        sr.setToEntry(i);
        dbh.storeFromFile(sr);
        tmp=i;
    }

        //System.out.println(dbh.print("en","en"));
        assertEquals(544796, dbh.getAllWords("en").size());
        assertEquals(96534,dbh.getAllRelations("en","en").size());
    }

}

/*
   ArrayList<Word> wordlist = sr.getWordList();
        ArrayList<ArrayList<Word>> wordsWithSynonyms = new ArrayList<>();
        for(int i=0;i<100;i++)
        {
            Word w = wordlist.get(i);
            ArrayList<Word> synonyms = new ArrayList<>();
            for(Relation r : relationArrayList){
                if(r.getIdFrom()==w.getId()){
                    for(Word w2 : wordlist){
                        if (w2.getId()==r.getIdTo()){
                            synonyms.add(w2);
                        }
                    }
                }
            }
            wordsWithSynonyms.add(synonyms);
        }
        for(int i = 0; i<100;i++){
            System.out.print(wordlist.get(i).getName()+": ");
            for(Word word : wordsWithSynonyms.get(i)){
                System.out.print(word.getName()+"/");
            }
            System.out.println(".");
        }
 */
