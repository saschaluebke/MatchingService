package database;

import components.Word;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Can only search for root forms of words use edu.mit.jwi.morph.WordnetStemmer to transform in rootform
 *
 */
public class WordnetHelper {
/*
    private String path;
    //private IDictionary dict;
    private File wnDir;

    public WordnetHelper(){

        String wnhome = System.getProperty("user.dir");
        path = wnhome + File.separator + "dict";
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

    public void allWordsToDatabase(DBHelper dbh,POS pos){
        Iterator<IIndexWord> iter = dict.getIndexWordIterator(pos);
        ArrayList<Word> wordList = new ArrayList<>();
        while(iter.hasNext()){

            IIndexWord word = iter.next();
            Word w = new Word(0,word.getLemma(),"en");
            wordList.add(w);
        }

        dbh.putWordListAll(wordList);

    }

    public void loadInMemory() {
        IRAMDictionary dict = new RAMDictionary(wnDir, ILoadPolicy.NO_LOAD);
        try {
            dict.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("\nLoading Wordnet into memory ... ");
        long t = System.currentTimeMillis();
        try {
            dict.load(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("done (%1d msec )\n", System.currentTimeMillis() - t);
    }

    public ArrayList<String> getSynonyms(IDictionary dict, String searchWord) {
        ArrayList<String> resultWords = new ArrayList<>();

        // look up first sense of the word
        IIndexWord idxWord = dict.getIndexWord(searchWord, POS.NOUN);
        IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning
        IWord word = dict.getWord(wordID);
        ISynset synset = word.getSynset();

        // iterate over words associated with the synset
        for (IWord w : synset.getWords()) {

            resultWords.add(word.getLemma());
        }
        return resultWords;
    }

    public ArrayList<String> getHypernyms(IDictionary dict, String searchWord) {
        ArrayList<String> resultWords = new ArrayList<>();

        // get the synset
        IIndexWord idxWord = dict.getIndexWord(searchWord, POS.NOUN);
        IWordID wordID = idxWord.getWordIDs().get(0); // 1st meaning
        IWord word = dict.getWord(wordID);
        ISynset synset = word.getSynset();

        // get the hypernyms
        List<ISynsetID> hypernyms =
                synset.getRelatedSynsets(Pointer.HYPERNYM);
// print out each h y p e r n y m s id and synonyms
        List<IWord> words;
        for (ISynsetID sid : hypernyms) {
            words = dict.getSynset(sid).getWords();

            for (Iterator<IWord> i = words.iterator(); i.hasNext(); ) {
                resultWords.add(i.next().getLemma());

            }
        }

        return resultWords;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public IDictionary getDictionary(){
        return dict;
    }
    */
}
