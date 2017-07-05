package databaseTests;

import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Saschbot on 21.03.2017.
 */
public class WordnetHelperTest {
        static MySQLQuery dbq;
        static DBHelper dbh;
        static database.WordnetHelper wnh;

        @BeforeClass
        public static void onceExecutedBeforeAll() {
            dbh = new DBHelper(new SimpleStrategy());
            dbq = new MySQLQuery();
            dbq.dropAllTables();
            dbq.truncate("languages");
            dbh.newLanguage("en");
            /*
            wnh = new database.WordnetHelper();
            String wnhome = System.getProperty("user.dir");
            String path = wnhome + File.separator+"dict";
            File wnDir = new File(path);
            wnh.loadInMemory();
            */
        }

        @Test
        public void allWordsin() {

            /*
            System.out.println("-- Long test starts take a break --");
            wnh.allWordsToDatabase(dbh, POS.NOUN);
            System.out.println(dbh.getWordList(0,"en").getName());
            */
            assertEquals("","");
            //assertEquals("dog",dbh.getWordList("Dog","en").get(0).getName());
        }
}
