package components;

import java.util.ArrayList;

/**
 * Translation results of the SynonymStrategy
 * Why 3 layers of lists for matchingSynonyms? ->
 * 1. Input word could be split up into several words "diabetes mellitus"
 * 2. Several matchtes could be found for "diabetes" and "mellitus"
 * 3. Several synonyms could be found for these matches.
 */
public class SynonymTranslationResult implements TranslationResult{
    private Word input;
    private String directTranslation;
    private ArrayList<ArrayList<ArrayList<String>>> matchingSynonyms;
    private ArrayList<ArrayList<ArrayList<String>>> matchingSynonymtranslations;
    private ArrayList<ArrayList<String>> matchings;
    private ArrayList<ArrayList<String>> translatedMatchings;
    private ArrayList<String> words;
    private int synonymCount, realSynCount;
    private int matchingCount;

    public SynonymTranslationResult(Word input){
        this.input = input;
    }

    public String getDirectTranslation() {
        return directTranslation;
    }

    public void setDirectTranslation(String directTranslations) {
        this.directTranslation = directTranslations;
    }

    @Override
    public Word getInput() {
        return input;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getMatchingSynonyms() {
        return matchingSynonyms;
    }

    public void setMatchingSynonyms(ArrayList<ArrayList<ArrayList<String>>> matchingSynonyms) {
        this.matchingSynonyms = matchingSynonyms;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getMatchingSynonymtranslations() {
        return matchingSynonymtranslations;
    }

    public void setMatchingSynonymtranslations(ArrayList<ArrayList<ArrayList<String>>> matchingSynonymtranslations) {
        this.matchingSynonymtranslations = matchingSynonymtranslations;
    }

    public int getSynonymCount() {
        return synonymCount;
    }

    public void setSynonymCount(int synonymCount) {
        this.synonymCount = synonymCount;
    }

    public ArrayList<ArrayList<String>> getMatchings() {
        return matchings;
    }

    public void setMatchings(ArrayList<ArrayList<String>> matchings) {
        this.matchings = matchings;
    }

    public ArrayList<ArrayList<String>> getTranslatedMatchings() {
        return translatedMatchings;
    }

    public void setTranslatedMatchings(ArrayList<ArrayList<String>> translatedMatchings) {
        this.translatedMatchings = translatedMatchings;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public int getMatchingCount() {
        return matchingCount;
    }

    public void setMatchingCount(int matchingCount) {
        this.matchingCount = matchingCount;
    }

    public int getRealSynCount() {
        return realSynCount;
    }

    public void setRealSynCount(int realSynCount) {
        this.realSynCount = realSynCount;
    }
}
