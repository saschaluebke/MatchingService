package database.dbStrategy.simpleStrategy;

import components.*;
import database.TranslatorGetProperties;
import database.dbStrategy.DBStrategy;
import matching.Matcher;
import matching.distance.LevenshteinNormalized;
import matching.iterate.PerformanceStrategy;
import matching.iterate.WordStrategy;
import matching.sorting.ScoreSort;
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

    public SynonymStrategy(){
        super();
    }

    @Override
    public OntologyAnalysis storeFromFile(FileReader fr){
        OntologyAnalysis ontologyAnalysis = new OntologyAnalysis();
        int wordCount=0;
        int synonymCount =0;
        int wordWithoutSyns=0;
        double varianz=0;
        double averageSynPerWord=0;

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
                    wordCount++;
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
                            int synonymID = synonyms.get(i).get(y).getId();
                            if(synonymID != 0){
                                Relation r = new Relation(0,w.getId(),synonyms.get(i).get(y).getId());
                                relations.add(r);
                            }

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


            double varianzTmp=0;
            int wordIndex=0;
            for(ArrayList<Word> syns : synonyms){
                if(syns.size()==0){
                    wordWithoutSyns++;
                }else{
                    if(syns.get(0).equals(words.get(wordIndex))){
                        wordWithoutSyns++;
                    }else{
                        synonymCount = synonymCount +syns.size();
                    }
                }
                wordIndex++;
            }

            averageSynPerWord = (double)synonymCount/(double)wordCount;

            for(ArrayList<Word> syns : synonyms) {

                varianzTmp = varianzTmp + (syns.size() - averageSynPerWord) * (syns.size() - averageSynPerWord);
            }
            varianz = varianzTmp/(wordCount-1);

        }else if(fr.getWordList()!=null && fr.getSecondWordList()!=null && fr.getSynonyms()==null){
            //TODO: insert Translations
        }
        ontologyAnalysis.setWordCount(wordCount);
        ontologyAnalysis.setSynCount(synonymCount);
        ontologyAnalysis.setVarianz(varianz);
        ontologyAnalysis.setWordsWithoutSyn(wordWithoutSyns);
        ontologyAnalysis.setAverageOfSynPerWord(averageSynPerWord);
        return ontologyAnalysis;
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
        ArrayList<String> allTranslations = new ArrayList<>();
        SynonymTranslationResult str = (SynonymTranslationResult) translationResult;
        String language = allWords.get(0).getLanguage();
        ArrayList<String> words = new ArrayList<>();
        ArrayList<ArrayList<String>> matchingsOfWords = new ArrayList<>();
        ArrayList<ArrayList<String>> translatedMatchingsOfWords = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> synonymsOfMatchesOfWords = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> translatedSynonymsOfMatchesOfWords = new ArrayList<>();
        Word input = translationResult.getInput();
        int synCount=0, matchCount=0;

        //Split inputWord into several Words if the WordStrategy is chosen.
        //matcher = getMatcher();
        //System.out.println(this.matcher.getIterateStrategy().getClass().getName());
        if(matcher.getIterateStrategy() != null && matcher.getIterateStrategy().getClass().equals(new WordStrategy().getClass())){
            //matcher = new Matcher(new PerformanceStrategy(),new LevenshteinNormalized(),new ScoreSort());
          //  System.out.println("Use WordStrategy Split all input");
            String[] splitted = input.getName().split("( )|(/)|(-)");
            for(String s: splitted){
                if(s.length()>4){
                    words.add(s);
                }
            }
        }else{
            words.add(input.getName());
        }

        String directTranslation = translator.translation(input.getName());

