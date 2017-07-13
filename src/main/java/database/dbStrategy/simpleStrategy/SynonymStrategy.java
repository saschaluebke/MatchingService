package database.dbStrategy.simpleStrategy;

import components.MatchResult;
import components.MatchResultSet;
import components.Relation;
import components.Word;
import database.TranslatorGetProperties;
import database.dbStrategy.DBStrategy;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import matching.Matcher;
import translators.Translator;
import utils.FileReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by sashbot on 08.07.17.
 */
public class SynonymStrategy extends SimpleStrategy implements DBStrategy {
    Matcher matcher;

    public SynonymStrategy(){
        super();
        matcher = getMatcher();
    }

    @Override
    public void storeFromFile(FileReader fr){

        fr.getFileContent();
        if(fr.getWordList()!=null && fr.getWordList().size() > 0 && fr.getSynonyms()!=null && fr.getSecondWordList()==null){
            ArrayList<Word> words = fr.getWordList();
            String language = words.get(0).getLanguage();
            ArrayList<ArrayList<Word>> synonyms = fr.getSynonyms();
        /*    System.out.println(words.size());

            for(int i= 0;i<words.size();i++){

                if (synonyms.get(i)!=null){

                    for(Word w : synonyms.get(i)){
                        System.out.println(w.getName());
                    }

                }
            }
*/
//Create a Wordlist with just new Words
            //putWordList(words,words.get(0).getLanguage());

            //these list will be in the database so there must be no dublicates!
            ArrayList<Word> newWordsForDB = new ArrayList<>();
            ArrayList<String> stringsOfNewWords = new ArrayList<>(); //list to prevent dublicates
           int lastId = getLastWordId(language);

            ArrayList<Relation> relations = new ArrayList<>();
            for(int i=0;i<words.size();i++){
                Word word = words.get(i);
                if(!stringsOfNewWords.contains(word.getName())){
                    lastId++;
                    word.setId(lastId);
                    newWordsForDB.add(word);
                    stringsOfNewWords.add(word.getName());
                }
                ArrayList<Word> removedSynonyms = new ArrayList<>();
                for(Word synonym : synonyms.get(i)){
                    if(!stringsOfNewWords.contains(synonym.getName())){
                        lastId++;
                        synonym.setId(lastId);
                        newWordsForDB.add(synonym);
                        stringsOfNewWords.add(synonym.getName());
                    }else{
                        removedSynonyms.add(synonym);
                    }
                }

                synonyms.get(i).removeAll(removedSynonyms);

                //Create relations and put them into the relationList
                synonyms.get(i).add(word);
                for(int x=0;x<synonyms.get(i).size();x++){
                    Word w = synonyms.get(i).get(x);
                    for(int y=0;y<synonyms.get(i).size();y++){
                        if(x!=y){
                            Relation r = new Relation(0,w.getId(),synonyms.get(i).get(y).getId());
                            relations.add(r);
                        }
                    }

                }
            }

            putWordList(newWordsForDB,language);

            //check if the auto-Identifier of MySQL is the same as in the newWords list
            Word checkWord = getWordById(lastId,language);
            if(checkWord.getId()!= newWordsForDB.get(newWordsForDB.size()-1).getId()){
                System.out.println("SynonymStrategy error: Not the same Id in storeFromFile");
                int lastIdinDB = getLastWordId(language);
                System.out.println("LastId: "+lastId+". Last Id in DB: "+lastIdinDB);
                Word lastWord = getWordById(lastIdinDB,language);
                System.out.println("Last Word: "+lastWord.getName()+"/"+lastWord.getId());
            }

            putRelationList(relations,fr.getFirstLanguage(),fr.getSecondLanguage());

        }else if(fr.getWordList()!=null && fr.getSecondWordList()!=null && fr.getSynonyms()==null){
            //TODO: insert Translations
        }
    }

    /*
    @Override
    public ArrayList<String> translate(Translator translator, Word input) {
        MatchResultSet mrs = matcher.getMatchingWordList(input,getAllWords(input.getLanguage()));
        ArrayList<Integer> idOfPerfectMatch = new ArrayList<>();
        for(ArrayList<MatchResult> matchResults : mrs.getMatchResults()){
            if (matchResults.get(0).getScore()==0){
                idOfPerfectMatch.add(matchResults.get(0).getID());
            }
        }
        ArrayList<Relation> synonyms = getAllRelations(input.getLanguage(),input.getLanguage());
        ArrayList<Integer> synonymsOfPerfectMatch = new ArrayList<>();
        for(Relation r : synonyms){
            if (idOfPerfectMatch.contains(r.getIdFrom())){
                synonymsOfPerfectMatch.add(r.getIdTo());
            }
        }
        synonymsOfPerfectMatch.addAll(idOfPerfectMatch);

        //remove all dublicates
        ArrayList<Integer> idTranslations = new ArrayList<Integer>(new LinkedHashSet<Integer>(synonymsOfPerfectMatch));
        ArrayList<String> translations = new ArrayList<>();
        for(int id : idTranslations){
            translations.add(translator.translation(getWordById(id,input.getLanguage()).getName()));
        }

        return translations;
    }
   */
    /**
     * 1. Try to Match with WordList of same language then input
     * 2. When you find something equal look at the synonym table
     * 3. Translate input and all of its synonyms
     *
     * @param translator
     * @param input
     * @return
     */
    public ArrayList<String> translate(Translator translator, Word input, ArrayList<Word> allWords,
                                       ArrayList<Relation> allRelation){
        MatchResultSet mrs = matcher.getMatchingWordList(input,allWords);
        ArrayList<Integer> idOfPerfectMatch = new ArrayList<>();
        TranslatorGetProperties tgp = new TranslatorGetProperties();
        double accuracy=0;
        try {
            accuracy = Double.parseDouble(tgp.getPropValues("Translate.accuracy"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * Search for matchings in DB when they are similar enough (accuracy) then fetch them
         */
        for(ArrayList<MatchResult> matchResults : mrs.getMatchResults()){
            if (matchResults.get(0).getScore()<accuracy){
                idOfPerfectMatch.add(matchResults.get(0).getID());
                //System.out.println(input.getName()+" : "+matchResults.get(0).getDbString());
            }
        }

        /**
         * Search for Synonyms of the words get the IDENTIFIERS
         */

        ArrayList<Relation> synonyms = allRelation;
        ArrayList<Integer> synonymsOfPerfectMatch = new ArrayList<>();
        for(Relation r : synonyms){
            if (idOfPerfectMatch.contains(r.getIdFrom())){
                synonymsOfPerfectMatch.add(r.getIdTo());
            }
        }

        /**
         * Find all Words from the Wordlist and translate them
         */
        ArrayList<String> translations = new ArrayList<>();
        String inputTrans =translator.translation(input.getName());
        translations.add(inputTrans);
        System.out.println("Input: "+input.getName()+" --> "+inputTrans);

        for(int id:idOfPerfectMatch){
            for(Word w:allWords){
                if(id==w.getId()){
                    String trans = translator.translation(w.getName());
                    translations.add(trans);
                    System.out.println("Match: "+w.getName()+" --> "+trans);
                }
            }
        }
        for(int id:synonymsOfPerfectMatch){
            for(Word w : allWords){
                if(id==w.getId()){
                    String trans = translator.translation(w.getName());
                    translations.add(trans);
                    System.out.println("Syn: "+w.getName()+" --> "+trans);
                }
            }
        }

        return translations;

    }

}
