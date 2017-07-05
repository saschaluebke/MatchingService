package matcherTests;

import components.MatchResultSet;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import matching.Matcher;
import matching.distance.*;
import matching.iterate.PerformanceStrategy;
import matching.sorting.ScoreSort;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DistanceTest {
    final double DELTA = 0.1;

    @Test
    public void levenshteinTest() {
        Levenshtein l = new Levenshtein();
        double result = l.getDistance("Hallo","Hello");
        assertEquals(1.0,result,DELTA);
    }

    @Test
    public void levenshteinTestNormalized() {
        LevenshteinNormalized ln = new LevenshteinNormalized();
        double result = ln.getDistance("Hallo","Hello");
        assertEquals(0.2,result,DELTA);

        result = ln.getDistance("LOLOLOLOL","Hello");
        assertEquals(1,result,DELTA);
    }

    @Test
    public void damarauLevenshteinTest() {
        DamarauLevenshtein dl = new DamarauLevenshtein();
        double result = dl.getDistance("Hlalo","Hallo");
        assertEquals(1.0,result,DELTA);
    }

    @Test
    public void jaroWinklerTest() {
        JaroWinkler dl = new JaroWinkler();
        double result = dl.getDistance("Ist fast gleich aber nicht ganz","Ist fast gleich aber nicht gans");
        assertEquals(0.0,result,DELTA);
        result = dl.getDistance("Sehr unterschiedlich","Unteschiedlich es ist sehr");
        assertEquals(0.3,result,DELTA);
        result = dl.getDistance("Vollkommen andersartig","zzzzzzz");
        assertEquals(1,result,DELTA);

        result = dl.getDistance("Hyperglykämie","Hypoglykämie");
        assertEquals(0.0,result,DELTA);

        result = dl.getDistance("a","b");
        assertEquals(1,result,DELTA);

        result = dl.getDistance("aa","bb");
        assertEquals(1,result,DELTA);

        result = dl.getDistance("aaa","bba");
        assertEquals(0.4,result,DELTA);

    }

    @Test
    public void NGramTest() {
        NGram nGram = new NGram(3);
        double result = nGram.getDistance("Hallo","Hello");
        assertEquals(0.2,result,DELTA);

        result = nGram.getDistance("LOLOLOLOLOL","Hello");
        assertEquals(1,result,DELTA);
    }

    @Test
    public void LongestCommonSubsequensTest() {
        LongestCommonSub lcs = new LongestCommonSub();
        double result = lcs.getDistance("Hallo","Hello");
        assertEquals(0.2,result,DELTA);

        result = lcs.getDistance("LOLOLOLOL","Hello");
        assertEquals(1.0,result,DELTA);
    }

    @Test
    public void JaccardTest() {
        Jaccard j = new Jaccard();
        double result = j.getDistance("Hallo","Hello");
        assertEquals(0.8,result,DELTA); //Ziemlich hoch!

        result = j.getDistance("Hallöchen dieser String ist groß und fast gleich","Halloechen dieser String ist groß und fast gleich");
        assertEquals(0.2,result,DELTA);

        result = j.getDistance("LOLOLOLOL","Hello");
        assertEquals(1.0,result,DELTA);
    }
}
