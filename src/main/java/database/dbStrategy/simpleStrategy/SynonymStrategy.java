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
