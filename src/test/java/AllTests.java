import databaseTests.DBSimpleStrategyTest;
import databaseTests.DBQueryTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import translatorsTests.TransltrTest;

@RunWith(Suite.class)
@SuiteClasses({  TransltrTest.class, DBQueryTest.class, DBSimpleStrategyTest.class})
public class AllTests {

}