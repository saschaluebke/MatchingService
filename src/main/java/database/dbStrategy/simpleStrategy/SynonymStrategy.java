package database.dbStrategy.simpleStrategy;

import components.MatchResult;
import components.MatchResultSet;
import components.Relation;
import components.Word;
import database.dbStrategy.DBStrategy;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import matching.Matcher;
import translators.Translator;
import utils.FileReader;

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
        if(fr.getWordList()!=null && fr.getSynonyms()!=null && fr.getSecondWordList()==null){
            //Put all words into the wordList
            fr.getFileContent();
            ArrayList<Word> words = fr.getWordList();
            ArrayList<ArrayList<Word>> synonyms = fr.getSynonyms();
            System.out.println(words.size()+"/"+synonyms.size());
            ArrayList<Relation> relations = new ArrayList<>();
            for(int i=0;i<words.size();i++){
                Word word = words.get(i);
                word.setId(putWord(word));
                for(Word synonym : synonyms.get(i)){
                    synonym.setId(putWord(synonym));
                }
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

            putRelationList(relations,fr.getFirstLanguage(),fr.getSecondLanguage());

        }else if(fr.getWordList()!=null && fr.getSecondWordList()!=null && fr.getSynonyms()==null){
            //TODO: insert Translations
        }
    }

    /**
     * 1. Try to Match with WordList of same language then input
     * 2. When you find something equal look at the synonym table
     * 3. Translate input and all of its synonyms
     *
     * @param translator
     * @param input
     * @return
     */
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
}
