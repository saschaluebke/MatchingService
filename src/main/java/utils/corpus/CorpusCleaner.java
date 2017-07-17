package utils.corpus;

import java.io.*;
import java.util.ArrayList;

/**
 * There should be no braces or special signs in the corpus
 */
public class CorpusCleaner {
    static String[] specialSigns = {"[","{","]","}","*","=",")","(","\"",",","."};
    private String path1,path2;
    private ArrayList<String> lines;
    private int sentenceCount;


    public CorpusCleaner(String path1,String path2){
        this.path1 = System.getProperty("user.dir")+path1;
        this.path2 = System.getProperty("user.dir")+path2;
        lines = new ArrayList<>();
    }

    public int cleanCorpus(String modus){
        String encoding = "UTF-8";
        BufferedReader reader1 = null;
        BufferedReader reader2 = null;
        BufferedWriter writer1 = null;
        BufferedWriter writer2 = null;
        int count = 0;
        try {

            reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(path1), encoding));
            reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(path2), encoding));

            writer1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path1 + "Cleaned"), encoding));
            writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path2 + "Cleaned"), encoding));
            String line1;
            String line2;
            for (; (line1 = reader1.readLine()) != null;) {
                count++;
                line2= reader2.readLine();

                line1 = cleanLine(line1,modus);
                line2 = cleanLine(line2,modus);

                //Just skip line when both lines are too short or there are more numbers than letters
                if((line1.length()>= 3 && line2.length()>= 3 ) && (letterOverNumbers(line1) && letterOverNumbers(line2))){
                    writer1.write(line1);
                    writer1.newLine();
                    writer2.write(line2);
                    writer2.newLine();
                }
            }

            writer1.close();
            reader1.close();
            writer2.close();
            reader2.close();
        } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private boolean letterOverNumbers(String line){
        int letters=0;
        int numbers=0;
        for(char c : line.toCharArray()){
            String s = String.valueOf(c);
             //match a number with - and or decimal
            if(s.matches("-?\\d+(\\.\\d+)?")){
                numbers++;
            }else{
                letters++;
            }
        }
        if(letters>=numbers){
            return true;
        }else{
            return false;
        }
    }

    private String cleanLine(String line, String modus){
        if (modus.equals("lowerCase")){
            line = line.toLowerCase();
        }

        if (modus.equals("specialSigns")){
            line = line.toLowerCase();
            for(String sign : specialSigns){
                line = line.replace(sign,"");
            }
        }

        if (modus.equals("braces")){
            line = line.toLowerCase();
            line = cleanBraces(line);
        }

        return line;
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
/*
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
*/
    private String cleanBraces(String input) {
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
