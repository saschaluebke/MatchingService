package matching.iterate;

import components.MatchResult;
import components.Word;
import matching.distance.DistanceStrategy;

import java.util.ArrayList;

public class WordStrategy implements IterateStrategy {
    private final String REGEX = "( )|(/)"; //Split by space or /
    private String searchString;
    private Word wordFromDB;
    private ArrayList<MatchResult> matchResults;

    @Override
    public void setWordFromDB(Word wordFromDB) {
        this.wordFromDB = wordFromDB;
    }

    @Override
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    //TODO: testen wie viel ersparniss (wie viele worttrenner im mittel pro wort)
    @Override
    public ArrayList<MatchResult> getMatchList(DistanceStrategy distanceStrategy) {
        matchResults = new ArrayList<>();
        String[] splitted = wordFromDB.getName().split(REGEX);
        for(String s: splitted){
            //TODO: die Matchresults haben nicht die richtigen Werte für dbString
            //da die String aufgespalten werden!
            CharacterStrategy cs = new CharacterStrategy();
            cs.setSearchString(searchString);
            cs.setWordFromDB(new Word(0,s,wordFromDB.getLanguage())); //TODO: man müsste bei den gespalteten Wörtern erstmal schauen ob es sie in der Datenbank schon gibt...
            matchResults.addAll(cs.getMatchList(distanceStrategy));
        }
        return matchResults;
    }
}
