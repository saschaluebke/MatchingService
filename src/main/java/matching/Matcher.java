package matching;

import components.MatchResult;
import components.MatchResultSet;
import components.Word;
import database.DBHelper;
import matching.distance.DistanceStrategy;
import matching.iterate.IterateStrategy;
import matching.sorting.SortStrategy;
import java.util.ArrayList;

public class Matcher {
    private SortStrategy sortStrategy;
    private DistanceStrategy distanceStrategy;
    private IterateStrategy iterateStrategy;

    public Matcher(IterateStrategy iterateStrategy, DistanceStrategy distanceStrategy,SortStrategy sortStrategy){
        this.iterateStrategy = iterateStrategy;
        this.sortStrategy = sortStrategy;
        this.distanceStrategy = distanceStrategy;
    }

    public MatchResultSet getMatchResult(Word searchString, Word wordFromDB){
        MatchResultSet mrs = new MatchResultSet(iterateStrategy,distanceStrategy,sortStrategy);
        iterateStrategy.setWordFromDB(wordFromDB);
        iterateStrategy.setSearchString(searchString.getName());
        ArrayList<MatchResult> matchResults = iterateStrategy.getMatchList(distanceStrategy);
/*
        for(MatchResult mr : matchResults){
            mr.setScore(distanceStrategy.getDistance(mr.getSearchString(),mr.getDbString()));
        }
*/


        ArrayList<MatchResult> sortedResults = sortStrategy.sort(matchResults);
        mrs.addMatchResults(sortedResults,wordFromDB);
        return mrs;
    }



    public MatchResultSet getMatchingWordList(Word searchString, ArrayList<Word> words){
        MatchResultSet mrs = new MatchResultSet(iterateStrategy,distanceStrategy,sortStrategy);
        //int count=0;
        for(Word w: words){
            MatchResultSet result = getMatchResult(searchString,w);
            if (result.getMatchResults().size()>0){
                mrs.addMatchResults(result.getMatchResults().get(0), w);
            }
           // count++;
           // System.out.println(count+": "+w.getName());
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
}
