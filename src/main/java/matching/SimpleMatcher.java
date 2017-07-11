package matching;

import components.MatchResult;
import components.MatchResultSet;
import components.Word;
import matching.distance.DistanceStrategy;
import matching.iterate.IterateStrategy;
import matching.sorting.SortStrategy;

import java.util.ArrayList;

/**
 * Created by sashbot on 10.07.17.
 */
public class SimpleMatcher extends Matcher {
    public SimpleMatcher(IterateStrategy iterateStrategy, DistanceStrategy distanceStrategy, SortStrategy sortStrategy) {
        super(null, null, null);
    }
    public SimpleMatcher(){
        super(null, null, null);
    }

    public MatchResultSet getMatchResult(Word searchWord, Word wordFromDB){
        MatchResultSet mrs = new MatchResultSet(null,null,null);
        ArrayList<ArrayList<MatchResult>> output = new ArrayList<>();
        String stringFromDB = wordFromDB.getName();
        ArrayList<MatchResult> mrlist = new ArrayList<>();
        int index = 0;
        String partOfStringFromDB = stringFromDB;
        index = matchIndex(stringFromDB,partOfStringFromDB);
        MatchResult mr = new MatchResult(wordFromDB,stringFromDB,1,index,
                    searchWord.getName().length()+index,0,searchWord.getName().length());
        mrlist.add(mr);
        output.add(mrlist);
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

    public int matchIndex(String search, String stringFromDB){
        int index;
        index = stringFromDB.indexOf(search);
        return index;
    }
}
