package evaluation;


import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.DBStrategy;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import databaseTests.DBQueryTest;
import org.junit.BeforeClass;
import org.junit.Test;
import translators.MosesClient;
import utils.OwlReader;
import utils.SpecialistReader;
import utils.WordNetHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Evaluation as Test because Spring looks after main classes and the only main class has
 * to be AppConfig!
 */
public class OnkoWikiEvaluation {
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
        SpecialistReader sr = new SpecialistReader("/src/main/resources/SpecialistLexicon/LEXICON","en");
        OwlReader owlr = new OwlReader("/src/main/resources/NCI_Thesaurus/Thesaurus-byName.owl","de");
        WordNetHelper wnh = new WordNetHelper("/src/main/resources/NCI_Thesaurus/Thesaurus-byName.owl","en");

        sr.setFromEntry(0);
        sr.setToEntry(Integer.MAX_VALUE);//TODO: Have to be smaller!
        dbh.takeFromFileReader(sr);

        owlr.setFromEntry(0);
        owlr.setToEntry(Integer.MAX_VALUE);
        dbh.takeFromFileReader(owlr);

        wnh.setFromEntry(0);
        wnh.setToEntry(Integer.MAX_VALUE);
        dbh.takeFromFileReader(wnh);

        //Set Translator
        mc = new MosesClient();
        mc.setFromLanguage("de");
        mc.setToLanguage("en");

        //Get Evaluaton Words
        input = new ArrayList<>();
        try {
            for(Scanner sc = new Scanner(new File("/home/sashbot/IdeaProjects/MatchingService/src/test/java/evaluation/OnkoWikiDaten.txt")); sc.hasNext(); ) {
                String line = sc.nextLine();
                line = line.replace("\t","");
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

    /*
    @Test
    public void getSmallFileContentTest() {
        input = new ArrayList<>();
        try {
            for(Scanner sc = new Scanner(new File("/home/sashbot/IdeaProjects/MatchingService/src/test/java/evaluation/OnkoWikiDaten.txt")); sc.hasNext(); ) {
                String line = sc.nextLine();
                line = line.replace("\t","");
//System.out.println(line);
                if(line.equals("")){

                }else{
                    input.add(line);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
System.out.println("-----------Beginning of translation------------");
        System.out.println("Input Words: "+input.size());
        ArrayList<String> output = new ArrayList<>();
        for(String in : input){
            output.add(mc.translation(in));
        }
        System.out.println("Output Words: "+output.size());


        try{
            PrintWriter writer = new PrintWriter("OnkoOut3.txt", "UTF-8");
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
             writer.print(writeString);
            writer.close();
        } catch (IOException e) {
            // do something
        }

        assertEquals(136,output.size());


        //When the Words are the same probably Moses could not translate it!
        //make table for an overview.




    }
    */
}
