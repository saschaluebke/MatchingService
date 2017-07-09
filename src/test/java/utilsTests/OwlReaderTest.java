package utilsTests;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.FileReader;
import utils.OwlReader;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 30.06.17.
 */
public class OwlReaderTest {
    static MySQLQuery dbq;
    static DBHelper dbh;
    static OwlReader fr;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh = new DBHelper(new SimpleStrategy());
        fr = new OwlReader("/src/main/resources/NCI/Thesaurus-byName.owl","en");

    }
/*
    @Test
    public void getSmallFileContentTest() {
        dbh.newLanguage("de");
        int count = or.getFileContent("/src/main/resources/NCI_Thesaurus/Thesaurus-byName.owl",121510);
        assertEquals(11,count);
    }

    @Test
    public void getBigFileContentTest() {
        dbh.newLanguage("de");

        fr.setFromEntry(0);
        fr.setToEntry(50000);
        dbh.takeFromFileReader(fr);

        assertEquals(187822,fr.getWordList().size());

         fr.setFromEntry(50000);
         fr.setToEntry(130000);
        dbh.takeFromFileReader(fr);

        assertEquals(260417,fr.getWordList().size());

        assertEquals(326718,dbh.getAllRelations("de","de").size());
    }
*/
    @Test
    public void synonymTest(){
        dbh.newLanguage("en");
        fr.setFromEntry(0);
        fr.setToEntry(1000);
        fr.getFileContent();

        ArrayList<Word> wordlist = fr.getWordList();
        ArrayList<Relation> relationArrayList = fr.getRelations();
        ArrayList<ArrayList<Word>> wordsWithSynonyms = new ArrayList<>();

        for(int i=0;i<1000;i++)
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
        for(int i = 0; i<1000;i++){
            System.out.print(wordlist.get(i).getName()+": ");
            for(Word word : wordsWithSynonyms.get(i)){
                System.out.print(word.getName()+"/");
            }
            System.out.println(".");
        }
    }


}
