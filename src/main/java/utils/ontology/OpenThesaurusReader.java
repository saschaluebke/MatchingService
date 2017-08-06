package utils.ontology;

import components.Relation;
import components.Word;
import utils.ontology.FileReader;

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

    public ArrayList<String> getAllLines() {
        ArrayList<String> allLines = new ArrayList<>();
        try {
            for (Scanner sc = new Scanner(new File(System.getProperty("user.dir") + path)); sc.hasNext(); ) {
                String line = sc.nextLine();
                if (line.charAt(0) == '#') {
                    continue;
                }
                allLines.add(line);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        return allLines;
    }

        @Override
    public void getFileContent() {
        //int entryCount=-1;
            OntologyCleaner oc = new OntologyCleaner();
       ArrayList<String> allLines = oc.cleanLinesFromBraces(getAllLines());
       words = new ArrayList<>();
       synonyms = new ArrayList<>();
       String line;
       for(int entryCount = fromEntry; entryCount<toEntry; entryCount++){
           line = allLines.get(entryCount);
           if (line.indexOf(";")>0){
               String word = line.substring(0,line.indexOf(";"));
               line = line.substring(line.indexOf(";")+1,line.length());
               Word firstInput = new Word(0,word,firstLanguage);
               if(!words.contains(firstInput)){
                   words.add(firstInput);
               }else{
                   continue;
               }

               ArrayList<Word> synonymList = new ArrayList<>();

               //When there is just one Word like Cat;cat then you have to create one Relation between Cat and cat
               //And put both in the specific WordList
               while(line.indexOf(";")>0){
                   String synonym = line.substring(0,line.indexOf(";"));
                   line = line.substring(line.indexOf(";")+1,line.length());
                   Word synonymInput = new Word(0,synonym,firstLanguage);

                   if(!firstInput.equals(synonymInput) && !firstInput.equals(synonymInput)){
                       synonymList.add(synonymInput);
                   }

               }
               if(line.indexOf(";")<0 && !line.equals("")){
                   synonymList.add(new Word(0,line,firstLanguage));
               }

               synonyms.add(synonymList);
           }
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
    public int getFromEntry() {
        return 0;
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
