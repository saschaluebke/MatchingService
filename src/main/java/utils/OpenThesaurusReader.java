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
 * Created by sashbot on 08.07.17.
 */
public class OpenThesaurusReader implements FileReader {
    ArrayList<Word> words;
    ArrayList<ArrayList<Word>> synonyms;
    String path;
    private int fromEntry, toEntry;
    private String firstLanguage;

    public OpenThesaurusReader(String path, String language){
        this.path = path;
        this.firstLanguage = language;//Openthesaurus are for Synonyms only
        words = new ArrayList<>();
        synonyms = new ArrayList<>();
    }

    @Override
    public void getFileContent() {
        int entryCount=0;
        try {
            for (Scanner sc = new Scanner(new File(System.getProperty("user.dir") + path)); sc.hasNext(); ) {
                String line = sc.nextLine();
                if(line.charAt(0)=='#'){
                    continue;
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

                if (line.indexOf(";")>0){
                    String word = line.substring(0,line.indexOf(";"));
                    line = line.substring(line.indexOf(";")+1,line.length());
                    Word firstInput = new Word(0,word,firstLanguage);
                    words.add(firstInput);
                    ArrayList<Word> synonymList = new ArrayList<>();
                while(line.indexOf(";")>0){
                    String synonym = line.substring(0,line.indexOf(";"));
                    line = line.substring(line.indexOf(";")+1,line.length());
                    Word synonymInput = new Word(0,synonym,firstLanguage);
                    synonymList.add(synonymInput);
                }
                synonyms.add(synonymList);
                }


            } } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Word> getWordList() {
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

    public ArrayList<ArrayList<Word>> getSynonyms() {
        return synonyms;
    }

    @Override
    public String getFirstLanguage() {
        return firstLanguage;
    }

    @Override
    public String getSecondLanguage() {
        return firstLanguage;
    }

    public void setFromEntry(int fromEntry) {
        this.fromEntry = fromEntry;
    }

    public void setToEntry(int toEntry) {
        this.toEntry = toEntry;
    }
}
