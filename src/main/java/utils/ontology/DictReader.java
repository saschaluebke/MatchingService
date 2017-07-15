package utils.ontology;

import components.Relation;
import components.Word;
import database.DBHelper;
import database.dbStrategy.simpleStrategy.SimpleStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * To read Dict.cc source file
 **/
public class DictReader implements FileReader{
    int fromEntry, toEntry;
    DBHelper dbh;
    String languageFrom, languageTo;
    ArrayList<Word> firstWordList, secondWordList;
    ArrayList<Relation> firstTranslation, secondTranslation;
    String path;

    public DictReader(String path, String languageFrom, String languageTo){
        this.languageFrom = languageFrom;
        this.languageTo = languageTo;
        this.path = path;
        dbh = new DBHelper(new SimpleStrategy());
    }

 @Override
    public void getFileContent() {
        int firstLanguageId = dbh.getLastWordId(languageFrom);
        int secondLanguageId = dbh.getLastWordId(languageTo);
        firstWordList = new ArrayList<>();
        secondWordList = new ArrayList<>();
        firstTranslation = new ArrayList<>();
        secondTranslation = new ArrayList<>();
int count = 0;
        try {
            for(Scanner sc = new Scanner(new File(System.getProperty("user.dir")+path)); sc.hasNext(); ) {
                String line = sc.nextLine();
                count++;
                if (count < fromEntry || count > toEntry){
                    if (count%10000 == 0){
                        System.out.println("EntryCount: "+count);
                    }

                    continue;
                }

                String[] splitString = line.split("\t");
                if(splitString.length < 2){
                    continue;
                }

                /**
                 * Moses doesn't like curly braces
                 */
                String firstWord = splitString[0];
                String secondWord = splitString[1];

                firstLanguageId++;
                firstWordList.add(new Word(firstLanguageId,firstWord,getFirstLanguage()));
                secondLanguageId++;
                secondWordList.add(new Word(secondLanguageId,secondWord,getSecondLanguage()));
                Relation translation1 = new Relation(0,firstLanguageId,secondLanguageId);
                Relation translation2 = new Relation(0,secondLanguageId, firstLanguageId);
                firstTranslation.add(translation1);
                secondTranslation.add(translation2);


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public ArrayList<Word> getWordList() {
        return firstWordList;
    }

    @Override
    public ArrayList<Word> getSecondWordList() {
        return secondWordList;
    }

    @Override
    public ArrayList<Relation> getFirstTranslation() {
        return firstTranslation;
    }

    @Override
    public ArrayList<Relation> getSecondTranslation() {
        return secondTranslation;
    }

    @Override
    public String getFirstLanguage() {
        return languageFrom;
    }

    @Override
    public String getSecondLanguage() {
        return languageTo;
    }

    @Override
    public ArrayList<ArrayList<Word>> getSynonyms() {
        return null;
    }

    @Override
    public int getFromEntry() {
        return 0;
    }

    public void setFromEntry(int fromEntry){
        this.fromEntry = fromEntry;
    }

    @Override
    public int getToEntry() {
        return 0;
    }

    public void setToEntry(int toEntry){
        this.toEntry = toEntry;
    }

    @Override
    public int getAllLinesCount() {
        return 0;
    }


    public void makeParallelCorpus(){
        try{
            PrintWriter writer = new PrintWriter("Dict.de", "UTF-8");

            for(int i=0;i<firstWordList.size();i++){
                String deutsch = firstWordList.get(i).getName();
                writer.println(deutsch);
            }

            writer.close();
        } catch (IOException e) {
            // do something
        }

        try{
            PrintWriter writer = new PrintWriter("Dict.en", "UTF-8");

            for(int i=0;i<firstWordList.size();i++){
                String englisch = secondWordList.get(i).getName();
                writer.println(englisch);
            }

            writer.close();
        } catch (IOException e) {
            // do something
        }

    }


}
