package matching;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sashbot on 10.08.17.
 */
public class MatchEvaluator {
    public ArrayList<String> getInput() {
        return input;
    }

    private ArrayList<String> input;

    public MatchEvaluator(String path) {
        input = new ArrayList<>();
        System.out.println(System.getProperty("user.dir") + path);
        BufferedReader br = null;
        try {
            br = new BufferedReader((new FileReader(System.getProperty("user.dir") + path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String thisLine = "";
        try {
            while ((thisLine = br.readLine()) != null) {
                input.add(thisLine);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void scaleUp(int multiplier){
        ArrayList<String> newInput = new ArrayList<>();
        for(int i=1; i<multiplier;i++){
            for(String s : input){
                newInput.add(s+s);
            }
        }

        input = newInput;

    }

}
