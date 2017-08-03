package matching;

import components.MatchResult;
import components.MatchResultSet;
import components.Word;
import matching.distance.DistanceStrategy;
import matching.distance.JaroWinkler;
import matching.iterate.IterateStrategy;
import matching.sorting.SortStrategy;

import java.util.ArrayList;

public class JaroWinklerMatcher  extends Matcher {
    JaroWinkler jw = new JaroWinkler();

    public JaroWinklerMatcher(IterateStrategy iterateStrategy, DistanceStrategy distanceStrategy, SortStrategy sortStrategy) {
        super(null, null, null);
    }
    public JaroWinklerMatcher(){
        super(null, null, null);
    }

    public MatchResultSet getMatchResult(Word searchWord, Word wordFromDB){
        MatchResultSet mrs = new MatchResultSet(null,null,null);
        ArrayList<ArrayList<MatchResult>> output = new ArrayList<>();
        String stringFromDB = wordFromDB.getName();
        ArrayList<MatchResult> mrlist = new ArrayList<>();
        double index = 0;
        String searchString = searchWord.getName();
        String[] searchStrings = searchString.split(" ");
        for(String string : searchStrings){
            index = matchIndex(string,stringFromDB);
            if(index<0.1){
                MatchResult mr = new MatchResult(wordFromDB,stringFromDB,0,0,
                        string.length()-1,0,string.length()-1);
                mrlist.add(mr);
                output.add(mrlist);
                mrs.setMatchResults(output);
            }
        }

        mrs.setMatchResults(output);
        return mrs;
    }

    public MatchResultSet getMatchingWordList(Word searchString, ArrayList<Word> words){
        MatchResultSet mrs = new MatchResultSet(null,null,null);
        //int count=0;
        for(Word w: words){
            MatchResultSet result = getMatchResult(searchString,w);
            if (result.getMatchResults().size()>0){
                mrs.addMatchResults(result.getMatchResults().get(0), w);
            }
        }
        return mrs;
    }

    public double matchIndex(String search, String stringFromDB){
        double index;
        index = jw.getDistance(search,stringFromDB);
        return index;
    }
}
