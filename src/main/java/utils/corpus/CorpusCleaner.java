package utils.corpus;

import java.io.*;
import java.util.ArrayList;

/**
 * There should be no braces or special signs in the corpus
 */
public class CorpusCleaner {
    static String[] specialSigns = {"[","{","]","}","*","=",")","(","\"",",","."};
    private String path;
    private ArrayList<String> lines;
    private int sentenceCount;


    public CorpusCleaner(String path){
        this.path = System.getProperty("user.dir")+path;
        lines = new ArrayList<>();
    }

    public int clean(String modus){
        String encoding = "UTF-8";
        int maxlines = 100;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        int count = 0;
        try {

                reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "Cleaned"), encoding));

            for (String line; (line = reader.readLine()) != null;) {
                count++;

                if (modus.equals("lowerCase")){
                    line = line.toLowerCase(); //TODO better design!
                }

                if (modus.equals("specialSigns")){
                    line = line.toLowerCase();
                    for(String sign : specialSigns){
                        line = line.replace(sign,"");
                    }
                }

                if (modus.equals("braces")){
                    line = line.toLowerCase();
                    line = cleanString(line);
                }

                writer.write(line);
                writer.newLine();
            }

            writer.close();
            reader.close();

        } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public String lowerCaseCorpus(){
        return "lowerCase";
    }

    public String specialSigns(){
        return "specialSigns";
    }

    public String braces(){
        return "braces";
    }

    private void getData(){
        BufferedReader br = null;
        try {
            br = new BufferedReader(new java.io.FileReader(new File(path)));
            String line = "";
            while((line = br.readLine()) != null){
                lines.add(line);
                sentenceCount++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void writeNewFile(){

        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new FileWriter(new File(path+"Cleaned")));
            for(String line : lines){
                bw.write(line+System.lineSeparator());
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String cleanString(String input) {
        input = input.toLowerCase();

        int bracketStart = input.indexOf("[");
        int curlyStart = input.indexOf("{");
        int bracketEnd = input.indexOf("]")+1;
        int curlyEnd = input.indexOf("}")+1;
        int braceStart = -1;
        int braceEnd = 0;
        if((bracketStart != -1 && bracketEnd != 0 && bracketEnd>bracketEnd) ||
                (curlyStart != -1 && curlyEnd != 0 && curlyEnd>curlyStart)){
            do {
                if (bracketStart == -1){
                    bracketStart = Integer.MAX_VALUE;
                }
                if(curlyStart == -1){
                    curlyStart = Integer.MAX_VALUE;
                }
                if(bracketStart<curlyStart){
                    braceStart = bracketStart;
                    braceEnd = bracketEnd;
                }else if (curlyStart<bracketStart){
                    braceStart = curlyStart;
                    braceEnd = curlyEnd;
                }else{
                    break;
                }

                if (braceStart != -1) {
                    if (braceEnd != 0) {
                        String before = input.substring(0, braceStart);
                        before = before.trim();
                        String after = "";
                        if(braceEnd<input.length() && braceEnd > braceStart){
                            after = input.substring(braceEnd, input.length());
                            after = after.trim();
                        }

                        input = before+" " + after;
                    }
                }

                bracketStart = input.indexOf("[");
                curlyStart = input.indexOf("{");
                bracketEnd = input.indexOf("]")+1;
                curlyEnd = input.indexOf("}")+1;
            }while ((bracketStart != -1 && bracketEnd != 0) || (curlyStart != -1 && curlyEnd != 0));
        }


        for(String sign : specialSigns){
            input = input.replace(sign,"");
        }
        input = input.trim();

        return input;
    }

    public int getSentenceCount() {
        return sentenceCount;
    }
}
