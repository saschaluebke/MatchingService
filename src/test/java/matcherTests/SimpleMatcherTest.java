package matcherTests;

import components.MatchResult;
import components.MatchResultSet;
import components.Word;
import matching.SimpleMatcher;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 13.07.17.
 */
public class SimpleMatcherTest {
    static SimpleMatcher sm;

    @Test
    public void SimpleMatcher(){
       sm = new SimpleMatcher();
       Word word1 = new Word(0,"Hallo","de");
        Word word2 = new Word(0,"Hallöchen","de");
       MatchResultSet mrs = sm.getMatchResult(word1,word2);
       System.out.println(mrs);
        assertEquals(0,mrs.getMatchResults().get(0).get(0).getScore(),0.1);

       word1 = new Word(0,"Hallo","de");
       word2 = new Word(0,"Halloöchen","de");
       mrs = sm.getMatchResult(word1,word2);
        System.out.println(mrs);
        assertEquals(1,mrs.getMatchResults().get(0).get(0).getScore(),0.1);
    }
}
