package database.dbStrategy.simpleStrategy;

import components.*;
import database.TranslatorGetProperties;
import database.dbStrategy.DBStrategy;
import matching.iterate.WordPerformanceStrategy;
import matching.iterate.WordStrategy;
import translators.Translator;
import utils.ontology.FileReader;
import utils.ontology.OntologyAnalysis;

import java.io.IOException;
import java.util.ArrayList;


public class SynonymStrategy extends SimpleStrategy implements DBStrategy{

    private int inputNumb=0;
    private final int MINLENGTH=4;

    public SynonymStrategy(){
        super();
    }

    @Override
    public void storeFromFile(FileReader fr){
        storeOnlyOneWord(fr);

        //storeHomonymsButNoShortcuts(fr);

        //Takes to long!
        //storeFromFileJoinDublicatesStrategy(fr);

    }

    private void storeOnlyOneWord(FileReader fr){

        ArrayList<Word> wordsForDB = new ArrayList<>(); //insert new Words here
        ArrayList<Relation> relationForDB = new ArrayList<>();
        String language = fr.getFirstLanguage();
        ArrayList<Word> allWords = getAllWords(language);
        ArrayList<Relation> allRelations = getAllRelations(language,language);
        int lastId = getLastWordId(language);

        fr.getFileContent();
        if(fr.getWordList()!=null && fr.getSynonyms()!=null && fr.getSecondWordList()==null) {
            ArrayList<Word> wordsFromFile = fr.getWordList();
            ArrayList<ArrayList<Word>> synonymsFromFile = fr.getSynonyms();

            for (int wordIndex = 0; wordIndex < wordsFromFile.size(); wordIndex++) {

                Word w1 = wordsFromFile.get(wordIndex);
                String[] wordSplit = w1.getName().split(" ");
                if( wordSplit.length>1){
                    continue;
                }
                if (w1.getName().length() < 4) {
                    continue;
                }

                String s1 = w1.getName();
                boolean findSameWord = false;
                Word wordFromDB = null;
                ArrayList<Word> relationsFromFile = synonymsFromFile.get(wordIndex);
                if (relationsFromFile.size() > 0) {
                    for (int wordFromDBIndex = 0; wordFromDBIndex < allWords.size(); wordFromDBIndex++) {
                        String stringFromDB = allWords.get(wordFromDBIndex).getName();

                        if (s1.equals(stringFromDB)) {
                            findSameWord = true;
                            wordFromDB = allWords.get(wordFromDBIndex);
                        }
                    }
                    if (findSameWord) {

                    } else {
                        /**
                         * If new Word is NOT in DB:
                         */
                        lastId++;
                        w1.setId(lastId);
                        wordsForDB.add(w1);
                        allWords.add(w1);

                        //Relationen
                        Word wordFromRelationFile = null;
                    /*
                    if (w1.getName().length() < 4) {
                        //It might be a shortcut!
                        for (int fromRelationFileIndex = 0; fromRelationFileIndex < relationsFromFile.size(); fromRelationFileIndex++) {
                            wordFromRelationFile = relationsFromFile.get(fromRelationFileIndex);
                            if (wordFromRelationFile.getName().equals(s1)) {

                            } else {

                                lastId++;
                                wordFromRelationFile.setId(lastId);
                                wordsForDB.add(wordFromRelationFile);
                                allWords.add(wordFromRelationFile);

                                //TODO: just relation to one site!
                                ArrayList<Relation> newRelations = new ArrayList<>();

                                newRelations.add(new Relation(0, w1.getId(), wordFromRelationFile.getId()));

                                relationForDB.addAll(newRelations);
                                allRelations.addAll(newRelations);

                            }
                        }
                    } else {
*/
                        for (int fromRelationFileIndex = 0; fromRelationFileIndex < relationsFromFile.size(); fromRelationFileIndex++) {
                            wordFromRelationFile = relationsFromFile.get(fromRelationFileIndex);
                            String[] relationWordSplit = wordFromRelationFile.getName().split(" ");
                            if (wordFromRelationFile.getName().equals(s1) || wordFromRelationFile.getName().length() < 4 || relationWordSplit.length>1) {

                            } else {

                                lastId++;
                                wordFromRelationFile.setId(lastId);
                                wordsForDB.add(wordFromRelationFile);
                                allWords.add(wordFromRelationFile);
                                ArrayList<Relation> newRelations = createRelationsFromNewSynonyms(w1, wordFromRelationFile, allRelations);
                                relationForDB.addAll(newRelations);
                                allRelations.addAll(newRelations);

                            }
                        }
                    }
                }

            }

            //}
        }


        if(wordsForDB.size() > 0){
            putWordList(wordsForDB,language);
            putRelationList(relationForDB,language,language);
        }

    }


