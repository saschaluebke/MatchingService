package translators;

import components.Language;

/**
 * Created by sascha on 05.07.17.
 */
public interface Translator {

    String translation(String input);
    void setFromLanguage(String language);
    void setToLanguage(String language);
    String getFromLanguage();
    String getToLanguage();

}
