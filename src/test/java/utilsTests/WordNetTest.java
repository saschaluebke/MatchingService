package utilsTests;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
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
        dbh = new DBHelper(new SimpleStrategy());
        wnh = new WordNetReader("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/WordNet/WordNet-3.0/dict","en");
    }

    @Test
    public void getBigFileContentTest() {
        dbh.newLanguage("en");
        wnh.setFromEntry(0);
        wnh.setToEntry(100);
        dbh.storeFromFile(wnh);
      //  fr.setFromEntry(0);
      //  fr.setToEntry(50000);
        //dbh.takeFromFileReader(wnh);
        System.out.println(dbh.print("en","en"));
        assertEquals(360, wnh.getWordList().size());

    }
}
