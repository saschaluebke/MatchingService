package utils;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.dbStrategy.simpleStrategy.SimpleStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sascha on 02.07.17.
 */
public class SpecialistReader implements FileReader {
    private ArrayList<Word> wordlist;
    private ArrayList<ArrayList<Word>> allSynonyms;
    private String language, path;
    private int entryCount, fromEntry, toEntry;
    private DBHelper dbh;

    public SpecialistReader(String path, String language){
        this.language = language;
        this.path = path;
        dbh = new DBHelper(new SimpleStrategy());
    }

    public ArrayList<String> getAllLines(){
        ArrayList<String> allLines = new ArrayList<>();

        try {
            for (Scanner sc = new Scanner(new File(System.getProperty("user.dir") + path)); sc.hasNext(); ) {
                String line = sc.nextLine();

                if (line.contains("{base=")) {
                    while (!line.contains("}")) {
                        line = line + sc.nextLine();
                    }
                }
                allLines.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return allLines;
    }

    @Override
    public void getFileContent() {
        entryCount=-1;
        wordlist = new ArrayList<>();
        allSynonyms = new ArrayList<>();
        int lastWordId = dbh.getLastWordId(language);
        ArrayList<String> allLines = getAllLines();
        for(String line : allLines){

            if(allSynonyms.size()>100000){
                System.out.println(allSynonyms.size()+"above");
            }
                entryCount++;
                if (entryCount < fromEntry || entryCount > toEntry){
                    if (entryCount>toEntry){
                        break;
                    }
                    if (entryCount%10000 == 0){
                       //System.out.println("EntryCount: "+entryCount);
                    }

                    continue;
                }

            if(allSynonyms.size()>100000){
                System.out.println(allSynonyms.size()+"above");
            }

                String firstline = "{base=";
                String name = "";
                if(line.contains(firstline)){
                    //System.out.println(line+line.indexOf("spelling_variant"));
                    int end = line.indexOf("spelling_variant");
                    if (end == -1){
                        end = line.indexOf("entry");
                    }
                    name = line.substring(firstline.length(),end);
                }
                ArrayList<String> acronyms = new ArrayList<>();

            if(allSynonyms.size()>100000){
                System.out.println(allSynonyms.size()+"aboveAcronym");
            }

                while(line.contains("acronym_of=")){
                    int realend = -1;
                    int end = line.indexOf("\t");
                    if(end == -1){
                       end = line.length()-1;
                       realend = line.indexOf("}");
                       // line = line.substring(end+1);
                    }else{
                        if(!(line.charAt(0)=='a'&&line.charAt(10)=='=')){
                            line = line.substring(end+1);
                            continue;
                        }
                    }

                    String acronym = line.substring(11,end);
                    if (acronym.indexOf("|")>0){
                        acronym = acronym.substring(0,acronym.indexOf("|"));
                    }

                    //System.out.println(line.indexOf("acronym_of")+11+"/"+line.indexOf("|"));
                    acronyms.add(acronym);
                    end = line.indexOf("\t");
                    if (realend==-1){
                        line = line.substring(end+1);
                    }else{
                        line = line.substring(realend+1);
                    }
                }

            if(allSynonyms.size()>100000){
                System.out.println(allSynonyms.size()+"aboveAbbreviation");
            }

                while(line.contains("abbreviation_of=")){
                    int realend = -1;
                    int end = line.indexOf("\t");
                    if(end == -1){
                        end = line.length()-1;
                        realend = line.indexOf("}");
                        // line = line.substring(end+1);
                    }else{
                        if(!(line.charAt(0)=='a'&&line.charAt(15)=='=')){
                            line = line.substring(end+1);
                            continue;
                        }
                    }

                    String abbreviation = line.substring(line.indexOf("abbreviation_of=")+16,end);
                    if (abbreviation.indexOf("|")>0){
                        abbreviation = abbreviation.substring(0,abbreviation.indexOf("|"));
                    }
                    acronyms.add(abbreviation);
                    //System.out.println(line.indexOf("acronym_of")+11+"/"+line.indexOf("|"));
                    end = line.indexOf("\t");
                    if (realend==-1){
                        line = line.substring(end+1);
                    }else{
                        line = line.substring(realend+1);
                    }
                }
            if(allSynonyms.size()>100000){
                System.out.println(allSynonyms.size()+"aboveEnd");
            }

                lastWordId++;
                Word w = new Word(lastWordId,name,language);
                wordlist.add(w);
                ArrayList<Word> synonyms = new ArrayList<>();
                for(String s : acronyms){
                    lastWordId++;
                    Word acronym = new Word(lastWordId,s,language);
                    synonyms.add(acronym);
                }

                allSynonyms.add(synonyms);
            int counter = allSynonyms.size();
            if(counter>100000){
                System.out.println(allSynonyms.size());
            }

            }

        }

        @Override
    public ArrayList<Word> getWordList() {
        return wordlist;
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
        return language;
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
        ArrayList<String> allLines = getAllLines();
        return allLines.size();
    }
}
