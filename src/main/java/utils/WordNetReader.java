package utils;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by sascha on 02.07.17.
 */
public class WordNetReader implements FileReader {
    ArrayList<Word> wordList;
    ArrayList<ArrayList<Word>> synonymList;
    String language,path;
    private IDictionary dict;
    private File wnDir;
    private DBHelper dbh;
    private int fromEntry, toEntry;
    private int entryCount;

    public WordNetReader(String path, String language){
        this.path = System.getProperty("user.dir")+path;
        this.language = language;
        //dbh = new DBHelper(new SimpleStrategy());
    }

public int getAllLines(){
    int allLines=0;


    Iterator<IIndexWord> iter = getIter();
    while(iter.hasNext()){
        allLines++;
        IIndexWord indexWord = iter.next();
        IWordID wordID = indexWord.getWordIDs().get(0);
        IWord w = dict.getWord(wordID);
        //System.out.println(w.getLemma());
    }
    return allLines;
}

    public Iterator<IIndexWord> getIter() {
        URL url = null;
        try {
            url = new URL("file", null, path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        wnDir = new File(path);

        dict = new Dictionary(url);
        try {
            dict.open();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<IIndexWord> iter = dict.getIndexWordIterator(POS.NOUN);

        return iter;
    }

    @Override
    public void getFileContent() {

        synonymList = new ArrayList<>();
        entryCount = 0;
        wordList = new ArrayList<>();
       // int lastId = dbh.getLastWordId(language);

        Iterator<IIndexWord> iter = getIter();
        while(iter.hasNext()){

                entryCount++;
                if (entryCount < fromEntry || entryCount > toEntry){
                    if (entryCount > toEntry){
                        break;
                    }
                    if (entryCount%10000 == 0){
                        System.out.println("EntryCount: "+entryCount);
                    }

                    continue;
                }

                IIndexWord indexWord = iter.next();
                IWordID wordID = indexWord.getWordIDs().get(0);
                IWord w = dict.getWord(wordID);
                //lastId++;
                //System.out.println(lastId+":"+w.getLemma());
                Word word  = new Word(0,w.getLemma(),language);
                wordList.add(word);
                ISynset synset = w.getSynset();
                ArrayList<Word> synonyms = new ArrayList<>();
                for(IWord word1 : synset.getWords()){
                   // System.out.println(word1.getLemma());
                  //  lastId++;
                    Word synonym = new Word(0,word1.getLemma(),language);
                    synonyms.add(synonym);
                }
                synonymList.add(synonyms);

            }

    }

    @Override
    public ArrayList<Word> getWordList() {
        return wordList;
    }

    @Override
    public ArrayList<Word> getSecondWordList() {
        return null;
    }

    @Override
    public ArrayList<Relation> getFirstTranslation() {
        return null;
    }

    @Override
    public ArrayList<Relation> getSecondTranslation() {
        return null;
    }

    @Override
    public String getFirstLanguage() {
        return language;
    }

    @Override
    public String getSecondLanguage() {
        return language;
    }

    @Override
    public ArrayList<ArrayList<Word>> getSynonyms() {
        return synonymList;
    }

    public void setToEntry(int toEntry) {
        this.toEntry = toEntry;
    }

    public void setFromEntry(int fromEntry) {
        this.fromEntry = fromEntry;
    }

}
