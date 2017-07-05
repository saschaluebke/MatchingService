package database.dbStrategy.complexStrategy;

import components.MatchResultSet;
import components.Relation;
import components.Word;
import database.MySQLQuery;
import database.dbStrategy.DBStrategy;
import matching.Matcher;
import matching.distance.LevenshteinNormalized;
import matching.iterate.PerformanceStrategy;
import matching.sorting.ScoreSort;
import translators.Translator;
import utils.FileReader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ComplexStrategy implements DBStrategy {
    private final int MAXCHARS = 100;
    private Matcher matcher;
    MySQLQuery dbq;

    public ComplexStrategy(){
        dbq = new MySQLQuery();
        matcher = new Matcher(new PerformanceStrategy(),new LevenshteinNormalized(),new ScoreSort());

        if (!dbq.isTable("languages")){
            dbq.queryUpdate("CREATE TABLE languages"
                    + " (id INT NOT NULL AUTO_INCREMENT,name VARCHAR(45) NULL,"
                    + "PRIMARY KEY (id));",new String[0]);
        }else{
            //TODO: search for all other tables necessary
        }
    }

    @Override
    public int putWord(Word word) {
        int id = 0;
        ResultSet rs = null;
        try {

            if ((word.getName().length()>MAXCHARS||word.getDescription().length()>MAXCHARS)){
                return -1; //TODO: Dirty code please clean up!
            }

            String[] par1 ={word.getName(),word.getDescription(),Integer.toString(word.getPrior()),Integer.toString(word.getCount()),"0",""};
            dbq.queryUpdate("INSERT INTO wordlist_"+word.getLanguage()+" VALUES (0, ?, ?, ?, ?, ?, ?)",par1);
            String[] par2 ={word.getName()};
            rs = dbq.query("SELECT * FROM wordlist_"+word.getLanguage()+" WHERE name = ? ",par2);

            rs.next();
            id = rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public int putRelation(Word word1, Word word2) {
        return 0;
    }

    @Override
    public void putRelationList(ArrayList<Relation> relations, String language1, String language2) {

    }

    @Override
    public int takeFromFileReader(FileReader fr) {
        return 0;
    }

    @Override
    public void putWordList(ArrayList<Word> wordList, String language) {

    }

    @Override
    public ArrayList<Word> getAllWords(String language) {
        return null;
    }

    @Override
    public ArrayList<Relation> getAllRelations(String languageFrom, String languageTo) {
        return null;
    }

    @Override
    public MatchResultSet searchWord(Word word) {
        return null;
    }

    @Override
    public Word getWordById(int id, String language) {
        return null;
    }

    @Override
    public ArrayList<Word> getWordList(String name, String language) {
        return null;
    }

    @Override
    public ArrayList<Relation> getRelation(Word word, String from, String to) {
        return null;
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

    //TODO: Alle größenangaben in Properties...
    @Override
    public boolean newLanguage(String language) {
        String[] languagePar = {language};
        ResultSet rs = dbq.query("SELECT * FROM languages WHERE name = ? ",languagePar);

        try {
            if(rs.next()){
                return false;
            }else{
                String[] par = new String[0];
                rs = dbq.query("SELECT * FROM languages",par);

                while(rs.next()){
                    String name = rs.getString("name");
                    dbq.queryUpdate("CREATE TABLE rel_"+language+"_"+name+" "
                            + "(id INT NOT NULL AUTO_INCREMENT,"
                            + "id_"+language+" INT NOT NULL,"
                            + "id_"+name+" INT NOT NULL,"
                            + "prior INT NOT NULL,"
                            + "count INT NOT NULL,"
                            + "PRIMARY KEY (id));",par);

                    dbq.queryUpdate("CREATE TABLE rel_"+name+"_"+language+" "
                            + "(id INT NOT NULL AUTO_INCREMENT,"
                            + "id_"+name+" INT NOT NULL,"
                            + "id_"+language+" INT NOT NULL,"
                            + "prior INT NOT NULL,"
                            + "count INT NOT NULL,"
                            + "PRIMARY KEY (id));",par);
                }

                dbq.queryUpdate("CREATE TABLE wordlist_"+language+" "
                        + "(id INT NOT NULL AUTO_INCREMENT,name VARCHAR(100) NULL,"
                        + "description VARCHAR(100) NULL,"
                        + "prior INT NOT NULL, "
                        + "count INT NOT NULL, "
                        + "updateflag INT NOT NULL, "
                        + "composed VARCHAR(100) NOT NULL, "
                        + "PRIMARY KEY (id));",par);
                dbq.queryUpdate("CREATE TABLE rel_"+language+"_"+language+" "
                        + "(id INT NOT NULL AUTO_INCREMENT,"
                        + "id_"+language+" INT NOT NULL,"
                        + "id_"+language+"2 INT NOT NULL,"
                        + "prior INT NOT NULL,"
                        + "count INT NOT NULL,"
                        + "PRIMARY KEY (id));",par);
                dbq.queryUpdate("INSERT INTO languages VALUES (0,'"+language+"' )",par);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public boolean updateTables(){
        return false;
    }

    @Override
    public int getLastWordId(String query) {
        return 0;
    }

    @Override
    public MatchResultSet translate(Translator translator, Word input) {
        return null;
    }
}
