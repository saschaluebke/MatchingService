package components;

import java.util.ArrayList;

/**
 * Translation results of the SynonymStrategy
 */
public class SynonymTranslationResult implements TranslationResult{
    private Word input;
    private String directTranslation;
    private ArrayList<String> matchings;
    private ArrayList<String> matchingTranslations;
    private ArrayList<ArrayList<String>> synonyms;
    private ArrayList<ArrayList<String>> synonymTranslations;

    public SynonymTranslationResult(Word input){
        this.input = input;
    }

    public String getDirectTranslation() {
        return directTranslation;
    }

    public void setDirectTranslation(String directTranslations) {
        this.directTranslation = directTranslations;
    }

    public ArrayList<ArrayList<String>> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<ArrayList<String>> synonyms) {
        this.synonyms = synonyms;
    }

    public ArrayList<ArrayList<String>> getSynonymTranslations() {
        return synonymTranslations;
    }

    public void setSynonymTranslations(ArrayList<ArrayList<String>> synonymTranslations) {
        this.synonymTranslations = synonymTranslations;
    }

    @Override
    public Word getInput() {
        return input;
    }

    public ArrayList<String> getMatchings() {
        return matchings;
    }

    public void setMatchings(ArrayList<String> matchings) {
        this.matchings = matchings;
    }

    public ArrayList<String> getMatchingTranslations() {
        return matchingTranslations;
    }

    public void setMatchingTranslations(ArrayList<String> matchingTranslations) {
        this.matchingTranslations = matchingTranslations;
    }
}
