package utilsTests;

import database.DBHelper;
import database.MySQLQuery;
import database.dbStrategy.DBStrategy;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.TSVFileReader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * Created by sashbot on 10.07.17.
 */
public class TSVFileReaderTest {
    static TSVFileReader tsvReader;

    @Test
    public void fileReaderTest(){
        tsvReader = new TSVFileReader("/src/main/resources/TSV/source.tsv","en");
        ArrayList<String> source = tsvReader.getFiles();
        tsvReader = new TSVFileReader("/src/main/resources/TSV/target.tsv","en");
        ArrayList<String> target = tsvReader.getFiles();

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("source")));

            for(String s: source){
                bw.write(s+System.lineSeparator());
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File("target")));

            for(String t: target){
                bw.write(t+System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
