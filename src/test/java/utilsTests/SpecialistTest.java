package utilsTests;

import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.DictReader;
import utils.SpecialistReader;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 02.07.17.
 */
public class SpecialistTest {
    static MySQLQuery dbq;
    static DBHelper dbh;
    static SpecialistReader sr;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh = new DBHelper(new SimpleStrategy());
        sr = new SpecialistReader("/src/main/resources/SpecialistLexicon/LEXICON","en");
    }

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
}
