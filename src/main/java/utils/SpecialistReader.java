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

    @Override
    public void getFileContent() {
        entryCount=0;
        wordlist = new ArrayList<>();
        allSynonyms = new ArrayList<>();
        int lastWordId = dbh.getLastWordId(language);
        try {
            for (Scanner sc = new Scanner(new File(System.getProperty("user.dir") + path)); sc.hasNext(); ) {
                String line = sc.nextLine();

                if(line.contains("{base=")){
                    while(!line.contains("}")){
                        line = line + sc.nextLine();
                    }
                }

                entryCount++;
                if (entryCount < fromEntry || entryCount > toEntry){
                    if (entryCount>toEntry){
                        break;
                    }
                    if (entryCount%10000 == 0){
                        System.out.println("EntryCount: "+entryCount);
                    }

                    continue;
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
                String tmp=line;
                while(line.contains("acronym_of=")){
                    int end = line.indexOf("\t");
                    if(end == -1){
                        end = line.indexOf("}");
                    }
                    tmp = line;
                    line = line.substring(end+1);
                    //System.out.println(line+line.indexOf("\t")+"/"+end);
                    if(line.contains("acronym_of=")){
                        continue;
                    }else{
                        line = tmp;
                    }

                    //System.out.println(line.indexOf("acronym_of")+11+"/"+end+line);
                    String acronym = line.substring(line.indexOf("acronym_of")+11,end);
                    if (acronym.indexOf("|")>0){
                        acronym = acronym.substring(0,acronym.indexOf("|"));
                    }

                    //System.out.println(line.indexOf("acronym_of")+11+"/"+line.indexOf("|"));
                    acronyms.add(acronym);
                    line = line.substring(end+1);

                    //System.out.println(line);
                }
                while(line.contains("abbreviation_of=")){
                    int end = line.indexOf("\t");
                    if(end == -1){
                        end = line.indexOf("}");
                        if (end == -1){
                            break;
                        }
                    }
                    tmp = line;
                    line = line.substring(end+1);
                    //System.out.println(line+line.indexOf("\t")+"/"+end);
                    if(line.contains("abbreviation_of=")){
                        continue;
                    }else{
                        line = tmp;
                    }
                    String abbreviation = line.substring(line.indexOf("abbreviation_of=")+16,end);
                    if (abbreviation.indexOf("|")>0){
                        abbreviation = abbreviation.substring(0,abbreviation.indexOf("|"));
                    }
                    acronyms.add(abbreviation);
                    line = line.substring(end+1);
                }
                //System.out.println(name+"/");
                //TODO: doublications?


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
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    public void setFromEntry(int fromEntry) {
        this.fromEntry = fromEntry;
    }

    public void setToEntry(int toEntry) {
        this.toEntry = toEntry;
    }
}
