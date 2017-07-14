package utilsTests;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
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
    static OwlReader owlReader;
    static int lineCount;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh = new DBHelper(new SynonymStrategy());
        owlReader = new OwlReader("/src/main/resources/ontologies/NCI/NCI.owl","de");

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
    public void getAllWordsTest(){
        ArrayList<String> allLines = owlReader.getAllLines();
        lineCount = allLines.size();
        assertEquals(121520,lineCount);
    }

    @Test
    public void simpleTest(){

        //ArrayList<String> allLines = owlReader.getAllLines();
        //lineCount = allLines.size();
        dbh.newLanguage("de");
        owlReader.setFromEntry(0);
        owlReader.setToEntry(1000);
        dbh.storeFromFile(owlReader);


        //System.out.println(dbh.print("en","en"));
        assertEquals(3175,dbh.getAllWords("de").size());
    }

/*
    @Test
    public void synonymTest(){

        ArrayList<String> allLines = owlReader.getAllLines();
        lineCount = allLines.size();
        dbh.newLanguage("en");
        int tmp=0;
        for(int i = 0; i<lineCount+100000; i=i+100000){

            owlReader.setFromEntry(tmp);
            owlReader.setToEntry(i);
            dbh.storeFromFile(owlReader);
            tmp=i;
        }

       //System.out.println(dbh.print("en","en"));
       assertEquals(1591354,dbh.getAllWords("en").size());
    }
*/

}

/*
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
 */
