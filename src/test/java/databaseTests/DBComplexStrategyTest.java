package databaseTests;

import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.complexStrategy.ComplexStrategy;
import database.dbStrategy.complexStrategy.model.ComplexWord;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 17.06.17.
 */
public class DBComplexStrategyTest {
    static DBHelper dbh;
    static MySQLQuery dbq;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbh = new DBHelper(new ComplexStrategy());
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
    }

    @Before
    public void executedBeforeEach() {
        dbq.dropAllTables();
        dbq = new MySQLQuery();
        dbh = new DBHelper(new ComplexStrategy());
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
    public void putWordTest() {
        dbh.newLanguage("en");
        ComplexWord w = new ComplexWord(0,"Hallo Welt","en");
        int index1 = dbh.putWord(w);
        assertEquals(1,index1);
        int index2 = dbh.putWord(w);
        assertEquals(1,index2);//TODO: Gedanken machen was hier am besten passieren soll... Besser gleiche wörter nicht erlaubt...
    }
}
