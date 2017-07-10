package evaluation;

import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.DBStrategy;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import translators.MosesClient;
import utils.OwlReader;
import utils.SpecialistReader;
import utils.WordNetReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 08.07.17.
 */
public class ICD10Evaluation {
    static MosesClient mc;
    static ArrayList<String> input;
    static DBStrategy strategy;
    static DBHelper dbh;
    static MySQLQuery sqlQuery;


    @BeforeClass
    public static void onceExecutedBeforeAll() {
        //Clean System
        strategy = new SimpleStrategy();
        dbh = new DBHelper(strategy,mc);
        sqlQuery = new MySQLQuery();
        sqlQuery.dropAllTables();
        sqlQuery.truncate("languages");
        dbh.newLanguage("de");
        dbh.newLanguage("en");

        //Load Words with Synonyms
        SpecialistReader sr = new SpecialistReader("/src/main/resources/SpecialistLexicon","en");
        OwlReader owlr = new OwlReader("/src/main/resources/Thesaurus-byName.owl","de");
        WordNetReader wnh = new WordNetReader("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/WordNet/WordNet-3.0/dict","en");

        sr.setFromEntry(0);
        sr.setToEntry(Integer.MAX_VALUE);//TODO: Have to be smaller!
        dbh.storeFromFile(sr);

        owlr.setFromEntry(0);
        owlr.setToEntry(Integer.MAX_VALUE);
        dbh.storeFromFile(owlr);

        wnh.setFromEntry(0);
        wnh.setToEntry(Integer.MAX_VALUE);
        dbh.storeFromFile(wnh);

        //Set Translator
        mc = new MosesClient();
        mc.setFromLanguage("en");
        mc.setToLanguage("de");

        //Get Evaluaton Words
        input = new ArrayList<>();
        try {
            for(Scanner sc = new Scanner(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/ICD10/icd10Input.txt")); sc.hasNext(); ) {
                String line = sc.nextLine();
                line = line.substring(7);
                if(line.equals("")){

                }else{
                    input.add(line);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("End of init.");
    }



    /**             just Translation    With Synonyms
     * Dict
     * EMEA
     * Book
     * Parl
     * Stamt
     * Wikipedia
     *
     * 0 no Translation
     * 1 false Translation
     * 2 no good Translation
     * 3 good Translation
     * 4 perfect Translation
     */
    @Test
    public void translationEvaluation() {
        strategy = new SimpleStrategy();
        dbh = new DBHelper(strategy,mc);

        ArrayList<String> output = new ArrayList<>();
        for(String in : input){
            output.add(mc.translation(in));
        }

        printSimpleEvaluation("simpleTranslation",output);
        assertEquals(true,true);
    }

    @Test
    public void synonymEvalution() {
        strategy = new SynonymStrategy();
        dbh = new DBHelper(strategy,mc);


        ArrayList<ArrayList<String>> output = new ArrayList<>();
        for(String in : input){
            ArrayList<String> translation = dbh.translate(new Word(0,in,"en"));
            output.add(translation);
        }
        printEvaluation("SynonymEvaluation",output);
    }

    public void printSimpleEvaluation(String name, ArrayList<String> output){
        try{
            PrintWriter writer = new PrintWriter(name+".txt", "UTF-16");//UTF-8?
            String writeString = "";
            int sameCounter = 0;
            for(int i=0;i<output.size();i++){
                String inputWord = input.get(i);
                String outputWord = output.get(i);
                String equalMarker = "0";
                if(inputWord.contains(outputWord)){
                    equalMarker = "X";
                    sameCounter++;
                }
                writeString = String.format("%20s %50s %50s \r\n", equalMarker, inputWord, outputWord);
                writer.println(writeString);
            }
            System.out.println("Same Words: "+sameCounter);
            //writer.print(writeString);
            writer.close();
        } catch (IOException e) {
            // do something
        }

    }

    public void printEvaluation(String name, ArrayList<ArrayList<String>> translations){
        try{
            PrintWriter writer = new PrintWriter(name+".txt", "UTF-16");//UTF-8?
            String writeString = "";
            for(int i=0; i<translations.size(); i++){
                String inputWord = input.get(i);
                String outputWord = "";
                for(String s : translations.get(i)){
                    outputWord = outputWord +"/"+ s;
                }
                writeString = String.format(" %50s %50s \r\n", inputWord, outputWord);
                writer.println(writeString);
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }
}
