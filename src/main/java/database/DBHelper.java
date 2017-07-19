package database;

import components.MatchResultSet;
import components.Relation;
import components.TranslationResult;
import components.Word;
import database.dbStrategy.DBStrategy;
import matching.Matcher;
import translators.Translator;
import utils.ontology.FileReader;

import java.util.ArrayList;

public class DBHelper{
    private DBStrategy dbStrategy;
    private Translator translator;

    public DBHelper(DBStrategy dbStrategy, Translator translator){
        this.dbStrategy = dbStrategy;
        this.translator = translator;
    }

    public DBHelper(DBStrategy dbStrategy){
        this.dbStrategy = dbStrategy;
    }

    public int putWord(Word word) {

        return dbStrategy.putWord(word);
    }

    public int putRelation(Word word1, Word word2) {
        return dbStrategy.putRelation(word1,word2);
    }

    public void putRelationList(ArrayList<Relation> relations, String language1, String language2){
        dbStrategy.putRelationList(relations,language1,language2);
    }

    public void storeFromFile(FileReader fr){
        dbStrategy.storeFromFile(fr);
    }

    public void putWordList(ArrayList<Word> wordList, String language){
        dbStrategy.putWordList(wordList,language);
    }

    public ArrayList<Word> getAllWords(String language) {
        return dbStrategy.getAllWords(language);
    }

    public ArrayList<Relation> getAllRelations(String languageFrom, String languageTo) {
        return dbStrategy.getAllRelations(languageFrom,languageTo);
    }

    public MatchResultSet searchWord(Word word) {
        return dbStrategy.searchWord(word);
    }

    public Word getWordById(int id, String  language) {
        return dbStrategy.getWordById(id,language);
    }

    public ArrayList<Relation> getRelation(Word word, String from, String to) {
        return dbStrategy.getRelation(word,from, to);
    }

    public ArrayList<Word> getWordFromRelation(Word word, String fromLanguage, String toLanguage){
        return dbStrategy.getWordFromRelation(word,fromLanguage,toLanguage);
    }

    public int getRelationFromId(int id, String fromLanguage, String toLanguage) {
        return dbStrategy.getRelationFromId(id,fromLanguage,toLanguage);
    }


    public void removeRelation(Relation relation, String fromLanguage, String toLanguage) {
        dbStrategy.removeRelation(relation,fromLanguage,toLanguage);
    }

    public void removeWord(Word word, String language) {
        dbStrategy.removeWord(word,language);
    }

    public Matcher getMatcher(){
        return dbStrategy.getMatcher();
    }
    public void setMatcher(Matcher matcher){
        dbStrategy.setMatcher(matcher);
    }

    public boolean newLanguage(String language) {
        return dbStrategy.newLanguage(language);
    }


    public DBStrategy getDbStrategy() {
        return dbStrategy;
    }

    public void setDbStrategy(DBStrategy dbStrategy) {
        this.dbStrategy = dbStrategy;
    }

    public ArrayList<Word> getWordList(String name, String language){
        return dbStrategy.getWordList(name,language);
    }

    public int getLastWordId(String language){
        return dbStrategy.getLastWordId(language);
    }

    public ArrayList<String> translate(TranslationResult translationResult,ArrayList<Word> allWords, ArrayList<Relation> allRelations){
        if(translator==null){
            System.out.println("Translator is null in DBHelper");
        }
        Word input = translationResult.getInput();
        /**
         * Clean input from big chars and braces
         */
        input.setWord(input.getName().toLowerCase());

        return dbStrategy.translate(translationResult,translator,allWords,allRelations);
    }

    public String print(String language1, String language2){
        return dbStrategy.print(language1,language2);
    }
}
