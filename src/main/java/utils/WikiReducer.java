package utils;

import java.io.*;
import java.util.ArrayList;

/**
 * The Wiki files are perfect but to big for my system 2* 3,2 Ghz 8GB ram. I reduce the source
 */
public class WikiReducer {
    String germanPath, englishPath;
    ArrayList<String> germanWords,englishWords;

    public WikiReducer(String germanPath, String englishPath){
        this.germanPath = System.getProperty("user.dir")+germanPath;
        this.englishPath = System.getProperty("user.dir")+englishPath;
        germanWords = new ArrayList<>();
        englishWords = new ArrayList<>();
    }

    public void reduce(int lines){
        BufferedReader br = null; //find out with file -i <filename>
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(germanPath)),"UTF8"));
            int count=0;
       while(count<lines){
           count++;
           germanWords.add(br.readLine());
       }

        br.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(englishPath)),"UTF8"));
            int count=0;
            while(count<lines){
                count++;
                englishWords.add(br.readLine());
            }

            br.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        print("WikiReduce.en",englishWords);
        print("WikiReduce.de",germanWords);

    }

    public void print(String path ,ArrayList<String> lines){

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(path)));

            for(String line : lines){
                writer.write(line+System.lineSeparator());
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
