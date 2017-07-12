package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by SashBot on 12.07.2017.
 */
public class FileScanner {
    private String path;
    private int wordCount, sentenceCount;


    public FileScanner(String path){
        this.path = System.getProperty("user.dir")+path;
    }

    public void loadData(){
        wordCount = 0;
        sentenceCount = 0;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new java.io.FileReader(new File(path)));
       String line;
       while((line = br.readLine()) != null) {

           while(line.length()>0){
               if (line.indexOf(" ")>-1){
                   line = line.substring(line.indexOf(" ")+1);

               }else{
                  line = "";
               }
               wordCount++;

           }

           sentenceCount++;
       }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getWordCount() {
        return wordCount;
    }

    public int getLines(){
        return sentenceCount;
    }
}
