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



        //TODO: This is just a Dirty initial call
        MySQLQuery dbq = new MySQLQuery();
        DBHelper dbh = new DBHelper(new SimpleStrategy());
        dbq.dropAllTables();
        dbq.truncate("languages");
        Transltr th = new Transltr();
        for (Language l:th.getLanguages()){
            dbh.newLanguage(l.getName());
        }


        SpringApplication.run(AppConfig.class, args);
   }

}
