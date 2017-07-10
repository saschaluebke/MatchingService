package utils;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by sashbot on 10.07.17.
 */
public class ICD10EvaluationMaker {
    private String path, output;

    public ICD10EvaluationMaker(String path, String output){
        this.path = path;
        this.output = output;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }

    public int generateICDFile(int modulo){
        ArrayList<String> lines = new ArrayList<>();
        int mainCount=0;
        try {
            BufferedReader br = new BufferedReader(new java.io.FileReader(new File(System.getProperty("user.dir")+path)));
            String line;
            int count=0;

            while ((line = br.readLine()) != null) {
                count++;
                if(count%modulo==0){
                    line = line.substring(8); //there are 8 chars for Identifiers in the data (not needed here).
                    lines.add(line);
                    mainCount++;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedWriter bw = new BufferedWriter((new FileWriter(new File(System.getProperty("user.dir")+output))));
            for(String line : lines){
                bw.write(line+System.lineSeparator());
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mainCount;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
