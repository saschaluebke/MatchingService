package utilsTests.corpusMakerTests;

import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.corpus.DictReader;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 01.07.17.
 */
public class DictReaderTest {
    static MySQLQuery dbq;
    static DBHelper dbh;
    static DictReader dr;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh = new DBHelper(new SimpleStrategy());
        dr = new DictReader("/src/main/resources/translation/Dict/de_en_Dict.txt","de","en");
    }
/*
    @Test
    public void getSmallFileContentTest() {
        dbh.newLanguage("de");
        dbh.newLanguage("en");

        dr.setFromEntry(0);
        dr.setToEntry(50000);
        dbh.takeFromFileReader(dr);

        assertEquals(50000,dr.getWordList().size());

        dr.setFromEntry(50001);
        dr.setToEntry(100000);
        dbh.takeFromFileReader(dr);

        assertEquals(50000,dr.getWordList().size());
        //TODO: Noch mehr testen sind sie wirklich drinne sind die translations richtig?
    }
  */
    @Test
    public void printCorpus(){
        dbh.newLanguage("de");//I dont use them in this test but .getContent needs them!
        dbh.newLanguage("en");
        dr.setFromEntry(0);
        dr.setToEntry(Integer.MAX_VALUE);
        dr.getFileContent();
        dr.makeParallelCorpus();
        assertEquals(1136891,dr.getWordList().size());
    }

}
