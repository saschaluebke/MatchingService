package components;

import matching.distance.DistanceStrategy;
import matching.iterate.IterateStrategy;
import matching.sorting.SortStrategy;
import org.springframework.beans.factory.support.ManagedList;

import java.util.ArrayList;

/**
 * Created by sascha on 09.06.17.
 */
public class MatchResultSet {
    //private double maxScore, minScore;
    private DistanceStrategy distanceStrategy;
    private SortStrategy sortStrategy;
    private ArrayList<ArrayList<MatchResult>> matchResults;
    private ArrayList<Word> words;
    private IterateStrategy iterateStrategy;

    public MatchResultSet(IterateStrategy iterateStrategy, DistanceStrategy distanceStrategy, SortStrategy sortStrategy){
        this.iterateStrategy = iterateStrategy;
        this.sortStrategy = sortStrategy;
        this.distanceStrategy = distanceStrategy;
        matchResults = new ArrayList<>();
        words = new ArrayList<>();
    }
/*
    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public double getMinScore() {
        return minScore;
    }

    public void setMinScore(double minScore) {
        this.minScore = minScore;
    }

*/

    public ArrayList<ArrayList<MatchResult>> getMatchResults() {
        return matchResults;
    }

    public void setMatchResults(ArrayList<ArrayList<MatchResult>> matchResults) {
        this.matchResults = matchResults;
    }

    public ArrayList<Word> getWords(){
        return words;
    }

    public void addMatchResults(ArrayList<MatchResult> matchResult, Word word){
        this.matchResults.add(matchResult);
        this.words.add(word);
    }

    @Override
    public String toString() {
        String output = "";
        if (matchResults.size()==0){
            return "No Matchresults";
        }

        for(ArrayList<MatchResult> mr : matchResults){
            output = output+mr.toString()+System.lineSeparator();
        }
        return output;
    }
}