    private void storeHomonymsButNoShortcuts(FileReader fr){

        ArrayList<Word> wordsForDB = new ArrayList<>(); //insert new Words here
        ArrayList<Relation> relationForDB = new ArrayList<>();
        String language = fr.getFirstLanguage();
        ArrayList<Word> allWords = getAllWords(language);
        ArrayList<Relation> allRelations = getAllRelations(language,language);
        int lastId = getLastWordId(language);

        fr.getFileContent();
        if(fr.getWordList()!=null && fr.getSynonyms()!=null && fr.getSecondWordList()==null) {
            ArrayList<Word> wordsFromFile = fr.getWordList();
            ArrayList<ArrayList<Word>> synonymsFromFile = fr.getSynonyms();

            for (int wordIndex = 0; wordIndex < wordsFromFile.size(); wordIndex++) {

                Word w1 = wordsFromFile.get(wordIndex);
                if (w1.getName().length() < 4) {
                    continue;
                }

                String s1 = w1.getName();
                boolean findSameWord = false;
                Word wordFromDB = null;
                ArrayList<Word> relationsFromFile = synonymsFromFile.get(wordIndex);
                if (relationsFromFile.size() > 0) {
                    for (int wordFromDBIndex = 0; wordFromDBIndex < allWords.size(); wordFromDBIndex++) {
                        String stringFromDB = allWords.get(wordFromDBIndex).getName();

                        if (s1.equals(stringFromDB)) {
                            findSameWord = true;
                            wordFromDB = allWords.get(wordFromDBIndex);
                        }
                    }
                    if (findSameWord) {

                    } else {
                        /**
                         * If new Word is NOT in DB:
                         */
                        lastId++;
                        w1.setId(lastId);
                        wordsForDB.add(w1);
                        allWords.add(w1);

                        //Relationen
                        Word wordFromRelationFile = null;
                    /*
                    if (w1.getName().length() < 4) {
                        //It might be a shortcut!
                        for (int fromRelationFileIndex = 0; fromRelationFileIndex < relationsFromFile.size(); fromRelationFileIndex++) {
                            wordFromRelationFile = relationsFromFile.get(fromRelationFileIndex);
                            if (wordFromRelationFile.getName().equals(s1)) {

                            } else {

                                lastId++;
                                wordFromRelationFile.setId(lastId);
                                wordsForDB.add(wordFromRelationFile);
                                allWords.add(wordFromRelationFile);

                                //TODO: just relation to one site!
                                ArrayList<Relation> newRelations = new ArrayList<>();

                                newRelations.add(new Relation(0, w1.getId(), wordFromRelationFile.getId()));

                                relationForDB.addAll(newRelations);
                                allRelations.addAll(newRelations);

                            }
                        }
                    } else {
*/
                        for (int fromRelationFileIndex = 0; fromRelationFileIndex < relationsFromFile.size(); fromRelationFileIndex++) {
                            wordFromRelationFile = relationsFromFile.get(fromRelationFileIndex);

                            if (wordFromRelationFile.getName().equals(s1) || wordFromRelationFile.getName().length() < 4) {

                            } else {

                                lastId++;
                                wordFromRelationFile.setId(lastId);
                                wordsForDB.add(wordFromRelationFile);
                                allWords.add(wordFromRelationFile);
                                ArrayList<Relation> newRelations = createRelationsFromNewSynonyms(w1, wordFromRelationFile, allRelations);
                                relationForDB.addAll(newRelations);
                                allRelations.addAll(newRelations);

                            }
                        }
                    }
                }

            }

        //}
            }


        if(wordsForDB.size() > 0){
            putWordList(wordsForDB,language);
            putRelationList(relationForDB,language,language);
        }

    }


