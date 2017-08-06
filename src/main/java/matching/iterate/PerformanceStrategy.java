package matching.iterate;

import components.MatchResult;
import components.Word;
import matching.distance.DistanceStrategy;

import java.util.ArrayList;

//TODO: Test if searchString > dbSTring
public class PerformanceStrategy implements IterateStrategy {
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
        if (searchString.length()>wordFromDB.getLanguage().length()){
            matchResults.add(new MatchResult(wordFromDB,searchString,distanceStrategy.getDistance(searchString,wordFromDB.getName()),0,wordFromDB.getName().length(),0,wordFromDB.getName().length()));
        }else{

            String[] splitted = searchString.split(REGEX);
            for(String currentString : splitted){
                for (int i = 0; i < wordFromDB.getName().length() - currentString.length()+1; i++) {
                    matchResults.add(new MatchResult(wordFromDB,currentString,distanceStrategy.getDistance(currentString,wordFromDB.getName().substring(i,i+currentString.length())),i,i+currentString.length(),0,currentString.length()));
                }
            }

        }
        return matchResults;
    }

}
