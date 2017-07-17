package utils.corpus;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by sashbot on 15.07.17.
 */
public class ICD10CorpusHelper implements CorpusHelper{
    private String path;

    public ICD10CorpusHelper(String path){
        setPath(path);
    }

    @Override
    public void setPath(String path) {
        path = System.getProperty("user.dir")+path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public ArrayList<String> searchCorpusAfter(String input) {
        String encoding = "UTF-8";
        BufferedReader reader = null;
        try {

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));
            for (String line; (line = reader.readLine()) != null;) {
                if (line.contains(input)){
                    //TODO
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; //TODO
    }
}
