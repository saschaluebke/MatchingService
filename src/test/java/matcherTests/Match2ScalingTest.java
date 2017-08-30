package matcherTests;

import components.MatchResultSet;
import components.Word;
import matching.MatchEvaluator;
import matching.Matcher;
import matching.distance.JaroWinkler;
import matching.distance.LevenshteinNormalized;
import matching.iterate.*;
import matching.sorting.ScoreSort;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 13.08.17.
 */
public class Match2ScalingTest {
    static ArrayList<Word> input;
    @BeforeClass
    public static void onceExecutedBeforeAll(){
        input = new ArrayList<>();
        MatchEvaluator me = new MatchEvaluator("/src/main/resources/evaluation/ICD10/icd10InputVersion1.txtCleaned");
        me.scaleUp(2);
        for(String inputString : me.getInput()){
            input.add(new Word(0,inputString,"en"));
        }
    }

    @Test
    public void WordStrategyTest() {
        Matcher matcher = new Matcher(new WordStrategy(), new LevenshteinNormalized(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
        }
    }

    @Test
    public void WordPerformanceStrategyTest() {
        Matcher matcher = new Matcher(new WordPerformanceStrategy(), new LevenshteinNormalized(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
        }
    }

    @Test
    public void WordJWStrategyTest() {
        Matcher matcher = new Matcher(new WordStrategy(), new JaroWinkler(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
        }
    }

    @Test
    public void WordPerformanceJWStrategyTest() {
        Matcher matcher = new Matcher(new WordPerformanceStrategy(), new JaroWinkler(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
        }
    }
//simple
    @Test
    public void SimpleJWStrategyTest() {
        Matcher matcher = new Matcher(new SimpleStrategy(), new JaroWinkler(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
        }
    }

    @Test
    public void SimpleLevenshteinStrategyTest() {
        Matcher matcher = new Matcher(new SimpleStrategy(), new LevenshteinNormalized(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
        }
    }


    @Test
    public void SimpleIterLevenshteinStrategyTest() {
        Matcher matcher = new Matcher(new PerformanceStrategy(), new LevenshteinNormalized(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
        }
    }

    @Test
    public void SimpleIterJWStrategyTest() {
        Matcher matcher = new Matcher(new PerformanceStrategy(), new JaroWinkler(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
        }
    }


    @Test
    public void characterStrategyTest() {
        Matcher matcher = new Matcher(new CharacterStrategy(), new LevenshteinNormalized(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
            //assertEquals(in.getName(),mrs.getMatchResults().get(0).get(0).getDbString());
        }
    }

    @Test
    public void SubstringStrategyTest() {
        Matcher matcher = new Matcher(new WordStrategy(), new SubstringDistance(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
        }
    }
}