    public void storeFromFileJoinDublicatesStrategy(FileReader fr){

        ArrayList<Word> wordsForDB = new ArrayList<>(); //insert new Words here
        ArrayList<Relation> relationForDB = new ArrayList<>();
        String language = fr.getFirstLanguage();
        ArrayList<Word> allWords = getAllWords(language);
        ArrayList<Relation> allRelations = getAllRelations(language,language);
        int lastId = getLastWordId(language);

        fr.getFileContent();
        if(fr.getWordList()!=null && fr.getSynonyms()!=null && fr.getSecondWordList()==null) {
            ArrayList<Word> wordsFromFile = fr.getWordList();
            ArrayList<ArrayList<Word>> synonymsFromFile = fr.getSynonyms();

            for(int wordIndex =0; wordIndex<wordsFromFile.size();wordIndex++){

                Word w1 = wordsFromFile.get(wordIndex);
                String s1 = w1.getName();
                boolean findSameWord = false;
                Word wordFromDB = null;
                ArrayList<Word> relationsFromFile = synonymsFromFile.get(wordIndex);
/**TODO: wenn man relationen hinzufügt müssen diese mit allen verwanden relationen kaskadiert werden
 * A 1      1 2     C kommt hinzu   3 1  hinzugeben
 * B 2      2 1                     1 3
 * C 3                              2 3
 *                                  3 2
 *
 *Dazu liste holen mit gleichen id
 * 0: einfach doppelt wie jetzt
 * ab 1: doppelt + alle toId in liste dann liste abarbeiten!
 *
 * Is the new Word already in DB?
 */
                for(int wordFromDBIndex = 0;wordFromDBIndex<allWords.size();wordFromDBIndex++){
                    String stringFromDB = allWords.get(wordFromDBIndex).getName();

                    if(s1.equals(stringFromDB)) {
                        findSameWord = true;
                        wordFromDB = allWords.get(wordFromDBIndex);
                    }
                }
                /**
                 * If new Word is already in DB: are a Synonym of it in DB?
                 */

                if(findSameWord){

                    Word wordFromRelationFile=null;
                    Word wordFromAllWords = null;
                    findSameWord = false;
                    for(int fromRelationFileIndex = 0; fromRelationFileIndex<relationsFromFile.size();fromRelationFileIndex++){
                        wordFromRelationFile = relationsFromFile.get(fromRelationFileIndex);
                        for(Word w2 : allWords){
                            wordFromAllWords = w2;
                            String stringFromAllWords = wordFromAllWords.getName();
                            if(stringFromAllWords.equals(wordFromRelationFile.getName())){
                                findSameWord = true;
                                wordFromRelationFile = wordFromAllWords;
                            }else{

                            }
                        }

                    /**
                     * If new Word and Synonym are in DB: Search if the relation is already in DB
                     */
                     if(findSameWord){


                        ArrayList<Relation> newRelations = createRelationsFromNewSynonyms(wordFromRelationFile, wordFromDB, allRelations);
                        //newRelations.addAll(createRelationsFromNewSynonyms(wordFromDB, wordFromRelationFile, allRelations));

                        relationForDB.addAll(newRelations);
                        allRelations.addAll(newRelations);

                    }else{
                        /**
                         * If new Word is in DB but not Synonym
                         */

                        lastId++;
                        wordFromRelationFile.setId(lastId);
                        wordsForDB.add(wordFromRelationFile);
                        allWords.add(wordFromRelationFile);

                        ArrayList<Relation> newRelations = createRelationsFromNewSynonyms(wordFromRelationFile, wordFromDB, allRelations);
                        relationForDB.addAll(newRelations);
                        allRelations.addAll(newRelations);
                    }
                    }

                }else{
                    /**
                     * If new Word is NOT in DB:
                     */
                    lastId++;
                    w1.setId(lastId);
                    wordsForDB.add(w1);
                    allWords.add(w1);
                    //Relationen
                    findSameWord = false;
                    Word wordFromRelationFile=null;
                    Word wordFromAllWords = null;


                    for(int fromRelationFileIndex = 0; fromRelationFileIndex<relationsFromFile.size();fromRelationFileIndex++){
                        wordFromRelationFile = relationsFromFile.get(fromRelationFileIndex);
                       for(Word w2 : allWords){
                            wordFromAllWords = w2;
                            String stringFromAllWords = wordFromAllWords.getName();
                            if(stringFromAllWords.equals(wordFromRelationFile.getName())){
                                findSameWord = true;
                               }
                        }


                    if(findSameWord){
                        /**
                         * if new Word is not but new Synonym is in DB
                         */

                        ArrayList<Relation> newRelations = createRelationsFromNewSynonyms(w1, wordFromRelationFile, allRelations);
                        relationForDB.addAll(newRelations);
                        allRelations.addAll(newRelations);

                    }else{
                        /**
                         * if new Word and Synonym is not in DB
                         */
                        lastId++;
                        wordFromRelationFile.setId(lastId);
                        wordsForDB.add(wordFromRelationFile);
                        allWords.add(wordFromRelationFile);
                        ArrayList<Relation> newRelations = createRelationsFromNewSynonyms(w1, wordFromRelationFile, allRelations);
                        relationForDB.addAll(newRelations);
                        allRelations.addAll(newRelations);

                    }
                    }
                }
            }

        }

        putWordList(wordsForDB,language);
        putRelationList(relationForDB,language,language);

    }

