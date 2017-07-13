package utils;

import java.util.ArrayList;

/**
 * Moses doesn't like all kinds of special Signs!
 */

//TODO: nur rausnehmen nicht abschneiden!
 public  class StringCleaner {
     static String[] specialSigns = {"[","]","{","}"};

     public static String clean(String input){
         for(String sign : specialSigns){
             int curlyStart = input.indexOf(sign);
             if(curlyStart != -1){
                 input = input.substring(0,curlyStart);
                 input = input.trim();
             }
         }
         return input;
     }

}
