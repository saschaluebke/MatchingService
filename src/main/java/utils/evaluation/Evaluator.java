package utils.evaluation;

import components.Relation;
import components.SynonymTranslationResult;
import components.Word;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.DBStrategy;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import jxl.CellView;
import jxl.write.Formula;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import matching.Matcher;
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
    private Translator translator;
    private CorpusViewer corpusViewer;


    public Evaluator(String fromLanguage, String toLanguage, Translator translator){
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
        this.translator = translator;
        sqlQuery = new MySQLQuery();

    }

    public void setMatcher(Matcher matcher){
        dbh.setMatcher(matcher);
    }



    public ArrayList<ArrayList<String>> simpleTranslate(String trainingsDataName, ArrayList<File> files,
                                                        String trainingPathEn, String trainingPathDe) {
        corpusViewer = new CorpusViewer(trainingPathEn,trainingPathDe);
        ArrayList<ArrayList<String>> fullOutput = new ArrayList<>();
        dbh = new DBHelper(new SimpleStrategy(),translator);
        ArrayList<Word> allWords = dbh.getAllWords(fromLanguage);
        ArrayList<Relation> allRelations = dbh.getAllRelations(fromLanguage,fromLanguage);
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
                        Word inputWord = new Word(0,line,fromLanguage);
                        SynonymTranslationResult str = new SynonymTranslationResult(inputWord);
                        ArrayList<String> translation = dbh.translate(str, allWords, allRelations);
                        String trans = translation.get(0);
                        System.out.println(translation);
                        if(counter>128){
                            System.out.println("spasst");
                        }
                        output.add(trans);
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

    public ArrayList<ArrayList<String>> synonymTranslate(String trainingsDataName, ArrayList<File> files,
                                                         String trainingPathEn, String trainingPathDe){
        dbh = new DBHelper(new SynonymStrategy(),translator);
        corpusViewer = new CorpusViewer(trainingPathEn,trainingPathDe);
        ArrayList<Word> allWords = dbh.getAllWords(fromLanguage);
        ArrayList<Relation> allRelations = dbh.getAllRelations(fromLanguage,fromLanguage);

        ArrayList<ArrayList<String>> output = null;
        ArrayList<SynonymTranslationResult> strList = new ArrayList<>();

        for (int i = 1; i < files.size(); i++) {

            output = new ArrayList<>();
            input = new ArrayList<>();

            try {
                for (Scanner sc = new Scanner(files.get(i)); sc.hasNext(); ) {
                    String line = sc.nextLine();
                    line = line.trim();
                    ArrayList<String> translation=new ArrayList<>();
                    if (line.equals("")) {

                    } else {
                        input.add(line);
                        Word inputWord = new Word(0, line, fromLanguage);
                        SynonymTranslationResult str = new SynonymTranslationResult(inputWord);
                        translation = dbh.translate(str,allWords,allRelations);
                        strList.add(str);
                        output.add(translation);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String fileName= "SynonymFrom_"+files.get(i).getName()+"_translated"+trainingsDataName+"_" + i;
            printSynonymeWithExcel(fileName, strList);
        }
        return output;
    }

    public void printSimpleWithExcel(String name, ArrayList<String> output){
        ExcelWriter ew = new ExcelWriter("/out/"+name+".xls",name);

        ew.addNumber(1,0,input.size());

        ew.addBoldLabel(0,1,"Input");
        ew.addBoldLabel(1,1,"Output");
        ew.addBoldLabel(2,1,"Unverändert");
        ew.addBoldLabel(3,1,"Schlecht");
        ew.addBoldLabel(4,1,"Gut");
        ew.addBoldLabel(5,1,"Perfekt");
        ew.addBoldLabel(6,1,"FoundInDB");
        WritableSheet ws = ew.getFindingsSheet();
        ew.addBoldLabel(0,1,"Input",ws);
        ew.addBoldLabel(1,1,"Findings",ws);

        int sameCounter = 0;
        for(int i=0;i<output.size();i++) {

            String inputWord = input.get(i).toLowerCase();
            ew.addLabel(0,i+2,inputWord,ws);
            ew.addLabel(0,i+2,inputWord);
            if(inputWord.length()<4){
                inputWord = " "+inputWord+" ";
            }
            ArrayList<String> findings = corpusViewer.getLinesWithWord(inputWord);
            ew.addNumber(6,i+2,findings.size()/2);
            if(findings.size()<1){

                ew.addLabel(1,i+2,"-",ws);
            }
            for(int n=0;n<findings.size();n++){
                ew.addLabel(n+1,i+2,findings.get(n),ws);
            }

            String outputWord = output.get(i);
            int equalMarker = 0;
            if(outputWord.length()>inputWord.length()){
                if (outputWord.contains(inputWord)) {
                    equalMarker = 1;
                    sameCounter++;
                }
            }else{
                if (inputWord.contains(outputWord)) {
                    equalMarker = 1;
                    sameCounter++;
                }
            }

            ew.addNumber(2,i+2,equalMarker);
            ew.addLabel(0,i+2,inputWord);
            ew.addLabel(1,i+2,outputWord);
        }

        ew.addNumber(2,0,sameCounter);

        int length = input.size()+7;

        StringBuffer buf = new StringBuffer();
        buf.append("SUM(C3:C"+length+")");
        Formula f = new Formula(2, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(D3:D"+length+")");
        f = new Formula(3, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(E3:E"+length+")");
        f = new Formula(4, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(F3:F"+length+")");
        f = new Formula(5, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(G3:G"+length+")");
        f = new Formula(6, 0, buf.toString());
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

    public void printSynonymeWithExcel(String name,ArrayList<SynonymTranslationResult> strList){
        ExcelWriter ew = new ExcelWriter("/out/"+name+".xls",name);

        ew.addBoldLabel(0,2,"Input");
        ew.addBoldLabel(1,2,"Output");
        ew.addBoldLabel(2,2,"Unverändert");
        ew.addBoldLabel(3,2,"Schlecht");
        ew.addBoldLabel(4,2,"Gut");
        ew.addBoldLabel(5,2,"Perfekt");
        ew.addBoldLabel(6,2,"Matches");
        ew.addBoldLabel(7,2,"Synonyme");
        ew.addBoldLabel(8,2,"FoundInDB");

        ew.addNumber(1,0,input.size());
        WritableSheet ws = ew.getFindingsSheet();
        ew.addBoldLabel(0,1,"Input",ws);
        ew.addBoldLabel(1,1,"Findings",ws);
        int allFindingsCount = 0;
        int allSynonymCount =0;
        int allMatchCount = 0;
        int sameCounter = 0;
        for(int i=0;i<strList.size();i++) {
            SynonymTranslationResult str = strList.get(i);
            String inputWord = input.get(i).toLowerCase();
            //if(inputWord.contains("other age")){
            //    System.out.println(input.get(i));
            //}
            String outputWord = str.getDirectTranslation();

                ew.addLabel(0,i+3,inputWord);
                ew.addLabel(0,i+3,inputWord,ws);
            if(inputWord.length()<4){
                inputWord = " "+inputWord+" ";
            }
                ArrayList<String> findings = corpusViewer.getLinesWithWord(inputWord);
                ew.addNumber(8,i+3,findings.size()/2);
                if(findings.size()<1){
                    ew.addLabel(1,i+3,"-",ws);
                }else{
                    allFindingsCount++;
                }
                for(int n=0;n<findings.size();n++){
                    ew.addLabel(n+1,i+3,findings.get(n),ws);
                }



            int equalMarker = 0;
            if(outputWord.length()>inputWord.length()){
                if (outputWord.contains(inputWord)) {
                    equalMarker = 1;
                    sameCounter++;
                }
            }else{
                if (inputWord.contains(outputWord)) {
                    equalMarker = 1;
                    sameCounter++;
                }
            }
            ew.addNumber(2,i+3,equalMarker);
            ew.addLabel(0,i+3,inputWord);
            ew.addLabel(1,i+3,outputWord);

            int synonymCount=str.getSynonyms().size();
            int matchCount=str.getMatchings().size();

            ew.addNumber(6,i+3,matchCount);
            ew.addNumber(7,i+3,synonymCount);

            if(synonymCount>0){
                allSynonymCount++;
            }
            if(matchCount>0){
                allMatchCount++;
            }

            //The rest of the row should be a list of Synonyms and there Translations
            for(int n = 0; n<str.getSynonyms().size();n++){
                ArrayList<ArrayList<String>> synonymList = str.getSynonyms();
                ArrayList<ArrayList<String>> synonymTranslationList = str.getSynonymTranslations();
                ArrayList<String> matchings = str.getMatchings();
                ArrayList<String> matchingTranslations = str.getMatchingTranslations();
                int position = 8;
              //  if(inputWord.contains("other age-related cataract")){
               //     System.out.println(input.get(i));
                //}

                for(int matchNumb=0; matchNumb<matchings.size();matchNumb++){
                    String match = matchings.get(matchNumb);
                    String matchTranslation = matchingTranslations.get(matchNumb);
                    ArrayList<String> synonymMatchList = synonymList.get(matchNumb);
                    ArrayList<String> synonymTranslationMatchList = synonymTranslationList.get(matchNumb);
                  //  if(inputWord.contains("other age-related cataract")){
                  //      System.out.println(input.get(i));
                  //  }
                    position++;
                    ew.addBoldLabel(position,i+3,match);
                    position++;
                    ew.addBoldLabel(position,i+3,matchTranslation);
                    for(int synNumb=0; synNumb<synonymMatchList.size();synNumb++){

                        String synonym = synonymMatchList.get(synNumb);
                        String synonymTranslation = synonymTranslationMatchList.get(synNumb);

                        position++;
                        ew.addLabel(position,i+3,synonym);
                        position++;
                        ew.addLabel(position,i+3,synonymTranslation);
                    }
                }
            }
           // if(inputWord.contains("other age-related cataract")){
           //     System.out.println(input.get(i));
           // }
        }

        ew.addNumber(6,1,allMatchCount);
        ew.addNumber(7,1,allSynonymCount);
        ew.addNumber(8,1,allFindingsCount);
        ew.addNumber(2,0,sameCounter);



        int length = input.size()+7;
        StringBuffer buf = new StringBuffer();
        buf.append("SUM(C3:C"+length+")");
        Formula f = new Formula(3, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(D3:D"+length+")");
        f = new Formula(3, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(E3:E"+length+")");
        f = new Formula(4, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(F3:F"+length+")");
        f = new Formula(5, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(G3:G"+length+")");
        f = new Formula(6, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(H3:H"+length+")");
        f = new Formula(7, 0, buf.toString());
        try {
            ew.getSheet().addCell(f);
        } catch (WriteException e) {
            e.printStackTrace();
        }

        buf = new StringBuffer();
        buf.append("SUM(I3:I"+length+")");
        f = new Formula(8, 0, buf.toString());
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

        //Autosize all cells to better read text entries
        //TODO: sounds good, doesn't work :(
        /*
        for(int i=0;i<200;i++){
            CellView cell = ew.getSheet().getRowView(i);
            cell.setAutosize(true);
            ew.getSheet().setColumnView(i,cell);
        }
        */

    }

    /**
     * If you want to fill the sql Database from here but it takes to long so I just load the SQL Dump from the
     * Tests in utilsTest/fileReaderTest. I leave it here for testing...
     */

    public void init(DBStrategy strategy, ArrayList<FileReader> fileReaders){
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

    /**
     * Just my first attempt for printing a result. depreciated
     */

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
