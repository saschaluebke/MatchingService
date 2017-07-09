package utils;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

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

    public WordNetReader(String path, String language){
        this.path = path;
        this.language = language;
        dbh = new DBHelper(new SimpleStrategy(),null);
        //String wnhome = System.getProperty("user.dir");
        //path = wnhome + File.separator + "dict";
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
    }

    public void testDictionary() throws IOException {


        IIndexWord idxWord = dict.getIndexWord("dog", POS.NOUN);
        IWordID wordID = idxWord.getWordIDs().get(0);
        IWord word = dict.getWord(wordID);
        System.out.println("Id = " + wordID);
        System.out.println(" Lemma = " + word.getLemma());
        System.out.println(" Gloss = " + word.getSynset().getGloss());
    }
/*
    @Override
    public void getFileContent() {
        String wordForm = "capacity";
        //  Get the synsets containing the word form=capicity

        File f=new File("WordNet\\2.1\\dict");
        System.setProperty("wordnet.database.dir", f.toString());
        //setting path for the WordNet Directory

        WordNetDatabase database = WordNetDatabase.getFileInstance();
        Synset[] synsets = database.getSynsets(wordForm);
        //  Display the word forms and definitions for synsets retrieved

        if (synsets.length > 0){
            ArrayList<String> al = new ArrayList<String>();
            // add elements to al, including duplicates
            HashSet hs = new HashSet();
            for (int i = 0; i < synsets.length; i++){
                String[] wordForms = synsets[i].getWordForms();
                for (int j = 0; j < wordForms.length; j++)
                {
                    al.add(wordForms[j]);
                }


                //removing duplicates
                hs.addAll(al);
                al.clear();
                al.addAll(hs);

                //showing all synsets
                for (int i = 0; i < al.size(); i++) {
                    System.out.println(al.get(i));
                }
            }
        }
              else
    {
        System.err.println("No synsets exist that contain the word form '" + wordForm + "'");
    }
    }
*/

    @Override
    public void getFileContent() {
        synonymList = new ArrayList<>();
        int entryCount = 0;
        wordList = new ArrayList<>();
            int lastId = dbh.getLastWordId(language);
                Iterator<IIndexWord> iter = dict.getIndexWordIterator(POS.NOUN);
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
                lastId++;
                //System.out.println(lastId+":"+w.getLemma());
                Word word  = new Word(lastId,w.getLemma(),language);
                wordList.add(word);
                ISynset synset = w.getSynset();
                ArrayList<Word> synonyms = new ArrayList<>();
                for(IWord word1 : synset.getWords()){
                   // System.out.println(word1.getLemma());
                    lastId++;
                    Word synonym = new Word(lastId,word1.getLemma(),language);
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
