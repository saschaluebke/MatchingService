package databaseTests;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DBSimpleStrategyTest {
    static DBHelper dbh;
    static MySQLQuery dbq;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbh = new DBHelper(new SimpleStrategy());
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
    }

    @Before
    public void executedBeforeEach() {
        dbq.dropAllTables();
        dbq = new MySQLQuery();
        dbh = new DBHelper(new SimpleStrategy());
        dbq.truncate("languages");
    }

    @Test
    public void dbhConstructorTest() {
        assertEquals(true,dbq.isTable("languages"));
        assertEquals(false,dbq.isTable("wordlist_en"));
    }

    @Test
    public void newLanguageTest() {
        dbh.newLanguage("en");

        assertEquals(true,dbq.isTable("wordlist_en"));
        assertEquals(true,dbq.isTable("rel_en_en"));

        dbh.newLanguage("de");
        assertEquals(true,dbq.isTable("wordlist_de"));
        assertEquals(true,dbq.isTable("rel_de_de"));
        assertEquals(true,dbq.isTable("rel_en_de"));
        assertEquals(true,dbq.isTable("rel_de_en"));
    }

    @Test
    public void searchWordListTestByName() {
        dbh.newLanguage("en");
        dbh.newLanguage("de");
        Word word = new Word(0,"Hallo","de");
        dbh.putWord(word);
        System.out.println(dbh.searchWord(word));
        assertEquals("Hallo",dbh.searchWord(word).getMatchResults().get(0).get(0).getDbString());
    }

    @Test
    public void getWordListTestByNameMulti() {
        dbh.newLanguage("en");
        dbh.newLanguage("de");

        Word wordGerman1 = new Word(0,"Bank","Sitzgelegenheit","de");
        Word wordGerman2 = new Word(0,"Bank","Kreditinstitut","de");
        wordGerman2.setDescription("Das ist eine Beschreibung");
        wordGerman2.setCount(10);
        wordGerman2.setPrior(10);
        dbh.putWord(wordGerman1);
        dbh.putWord(wordGerman2);

        assertEquals(2,dbh.searchWord(wordGerman2).getMatchResults().size());
    }

    @Test
    public void searchWordListTestById() {
        dbh.newLanguage("en");
        dbh.newLanguage("de");

        dbh.putWord(new Word(0,"Hallo","de"));

        assertEquals(1,dbh.getWordById(1,"de").getId());
    }

    @Test
    public void getLastWordIdTest() {
        dbh.newLanguage("en");
        dbh.newLanguage("de");

        dbh.putWord(new Word(0,"Hallo","de"));

        assertEquals(1,dbh.getLastWordId("de"));

        dbh.putWord(new Word(0,"Ballo","de"));

        assertEquals(2,dbh.getLastWordId("de"));
    }

    @Test
    public void putRelations() {
        dbh.newLanguage("en");
        dbh.newLanguage("de");

        dbh.putWord(new Word(0,"Hallo","de"));
        dbh.putWord(new Word(0,"Hallöchen","de"));

        dbh.putWord(new Word(0,"Hi","de"));

        ArrayList<Relation> relations = new ArrayList<>();
        relations.add(new Relation(0,1,2));
        relations.add(new Relation(0,1,3));

        dbh.putRelationList(relations,"de","de");

        assertEquals(2,dbh.getAllRelations("de","de").size());
    }



}
