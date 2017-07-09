package utilsTests;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.OwlReader;
import utils.WordNetHelper;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 02.07.17.
 */
public class WordNetTest {
    static MySQLQuery dbq;
    static DBHelper dbh;
    static WordNetHelper wnh;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh = new DBHelper(new SimpleStrategy());
        wnh = new WordNetHelper("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/WordNet/WordNet-3.0/dict","en");
    }

    @Test
    public void getBigFileContentTest() {
        dbh.newLanguage("en");
        wnh.setFromEntry(0);
        wnh.setToEntry(100);
        wnh.getFileContent();
      //  fr.setFromEntry(0);
      //  fr.setToEntry(50000);
        //dbh.takeFromFileReader(wnh);
        ArrayList<Relation> relationArrayList = wnh.getRelations();
        ArrayList<Word> wordlist = wnh.getWordList();
        ArrayList<ArrayList<Word>> wordsWithSynonyms = new ArrayList<>();
        for(int i=0;i<100;i++)
        {
            Word w = wordlist.get(i);
            ArrayList<Word> synonyms = new ArrayList<>();
            for(Relation r : relationArrayList){
                if(r.getIdFrom()==w.getId()){
                    for(Word w2 : wordlist){
                        if (w2.getId()==r.getIdTo()){
                            synonyms.add(w2);
                        }
                    }
                }
            }
            wordsWithSynonyms.add(synonyms);
        }
        for(int i = 0; i<100;i++){
            System.out.print(wordlist.get(i).getName()+": ");
            for(Word word : wordsWithSynonyms.get(i)){
                System.out.print(word.getName()+"/");
            }
            System.out.println(".");
        }
        assertEquals(360, wnh.getWordList().size());

    }
}