ArrayList<String> uniqueSynonymMatchings = new ArrayList<>();
ArrayList<String> uniqueMatchings = new ArrayList<>();
        for(int i=0;i<words.size();i++) {
            String currentInputString = words.get(i);
            Word currentWord;
            if (words.size() == 1) {
                currentWord = input;
            } else {
                currentWord = new Word(0, currentInputString, language);
            }
            ArrayList<String> matchings = new ArrayList<>();
            ArrayList<String> translatedMatchings = new ArrayList<>();
            ArrayList<ArrayList<String>> synonymMatchesOfWords = new ArrayList<>();
            ArrayList<ArrayList<String>> translatedSynonymMatchesOfWords = new ArrayList<>();


            //Look in DB for matchings
            MatchResultSet mrs = matcher.getMatchingWordList(currentWord, allWords);
            TranslatorGetProperties tgp = new TranslatorGetProperties();
            double accuracy = 0;
            try {
                accuracy = Double.parseDouble(tgp.getPropValues("Translate.accuracy"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (ArrayList<MatchResult> matchResults : mrs.getMatchResults()) {
                if (matchResults != null && matchResults.size() > 0) {
                    ArrayList<String> synonymMatchings = new ArrayList<>();
                    ArrayList<String> translatedSynonymMatchings = new ArrayList<>();
                    for(MatchResult mr : matchResults){
                        int matchID = mr.getID();
                        Word w = allWords.get(matchResults.get(0).getID()-1);//SQL Index starts with 1
                        if (mr.getScore() < accuracy && !(uniqueMatchings.contains(w.getName()))) {
                            matchCount++;
                            uniqueMatchings.add(w.getName());
                            String trans = translator.translation(w.getName());
                            matchings.add(w.getName());
                            translatedMatchings.add(trans);

                            for (Relation r : allRelation) {
                                if (matchID == r.getIdFrom()) {
                                    int synID = r.getIdTo();
                                    Word word = allWords.get(synID-1);

                                         if(!uniqueSynonymMatchings.contains(word.getName())){
                                             synCount++;
                                             String translat = translator.translation(word.getName());
                                             synonymMatchings.add(word.getName());
                                             uniqueSynonymMatchings.add(word.getName());
                                             translatedSynonymMatchings.add(translat);

                                             allTranslations.add(translat);
                                         }

                                        }
                            }
                            synonymMatchesOfWords.add(synonymMatchings);
                            translatedSynonymMatchesOfWords.add(translatedSynonymMatchings);
                        }
                    }

                    }

            }
            synonymsOfMatchesOfWords.add(synonymMatchesOfWords);
            translatedSynonymsOfMatchesOfWords.add(translatedSynonymMatchesOfWords);
            matchingsOfWords.add(matchings);
            translatedMatchingsOfWords.add(translatedMatchings);

        }
        str.setMatchings(matchingsOfWords);
        str.setTranslatedMatchings(translatedMatchingsOfWords);
        str.setMatchingSynonyms(synonymsOfMatchesOfWords);
        str.setMatchingSynonymtranslations(translatedSynonymsOfMatchesOfWords);
        str.setSynonymCount(synCount);
        str.setMatchingCount(matchCount);
        str.setDirectTranslation(directTranslation);
        str.setWords(words);

        return allTranslations;

    }












/*
    /**
     * 1. Try to Match with WordList of same language then input
     * 2. When you find something equal look at the synonym table
     * 3. Translate input and all of its synonyms
     * 4. Put direktTranslation of input, matchings - and there translations and synonym of that matchings - and there translations into TranslationResult
     *
     * @return the real full return is in translationResult. Here the translations will be returned just for the Unit Test
     */
/*
    public ArrayList<String> translate(TranslationResult translationResult, Translator translator, ArrayList<Word> allWords,
                                       ArrayList<Relation> allRelation){
        /**
         * TODO: Hier müsste unterschieden werden zwischen einem matcher mit wordstrategy oder ohne
         * mit word würde dann heißen dass man den input in wörter splittet und dann nach den einzelnen
         * Matches und dann synonymen sucht!
         */
/*
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


        for(ArrayList<MatchResult> matchResults : mrs.getMatchResults()){
            if (matchResults.size()>0){
                if (matchResults.get(0).getScore()<accuracy){
                    idOfPerfectMatch.add(matchResults.get(0).getID());
                    //System.out.println(input.getName()+" : "+matchResults.get(0).getDbString());
                }
            }

        }



        ArrayList<Relation> synonyms = allRelation;
        ArrayList<Integer> synonymsOfPerfectMatch = new ArrayList<>();
        for(Relation r : synonyms){
            if (idOfPerfectMatch.contains(r.getIdFrom())){
                synonymsOfPerfectMatch.add(r.getIdTo());
            }
        }


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
           for(Word w:allWords){
                if(id==w.getId()){
                    String trans = translator.translation(w.getName());

                    matchings.add(w.getName());
                    matchingTranslations.add(trans);

                    translations.add(trans);
                    System.out.println("Match: "+w.getName()+" --> "+trans);
                }
            }
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
    */

}
