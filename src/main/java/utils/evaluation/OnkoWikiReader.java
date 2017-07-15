package utils.evaluation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by sashbot on 11.07.17.
 */
public class OnkoWikiReader {

    public ArrayList<String> getLines(String path){
        ArrayList<String> output = new ArrayList<>();
        path = System.getProperty("user.dir")+path;
        try {
            for(Scanner sc = new Scanner(new File(path)); sc.hasNext(); ) {
                String line = sc.nextLine();
                line = line.replace("\t","");
                if(line.equals("")){

                }else{
                    output.add(line);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return output;
    }

    }

