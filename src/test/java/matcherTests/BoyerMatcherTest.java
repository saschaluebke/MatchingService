package matcherTests;

import components.MatchResult;
import components.MatchResultSet;
import components.Word;
import matching.BoyerMatcher;
import matching.distance.Levenshtein;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 19.07.17.
 */
public class BoyerMatcherTest {
    static BoyerMatcher bm;

    @Test
    public void levenshteinTest() {
        bm = new BoyerMatcher();
        Word w1 = new Word(0,"Hallo","de");
        Word w2 = new Word(0,"Hallol","de");
        MatchResultSet mrs = bm.getMatchResult(w1,w2);
        assertEquals(1,mrs.getMatchResults().size());
        assertEquals(1,mrs.getMatchResults().get(0).get(0).getScore());
    }
}
