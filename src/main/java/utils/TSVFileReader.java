package utils;

import components.Relation;
import components.Word;
import database.DBHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//To read the source and target.tsv files in test/java/files
public class TSVFileReader implements utils.FileReader {
    ArrayList<String> fileContent = new ArrayList<>();
    String path;
    String language;
    //List<MappingTerm> mappingTerms = new ArrayList<MappingTerm>();

    private int numberOfTerms = 0;

    public TSVFileReader(String filename, String language) {
        this.path = System.getProperty("user.dir")+filename;
        this.language = language;

    }

    // Simple file reader
    public String[] readLines(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }

    public String cleanString(String s){
        String temp[] = s.split("\t");
        s = temp[0];
        s = s.replaceAll("[^A-Za-z0-9 ?]", "");
        s = s.replaceAll("  ", " ").toLowerCase();
        s = s.trim();
        return s;
    }

    public ArrayList<String> getFiles(){
        getFileContent();
        return fileContent;
    }

    public void getFileContent(){
        try {
            String[] fc = readLines(path);
            //fileContent = readLines(filename);
            for (int a = 0; a < fc.length; a++) {

                fc[a] = cleanString(fc[a]);
                fileContent.add(fc[a]);
                //MappingTerm temp = new MappingTerm(fileContent[a]);
                //mappingTerms.add(temp);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Word> getWordList() {
        ArrayList<Word> words = new ArrayList<>();
        for(String s : fileContent){
            words.add(new Word(0,s,language));
        }
        return words;
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
        return language;
    }

    @Override
    public String getSecondLanguage() {
        return null;
    }

    @Override
    public ArrayList<ArrayList<Word>> getSynonyms() {
        return null;
    }

    @Override
    public int getFromEntry() {
        return 0;
    }

    @Override
    public void setFromEntry(int entry) {

    }

    @Override
    public int getToEntry() {
        return 0;
    }

    @Override
    public void setToEntry(int entry) {

    }

    @Override
    public int getAllLinesCount() {
        return 0;
    }

    public ArrayList<Word> getFileContentWords(String language) {
        ArrayList<Word> wordList = new ArrayList<>();

        for(String s : fileContent){
            wordList.add(new Word(0,s,language));
        }

        return wordList;
    }
/*
    public MappingTerm getTerm(int i) {
        return mappingTerms.get(i);
    }

    public void setTerm(int a, MappingTerm term) {
        mappingTerms.set(a, term);
    }

    public String getTermString(int i) {
        return mappingTerms.get(i).getTermString();
    }

    public String getSimplifiedTermString(int i) {
        return mappingTerms.get(i).getSimplifiedTermString();
    }
*/
}
