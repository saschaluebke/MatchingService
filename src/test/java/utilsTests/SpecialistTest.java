package utilsTests;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.DictReader;
import utils.SpecialistReader;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sascha on 02.07.17.
 */
public class SpecialistTest {
    static MySQLQuery dbq;
    static DBHelper dbh;
    static SpecialistReader sr;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
        dbq = new MySQLQuery();
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh = new DBHelper(new SimpleStrategy());
        sr = new SpecialistReader("/src/main/resources/SpecialistLexicon/LEXICON","en");
    }
/*
    @Test
    public void getSmallFileContentTest() {
        dbh.newLanguage("en");

        sr.setFromEntry(0);
        sr.setToEntry(50000);
        dbh.takeFromFileReader(sr);

        assertEquals(51951,sr.getWordList().size());
System.out.println("----First Round-----");
        sr.setFromEntry(50001);
        sr.setToEntry(100000);
        dbh.takeFromFileReader(sr);

        assertEquals(51367,sr.getWordList().size());
        //TODO: Noch mehr testen sind sie wirklich drinne sind die translations richtig?
    }

    */
    @Test
    public void synonymsTest() {
        dbh.newLanguage("en");
        sr.setFromEntry(0);
        sr.setToEntry(100);
        sr.getFileContent();
        //  fr.setFromEntry(0);
        //  fr.setToEntry(50000);
        //dbh.takeFromFileReader(wnh);
        ArrayList<Relation> relationArrayList = sr.getRelations();
        ArrayList<Word> wordlist = sr.getWordList();
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
        assertEquals(194, sr.getWordList().size());

    }

}
