package matcherTests;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import components.MatchResult;
import components.MatchResultSet;
import components.SynonymTranslationResult;
import components.Word;
import matching.MatchEvaluator;
import matching.Matcher;
import matching.distance.JaroWinkler;
import matching.distance.LevenshteinNormalized;
import matching.iterate.CharacterStrategy;
import matching.iterate.WordPerformanceStrategy;
import matching.iterate.WordStrategy;
import matching.sorting.IntervalSort;
import matching.sorting.ScoreSort;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Dieser Test nimmt nacheinander aus einer Liste Ausdrücke. Es wird dann jeder Ausdruck in der Liste mit dem
 * gewählten Ausdruck gematched. Es wird dann geprüft, ob jeder Ausdruck einen perfektes Match gefunden hat (sich selbst
 * in der Liste). Dieser Test ist dafür da für die unterschiedlichen Matching strategien die Zeit zu messen.
 */
public class MatchScalingTest {
static ArrayList<Word> input;
    @BeforeClass
    public static void onceExecutedBeforeAll(){
        input = new ArrayList<>();
        MatchEvaluator me = new MatchEvaluator("/src/main/resources/evaluation/ICD10/icd10InputVersion1.txtCleaned");
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

    @Test
    public void characterStrategyTest() {
        Matcher matcher = new Matcher(new CharacterStrategy(), new LevenshteinNormalized(),new ScoreSort());
        for(Word in : input){
            MatchResultSet mrs = matcher.getMatchingWordList(in.getName(), input);
            assertEquals(in.getName(),mrs.getMatchResults().get(0).get(0).getDbString());
        }
    }
}
