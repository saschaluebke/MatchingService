package matcherTests;

import components.MatchResult;
import components.Word;
import matching.distance.DistanceStrategy;
import matching.distance.LevenshteinNormalized;
import matching.iterate.CharacterStrategy;
import matching.iterate.WordStrategy;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 11.06.17.
 */
public class IterateStrategyTest  {
    static DistanceStrategy distanceStrategy;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        distanceStrategy = new LevenshteinNormalized();
    }

    @Test
    public void characterStrategyTest() {
        CharacterStrategy cs = new CharacterStrategy();
        cs.setWordFromDB(new Word(0,"Hallöchen","de"));
        cs.setSearchString("Hallo");
        ArrayList<MatchResult> results = cs.getMatchList(distanceStrategy);
        System.out.println(results.toString());
       // assertEquals(0,result);
    }

    @Test
    public void wordStrategyTest() {
        WordStrategy ws = new WordStrategy();
        ws.setWordFromDB(new Word(0,"Hallöchen Ich bin Sascha","de"));
        ws.setSearchString("Hallo");
        ArrayList<MatchResult> results = ws.getMatchList(distanceStrategy);
        //System.out.println("Result:"+results.toString());
        ws.setWordFromDB(new Word(0,"Hallöchen/ich/bin SaschaLuebke","de"));
        results = ws.getMatchList(distanceStrategy);
        //System.out.println("Result:"+results.toString());
        assertEquals("Hal",results.get(0).getDbString());
    }
}