    private ArrayList<Relation> createRelationsFromNewSynonyms(Word wordInDB, Word newSynonym,  ArrayList<Relation> allRelations){
        /**
         * Search for Synonyms of the word if there are any these are synonyms for the new synonym too!
         */

        ArrayList<Relation> relations = new ArrayList<>();

        relations.add(new Relation(0,newSynonym.getId(),wordInDB.getId()));
        relations.add(new Relation(0,wordInDB.getId(),newSynonym.getId()));

        for(Relation r : allRelations) {
            if (r.getIdFrom() == newSynonym.getId()) {
                relations.add(new Relation(0, r.getIdTo(), wordInDB.getId()));
                relations.add(new Relation(0,wordInDB.getId(),r.getIdTo()));
            }
            if(r.getIdFrom() == wordInDB.getId()){
                relations.add(new Relation(0, r.getIdTo(), newSynonym.getId()));
                relations.add(new Relation(0,newSynonym.getId(),r.getIdTo()));
            }
        }
        return relations;
    }

/*
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
                if(word.getName().length()>=MINLENGTH && !stringsOfNewWords.contains(word.getName())){
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

            ArrayList<Word> allWords = getAllWords(language);
            int sameWords = 0;
            for(Word w1 : newWordsForDB){
                for(Word w2 : allWords){
                    if(w1.getName().equals(w2.getName())){

                    }

                }
            }
            System.out.println("Same: "+sameWords);
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
*/

