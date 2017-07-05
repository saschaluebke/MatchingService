package databaseTests;

import database.MySQLQuery;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DBQueryTest {

    static MySQLQuery dbq = null;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
    }

    @Test
    public void dbhConstructorTest() {
        assertEquals(true,dbq.isTable("languages"));
    }




}
