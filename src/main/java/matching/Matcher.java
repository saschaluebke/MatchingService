package matching;

import components.MatchResult;
import components.MatchResultSet;
import components.Word;
import database.DBHelper;
import database.TranslatorGetProperties;
import matching.distance.DistanceStrategy;
import matching.distance.EqualDistance;
import matching.iterate.IterateStrategy;
import matching.iterate.SimpleStrategy;
import matching.sorting.SortStrategy;

import java.io.IOException;
import java.util.ArrayList;

public class Matcher {
    private SortStrategy sortStrategy;
    private DistanceStrategy distanceStrategy;
    private IterateStrategy iterateStrategy;
    private MatchResultSet currentMatchingWordList;
    private double accuracy;
    private int maxMatches;

    public Matcher(IterateStrategy iterateStrategy, DistanceStrategy distanceStrategy,SortStrategy sortStrategy){
        this.iterateStrategy = iterateStrategy;
        this.sortStrategy = sortStrategy;
        this.distanceStrategy = distanceStrategy;
        TranslatorGetProperties tgp = new TranslatorGetProperties();
        try {
            accuracy = Double.parseDouble(tgp.getPropValues("Translate.accuracy"));
            maxMatches = Integer.parseInt(tgp.getPropValues("Matcher.maxMatches"));
           // mrs.getMatchResults().size()<maxMatches && //TODO: Nicht die matches limitieren sondern die übersetzung
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MatchResultSet getMatchResult(String searchString, Word wordFromDB){
        MatchResultSet mrs;
        if(searchString.length() < 4){
            mrs = new MatchResultSet(new SimpleStrategy(),new EqualDistance(),sortStrategy);

        }else {
            mrs = new MatchResultSet(iterateStrategy, distanceStrategy, sortStrategy);
        }
            iterateStrategy.setWordFromDB(wordFromDB);
            iterateStrategy.setSearchString(searchString);

            ArrayList<MatchResult> matchResults = iterateStrategy.getMatchList(distanceStrategy);
            ArrayList<MatchResult> sortedResults=null;
      /*  if(matchResults.size()==1 && matchResults.get(0).getScore()<accuracy){

                sortedResults = matchResults;
                mrs.addMatchResults(sortedResults, wordFromDB);
                return mrs;

        }else*/ if(matchResults.size() >= 1){
              // sortedResults = sortStrategy.sort(matchResults);
            if(matchResults.get(0).getScore() < accuracy){
                mrs.addMatchResults(matchResults,wordFromDB);
            }
/*                if(sortedResults.get(0).getScore() < accuracy){
                    mrs.addMatchResults(sortedResults,wordFromDB);
                    //System.out.println(mrs.getMatchResults().get(0).size());
                }

*/

                return mrs;
            }else{
                return null;
            }
        }






    public MatchResultSet getMatchingWordList(String searchString, ArrayList<Word> words){
        MatchResultSet mrs = new MatchResultSet(iterateStrategy,distanceStrategy,sortStrategy);
        //int count=0;
        for(Word w: words){
            MatchResultSet result = getMatchResult(searchString,w);
            if (result != null && result.getMatchResults().size()>0 ){
                ArrayList<MatchResult> mr = result.getMatchResults().get(0);
                if (mr != null && mr.size()>0){
                    mrs.addMatchResults(mr, w);
                }

            }
        }
        if(mrs.getMatchResults().size()>0){
            mrs = sortStrategy.sortList(mrs);
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
