package utilsTests;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.WordNetReader;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 02.07.17.
 */
public class WordNetTest {
    static MySQLQuery dbq;
    static DBHelper dbh;
    static WordNetReader wnh;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh = new DBHelper(new SynonymStrategy());
        wnh = new WordNetReader("/src/main/resources/ontologies/WordNet/WordNet-3.0/dict","en");
    }

    @Test
    public void lemmaTest(){
        String lemma = "Vogel {f}";
        int curlyStart = lemma.indexOf("{");
        if(curlyStart != -1){
            lemma = lemma.substring(0,curlyStart);
            lemma = lemma.trim();
        }
        assertEquals("Vogel",lemma);
    }

    @Test
    public void simpleWordNetTest() {
        dbh = new DBHelper(new SimpleStrategy());
        dbh.newLanguage("en");
        wnh.setFromEntry(0);
        wnh.setToEntry(100);
        dbh.storeFromFile(wnh);
      //  fr.setFromEntry(0);
      //  fr.setToEntry(50000);
        //dbh.takeFromFileReader(wnh);
        System.out.println(dbh.print("en","en"));
        assertEquals(100, wnh.getWordList().size());

    }

    @Test
    public void getAllLinesTest() {
        int lineCount = wnh.getAllLinesCount();
        assertEquals(117798,lineCount);
    }


    @Test
    public void getSynonymWordNetTest() {
        dbh = new DBHelper(new SynonymStrategy());
        int lineCount = wnh.getAllLinesCount();
        dbh.newLanguage("en");
        int tmp=0;
        for(int i = 0; i<lineCount+100000; i=i+100000){

            wnh.setFromEntry(tmp);
            wnh.setToEntry(i);
            dbh.storeFromFile(wnh);
            tmp=i;
        }

        //System.out.println(dbh.print("en","en"));
        assertEquals(299233,dbh.getAllWords("en").size());

        Word randomWord = dbh.getWordById(115,"en");
        ArrayList<Word> relations = dbh.getWordFromRelation(randomWord,"en","en");
        System.out.println(randomWord.getName()+" hat "+relations.size()+" Synonyme.");
        for(Word word : relations){
            System.out.println(word.getName());
        }
        assertEquals(325764,dbh.getAllRelations("en","en").size());
    }
}
