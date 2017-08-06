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

    @Override
    public ArrayList<MatchResult> getMatchList(DistanceStrategy distanceStrategy) {
        matchResults = new ArrayList<>();
        String[] splitted = searchString.split(REGEX);
        for(String string: splitted){
            MatchResult newMatchResult = new MatchResult(wordFromDB,string,distanceStrategy.getDistance(searchString,wordFromDB.getName()),0,searchString.length(),0,wordFromDB.getName().length());
            matchResults.add(newMatchResult);
        }
        return matchResults;
    }
}
