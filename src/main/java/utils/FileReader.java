package utils;

import components.Relation;
import components.Word;

import java.util.ArrayList;

public interface FileReader {
    void getFileContent();
    ArrayList<Word> getWordList();
    ArrayList<Word> getSecondWordList(); //For parallel Textcorpus
    ArrayList<Relation> getFirstTranslation(); //For parallel Textcorpus
    ArrayList<Relation> getSecondTranslation(); //For parallel Textcorpus (other way round)
    ArrayList<Relation> getRelations(); //for Synonyms
    String getFirstLanguage();
    String getSecondLanguage();
    ArrayList<ArrayList<Word>> getSynonyms();
}
