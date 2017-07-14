package databaseTests;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import translators.MosesClient;
import translators.TranslationTest;
import translators.Translator;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 08.07.17.
 */
public class SynonymStrategyTest {
    static DBHelper dbh;
    static MySQLQuery dbq;
    static SynonymStrategy strategy;
    static Translator mosesTranslate;
    static Translator testTranslate;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        mosesTranslate = new MosesClient();
        testTranslate = new TranslationTest();
        strategy = new SynonymStrategy();
        dbh = new DBHelper(strategy);
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
    public void translationTest() {
        dbh.newLanguage("en");
        dbh.newLanguage("de");

        Word glucose = new Word(0,"Glucose","en");
        glucose.setId(dbh.putWord(glucose));

        Word sugar = new Word(0,"Sugar","en");
        sugar.setId(dbh.putWord(sugar));

        Word home = new Word(0,"Home", "en");
        home.setId(dbh.putWord(home));

        dbh.putRelation(glucose,sugar);
        dbh.putRelation(sugar,glucose);
    }


}
