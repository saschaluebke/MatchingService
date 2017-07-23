package utils.evaluation;

import java.io.*;
import java.util.ArrayList;

/**
 * After a Translation I would know which line(s) in the Corpus give Moses the hint to translate a word in a specific manner
 * So I get the input and search for it in the right corpus. Then I Print out all of these lines and its translations
 */
public class CorpusViewer {
    private String path1, path2;

    public CorpusViewer(String path1, String path2) {
        this.path1 = System.getProperty("user.dir") + path1;
        this.path2 = System.getProperty("user.dir") + path2;
    }
/*
    public void printWordFromCorpus(String word){
        ArrayList<String> lines = getLinesWithWord(word);
        if(lines.size()<1){
            System.out.println("No word found in "+path1);
        }else{
            //getLinesWithWord put a line from path2 after a line from path1
            for(int i=0; i<lines.size();i++){
                String line1 = lines.get(i);
                i++;
                String line2 = lines.get(i);
                System.out.println(line1+" --> "+line2);
            }
        }
    }*/

    //TODO: Spalten im zweiten einfügen 1,2,3.. nach wörtern und dann die anzahl der gefundenen wörter eintragen

    public ArrayList<ArrayList<String>> getLinesWithWord(String word) {
        String[] words = word.split(" ");
        String encoding = "UTF-8";
        ArrayList<ArrayList<String>> lines = new ArrayList<>();
        BufferedReader reader1;
        BufferedReader reader2;
        try {
            reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(path1), encoding));
            reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(path2), encoding));
            String line1;
            String line2;
            int lineNumber = 0;
            for (String w : words) {
                ArrayList<String> wordLine = new ArrayList<>();

               // if (w.length() > 4) {

                    try {
                        for (; (line1 = reader1.readLine()) != null; ) {
                            lineNumber++;

                            line2 = reader2.readLine();
                            if (line1.contains(w)) {
                                line1 = lineNumber + ": " + line1;
                                wordLine.add(line1);
                                wordLine.add(line2);
                            }
                        }
                        lines.add(wordLine);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
          //  }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
