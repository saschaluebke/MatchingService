package utils.corpus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class SpringerCorpusMaker{
    File[] listOfEnglishFiles, listOfGermanFiles;

    public SpringerCorpusMaker(String pathEnglish, String pathGerman){
        File englishFolder = new File(pathEnglish);
        File germanFolder = new File(pathGerman);
        File[] rawListOfEnglishFiles = englishFolder.listFiles();
        File[] rawListOfGermanFiles = germanFolder.listFiles();
        listOfEnglishFiles = equalise(rawListOfEnglishFiles,rawListOfGermanFiles)[0];
        listOfGermanFiles = equalise(rawListOfEnglishFiles,rawListOfGermanFiles)[1];
        if (listOfEnglishFiles.length == listOfGermanFiles.length) {

        }else{
                System.out.println("SpringerMaker: List of EnglishFiles("+listOfEnglishFiles.length+") != " +
                        "List of GermanFiles("+listOfGermanFiles.length+")");
            }
    }

    /**
     * Write all Chunk file in one Line for english and german
     * @return
     */
    public void getFileContent() {
        ArrayList<String> listOfEnglishLines = new ArrayList<>();
        ArrayList<String> listOfGermanLines = new ArrayList<>();
        try {
            for (int i = 0; i < listOfEnglishFiles.length; i++) {
                //for English


                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(listOfEnglishFiles[i]),"iso-8859-1")); //find out with file -i <filename>
                String line = br.readLine();
                listOfEnglishLines.add(line);
                br.close();

                //for German
                br = new BufferedReader(new InputStreamReader(new FileInputStream(listOfGermanFiles[i]),"iso-8859-1"));
                line = br.readLine();
                listOfGermanLines.add(line);

                br.close();
            }
            if (listOfEnglishLines.size()== listOfGermanLines.size()){
                print("SpringerChunk.en", listOfEnglishLines);
                print("SpringerChunk.de", listOfGermanLines);
            }else{
                System.out.println(listOfEnglishLines.size()+ " != " +listOfGermanLines.size());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


        /**
     *  Frite all Lines that match in an english and german File
     * @return output lines (must be equal for parallel text corpus)
     */
    public int getFileContentByLine(){
        ArrayList<String> listOfEnglishLines = new ArrayList<>();
        ArrayList<String> listOfGermanLines = new ArrayList<>();

        //Get English and German Files and Put them sentence for sentence into one File
            for (int i = 0; i < listOfEnglishFiles.length; i++) {
                ArrayList<String> newEnglishLines = getLinesFromFile(listOfEnglishFiles[i]);
                ArrayList<String> newGermanLines = getLinesFromFile(listOfGermanFiles[i]);

                if(newEnglishLines.size()==newGermanLines.size()){

                    listOfEnglishLines.addAll(newEnglishLines);
                    listOfGermanLines.addAll(newGermanLines);
                }else{
                    continue;
                }
            }
            if (listOfEnglishLines.size() == listOfGermanLines.size()){
                print("SpringerPlain.en",listOfEnglishLines);
                print("SpringerPlain.de",listOfGermanLines);
            }else{
                System.out.println("SpringerMaker: List of EnglishLines("+listOfEnglishLines.size()+") != " +
                        "List of GermanLines("+listOfEnglishLines.size()+")");
            }

        return listOfEnglishLines.size();

    }

    private ArrayList<String> getLinesFromFile(File file) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            String line = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"iso-8859-1")); //find out with file -i <filename>


            line = br.readLine();
            while (line.indexOf(".") > 0) {
                lines.add(line.substring(0, line.indexOf(".")));
                line = line.substring(line.indexOf(".")+1);

            }

            return lines;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private void print(String path ,ArrayList<String> lines){

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

    //EnglishFiles = 7823; GermanFiles = 7808 so you have to sort the Files that doesn't match
    private File[][] equalise(File[] firstArray, File[] secondArray){
        File[][] newFiles = new File[2][];
        List<File> first = new ArrayList<>();
        List<File> second = new ArrayList<>();

        for(File f1 : firstArray){
            File taken1 = null;
            File taken2 = null;
            for(File f2 : secondArray){
                String s1 = f1.getName();
                String s2 = f2.getName();

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
