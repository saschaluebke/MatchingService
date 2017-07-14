package database.dbStrategy.simpleStrategy;

import components.MatchResult;
import components.MatchResultSet;
import components.Relation;
import components.Word;
import database.MySQLQuery;
import database.dbStrategy.DBStrategy;
import matching.Matcher;
import matching.SimpleMatcher;
import matching.distance.DistanceStrategy;
import matching.distance.LevenshteinNormalized;
import matching.iterate.IterateStrategy;
import matching.iterate.PerformanceStrategy;
import matching.sorting.ScoreSort;
import matching.sorting.SortStrategy;
import translators.Translator;
import utils.FileReader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SimpleStrategy implements DBStrategy {
    final IterateStrategy iterateStrategy = new PerformanceStrategy();
    final DistanceStrategy distanceStrategy = new LevenshteinNormalized();
    final SortStrategy sortStrategy = new ScoreSort();

    final int MAXCHARS = 100; //TODO in properties rein!
    private Matcher matcher;
    MySQLQuery dbq;

    public SimpleStrategy() {
        dbq = new MySQLQuery();
        matcher = new Matcher(new PerformanceStrategy(), new LevenshteinNormalized(), new ScoreSort());
//matcher = new SimpleMatcher();
        if (!dbq.isTable("languages")) {
            dbq.queryUpdate("CREATE TABLE languages"
                    + " (id INT NOT NULL AUTO_INCREMENT,name VARCHAR(45) NULL,"
                    + "PRIMARY KEY (id));", new String[0]);
        } else {
            //TODO: search for all other tables necessary
        }
    }

    //TODO: Konstruktor für strategien? Methoden für strategien?

    public Matcher getMatcher() {
        return matcher;
    }


    @Override
    public int putWord(Word word) {
        int id = 0;
        ResultSet rs = null;
        try {

            if ((word.getName().length() > MAXCHARS || word.getDescription().length() > MAXCHARS)) {
                return -1; //TODO: Dirty code please clean up!
            }

            String[] par1 = {word.getName(), word.getDescription(), Integer.toString(word.getPrior()), Integer.toString(word.getCount())};
            dbq.queryUpdate("INSERT INTO wordlist_" + word.getLanguage() + " VALUES (0, ?, ?, ?, ?)", par1);
            String[] par2 = {word.getName()};
            rs = dbq.query("SELECT * FROM wordlist_" + word.getLanguage() + " WHERE name = ? ", par2);

            rs.next();
            id = rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public int putRelation(Word word1, Word word2) {
        int id = -1;
        //enter Synonym
        if (word1.getLanguage().equals(word2.getLanguage())) {
            //TODO: See prior and count
            String[] par1 = {Integer.toString(word1.getId()), Integer.toString(word2.getId()), Integer.toString(0), Integer.toString(0)};
            id = dbq.queryUpdate("INSERT INTO rel_" + word1.getLanguage() + "_" + word2.getLanguage() + " VALUES (0, ?, ? , ? ,?)", par1);
        } else //enter translation
        {
            String[] par1 = {Integer.toString(word1.getId()), Integer.toString(word2.getId()), Integer.toString(0), Integer.toString(0)};
            id = dbq.queryUpdate("INSERT INTO rel_" + word1.getLanguage() + "_" + word2.getLanguage() + " VALUES (0, ?, ? , ? ,?)", par1);

        }
        return id;
    }

    @Override
    public void putRelationList(ArrayList<Relation> relations, String language1, String language2) {
        ArrayList<String> parList = new ArrayList<>();
        if(relations==null || relations.size()<1){
            System.out.println("SimpleStrategyError: "+relations+" null or 0");
            return;
        }
        StringBuilder querry = new StringBuilder("INSERT INTO rel_" + language1+"_"+language2 + " VALUES ");
        for (int i = 0; i < relations.size(); i++) {
            Relation rel = relations.get(i);

           // String[] parTemp = {w.getName(), w.getDescription(), Integer.toString(w.getPrior()), Integer.toString(w.getCount())};
            parList.add(Integer.toString(rel.getIdFrom()));
            parList.add(Integer.toString(rel.getIdTo()));

            if (i < relations.size() - 1) {
                querry.append("(0, ?, ?, 0 ,0),");
            } else {
                querry.append("(0, ?, ?, 0 ,0);");
            }
        }
        String[] pars = parList.toArray(new String[parList.size()]);
        dbq.queryUpdate(querry.toString(), pars);
    }
/*
    @Override
    public int takeFromFileReader(FileReader fr) {
        fr.getFileContent();
        ArrayList<Word> wordList = fr.getWordList();
        String firstLanguage = fr.getFirstLanguage();
        String secondLanguage = fr.getSecondLanguage();
        ArrayList<Word> wordList2 = fr.getSecondWordList();
        ArrayList<Relation> translation1 = fr.getFirstTranslation();
        ArrayList<Relation> translation2 = fr.getSecondTranslation();

        if (wordList != null && wordList.size()>0){
            putWordList(wordList,secondLanguage);
        }
        if (wordList2 != null && wordList2.size()>0){
            putWordList(wordList2,secondLanguage);
        }
        if (translation1 != null && translation1.size()>0){
            putRelationList(translation1,firstLanguage,secondLanguage);
        }
        if (translation2 != null && translation2.size()>0){
            putRelationList(translation2, secondLanguage, firstLanguage);
        }

   /*
        ArrayList<Word> wordList = new ArrayList<>();
        for (String s : fr.getFileContent()) {
            wordList.add(new Word(0, s, language)); //or just leave language = ""
        }

        putWordList(wordList,language);

        return wordList.size();
    }
*/
    @Override
    public void storeFromFile(FileReader fr) {
        fr.getFileContent();
        ArrayList<Word> words = fr.getWordList();
        if (words != null && words.size()>0){
            putWordList(words,words.get(0).getLanguage());
        }
    }

    @Override
    public void putWordList(ArrayList<Word> wordList, String language) {
        ArrayList<String> parList = new ArrayList<>();
        StringBuilder querry = new StringBuilder("INSERT INTO wordlist_" + wordList.get(0).getLanguage() + " VALUES ");
        for (int i = 0; i < wordList.size(); i++) {
            Word w = wordList.get(i);
            if ((w.getName().length() > MAXCHARS || w.getDescription().length() > MAXCHARS)) {
                continue; //TODO: Dirty code please clean up!
            }
            String[] parTemp = {w.getName(), w.getDescription(), Integer.toString(w.getPrior()), Integer.toString(w.getCount())};
            parList.add(w.getName());
            parList.add(w.getDescription());
            parList.add(Integer.toString(w.getPrior()));
            parList.add(Integer.toString(w.getCount()));
            if (i < wordList.size() - 1) {
                querry.append("(0, ?, ?, ?, ?),");
            } else {
                querry.append("(0, ?, ?, ?, ?);");
            }
        }
        String[] pars = parList.toArray(new String[parList.size()]);
        dbq.queryUpdate(querry.toString(), pars);
    }

    @Override
    public ArrayList<Word> getAllWords(String language) {
        ArrayList<Word> wordList = new ArrayList<>();
        ResultSet rs = null;
        String[] par = new String[0];
        rs = dbq.query("SELECT * FROM wordlist_" + language, par);
        try {
            while (rs.next() == true) {
                String name;
                int id = rs.getInt("id");
                name = rs.getString("name");
                Word flotsam = new Word(id, name, language);
                flotsam.setDescription(rs.getString("description"));
                if (flotsam.getDescription().equals("")) {
                    flotsam.setDescription("-");
                }
                flotsam.setPrior(rs.getInt("prior"));
                flotsam.setCount(rs.getInt("count"));
                wordList.add(flotsam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wordList;
    }

    @Override
    public ArrayList<Relation> getAllRelations(String languageFrom, String languageTo) {
        ArrayList<Relation> relations = new ArrayList<>();
        ResultSet rs = null;
        String[] par = new String[0];
        rs = dbq.query("SELECT * FROM rel_" + languageFrom+"_"+languageTo, par);
        try {
            while (rs.next() == true) {
                int id = rs.getInt("id");
                int intFrom = rs.getInt("idFrom");
                int intTo = rs.getInt("idTo");
                Relation flotsam = new Relation(id, intFrom, intTo);
                flotsam.setPrior(rs.getInt("prior"));
                flotsam.setCount(rs.getInt("count"));
                relations.add(flotsam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return relations;
    }

    @Override
    public MatchResultSet searchWord(Word word) {
        MatchResultSet mrs = matcher.getMatchingWordList(word, getAllWords(word.getLanguage()));
        return mrs;
    }

    @Override
    public Word getWordById(int id, String language) {
        int count = 0;
        int prior = 0;
        String description = "-";
        ResultSet rs = null;
        String name = "";
        String[] par = {Integer.toString(id)};
        rs = dbq.query("SELECT * FROM wordlist_" + language + " WHERE id = ? ", par);

        try {
            if (rs.next() == true) {
                id = rs.getInt("id");
                name = rs.getString("name");
                prior = rs.getInt("prior");
                count = rs.getInt("count");
                description = rs.getString("description");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Word responseWord = new Word(id, name, language);
        responseWord.setPrior(prior);
        responseWord.setCount(count);
        responseWord.setDescription(description);
        return responseWord;
    }

    @Override
    public ArrayList<Word> getWordList(String name, String language) {

        ArrayList<Word> wordList = new ArrayList<>();

        ResultSet rs = null;
        String[] par = {name};

        //System.out.println("SELECT * FROM wordlist_" + language + " WHERE name = "+name);

        rs = dbq.query("SELECT * FROM wordlist_" + language + " WHERE name = ? ", par);

        try {
            while (rs.next() == true) {

                int id = rs.getInt("id");
                name = rs.getString("name");
                Word flotsam = new Word(id, name, language);
                flotsam.setDescription(rs.getString("description"));
                if (flotsam.getDescription().equals("")) {
                    flotsam.setDescription("-");
                }
                flotsam.setPrior(rs.getInt("prior"));
                flotsam.setCount(rs.getInt("count"));
                wordList.add(flotsam);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wordList;

    }

    @Override
    public ArrayList<Relation> getRelation(Word word, String from, String to) {
        ArrayList<Relation> relationList = new ArrayList<>();
        String[] par = {Integer.toString(word.getId())};
        ResultSet rs = dbq.query("SELECT * FROM rel_" + from + "_" + to + " WHERE idFrom = ? ", par);
        try {
            while (rs.next()) {
                int id = rs.getInt("id");
                int idFrom = rs.getInt("idFrom");
                int idTo = rs.getInt("idTo");
                Relation relation = new Relation(id, idFrom, idTo);
                relation.setCount(rs.getInt("count"));
                relation.setPrior(rs.getInt("prior"));
                relationList.add(relation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (relationList.size() > 1) {
            relationList.sort((relation1, relation2) -> relation1.compareTo(relation2));
        }

        return relationList;
    }

    @Override
    public ArrayList<Word> getWordFromRelation(Word word, String languageFrom, String languageTo) {
        ArrayList<Relation> relations = getRelation(word,languageFrom,languageTo);
        ArrayList<Word> words = new ArrayList<>();
        for(Relation r : relations){
            words.add(getWordById(r.getIdTo(),languageTo));
        }
        return words;
    }

    @Override
    public int getRelationFromId(int id, String fromLanguage, String toLanguage) {
        return 0;
    }

    @Override
    public void removeRelation(Relation relation, String fromLanguage, String toLanguage) {

    }

    @Override
    public void removeWord(Word word, String language) {

    }


    @Override
    public boolean newLanguage(String language) {
        String[] languagePar = {language};
        ResultSet rs = dbq.query("SELECT * FROM languages WHERE name = ? ", languagePar);

        try {
            if (rs.next()) {
                return false;
            } else {
                String[] par = new String[0];
                rs = dbq.query("SELECT * FROM languages", par);

                while (rs.next()) {
                    String name = rs.getString("name");
                    dbq.queryUpdate("CREATE TABLE rel_" + language + "_" + name + " "
                            + "(id INT NOT NULL AUTO_INCREMENT,"
                            + "idFrom INT NOT NULL,"
                            + "idTo INT NOT NULL,"
                            + "prior INT NOT NULL,"
                            + "count INT NOT NULL,"
                            + "PRIMARY KEY (id));", par);

                    dbq.queryUpdate("CREATE TABLE rel_" + name + "_" + language + " "
                            + "(id INT NOT NULL AUTO_INCREMENT,"
                            + "idFrom INT NOT NULL,"
                            + "idTo INT NOT NULL,"
                            + "prior INT NOT NULL,"
                            + "count INT NOT NULL,"
                            + "PRIMARY KEY (id));", par);
                }

                dbq.queryUpdate("CREATE TABLE wordlist_" + language + " "
                        + "(id INT NOT NULL AUTO_INCREMENT,name VARCHAR(100) NULL,"
                        + "description VARCHAR(100) NULL,"
                        + "prior INT NOT NULL, "
                        + "count INT NOT NULL, "
                        + "PRIMARY KEY (id));", par);
                dbq.queryUpdate("CREATE TABLE rel_" + language + "_" + language + " "
                        + "(id INT NOT NULL AUTO_INCREMENT,"
                        + "idFrom INT NOT NULL,"
                        + "idTo INT NOT NULL,"
                        + "prior INT NOT NULL,"
                        + "count INT NOT NULL,"
                        + "PRIMARY KEY (id));", par);
                dbq.queryUpdate("INSERT INTO languages VALUES (0,'" + language + "' )", par);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public boolean updateTables() {
        return false;
    }

    @Override
    public int getLastWordId(String language) {
        String query = "SELECT MAX(id) FROM wordlist_" + language;
        return dbq.getLastWordId(query);
    }
/*
    @Override
    public ArrayList<String> translate(Translator translator, Word input) {

        Matcher matcher = new Matcher(iterateStrategy, distanceStrategy, sortStrategy);
        ArrayList<Word> allWords = getAllWords(translator.getFromLanguage());
        MatchResultSet mrs = matcher.getMatchingWordList(input,allWords);
        ArrayList<String> translations = new ArrayList<>();
        for(ArrayList<MatchResult> mrlist : mrs.getMatchResults()){
            if(mrlist.get(0).getScore() == 0){
               translations.add(getWordById(mrlist.get(0).getID(),translator.getFromLanguage()).getName());

            }
        }

        return translations;
    }
*/
    @Override
    public ArrayList<String> translate(Translator translator, Word input, ArrayList<Word> allWords, ArrayList<Relation> allRelation) {
        /**
         * Just translate input
         */
        ArrayList<String> translations = new ArrayList<>();
        translations.add(translator.translation(input.getName()));
        return translations;
        /*matcher = new Matcher(iterateStrategy, distanceStrategy, sortStrategy);
        MatchResultSet mrs = matcher.getMatchingWordList(input,allWords);
        ArrayList<String> translations = new ArrayList<>();
        for(ArrayList<MatchResult> mrlist : mrs.getMatchResults()){
            if(mrlist.get(0).getScore() == 0){
                translations.add(getWordById(mrlist.get(0).getID(),translator.getFromLanguage()).getName());

            }
        }

        return translations;
        */
    }

    //TODO: Output of Translations and single Wordlists // error if to many
    public String print(String language1, String language2){
        StringBuilder output = new StringBuilder();
        if (language1.equals(language2)){
            ArrayList<Word> allWords = getAllWords(language1);

            for(Word word : allWords){
                ArrayList<Word> allSynonyms = getWordFromRelation(word,language1,language2);
                output.append(word.getName()+": ");
                for(Word synonym : allSynonyms){
                    output.append(synonym.getName()+"/");
                }
                output.append("."+System.lineSeparator());
            }
        }
        return output.toString();
    }


}
/*





    public ArrayList<Integer> getClickCount(String table,ArrayList<Integer> idList){
        ArrayList<Integer> countList = new ArrayList<>();
        int c =0;
        for (int id : idList){
            String[] par = {Integer.toString(id)};
            ResultSet rs = query("SELECT * FROM "+table+" WHERE id = ? ",par);

            try {
                rs.next();
                c = rs.getInt("count");
                countList.add(c);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return countList;
    }

    public int getClickCount(String table, int id){
        String[] par = {Integer.toString(id)};
        ResultSet rs = query("SELECT * FROM "+table+" WHERE id = ? ",par);
        int c =0;
        try {
            rs.next();
            return c = rs.getInt("count");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public int addClickCount(String table, int id){
        String[] par1 = {Integer.toString(id)};
        ResultSet rs = query("SELECT * FROM "+table+" WHERE id = ? ",par1);
        int c =0;
        try {
            rs.next();
            c = rs.getInt("count");
            c++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] par2 = {Integer.toString(id)};
        queryUpdate("UPDATE "+table+" SET count = "+c+" WHERE id = ? ",par2);
        return c;
    }
 */
