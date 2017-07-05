package matching.iterate;

import components.MatchResult;
import components.Word;
import matching.distance.DistanceStrategy;

import java.util.ArrayList;

public class CharacterStrategy implements IterateStrategy {

    private String searchString;
    private Word wordFromDB;
    private ArrayList<MatchResult> matchList;

    @Override
    public ArrayList<MatchResult> getMatchList(DistanceStrategy distanceStrategy) {
        this.matchList = new ArrayList<>();

        for(int v=3;v<=searchString.length();v++) {
            for (int h = 0; h < v - 2; h++) {
                String currentSearchString = searchString.substring(h,v);

                for (int i = 0; i < wordFromDB.getName().length() - currentSearchString.length()+1; i++) {
                    MatchResult newMatchResult = new MatchResult(wordFromDB,searchString,distanceStrategy.getDistance(currentSearchString,wordFromDB.getName().substring(i,i+currentSearchString.length())),i,i+currentSearchString.length(),h,v);
                    matchList.add(newMatchResult);
                }
            }
        }

        return matchList;
    }
    @Override
    public void setWordFromDB(Word wordFromDB) {
        this.wordFromDB = wordFromDB;
    }

    @Override
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }




}
