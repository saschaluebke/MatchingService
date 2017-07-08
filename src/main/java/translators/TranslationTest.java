package translators;
import org.apache.xmlrpc.*;
import org.apache.xmlrpc.client.XmlRpcClient;

import java.util.Vector;

/**
 * Not a real Translater just for test reasons
 */
public class TranslationTest implements  Translator {

    @Override
    public String translation(String input) {
        if (input.equals("Glucose")){
            return "Glukose";
        }else if (input.equals("Sugar")){
            return "Zucker";
        }
        return "no Translation";
    }

    @Override
    public void setFromLanguage(String language) {

    }

    @Override
    public void setToLanguage(String language) {

    }

    @Override
    public String getFromLanguage() {
        return null;
    }

    @Override
    public String getToLanguage() {
        return null;
    }
}
