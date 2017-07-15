package utils.evaluation;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.DBStrategy;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import jxl.write.Formula;
import jxl.write.WriteException;
import translators.Translator;
import utils.ontology.FileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sashbot on 13.07.17.
 */
public class Evaluator {
    private String fromLanguage, toLanguage;
    private ArrayList<FileReader> fileReaders;
    private ArrayList<String> input;
    private DBHelper dbh;
    private MySQLQuery sqlQuery;
    private DBStrategy strategy;
    private Translator translator;


    public Evaluator(String fromLanguage, String toLanguage, Translator translator){
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
        this.translator = translator;
        sqlQuery = new MySQLQuery();
    }

    public void init(DBStrategy strategy, ArrayList<FileReader> fileReaders){
        this.strategy = strategy;
        dbh = new DBHelper(strategy,translator);

        sqlQuery.dropAllTables();
        sqlQuery.truncate("languages");
        dbh.newLanguage(fromLanguage);
        dbh.newLanguage(toLanguage);

        for(FileReader fr : fileReaders) {
            int lineCount = fr.getAllLinesCount();
            if (lineCount > 100000){
                System.out.println(lineCount+" for now "+100000+" in Evaluator Konstruktor");
                fr.setFromEntry(0);
                fr.setToEntry(100000);
                dbh.storeFromFile(fr);
            }else{
                int tmp = 0;
                for (int i = 0; i < lineCount + 100000; i = i + 100000) {
                    fr.setFromEntry(tmp);
                    fr.setToEntry(i);
                    dbh.storeFromFile(fr);
                    tmp = i;
                }
            }

            System.out.println(fr.getClass()+" excecuted!");
        }
    }

    public ArrayList<ArrayList<String>> simpleTranslate(String trainingsDataName, ArrayList<File> files, boolean noInit) {
        ArrayList<ArrayList<String>> fullOutput = new ArrayList<>();
        if(noInit){

        }else{
           //TODO: Simple Translate need no synonyms. But maybe translations from DB?

        }

        strategy = new SimpleStrategy();

        ArrayList<String> output = null;
        for (int i = 1; i < files.size(); i++) {

            output = new ArrayList<>();
            input = new ArrayList<>();
            int counter = 0;

            try {
                for (Scanner sc = new Scanner(files.get(i)); sc.hasNext(); ) {
                    String line = sc.nextLine();
                    if (line.equals("")) {

                    } else {
                        counter++;
                        input.add(line);
                        System.out.print(counter + ": " + line + " --> ");
                        String translation = translator.translation(line);
                        System.out.println(translation);
                        output.add(translation);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            printSimpleWithExcel("SimpleFrom_"+files.get(0).getName()+"_translated"+trainingsDataName, output);
            fullOutput.add(output);
        }
        return fullOutput;
    }

    public ArrayList<ArrayList<String>> synonymTranslate(String trainingsDataName, ArrayList<FileReader> fileReaders, ArrayList<File> files, boolean noInit){
       /*
        if(noInit){
            dbh = new DBHelper(new SynonymStrategy(),translator);
        }else{
            boolean newFileReader = false;
            for (FileReader fr : fileReaders) {
                if (this.fileReaders != null && !this.fileReaders.contains(fr)) {
                    newFileReader = true;
                }
            }
            if (this.strategy != null && this.strategy.getClass().equals(strategy.getClass()) && !newFileReader) {
                System.out.println("No init necessary.");
            } else {
                init(strategy, fileReaders);
            }

        }
        */
        dbh = new DBHelper(new SynonymStrategy(),translator);
        ArrayList<Word> allWords = dbh.getAllWords(fromLanguage);
        ArrayList<Relation> allRelations = dbh.getAllRelations(fromLanguage,fromLanguage);

        ArrayList<ArrayList<String>> output = null;
        for (int i = 1; i < files.size(); i++) {

            output = new ArrayList<ArrayList<String>>();
            input = new ArrayList<>();

            try {
                for (Scanner sc = new Scanner(files.get(i)); sc.hasNext(); ) {
                    String line = sc.nextLine();
                    ArrayList<String> translation=new ArrayList<>();
                    if (line.equals("")) {

                    } else {
                        input.add(line);
                        translation = dbh.translate(new Word(0, line, fromLanguage),allWords,allRelations);
                        output.add(translation);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            printEvaluation("SynonymFrom_"+files.get(i).getName()+"_translated"+trainingsDataName+"_Number" + i, output);

        }
        return output;
    }

    public void printSimpleWithExcel(String name, ArrayList<String> output){
        ExcelWriter ew = new ExcelWriter("/out/"+name+".xls",name);

        ew.addLabel(0,1,"Input");
        ew.addLabel(1,1,"Output");
        ew.addLabel(2,1,"Unverändert");
        ew.addLabel(3,1,"Schlecht");
        ew.addLabel(4,1,"Gut");
        ew.addLabel(5,1,"Perfekt");

        int sameCounter = 0;
        for(int i=0;i<output.size();i++) {

            String inputWord = input.get(i);
            String outputWord = output.get(i);
            String equalMarker = "0";
            if (inputWord.contains(outputWord)) {
                equalMarker = "X";
                sameCounter++;
            }
            ew.addLabel(2,i+2,equalMarker);
            ew.addLabel(0,i+2,inputWord);
            ew.addLabel(1,i+2,outputWord);
        }

        ew.addNumber(2,0,sameCounter);


        StringBuffer buf = new StringBuffer();
        buf.append("SUM(E3:E200)");
        Formula f = new Formula(3, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(F3:F200)");
        f = new Formula(4, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(G3:G200)");
        f = new Formula(5, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        try {
            ew.write();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }

    public void printSimpleEvaluation(String name, ArrayList<String> output){
        try{
            PrintWriter writer = new PrintWriter(name+".txt", "UTF-8");//UTF-8?
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
            PrintWriter writer = new PrintWriter(name+".txt", "UTF-8");//UTF-8?
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
