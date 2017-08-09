package utilsTests.fileReaderTest;

import components.Relation;
import components.Word;
import utils.ontology.FileReader;

import java.util.ArrayList;

/**
 * Created by sashbot on 08.08.17.
 */
public class TestReader implements FileReader {
    private ArrayList<Word> words;
    private ArrayList<ArrayList<Word>> synonyms;

    public TestReader(){
        words = new ArrayList<>();
        synonyms = new ArrayList<>();
    }

    @Override
    public void getFileContent() {
        words.add(new Word(0,"Kernspaltung","de"));
        words.add(new Word(0,"Fission","de"));
        words.add(new Word(0,"apfel","de"));
        words.add(new Word(0,"apfel","de"));
        ArrayList<Word> blankList = new ArrayList<>();
        synonyms.add(blankList);

        ArrayList<Word> synonymList = new ArrayList<>();
        synonymList.add(new Word(0,"Wort","de"));

        for(int i=1; i<words.size()-1;i++){
            synonyms.add(synonymList);
        }
        ArrayList<Word> newSyns = new ArrayList<>();
        newSyns.add(new Word(0,"medizin","de"));
        synonyms.add(newSyns);





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

    @Override
    public String getFirstLanguage() {
        return "de";
    }

    @Override
    public String getSecondLanguage() {
        return null;
    }

    @Override
    public ArrayList<ArrayList<Word>> getSynonyms() {
        return synonyms;
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
}
