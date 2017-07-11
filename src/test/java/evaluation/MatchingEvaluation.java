package evaluation;

import components.MatchResult;
import components.MatchResultSet;
import components.Word;
import database.DBHelper;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import matching.BoyerMatcher;
import matching.Matcher;
import matching.SimpleMatcher;
import matching.distance.Levenshtein;
import matching.iterate.PerformanceStrategy;
import matching.sorting.ScoreSort;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.TSVFileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 *  TSV Datensatz in SQL Tabelle laden. Mit DBH die gesamte Wortliste laden und mit indexof durchgehen.
 *  Danach einen Matcher versuchen lassen.
 */
public class MatchingEvaluation {
    static Matcher matcher;
    static SimpleMatcher simpleMatcher;
    static ArrayList<Word> target, source;
    static TSVFileReader tsvReader;
    static BoyerMatcher boyerMatcher;



    @BeforeClass
    public static void onceExecutedBeforeAll() {
        matcher = new Matcher(new PerformanceStrategy(),new Levenshtein(), new ScoreSort());
        simpleMatcher = new SimpleMatcher();
        boyerMatcher = new BoyerMatcher();
        tsvReader = new TSVFileReader("/src/main/resources/TSV/source.tsv","en");
        tsvReader.getFileContent();
        ArrayList<Word> fullSource = tsvReader.getWordList();
        source = new ArrayList<>();
        for(int i=0;i<fullSource.size();i=i+100){
            source.add(fullSource.get(i));
        }
        tsvReader = new TSVFileReader("/src/main/resources/TSV/target.tsv","en");
        ArrayList<String> test = tsvReader.getFiles();
        ArrayList<Word> fullTarget = tsvReader.getWordList();
        tsvReader.getFileContent();
        target = new ArrayList<>();
        for(int i=0;i<fullTarget.size();i=i+400){
            target.add(fullTarget.get(i));
        }

    }

    @Test
    public void indexOfEvaluation(){
        //Get matching from the String.indexOf Method loop through the data
        int counter = 0;
        Word sameTarget=null;
        Word sameSource=null;
        for(Word s: source){
            for(Word t : target){
                int finding = t.getName().indexOf(s.getName());
                //System.out.println(s.getName()+"/"+t.getName()+"= "+finding);
                if (finding>-1){
                    sameSource = s;
                    sameTarget = t;
                    counter++;
                }
            }
        }


        assertEquals(6,counter);
        assertEquals("met",sameSource.getName());
        assertEquals("methylprednisolon d07aa01",sameTarget.getName());
    }

    @Test
    public void performanceMatchingEvaluation(){
        //Get matching from the String.indexOf Method loop through the data
        MatchResultSet mrs=null;

        for(Word s: source){
            for(Word t : target){
                mrs = matcher.getMatchResult(s,t);
            }
        }

    }

    @Test
    public void simpleMatcherEvaluation(){
        //Get matching from the String.indexOf Method loop through the data
        MatchResultSet mrs=null;

        for(Word s: source){
            for(Word t : target){
                mrs = simpleMatcher.getMatchResult(s,t);
            }
        }

    }

    @Test
    public void boyerMatcherTest(){
        //Get matching from the String.indexOf Method loop through the data
        MatchResultSet mrs=null;

        for(Word s: source){
            for(Word t : target){
                mrs = boyerMatcher.getMatchResult(s,t);
            }
        }

    }


}
