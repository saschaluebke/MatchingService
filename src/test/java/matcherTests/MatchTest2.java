package matcherTests;

import components.MatchResultSet;
import components.Word;
import matching.Matcher;
import matching.distance.JaroWinkler;
import matching.distance.LevenshteinNormalized;
import matching.iterate.SimpleStrategy;
import matching.sorting.ScoreSort;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by SashBot on 21.08.2017.
 */
public class MatchTest2 {

    @Test
    public void EqualTest() {
        Matcher matcher = new Matcher(new SimpleStrategy(), new LevenshteinNormalized(),new ScoreSort());
        MatchResultSet mrs = matcher.getMatchResult("Hypoglykämie", new Word(0,"Hyperglykämie","en"));
        assertEquals(0.15,mrs.getMatchResults().get(0).get(0).getScore(),0.1);
         matcher = new Matcher(new SimpleStrategy(), new JaroWinkler(),new ScoreSort());
         mrs = matcher.getMatchResult("Hypoglykämie", new Word(0,"Hyperglykämie","en"));
        assertEquals(0,mrs.getMatchResults().get(0).get(0).getScore(),0.1);
    }

    @Test
    public void DifferentTest() {
        Matcher matcher = new Matcher(new SimpleStrategy(), new LevenshteinNormalized(),new ScoreSort());
        MatchResultSet mrs = matcher.getMatchResult("Schienbein", new Word(0,"Schienenersatzverkehr","en"));
        assertEquals(0.6,mrs.getMatchResults().get(0).get(0).getScore(),0.1);
        matcher = new Matcher(new SimpleStrategy(), new JaroWinkler(),new ScoreSort());
        mrs = matcher.getMatchResult("Schienbein", new Word(0,"Schienenersatzverkehr","en"));
        assertEquals(0.2,mrs.getMatchResults().get(0).get(0).getScore(),0.1);
    }

    @Test
    public void RezeptorTest() {
        Matcher matcher = new Matcher(new SimpleStrategy(), new LevenshteinNormalized(),new ScoreSort());
        MatchResultSet mrs = matcher.getMatchResult("hormonrezeptor", new Word(0,"rezeptor","en"));
        assertEquals(0.4,mrs.getMatchResults().get(0).get(0).getScore(),0.1);
        matcher = new Matcher(new SimpleStrategy(), new JaroWinkler(),new ScoreSort());
        mrs = matcher.getMatchResult("hormonrezeptor", new Word(0,"rezeptor","en"));
        assertEquals(0.2,mrs.getMatchResults().get(0).get(0).getScore(),0.1);
    }
}
