package matching.iterate;

import components.MatchResult;
import components.Word;
import database.TranslatorGetProperties;
import matching.distance.DistanceStrategy;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sashbot on 06.08.17.
 */
public class WordPerformanceStrategy extends WordStrategy implements IterateStrategy {
    private final String REGEX = "( )|(/)"; //Split by space or /
    private String searchString;
    private Word wordFromDB;
    private ArrayList<MatchResult> matchResults;
    private double accuracy = 0;

    public WordPerformanceStrategy(){
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
        //String[] splitted = searchString.split(REGEX);
        //for(String currentString : splitted){
        String currentString = searchString;
       // if (currentString.length()>wordFromDB.getName().length()){
      //      matchResults.add(new MatchResult(wordFromDB,searchString,distanceStrategy.getDistance(searchString,wordFromDB.getName()),0,wordFromDB.getName().length(),0,wordFromDB.getName().length()));
     //   }else{
            for (int i = 0; i < wordFromDB.getName().length() - currentString.length()+1; i++) {
                double distance = distanceStrategy.getDistance(currentString,wordFromDB.getName().substring(i,i+currentString.length()));
                //if (distance < accuracy){
                    matchResults.add(new MatchResult(wordFromDB,currentString,distance,i,i+currentString.length(),0,currentString.length()));

                //}
            }
      //  }

        return matchResults;
    }
}

