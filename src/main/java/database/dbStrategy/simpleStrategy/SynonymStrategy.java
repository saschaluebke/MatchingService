package database.dbStrategy.simpleStrategy;

import components.*;
import database.TranslatorGetProperties;
import database.dbStrategy.DBStrategy;
import matching.Matcher;
import matching.iterate.WordStrategy;
import translators.Translator;
import utils.ontology.FileReader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sashbot on 08.07.17.
 */
public class SynonymStrategy extends SimpleStrategy implements DBStrategy{

    private int inputNumb=0;
    private final int MINLENGTH=4;
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

            //these list will be in the database so there must be no dublicates!
            ArrayList<Word> newWordsForDB = new ArrayList<>();
            ArrayList<String> stringsOfNewWords = new ArrayList<>(); //list to prevent dublicates
           int lastId = getLastWordId(language);

            ArrayList<Relation> relations = new ArrayList<>();
            for(int i=0;i<words.size();i++){

                Word word = words.get(i);
                if(word.getName().length()>=MINLENGTH && !stringsOfNewWords.contains(word.getName())){
                    lastId++;
                    word.setId(lastId);
                    newWordsForDB.add(word);
                    stringsOfNewWords.add(word.getName());
                }
                ArrayList<Word> removedSynonyms = new ArrayList<>();
                for(Word synonym : synonyms.get(i)){
                    if(synonym.getName().length()>=MINLENGTH && !stringsOfNewWords.contains(synonym.getName())){
                        lastId++;
                        synonym.setId(lastId);
                        newWordsForDB.add(synonym);
                        stringsOfNewWords.add(synonym.getName());
                        if (language.equals("en")){
                            language = language;
                        }
                    }else{
                        removedSynonyms.add(synonym);
                    }
                }

                synonyms.get(i).removeAll(removedSynonyms);

                //Create relations and put them into the relationList
                if(word.getName().length()>=MINLENGTH){
                    synonyms.get(i).add(word);
                }
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

    /**
     * 1. Try to Match with WordList of same language then input
     * 2. When you find something equal look at the synonym table
     * 3. Translate input and all of its synonyms
     * 4. Put direktTranslation of input, matchings - and there translations and synonym of that matchings - and there translations into TranslationResult
     *
     * @return the real full return is in translationResult. Here the translations will be returned just for the Unit Test
     */
    public ArrayList<String> translate(TranslationResult translationResult, Translator translator, ArrayList<Word> allWords,
                                       ArrayList<Relation> allRelation){
        /**
         * TODO: Hier müsste unterschieden werden zwischen einem matcher mit wordstrategy oder ohne
         * mit word würde dann heißen dass man den input in wörter splittet und dann nach den einzelnen
         * Matches und dann synonymen sucht!
         */
        Word input = translationResult.getInput();
        if(matcher.getIterateStrategy().getClass().equals(new WordStrategy().getClass())){
            System.out.println("Use WordStrategy Split all input");
            String[] splitted = input.getName().split("( )|(/)|(-)");
            for(String s: splitted){
                //TODO: wie soll ich das hier machen! Einfach unter einander?
            }
        }else{

        }

        SynonymTranslationResult str = (SynonymTranslationResult) translationResult;
        ArrayList<String> matchings = new ArrayList<>();
        ArrayList<String> matchingTranslations = new ArrayList<>();
        ArrayList<ArrayList<String>> synonymNames = new ArrayList<>();
        ArrayList<ArrayList<String>> synonmyTranslations = new ArrayList<>();


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
            if (matchResults.size()>0){
                if (matchResults.get(0).getScore()<accuracy){
                    idOfPerfectMatch.add(matchResults.get(0).getID());
                    //System.out.println(input.getName()+" : "+matchResults.get(0).getDbString());
                }
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
        inputNumb++;
        System.out.println(inputNumb+"Input: "+input.getName()+" --> "+inputTrans);
        String language = allWords.get(0).getLanguage();
        for(int id:idOfPerfectMatch){
            Word w = this.getWordById(id,language);
            String trans = translator.translation(w.getName());

            matchings.add(w.getName());
            matchingTranslations.add(trans);

            translations.add(trans);
            System.out.println("Match: "+w.getName()+" --> "+trans);
          /*  for(Word w:allWords){
                if(id==w.getId()){
                    String trans = translator.translation(w.getName());

                    matchings.add(w.getName());
                    matchingTranslations.add(trans);

                    translations.add(trans);
                    System.out.println("Match: "+w.getName()+" --> "+trans);
                }
            }*/
        }
        for(int id:synonymsOfPerfectMatch){
            ArrayList<String> synonymList = new ArrayList<>();
            ArrayList<String> synonymTranslationList = new ArrayList<>();
            for(Word w : allWords){
                if(id==w.getId()){
                    String trans = translator.translation(w.getName());

                    synonymList.add(w.getName());
                    synonymTranslationList.add(trans);

                    translations.add(trans);
                    System.out.println("Syn: "+w.getName()+" --> "+trans);
                }
            }
            synonymNames.add(synonymList);
            synonmyTranslations.add(synonymTranslationList);
        }


        str.setDirectTranslation(inputTrans);
        str.setMatchings(matchings);
        str.setMatchingTranslations(matchingTranslations);
        str.setSynonyms(synonymNames);
        str.setSynonymTranslations(synonmyTranslations);

        return translations;

    }

}
