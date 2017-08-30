package matching.iterate;

import components.MatchResult;
import components.Word;
import database.TranslatorGetProperties;
import matching.distance.DistanceStrategy;

import java.io.IOException;
import java.util.ArrayList;

public class WordStrategy implements IterateStrategy {
    private final String REGEX = "( )|(/)"; //Split by space or /
    private String searchString;
    private Word wordFromDB;
    private ArrayList<MatchResult> matchResults;
    private double accuracy;

    public WordStrategy(){
        TranslatorGetProperties tgp = new TranslatorGetProperties();
        accuracy = 0;
        try {
            accuracy = Double.parseDouble(tgp.getPropValues("Translate.accuracy"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        String currentString = searchString;
        String[] splitted = searchString.split(REGEX);
            for(String string: splitted){
                double distance = distanceStrategy.getDistance(searchString,wordFromDB.getName());
                //if (distance < accuracy){
                    MatchResult newMatchResult = new MatchResult(wordFromDB,string,distanceStrategy.getDistance(searchString,wordFromDB.getName()),0,searchString.length(),0,wordFromDB.getName().length());
                    matchResults.add(newMatchResult);
                //}

            }

        return matchResults;
    }
}
