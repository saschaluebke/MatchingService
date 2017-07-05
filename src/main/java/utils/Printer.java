package utils;

import components.Word;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Printer {

    public void print(String path1, ArrayList<Word> firstWordList, String path2, ArrayList<Word> secondWordList){
        try {
            PrintWriter out = new PrintWriter(path1);
            for(Word w : firstWordList){
                out.println(w.getName());
            }
            PrintWriter out2 = new PrintWriter(path2);
            for(Word w : secondWordList){
                out2.println(w.getName());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
