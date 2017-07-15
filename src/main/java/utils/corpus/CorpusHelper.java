package utils.corpus;

import java.util.ArrayList;

/**
 * Created by sashbot on 15.07.17.
 */
public interface CorpusHelper {

    void setPath(String path);
    String getPath();
    ArrayList<String> searchCorpusAfter(String input); //Possible LineNumber + Output
}
