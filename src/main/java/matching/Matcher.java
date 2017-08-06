package matching;

import components.MatchResult;
import components.MatchResultSet;
import components.Word;
import database.DBHelper;
import database.TranslatorGetProperties;
import matching.distance.DistanceStrategy;
import matching.iterate.IterateStrategy;
import matching.sorting.SortStrategy;

import java.io.IOException;
import java.util.ArrayList;

public class Matcher {
    private SortStrategy sortStrategy;
    private DistanceStrategy distanceStrategy;
    private IterateStrategy iterateStrategy;
    private MatchResultSet currentMatchingWordList;
    private double accuracy;

    public Matcher(IterateStrategy iterateStrategy, DistanceStrategy distanceStrategy,SortStrategy sortStrategy){
        this.iterateStrategy = iterateStrategy;
        this.sortStrategy = sortStrategy;
        this.distanceStrategy = distanceStrategy;
        TranslatorGetProperties tgp = new TranslatorGetProperties();
        try {
            accuracy = Double.parseDouble(tgp.getPropValues("Translate.accuracy"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MatchResultSet getMatchResult(Word searchString, Word wordFromDB){
        MatchResultSet mrs = new MatchResultSet(iterateStrategy,distanceStrategy,sortStrategy);
        iterateStrategy.setWordFromDB(wordFromDB);
        iterateStrategy.setSearchString(searchString.getName());

        ArrayList<MatchResult> matchResults = iterateStrategy.getMatchList(distanceStrategy);
        ArrayList<MatchResult> sortedResults=null;
        if(matchResults.size()==1 && matchResults.get(0).getScore()<accuracy){

                sortedResults = matchResults;
                mrs.addMatchResults(sortedResults, wordFromDB);
                return mrs;

        }else{
            return null;
        }
        //sortedResults = sortStrategy.sort(matchResults);
        //mrs.addMatchResults(sortedResults,wordFromDB);

        //return mrs;
    }



    public MatchResultSet getMatchingWordList(Word searchString, ArrayList<Word> words){
        MatchResultSet mrs = new MatchResultSet(iterateStrategy,distanceStrategy,sortStrategy);
        //int count=0;
        for(Word w: words){
            MatchResultSet result = getMatchResult(searchString,w);
            if (result != null && result.getMatchResults().size()>0 ){
                mrs.addMatchResults(result.getMatchResults().get(0), w);
            }
        }
        return mrs;
    }

    public SortStrategy getSortStrategy() {
        return sortStrategy;
    }

    public void setSortStrategy(SortStrategy sortStrategy) {
        this.sortStrategy = sortStrategy;
    }

    public DistanceStrategy getDistanceStrategy() {
        return distanceStrategy;
    }

    public void setDistanceStrategy(DistanceStrategy distanceStrategy) {
        this.distanceStrategy = distanceStrategy;
    }

    public void setIterateStrategy(IterateStrategy iterateStrategy){
        this.iterateStrategy = iterateStrategy;
    }

    public IterateStrategy getIterateStrategy(){
        return this.iterateStrategy;
    }

    public MatchResultSet getCurrentMatchingWordList() {
        return currentMatchingWordList;
    }
}
