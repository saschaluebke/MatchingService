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
import matching.distance.LevenshteinNormalized;
import matching.iterate.PerformanceStrategy;
import matching.sorting.ScoreSort;
import translators.Translator;
import utils.ontology.FileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Evaluator {
    private String fromLanguage, toLanguage;
    private ArrayList<FileReader> fileReaders;
    private ArrayList<String> input;
    private DBHelper dbh;
    private MySQLQuery sqlQuery;
    private Translator translator;
    private CorpusViewer corpusViewer;
    private Matcher matcher;


    public Evaluator(String fromLanguage, String toLanguage, Translator translator){
        this.fromLanguage = fromLanguage;
        this.toLanguage = toLanguage;
        this.translator = translator;
        this.matcher = new Matcher(new PerformanceStrategy(),new LevenshteinNormalized(), new ScoreSort());
        sqlQuery = new MySQLQuery();

    }

    public void setMatcher(Matcher matcher){
        this.matcher = matcher;
    }



    public ArrayList<ArrayList<String>> simpleTranslate(String trainingsDataName, ArrayList<File> files,
                                                        String trainingPathEn, String trainingPathDe) {
        corpusViewer = new CorpusViewer(trainingPathEn,trainingPathDe);
        ArrayList<ArrayList<String>> fullOutput = new ArrayList<>();
        dbh = new DBHelper(new SimpleStrategy(),translator);
        dbh.setMatcher(matcher);
        ArrayList<Word> allWords = dbh.getAllWords(fromLanguage);
        ArrayList<Relation> allRelations = dbh.getAllRelations(fromLanguage,fromLanguage);
        ArrayList<String> output = null;
        boolean showFindings = true;
        for (int i = 0; i < files.size(); i++) {

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
                        output.add(trans);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("SimplePrint:"+files.get(i).getName());
            printSimpleWithExcel("SimpleFrom_"+files.get(i).getName()+"_translated"+trainingsDataName, output,showFindings);
            showFindings = false;
            fullOutput.add(output);
        }
        return fullOutput;
    }


    public ArrayList<ArrayList<String>> synonymTranslate(String trainingsDataName, ArrayList<File> files,
                                                         String trainingPathEn, String trainingPathDe){
        dbh = new DBHelper(new SynonymStrategy(),translator);
        dbh.setMatcher(matcher);
        ArrayList<Word> allWords = dbh.getAllWords(fromLanguage);
        ArrayList<Relation> allRelations = dbh.getAllRelations(fromLanguage,fromLanguage);

        ArrayList<ArrayList<String>> output = null;
        ArrayList<SynonymTranslationResult> strList = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            output = new ArrayList<>();
            input = new ArrayList<>();
            int count=0;
            try {
                for (Scanner sc = new Scanner(files.get(i)); sc.hasNext(); ) {
                    String line = sc.nextLine();
                    line = line.trim();
                    ArrayList<String> translation=new ArrayList<>();
                    if (line.equals("")) {

                    } else {
                        count++;
                        input.add(line);
                        Word inputWord = new Word(0, line, fromLanguage);
                        SynonymTranslationResult str = new SynonymTranslationResult(inputWord);
                        dbh.setMatcher(matcher);
                        translation = dbh.translate(str,allWords,allRelations);
                        System.out.println(count+": "+inputWord.getName()+" --> "+str.getDirectTranslation());
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

    public void printSimpleWithExcel(String name, ArrayList<String> output, boolean showFindings){
        ExcelWriter ew = new ExcelWriter("/out/"+name+".xls",name);

        ew.addNumber(1,0,input.size());

        ew.addBoldLabel(0,1,"Input");
        ew.addBoldLabel(1,1,"Output");
        ew.addBoldLabel(2,1,"Unverändert");
        ew.addBoldLabel(3,1,"FachGesamt");
        ew.addBoldLabel(4,1,"FachScore");
        ew.addBoldLabel(5,1,"AllgGesamt");
        ew.addBoldLabel(6,1,"AllgScore");
        ew.addBoldLabel(7,1,"InTraining");
        WritableSheet ws = ew.getSheet(1);
        ew.addBoldLabel(0,1,"Input",ws);
        ew.addBoldLabel(1,1,"Findings",ws);
        int counter = 0;
        int position=1;
        for(int i=0;i<output.size();i++) {
            String inputWord = input.get(i).toLowerCase();
            counter++;
            System.out.println(counter+output.get(i));
            ArrayList<Integer> findings = corpusViewer.getIntWithWord(inputWord);
            //ArrayList<ArrayList<String>> findings = corpusViewer.getLinesWithWord(inputWord);
            int findingCount=0;
            if(showFindings){
                for(int findingIndex=0;findingIndex<findings.size();findingIndex++){
                    //  findingCount = findingCount + findings.get(findingIndex).size();
                    findingCount = findingCount + findings.get(findingIndex);
                }
                ew.addNumber(7,i+2,findingCount);
                for (int n = 0; n < findings.size(); n++) {
                    // ArrayList<String> find = findings.get(n);
                    int size = findings.size();
                    if (size > position) {
                        position = size;
                    }
                    //   ew.addNumber(n + 2, i + 2, findings.get(n).size(), ws);
                    ew.addNumber(n+2,i+2,findings.get(n),ws);
                    //ew.addLabel(n+1,i+3,findings.get(n),ws);
                }
            }


            for (int n = 1; n < position; n++) {
                ew.addBoldLabel(n + 1, 1, String.valueOf(n),ws);
                // ew.addBoldLabel(n+1,i+n+2,findings.get(i).get(n));//Bin mir nicht sicher ob das hinhaut!
                //ich will hier die einzelnen bestandteile der wörter haben!
                //wir haben zB bla blub - erste spalte erste zeile soll also bla und seine findings stehen
                //blub soll eine zeile unten drunter kommen deshalb auch das diagonale n und i in row
            }
        }
        int sameCounter = 0;
        for(int i=0;i<output.size();i++) {

            String inputWord = input.get(i).toLowerCase();
            ew.addLabel(0,i+2,inputWord,ws);
            ew.addLabel(0,i+2,inputWord);
            if(inputWord.length()<4){
                inputWord = " "+inputWord+" ";
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

        buf = new StringBuffer();
        buf.append("SUM(H3:H"+length+")");
        f = new Formula(7, 0, buf.toString());
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
        ew.addBoldLabel(3,2,"Fachwörter");
        ew.addBoldLabel(4,2,"FachScore");
        ew.addBoldLabel(5,2,"Allgemein");
        ew.addBoldLabel(6,2,"AllgemeinScore");
        ew.addBoldLabel(7,2,"Matches");
        ew.addBoldLabel(8,2,"Synonyme");

        ew.addNumber(1,0,input.size());
        WritableSheet ws = ew.getSheet(1);
        ew.addBoldLabel(0,1,"Input",ws);
        ew.addBoldLabel(1,1,"Findings",ws);
        int allSynonymCount =0;
        int allMatchCount = 0;
        int sameCounter = 0;
        int matchingSheetIndex=2;
        for(int i=0;i<strList.size();i++) {
            SynonymTranslationResult str = strList.get(i);
            String inputWord = str.getInput().getName().toLowerCase();
            //if(inputWord.contains("other age")){
            //    System.out.println(input.get(i));
            //}
            String outputWord = str.getDirectTranslation();

                ew.addLabel(0,i+3,inputWord);
                ew.addLabel(0,i+3,inputWord,ws);
            if(inputWord.length()<4){
                inputWord = " "+inputWord+" ";
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

            int synonymCount=str.getSynonymCount();
            int matchCount=str.getMatchingCount();

            ew.addNumber(7,i+3,matchCount);
            ew.addNumber(8,i+3,synonymCount);

            if(synonymCount>0){
                allSynonymCount++;
            }
            if(matchCount>0){
                allMatchCount++;
            }

            int pos2 = 1;
            int pos=9;

            WritableSheet wsMatch;
            matchingSheetIndex = ew.addSheet();
            wsMatch = ew.getSheet(matchingSheetIndex);
            int matchPosition=0;
            for(int wordIndex = 0; wordIndex<str.getWords().size();wordIndex++){
                ew.addBoldLabel(pos,i+3,"Word: "+str.getWords().get(wordIndex));
                ew.addBoldLabel(pos2,i+3,String.valueOf(str.getMatchings().get(wordIndex).size()),ws);
                pos2++;
                //add up synonyms to get all synonyms of a specific
                int synCount=0;
                for(ArrayList<String> synList : str.getMatchingSynonyms().get(wordIndex)){

                    synCount = synCount + synList.size();
                }
                //ew.addNumber(pos2,i+2,str.getMatchingSynonyms().get(wordIndex).get(matchingIndex).size(),ws);
                ew.addNumber(pos2,i+3,synCount,ws);
                pos2++;
                pos++;

                ew.addBoldLabel(matchPosition,0,str.getWords().get(wordIndex),wsMatch);

                for(int matchingIndex = 0; matchingIndex<str.getMatchings().get(wordIndex).size();matchingIndex++){
                  //  ew.addBoldLabel(pos,i+3,str.getMatchings().get(wordIndex).get(matchingIndex));
                   ew.addBoldLabel(matchPosition,1,str.getMatchings().get(wordIndex).get(matchingIndex),wsMatch);
                    matchPosition++;
                    //pos++;
                    int synonymPos = 2;
                        for(int synonymIndex = 0; synonymIndex<str.getMatchingSynonyms().get(wordIndex).get(matchingIndex).size();synonymIndex++){
                            ew.addLabel(matchingIndex,synonymPos,String.valueOf(str.getMatchingSynonyms().get(wordIndex).get(matchingIndex).get(synonymIndex)),wsMatch);
                            synonymPos++;
                            ew.addLabel(matchingIndex,synonymPos,str.getMatchingSynonymtranslations().get(wordIndex).get(matchingIndex).get(synonymIndex),wsMatch);
                            synonymPos++;
                            ew.addLabel(matchingIndex,synonymPos," ",wsMatch);
                            synonymPos++;

                            String synonym = str.getMatchingSynonyms().get(wordIndex).get(matchingIndex).get(synonymIndex);
                            String synonymTranslation = str.getMatchingSynonymtranslations().get(wordIndex).get(matchingIndex).get(synonymIndex);
                            if(!synonym.equals(synonymTranslation)){
                                ew.addLabel(pos,i+3,str.getMatchingSynonymtranslations().get(wordIndex).get(matchingIndex).get(synonymIndex));
                                pos++;
                            }

                    }
                }
                matchPosition++;
            }


           // if(inputWord.contains("other age-related cataract")){
           //     System.out.println(input.get(i));
           // }
        }

        ew.addNumber(7,1,allMatchCount);
        ew.addNumber(8,1,allSynonymCount);
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