    /**
     * 1. Try to Match with WordList of same language then input
     * 2. When you find something equal, look at the synonym table
     * 3. Translate input and all of its synonyms
     * 4. Put direktTranslation of input, matchings - and there translations and synonym of that matchings - and there translations into TranslationResult
     *
     * @return the real full return is in translationResult. Here the translations will be returned just for the Unit Test
     */
    public ArrayList<String> translate(TranslationResult translationResult, Translator translator, ArrayList<Word> allWords,
                                       ArrayList<Relation> allRelation){
        int maxMatches = 0;
        double accuracy = 0.0;
        TranslatorGetProperties tgp = new TranslatorGetProperties();
        try {
            accuracy = Double.parseDouble(tgp.getPropValues("Translate.accuracy"));
            maxMatches = Integer.parseInt(tgp.getPropValues("Matcher.maxMatches"));
           } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> directTranslations = new ArrayList<>();
        ArrayList<String> allTranslations = new ArrayList<>();
        SynonymTranslationResult str = (SynonymTranslationResult) translationResult;
        String language = translator.getFromLanguage();
        ArrayList<String> words = new ArrayList<>();
        ArrayList<ArrayList<String>> matchingsOfWords = new ArrayList<>();
        ArrayList<ArrayList<String>> translatedMatchingsOfWords = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> synonymsOfMatchesOfWords = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<String>>> translatedSynonymsOfMatchesOfWords = new ArrayList<>();
        int synCount=0, matchCount=0, realMatchCount=0, realSynCount=0;

        Word input = translationResult.getInput();

        //Moses can not handle special signs which put two words together
/*
        ArrayList<String> specialSigns = new ArrayList<>();
        specialSigns.add("/");
        specialSigns.add("-");
        specialSigns.add("\\(");
        specialSigns.add("\\)");

        String inputString = input.getName();
        for(String specialSign : specialSigns){
            String[] specialSplitted = inputString.split("("+specialSign+")");
            if(specialSplitted.length > 1){
                inputString = "";
                for(int i = 0; i<specialSplitted.length;i++){
                    String string = specialSplitted[i];
                    if(i<specialSplitted.length-1){
                        inputString = inputString + string + " " + specialSign + " ";
                    }else{
                        inputString = inputString + string;
                    }
                }
            }
        }
        inputString = inputString.replace('\\',' ');
        input.setWord(inputString);

        System.out.println("Direct:" + inputString);

        String directTranslation  = translator.translation(inputString);
 */
String directTranslation = translator.translation(input.getName());
        //Split inputWord into several Words if the WordStrategy is chosen.
        //matcher = getMatcher();
        //System.out.println(this.matcher.getIterateStrategy().getClass().getName());
        if(matcher.getIterateStrategy() != null &&
                (matcher.getIterateStrategy().getClass().equals(new WordStrategy().getClass())||
                matcher.getIterateStrategy().getClass().equals(new WordPerformanceStrategy().getClass()))){
            //matcher = new Matcher(new PerformanceStrategy(),new LevenshteinNormalized(),new ScoreSort());
          //  System.out.println("Use WordStrategy Split all input");
            String[] splitted = input.getName().split("( )|(/)|(-)");
            for(String s: splitted){
                if(s.length()>2){
                    words.add(s);
                }
            }
        }else{
            words.add(input.getName());
        }

//ArrayList<String> uniqueSynonymMatchings = new ArrayList<>();


        for(int i=0;i<words.size();i++) {
            realMatchCount = 0;
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
            ArrayList<String> uniqueMatchings = new ArrayList<>();

            //Look in DB for matchings
            MatchResultSet mrs = matcher.getMatchingWordList(currentInputString, allWords);
      /*      if(mrs.getMatchResults().size()>0){
                System.out.println( mrs.getMatchResults().get(0).size());
                System.out.println(mrs.getMatchResults().get(0).get(0).getScore());
            }*/


            //System.out.println(mrs.getMatchResults().get(0).get(0).getScore());

            for (ArrayList<MatchResult> matchResults : mrs.getMatchResults()) {
                if (matchResults != null && matchResults.size() > 0) {
                    ArrayList<String> synonymMatchings = new ArrayList<>();
                    ArrayList<String> translatedSynonymMatchings = new ArrayList<>();
                    for (MatchResult mr : matchResults) {
                        int synonymCounting = 0;
                        int matchID = mr.getID();
                        Word w = allWords.get(matchResults.get(0).getID() - 1);//SQL Index starts with 1
                        if (mr.getScore() < accuracy && !(uniqueMatchings.contains(w.getName()))) {
                            matchCount++;
                            uniqueMatchings.add(w.getName());
                            // String trans = translator.translation(w.getName());
                            matchings.add(w.getName());
                            //   translatedMatchings.add(trans);
                            boolean matchFlag = false;
                            boolean moreSyns = true;
                            if (realMatchCount >= maxMatches) {
                                moreSyns = false;
                            }
                            if(moreSyns){
                                translatedMatchings.add(translator.translation(w.getName()));
                            }else{
                                translatedMatchings.add("["+w.getName()+"]");
                            }
                                for (Relation r : allRelation) {
                                    if (matchID == r.getIdFrom()) {
                                        synonymCounting++;
                                        int synID = r.getIdTo();
                                        Word word = allWords.get(synID - 1);
                                        String wordString = word.getName();
                                     /*   if (!uniqueSynonymMatchings.equals(word.getName())) {*/
                                            synCount++;

                                            //System.out.println("Syn: " + word.getName());
                                            String translat;
                                            if (moreSyns) {
                                                translat = translator.translation(word.getName());
                                            } else {

                                                wordString = ("["+word.getName()+"]");
                                                translat = "["+word.getName()+"]";
                                            }

                                            if (!translat.equals(wordString)) {
                                                realSynCount++;
                                            }
                                            if (!matchFlag) {
                                                realMatchCount++;
                                                matchFlag = true;
                                            }
                                            synonymMatchings.add(wordString);
                                           // uniqueSynonymMatchings.add(wordString);
                                            translatedSynonymMatchings.add(translat);

                                            allTranslations.add(translat);
                                     /*   }*/


                                    }
                                }
                         //  } else {
                           //    synonymMatchings.add("-");
                            //    translatedSynonymMatchings.add("-");
                          // }
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
        str.setRealSynCount(realSynCount);
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
