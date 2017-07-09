package utils;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.dbStrategy.simpleStrategy.SimpleStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class OwlReader implements FileReader {
    private DBHelper dbh;
    private String path;
    private ArrayList<Word> wordList;
    private ArrayList<Word> synonyms;
    private ArrayList<ArrayList<Word>> allSynonyms;
    private int fromEntry, toEntry;
    private String firstLanguage, secondLanguage;

    public OwlReader(String path, String language){
        this.path = path;
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

    @Override
    public void getFileContent() {
        int wordCount = 0;
        int entryCount = 0;
        allSynonyms = new ArrayList<>();
        int lastId = dbh.getLastWordId(firstLanguage);
        //ArrayList<Word> currentWordsInDB = dbh.getAllWords(firstLanguage); Too slow for testing
        //ArrayList<String> currentWords = new ArrayList<>();
       /* for(Word w : currentWordsInDB){
            currentWords.add(w.getName());
        }
*/
        ArrayList<Word> allWords = new ArrayList<>();
        ArrayList<Relation> allRelation = new ArrayList<>();
        try {

            for(Scanner sc = new Scanner(new File(System.getProperty("user.dir")+path)); sc.hasNext(); ) {
                String line = sc.nextLine();

                if (line.contains("<owl:Class")){
                    String definition = "";
                    String name = "";
                    ArrayList<String> stringSynonyms = new ArrayList<>();

                    while(!line.contains("</owl:Class>")){
                        line = line + sc.nextLine();
                    }

                    entryCount++;
                    if (entryCount < fromEntry || entryCount > toEntry){
                        if (entryCount > toEntry){
                            break;
                        }
                        if (entryCount%10000 == 0){
                            System.out.println("EntryCount: "+entryCount);
                        }

                        continue;
                    }
                    //System.out.println(entryCount);
                    //Get name
                    String firstLine = "<rdfs:label rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"";
                    if (line.contains(firstLine)){
                        int start = line.indexOf(firstLine)+firstLine.length()+1;
                        int end = line.indexOf("</rdfs:label>");
                        name = line.substring(start,end);
                        int trimmer = name.indexOf(">");
                        if(trimmer != -1){
                            name = name.substring(trimmer+1);
                        }
                        //System.out.println("Name: "+name);
                    }

                    //Get Description (is always there)
                    if (line.contains("<ncicp:def-definition>")){
                        int start = line.indexOf("<ncicp:def-definition>")+22;
                        int end = line.indexOf("</ncicp:def-definition>");
                        definition = line.substring(start,end);
                        //System.out.println(definition);
                    }
                    //Get Synonyms till the end of class
                    String lastLine = "<ncicp:ComplexTerm><ncicp:term-name>";
                    while(line.contains(lastLine)){
                        int end = 0;
                        int start = line.indexOf(lastLine)+lastLine.length()+1;
                        end = line.indexOf("</ncicp:term-name>",start);
                        //System.out.println(start + "/"+end);
                        String synonym = line.substring(start,end);
                        if (!synonym.equals(name)){
                            stringSynonyms.add(synonym);
                        }
                        //System.out.println(synonyms.size());
                        line = line.substring(end+18);
                    }
                    wordCount++;
                    //System.out.println(wordCount);

                    //TODO gleiche wörter erst ausschließen wenn testphase beendet weil das sau lange dauert...
                    //                if (currentWords.contains(name)){
                    //TODO: What if word is a Homonym?
                    //                   }else{
                    lastId++;
                    Word w = new Word(lastId,name,firstLanguage);
                    w.setDescription(definition);
                    allWords.add(w);
                    for(String s : stringSynonyms){
                        lastId++;
                        Word synonym = new Word(lastId,s,firstLanguage);
                        synonyms.add(synonym);
                    }
                    allSynonyms.add(synonyms);
                }
            }
            //Put all Words and Synonym Relations into the db in one step to avoid too many transactions
            System.out.println("------Only time when something is put into db------");

            //    dbh.putWordList(allWords,language);
            //   dbh.putRelationList(allRelation,language,language);
            wordList = allWords;


        } catch (FileNotFoundException e) {
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

    public void setFromEntry(int fromEntry) {
        this.fromEntry = fromEntry;
    }

    public void setToEntry(int toEntry) {
        this.toEntry = toEntry;
    }
}
