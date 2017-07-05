import databaseTests.DBSimpleStrategyTest;
import databaseTests.DBQueryTest;
import databaseTests.WordnetHelperTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import translatorsTests.TransltrTest;

@RunWith(Suite.class)
@SuiteClasses({  TransltrTest.class, DBQueryTest.class, DBSimpleStrategyTest.class, WordnetHelperTest.class})
public class AllTests {

}