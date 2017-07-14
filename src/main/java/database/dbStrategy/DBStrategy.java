package database.dbStrategy;

import components.MatchResult;
import components.MatchResultSet;
import components.Relation;
import components.Word;
import database.DBHelper;
import translators.Translator;
import utils.FileReader;

import java.util.ArrayList;


public interface DBStrategy {

    int putWord(Word word);
    void putWordList(ArrayList<Word> wordList, String language);
    int putRelation(Word word1, Word word2);
    void putRelationList(ArrayList<Relation> relations, String language1, String language2);
    void storeFromFile(FileReader fr);

    MatchResultSet searchWord(Word word);
    Word getWordById(int id, String language);
    ArrayList<Word> getWordList(String name, String language);
    ArrayList<Word> getAllWords(String language);
    void removeWord(Word word, String language);

    ArrayList<Relation> getRelation(Word word, String from, String to);
    ArrayList<Word> getWordFromRelation(Word word, String LanguageFrom, String LanguageTo);
    ArrayList<Relation> getAllRelations(String languageFrom, String languageTo);
    int getRelationFromId(int id, String fromLanguage, String toLanguage);
    void removeRelation(Relation relation, String fromLanguage, String toLanguage);

    boolean newLanguage(String name);
    boolean updateTables();
    int getLastWordId(String query);
    //ArrayList<String> translate(Translator translator, Word input);
    ArrayList<String> translate(Translator translator, Word input, ArrayList<Word> allWords,
    ArrayList<Relation> allRelation);

    String print(String language1, String language2);

}
