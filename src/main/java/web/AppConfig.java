package web;

import components.Language;
import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import translators.Transltr;

@EnableAutoConfiguration
@ComponentScan
public class AppConfig {
    public static void main(String[] args) {

        MySQLQuery dbq = new MySQLQuery();
        DBHelper dbh = new DBHelper(new SimpleStrategy());
        dbq.dropAllTables();
        dbq.truncate("languages");
        dbh.newLanguage("de");
        dbh.newLanguage("en");
        SpringApplication.run(AppConfig.class, args);
   }

}
