package utils.ontology;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import utils.ontology.FileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//TODO: Testen!
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
        entryCount = 0;
        wordlist = new ArrayList<>();
        allSynonyms = new ArrayList<>();
        int lastWordId = dbh.getLastWordId(language);
        ArrayList<String> allLines = getAllLines();
        String line;
        for(entryCount = fromEntry; entryCount<toEntry; entryCount++){
            line = allLines.get(entryCount);

                String firstline = "{base=";
                String name = "";
                if(line.contains(firstline)){
                    //System.out.println(line+line.indexOf("spelling_variant"));
                    int end = line.indexOf("spelling_variant");
                    if (end == -1){
                        end = line.indexOf("entry");
                    }
                    name = line.substring(firstline.length(),end);
                    if(name.length() < 4){
                        continue;
                    }
                }
                ArrayList<String> acronyms = new ArrayList<>();


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
                    if(acronym.length() < 4){
                        break;
                    }
                    acronyms.add(acronym);
                    end = line.indexOf("\t");
                    if (realend==-1){
                        line = line.substring(end+1);
                    }else{
                        line = line.substring(realend+1);
                    }
                }
                int counter=0;
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
                    if(abbreviation.length()<4){
                        break;
                    }
                    acronyms.add(abbreviation);
                    //System.out.println(line.indexOf("acronym_of")+11+"/"+line.indexOf("|"));
                    end = line.indexOf("\t");
                    if (realend==-1){
                        line = line.substring(end+1);
                    }else{
                        line = line.substring(realend+1);
                    }
                    counter++;
                    if(counter > 10){
                        break;
                    }
                }

                lastWordId++;
                Word w = new Word(lastWordId,name,language);

                ArrayList<Word> synonyms = new ArrayList<>();
                for(String s : acronyms){
                    lastWordId++;
                    Word acronym = new Word(lastWordId,s,language);
                    synonyms.add(acronym);
                }
                if(synonyms.size()<1){
                    continue;
                   // System.out.println("Hallo");
                }
                wordlist.add(w);
                allSynonyms.add(synonyms);
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
