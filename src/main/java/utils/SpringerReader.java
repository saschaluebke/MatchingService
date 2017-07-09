package utils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * No file Reader because it creates only training tables for moses!
 */
public class SpringerReader {
    private static String path;

    public SpringerReader(String path){
        this.path = path;
    }

    public static File[] sortFromDirectory(String dir){
        File f = new File(dir);
        File[] fileArray = f.listFiles();
        Arrays.sort(fileArray);
        return fileArray;
    }

    public static void getSpringerXML(String textfile, File[] fileArray) throws IOException, JDOMException {
        FileWriter fw = new FileWriter(textfile);
        BufferedWriter bw = new BufferedWriter(fw);

        for(File f1 : fileArray){
            path = f1.getAbsolutePath();
            SAXBuilder jdomBuilder = new SAXBuilder();

            // jdomDocument is the JDOM2 Object
            Document jdomDocument1 = jdomBuilder.build(path);
            Element rss1 = jdomDocument1.getRootElement();
            List<Element> channel1 = rss1.getChildren("sentence");


            for (Element elements : channel1) {
                List<Element> textList = elements.getChild("text").getChildren();
                for (Element a : textList) {
                    bw.write(a.getContent().get(0).getValue()+" ");
                }
                bw.newLine();
            }
        }


        bw.close();
        System.out.println(textfile+" created!");
    }

    public static File[][] equalise(File[] firstArray, File[] secondArray){
        File[][] newFiles = new File[2][];
        List<File> first = new ArrayList<>();
        List<File> second = new ArrayList<>();

        for(File f1 : firstArray){
            File taken1 = null;
            File taken2 = null;
            for(File f2 : secondArray){
                String s1 = f1.getName();
                String s2 = f2.getName();

 /* //Nur die Zahlen verwenden
                  String s1Cut1=s1.substring(s1.indexOf(".")+1,s1.length());
                    String s1Cut2 = s1Cut1.substring(0,s1Cut1.indexOf("."));
                    String s2Cut1=s2.substring(s2.indexOf(".")+1,s2.length());
                    String s2Cut2 = s2Cut1.substring(0,s2Cut1.indexOf("."));*/

                //Titel und Zahlen
                String s1Cut0=s1.substring(0,s1.indexOf("."));
                String s1Cut1=s1.substring(s1.indexOf(".")+1,s1.length());
                String s1Cut2 = s1Cut1.substring(0,s1Cut1.indexOf("."));
                s1 = s1Cut0 + s1Cut2;

                String s2Cut0 = s2.substring(0,s2.indexOf("."));
                String s2Cut1=s2.substring(s2.indexOf(".")+1,s2.length());
                String s2Cut2 = s2Cut1.substring(0,s2Cut1.indexOf("."));
                s2 = s2Cut0 + s2Cut2;
                if (s1.equals(s2)){
                    taken1 = f1;
                    taken2 = f2;
                }
            }
            if (taken1 != null && taken2 != null){
                first.add(taken1);
                second.add(taken2);
            }
        }
        File[] newFirstArray = new File[first.size()];
        for(int i=0;i<first.size();i++){
            newFirstArray[i]=first.get(i);
        }
        File[] newSecondArray = new File[second.size()];
        for(int i=0;i<second.size();i++){
            newSecondArray[i]=second.get(i);
        }


        newFiles[0] = newFirstArray;
        newFiles[1] = newSecondArray;
        if (newFiles[0].length == newFiles[1].length){
            return newFiles;
        }else{
            return null;
        }

    }

}

/*
File[] englishFile = sortFromDirectory("/home/sascha/IdeaProjects/Springer2/resources2/SpringerEnglish");
            File[] germanFile = sortFromDirectory("/home/sascha/IdeaProjects/Springer2/resources2/SpringerGerman");

            File[][] files = equalise(englishFile,germanFile);
            englishFile = files[0];
            germanFile = files[1];

           getSpringerXML("SpringerCorpusEnglish.txt",englishFile);
            getSpringerXML("SpringerCorpusGerman.txt",germanFile);

 */

/* Not needed
     public static File[] getCutName(String sourceFile){
            File f = new File(sourceFile);
            File[] fileArray = f.listFiles();
            for(File f1 : fileArray){
                String s = f1.getName();
                //System.out.println(s);
                String s1=s.substring(s.indexOf(".")+1,s.length());
                //System.out.println(s1);
                String s2 = s1.substring(0,s1.indexOf("."));
                //System.out.println(s2);

                Path source = f1.toPath();
                try {
                    Files.move(source, source.resolveSibling(s2));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //Arrays.sort(fileArray);
            return fileArray;
        }

        public static File[] sortFileArray(File[] fileArray){
            Arrays.sort(fileArray);
            return fileArray;
        }
 */
