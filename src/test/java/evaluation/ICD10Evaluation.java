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
import utils.OpenThesaurusReader;
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
    static Boolean setUpDatabase=true;


    @BeforeClass
    public static void onceExecutedBeforeAll() {
        strategy = new SimpleStrategy();
        dbh = new DBHelper(strategy,mc);
        sqlQuery = new MySQLQuery();

        if (setUpDatabase) {
            sqlQuery.dropAllTables();
            sqlQuery.truncate("languages");
            dbh.newLanguage("de");
            dbh.newLanguage("en");


            //Load Words with Synonyms
            SpecialistReader sr = new SpecialistReader("/src/main/resources/ontologies/SpecialistLexicon/LEXICON", "en");
            OwlReader owlr = new OwlReader("/src/main/resources/ontologies/NCI/Thesaurus-byName.owl", "de");
            WordNetReader wnh = new WordNetReader("/src/main/resources/ontologies/WordNet/WordNet-3.0/dict", "en");

            //--- SpecialistLEXICON ---
            ArrayList<String> allLines = sr.getAllLines();
            int lineCount = allLines.size();
            dbh.newLanguage("en");
            int tmp = 0;
            for (int i = 0; i < lineCount + 100000; i = i + 100000) {

                sr.setFromEntry(tmp);
                sr.setToEntry(i);
                dbh.storeFromFile(sr);
                tmp = i;
            }

            //--- NCI ---
            allLines = owlr.getAllLines();
            lineCount = allLines.size();
            dbh.newLanguage("en");
            tmp = 0;
            for (int i = 0; i < lineCount + 100000; i = i + 100000) {

                owlr.setFromEntry(tmp);
                owlr.setToEntry(i);
                dbh.storeFromFile(owlr);
                tmp = i;
            }

            //--- WordNet ---
            lineCount = wnh.getAllLines();
            tmp = 0;
            for (int i = 0; i < lineCount + 100000; i = i + 100000) {

                wnh.setFromEntry(tmp);
                wnh.setToEntry(i);
                dbh.storeFromFile(wnh);
                tmp = i;
            }
        }


        //Set Translator
        mc = new MosesClient();
        mc.setFromLanguage("en");
        mc.setToLanguage("de");



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

        ArrayList<String> output=null;
        for(int i  = 1; i<4 ; i++){
            output = new ArrayList<>();
            input = new ArrayList<>();
            //Get Evaluaton Words
            input = new ArrayList<>();
            int counter = 0;
            try {
                for(Scanner sc = new Scanner(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion"+i+".txt")); sc.hasNext(); ) {
                    String line = sc.nextLine();
                    if(line.equals("")){

                    }else{
                        counter++;
                        input.add(line);
                        System.out.print(counter+": "+line+" --> ");
                        String translation = mc.translation(line);
                        System.out.println(translation);
                        output.add(translation);

                    }

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            printSimpleEvaluation("simpleTranslation_Emea"+i,output);
        }



        assertEquals(179,output.size());
    }

    @Test
    public void synonymEvalution() {
        strategy = new SynonymStrategy();
        dbh = new DBHelper(strategy, mc);
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            //Get Evaluaton Words
            input = new ArrayList<>();
            output = new ArrayList<>();
            try {
                for (Scanner sc = new Scanner(new File("/home/sashbot/IdeaProjects/MatchingService/src/main/resources/evaluation/ICD10/icd10InputVersion" + i+".txt")); sc.hasNext(); ) {
                    ArrayList<String> translation=new ArrayList<>();
                    String line = sc.nextLine();
                    line = line.substring(6);
                    if (line.equals("")) {

                    } else {
                        input.add(line);
                        translation = dbh.translate(new Word(0, line, "en"));
                        output.add(translation);
                    }

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            printEvaluation("SynonymEvaluation"+i, output);
            assertEquals(1000, output.size());
        }
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
