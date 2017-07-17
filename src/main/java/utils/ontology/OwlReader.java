package utils.ontology;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import utils.ontology.FileReader;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class OwlReader implements FileReader {
    private DBHelper dbh;
    private String path;
    private ArrayList<Word> wordList;
    private ArrayList<ArrayList<Word>> allSynonyms;
    private int fromEntry, toEntry;
    private String firstLanguage, secondLanguage;

    public OwlReader(String path, String language){
        this.path = System.getProperty("user.dir")+path;
        this.firstLanguage = language;
        this.secondLanguage = language; //OWL file are with synonyms not translations
        dbh = new DBHelper(new SimpleStrategy());
    }

    public DBHelper getDbh() {
        return dbh;
    }

    public void setDbh(DBHelper dbh) {
        this.dbh = dbh;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<String> getAllLines() {
        ArrayList<String> allLines = new ArrayList<>();

        try {

            for (Scanner sc = new Scanner(new File( path)); sc.hasNext(); ) {
                String line = sc.nextLine();

                if (line.contains("<owl:Class")) {
                    while (!line.contains("</owl:Class>")) {
                        line = line + sc.nextLine();
                    }
                    allLines.add(line);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return allLines;
    }

    /**
     *
     * Read TransferFile until fromEntry line just to get the start line
     * Read TranferFile from entry point to the last entry point
     * DBHelper will put the wordList and allSynonyms into DB
     * You have to do that because of the data transfer limitation of sql and the limitation of the heap space of java
     */
        @Override
    public void getFileContent() {
            wordList = new ArrayList<>();
            allSynonyms = new ArrayList<>();
            String encoding = "UTF-8";
            BufferedReader reader = null;
            try {

                reader = new BufferedReader(new InputStreamReader(new FileInputStream(path+"Transfer"), encoding));
                for(int i=fromEntry;i>0;i--){
                    reader.readLine(); //ignore line
                }
                for(int i=fromEntry;i<toEntry;i++){
                    String line = reader.readLine();
                    if(line == null){
                        System.out.println("You reach end of data. At line "+i);
                        break; //reach end of line

                    }
                    if(line.equals("")){
                        continue;
                    }
                    String name, description="-";
                    ArrayList<Word> synonyms=new ArrayList<>();
                    //Split line into word /// description /// synonym1 /// synonym2 ...
                    int nextPart = -1;

                    //Get Word
                    if(( nextPart = line.indexOf("///")) != -1){
                        name = line.substring(0,nextPart);
                        if(nextPart+3<line.length()){
                            line = line.substring(nextPart+3,line.length());
                        }else{
                            line="";
                        }
                    }else{
                        //there was no Word!
                        continue;
                    }

                    // Get Description
                    if(( nextPart = line.indexOf("///")) != -1){
                        description = line.substring(0,nextPart);
                        if(nextPart+3<line.length()){
                            line = line.substring(nextPart+3,line.length());
                        }else{
                            line="";
                        }
                    }

                    //Get Synonyms
                    while(( nextPart = line.indexOf("///")) != -1){
                        String synonymString = line.substring(0,nextPart);
                        Word synonym = new Word(0,synonymString,firstLanguage);
                        synonyms.add(synonym);
                        if(nextPart+3<line.length()){
                            line = line.substring(nextPart+3,line.length());
                        }else{
                            line = "";
                        }
                    }
                    Word word = new Word(0,name,firstLanguage);
                    word.setDescription(description);
                    wordList.add(word);
                    allSynonyms.add(synonyms);
                }

                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public int transferOWLFile() {
            int lineCount=0;
            boolean appendFile = false;
            int brokenClassesCount = 0; //Count if a class has a start label but no end label
            ArrayList<String> allLines = getAllLines();
            for (String line : allLines) {
                ArrayList<String> synonyms = new ArrayList<>();
                String definition = "";
                String name = "";
                String firstLine = "<rdfs:label rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"";
                if (line.contains(firstLine)) {
                    int start = line.indexOf(firstLine) + firstLine.length() + 1;
                    int end = line.indexOf("</rdfs:label>");
                    if (end != -1) {
                        name = line.substring(start, end);
                    } else {
                        brokenClassesCount++;
                        continue;
                    }

                    int trimmer = name.indexOf(">");
                    if (trimmer != -1) {
                        name = name.substring(trimmer + 1);
                    }
                    //System.out.println("Name: "+name);
                }
                //Get Description (is always there)
                if (line.contains("<ncicp:def-definition>")) {
                    int start = line.indexOf("<ncicp:def-definition>") + 22;
                    int end = line.indexOf("</ncicp:def-definition>");
                    definition = line.substring(start, end);
                    //System.out.println(definition);
                }
                //Get Synonyms till the end of class
                String lastLine = "<ncicp:ComplexTerm><ncicp:term-name>";
                while (line.contains(lastLine)) {
                    int end = 0;
                    int start = line.indexOf(lastLine) + lastLine.length() + 1;
                    end = line.indexOf("</ncicp:term-name>", start);
                    //System.out.println(start + "/"+end);
                    String synonym = line.substring(start, end);
                    if (!synonym.equals(name)) {
                        synonyms.add(synonym);
                    }
                    //System.out.println(synonyms.size());
                    line = line.substring(end + 18);
                }
                //TODO scannen ob nicht einmal zu früh ein lineSeperator oder /// drinne ist (in der definition zum beispel)

                printTransferFile(name,definition,synonyms,appendFile);
                lineCount++;
                appendFile = true;
            }
            return lineCount;
        }
    private void printTransferFile(String word, String description, ArrayList<String> synonmys,boolean appendFile){
        String encoding = "UTF-8";
        BufferedWriter writer = null;
        int count = 0;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "Transfer",appendFile), encoding));
            writer.write(word);
            writer.write("///"+description);
            for(String synonym : synonmys){
                writer.write("///"+synonym);
            }
            writer.write(System.lineSeparator());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public ArrayList<Word> getWordList() {
        return wordList;
    }

    @Override
    public ArrayList<Word> getSecondWordList() {
        return null;
    }

    @Override
    public ArrayList<Relation> getFirstTranslation() {
        return null;
    }

    @Override
    public ArrayList<Relation> getSecondTranslation() {
        return null;
    }

    @Override
    public String getFirstLanguage() {
        return firstLanguage;
    }

    @Override
    public String getSecondLanguage() {
        return secondLanguage;
    }

    @Override
    public ArrayList<ArrayList<Word>> getSynonyms() {
        return allSynonyms;
    }

    @Override
    public int getFromEntry() {
        return 0;
    }

    public void setFromEntry(int fromEntry) {
        this.fromEntry = fromEntry;
    }

    @Override
    public int getToEntry() {
        return 0;
    }

    public void setToEntry(int toEntry) {
        this.toEntry = toEntry;
    }

    @Override
    public int getAllLinesCount() {
        //ArrayList<String> allLines = getAllLines();
        //return allLines.size();
        return transferOWLFile();
    }
}
