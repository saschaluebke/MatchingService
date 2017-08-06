package utils.ontology;

import java.util.ArrayList;

/**
 * Created by sashbot on 05.08.17.
 */
public class OntologyCleaner {

    public ArrayList<String> cleanLinesFromBraces(ArrayList<String> input){
        ArrayList<String> output = new ArrayList<>();
        for(String line : input){
            output.add(cleanFromBraces(line,"(",")"));
        }


        return  output;
    }

    private String cleanFromBraces(String input, String braceOpen, String braceClosed){
        int braceStart = input.indexOf(braceOpen);
        int braceEnd = input.indexOf(braceClosed);
        while(braceStart!=-1||braceEnd!=-1){
            String before = "";
            String after = "";
            if (braceStart<braceEnd){
                if(braceStart<1){
                    if(braceStart==-1){
                        break; //No open Brace!
                    }
                }else{
                    before= before = input.substring(0, braceStart);
                }
                before = before.trim();
                if(braceEnd+1<input.length()){
                    after = input.substring(braceEnd+1, input.length());
                    after = after.trim();
                }
                input = before+" " + after;
                input = input.trim();
            }else{
                if(braceEnd<1){
                    if(braceEnd==-1){
                        break;//No closed Brace!
                    }

                    after=input.substring(1,input.length());
                }else{
                    before=input.substring(0,braceEnd);
                    if(braceEnd+1<input.length()){
                        after = input.substring(braceEnd+1,input.length());
                    }

                }



            }
            input = before+after;
            braceStart = input.indexOf(braceOpen);
            braceEnd = input.indexOf(braceClosed);
        }

        return input;
    }
}
