package utilsTests;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.OpenThesaurusReader;
import utils.SpecialistReader;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 08.07.17.
 */
public class OpenThesaurusTest{
        static MySQLQuery dbq;
        static DBHelper dbh;
        static OpenThesaurusReader otr;

        @BeforeClass
        public static void onceExecutedBeforeAll() {
            dbq = new MySQLQuery();
            dbq.dropAllTables();
            dbq.truncate("languages");
            dbh = new DBHelper(new SynonymStrategy());
            otr = new OpenThesaurusReader("/src/main/resources/openThesaurus/openthesaurus.txt","de");
        }
/*
        @Test
        public void synonymsTest() {
            dbh.newLanguage("de");
            otr.setFromEntry(0);
            otr.setToEntry(100);
            otr.getFileContent();
            //  fr.setFromEntry(0);
            //  fr.setToEntry(50000);
            //dbh.takeFromFileReader(wnh);
            ArrayList<Relation> relationArrayList = otr.getRelations();
            ArrayList<Word> wordlist = otr.getWordList();
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
            assertEquals(100, otr.getWordList().size());

        }
        */
    @Test
    public void dbTest(){
        dbh.newLanguage("de");
        otr.setFromEntry(0);
        otr.setToEntry(100);
        dbh.storeFromFile(otr);


        ArrayList<Word> allWords = dbh.getAllWords("de");

        for(Word word : allWords){
            ArrayList<Word> allSynonyms = dbh.getWordFromRelation(word,"de","de");
            System.out.print(word+": ");
            for(Word synonym : allSynonyms){
                System.out.print(synonym+"/");
            }
            System.out.println(".");
        }
        assertEquals(100, otr.getWordList().size());


    }
}
