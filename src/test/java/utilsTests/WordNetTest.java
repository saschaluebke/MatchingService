package utilsTests;

import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.OwlReader;
import utils.WordNetHelper;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 02.07.17.
 */
public class WordNetTest {
    static MySQLQuery dbq;
    static DBHelper dbh;
    static WordNetHelper wnh;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh = new DBHelper(new SimpleStrategy());
        wnh = new WordNetHelper("/src/main/resources/NCI_Thesaurus/Thesaurus-byName.owl","en");
    }

    @Test
    public void getBigFileContentTest() {
        dbh.newLanguage("en");
wnh.getFileContent();
      //  fr.setFromEntry(0);
      //  fr.setToEntry(50000);
        //dbh.takeFromFileReader(wnh);

        assertEquals(187822, wnh.getWordList().size());
    }
}
