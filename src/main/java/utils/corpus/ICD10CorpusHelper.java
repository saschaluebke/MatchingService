package utils.corpus;

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
        return null; //TODO
    }
}
