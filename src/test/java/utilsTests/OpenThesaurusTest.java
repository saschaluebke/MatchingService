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
public void simpleTest(){
    otr = new OpenThesaurusReader("/src/main/resources/ontologies/openThesaurus/TestOntology","de");
    dbh = new DBHelper(new SynonymStrategy());
    dbh.newLanguage("de");
   /*
    int allLinesCount = otr.getAllLinesCount();
    int tmp=0;
    for(int i = 0; i<allLinesCount+100000; i=i+100000){

        otr.setFromEntry(tmp);
        otr.setToEntry(i);
        dbh.storeFromFile(otr);
        tmp=i;
    }
    */
    otr.setFromEntry(0);
    otr.setToEntry(1);
    dbh.storeFromFile(otr);
    /**
     * Wordlist Cat,cat
     * Relations Cat->cat and cat->Cat
     */
    assertEquals(2, dbh.getAllWords("de").size());
    assertEquals(2,dbh.getAllRelations("de","de").size());

    otr.setFromEntry(1);
    otr.setToEntry(2);
    dbh.storeFromFile(otr);
    /**
     * WordList Cat, cat, dog, Dog, Hound
     * Relations Cat->cat,cat->Cat and dog->Dog,Dog->dog,dog->Hound,Hound->Dog,Dog->Hound,Hound->Dog
     */
    assertEquals(5, dbh.getAllWords("de").size());
    assertEquals(8,dbh.getAllRelations("de","de").size());


}
/*
    @Test
    public void fullTest(){
        otr = new OpenThesaurusReader("/src/main/resources/ontologies/openThesaurus/openthesaurus.txt","de");
        dbh = new DBHelper(new SynonymStrategy());
        ArrayList<String> allLines = otr.getAllLines();
        int lineCount = allLines.size();
        dbh.newLanguage("de");
        int tmp=0;
        for(int i = 0; i<lineCount+100000; i=i+100000){

            otr.setFromEntry(tmp);
            otr.setToEntry(i);
            dbh.storeFromFile(otr);
            tmp=i;
        }
        //System.out.println(dbh.print("de","de"));

        assertEquals(102093, dbh.getAllWords("de").size());
        assertEquals(537338,dbh.getAllRelations("de","de").size());


    }
    */
}

/*
  ArrayList<Word> allWords = dbh.getAllWords("de");

        for(Word word : allWords){
            ArrayList<Word> allSynonyms = dbh.getWordFromRelation(word,"de","de");
            System.out.print(word.getName()+": ");
            for(Word synonym : allSynonyms){
                System.out.print(synonym.getName()+"/");
            }
            System.out.println(".");
        }

 */