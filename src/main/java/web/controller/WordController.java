package web.controller;

import components.Language;
import components.Relation;
import components.Word;
import database.DBHelper;
import database.dbStrategy.simpleStrategy.SimpleStrategy;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import translators.MosesClient;
import translators.Transltr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * TODO: Wie Seite Designen vorallem die Ergebnisse. Vielleicht in anderen Dateiformaten zugänglich machen?
 */
//@Controller
public class WordController {
    static Logger log = Logger.getLogger(WordController.class.getName());


    //@RequestMapping(value="/translated", method=RequestMethod.POST)
    public String recoverPass(@RequestParam Map<String, String> params, @RequestParam("word") String word, @RequestParam("to") String to, @RequestParam("from") String from,
                              @RequestParam("request") String request, @RequestParam("translation") String translate, @RequestParam("descPut") String descPut,
                              @RequestParam("wordPut") String wordPut, @RequestParam("fromPut") String fromPut, @RequestParam("prior") int prior,
                              @RequestParam("newDescPut")String newDescPut, final ModelMap modelMap) {

        //System.out.println(params.toString());
        DBHelper dbh = new DBHelper(new SimpleStrategy());
        Transltr translator = new Transltr();
        MosesClient mosesClient = new MosesClient();
        translator.setFromLanguage(from);
        translator.setToLanguage(to);

        //Create Language Lists
        List<Language> languages =  translator.getLanguages();
        List<Language> languageFrom =  new ArrayList<>();
        for (int i = 0;i<languages.size();i++){
            if (languages.get(i).getName().equals(from)){
                languageFrom.add(languages.get(i));
            }
        }
        languageFrom.addAll(languages);
        modelMap.addAttribute("languageFrom", languageFrom);

        List<Language> languageTo =  new ArrayList<>();
        for (int i = 0;i<languages.size();i++){
            if (languages.get(i).getName().equals(to)){
                languageTo.add(languages.get(i));
            }
        }
        languageTo.addAll(languages);
        modelMap.addAttribute("languageTo", languageTo);

        List<Language> languagePut =  new ArrayList<>();
        for (int i = 0;i<languages.size();i++){
            if (languages.get(i).getName().equals(fromPut)){
                languagePut.add(languages.get(i));
            }
        }
        languagePut.addAll(languages);
        modelMap.addAttribute("languagePut", languagePut);


        //Create translationModeList
        List<String> translationModes = new ArrayList<>();
        //translationList.add("Transltr"); Not online anymore!
        translationModes.add("Database");
        translationModes.add("Moses");
        List<String> translationList = new ArrayList<>();
        for (int i = 0;i<translationModes.size();i++){
            if (translationModes.get(i).equals(translate)){
                translationList.add(translationModes.get(i));
            }
        }
        translationList.addAll(translationModes);

        //execute Actions
        Word selectedWord = new Word (0,"!No Word found!","en");
        ArrayList<Word> wordList = new ArrayList<>();
        Word initialWord = new Word(0,word,from);
        Word putIn = new Word(0,wordPut,fromPut);
        putIn.setDescription(descPut);
        //if (request.equals("cache list")){

            //TODO Prior nachträglich verändern... übersetzungsliste verändern...

       // }
         if (request.equals("Put Word")){
            putIn.setId(dbh.putWord(putIn));
            selectedWord = putIn;
        }else if(request.equals("Get Word")) {
            wordList = dbh.getWordList(putIn.getName(),fromPut);

            if (wordList.size()>0){
                selectedWord = wordList.get(0);
            }else{
                selectedWord = new Word(0,"!No Word found!","en");
            }
        }else {
            if (translate.equals("Transltr")){
                String translation = translator.translate(word);
                selectedWord = new Word(0,translation,to);
                wordList.add(selectedWord);
                //TODO: Was soll passieren wenn das Wort schon in der Datenbank ist? und Beschreibung gleich....
                //TODO: Was passiert mit dem initial Wort (Problem wenn es das noch nicht gibt...) Neues Fenster?
                if (request.equals("cache result")){
                     initialWord.setId(dbh.putWord(initialWord));
                    selectedWord.setPrior(prior);
                    selectedWord.setDescription(newDescPut);
                    selectedWord.setId(dbh.putWord(selectedWord));
                    dbh.putRelation(initialWord,selectedWord);
                    //TODO auch die andere Relation und welche prior da?
                }
            }else if(translate.equals("Moses")) {
                String translation = mosesClient.translation(word);
                selectedWord = new Word(0,translation,to);
                wordList.add(selectedWord);
                if (request.equals("cache result")){
                    initialWord.setId(dbh.putWord(initialWord));
                    selectedWord.setPrior(prior);
                    selectedWord.setDescription(newDescPut);
                    selectedWord.setId(dbh.putWord(selectedWord));
                    dbh.putRelation(initialWord,selectedWord);
                    //TODO auch die andere Relation und welche prior da?
                }
            }else
             {
                if (request.equals("cache result")){
                    selectedWord = new Word(0,"!Only After Translation Request!","en");

                }else{
                    //TODO: was ist wenn es viele Homonyme gibt? -> Auch nach Description sehen.
                    //TODO: Was soll wie sortiert werden?
                    ArrayList<Word> allWords =  dbh.getWordList(word,from);


                    ArrayList<Relation> relationList = new ArrayList<>();
                    for(Word w : allWords){
                         relationList.addAll(dbh.getRelation(w,from,to));
                    }
                    relationList.sort((relation1, relation2) -> relation1.compareTo(relation2));
                    if (relationList.size()<1){
                        //No translation found
                    }else{
                        selectedWord = dbh.getWordById(relationList.get(0).getIdFrom(),to);
                        for(Relation r:relationList){
                            wordList.add(dbh.getWordById(r.getIdTo(),to));
                        }
                    }
                }

            }

        }

        modelMap.addAttribute("translationList", translationList);
        modelMap.addAttribute("initialWord",initialWord);
        modelMap.addAttribute("to",to);
        modelMap.addAttribute("from",from);
        modelMap.addAttribute("word",selectedWord);
        modelMap.addAttribute("wordList",wordList);

        return "home";
    }


  /*  @RequestMapping("/translated")
    public String translate(@PathVariable String name, ModelMap modelMap) {
        Word word = wordRepo.findByName(name);
        modelMap.put("word",word);
        return "home";
    }
*/
   // @RequestMapping("/")
    public String home(ModelMap modelMap) {
        /*
        Initialization for Home - page
         */
        Transltr translator = new Transltr();

    //Initialize language Dropdown Menues
        List<Language> languages =  translator.getLanguages();
        List<Language> languageFrom =  new ArrayList<>();
        languageFrom.add(new Language("de","German"));
        languageFrom.addAll(languages);
        modelMap.addAttribute("languageFrom", languageFrom);
        List<Language> languageTo =  new ArrayList<>();
        languageTo.add(new Language("en","English"));
        languageTo.addAll(languages);
        modelMap.addAttribute("languageTo", languageTo);
        List<Language> languagePut = new ArrayList<>();
        languagePut.addAll(languages);
        modelMap.addAttribute("languagePut", languagePut);

        //Inizialize TranslationMode List
        List<String> translationList = new ArrayList<>();
        //translationList.add("Transltr"); Not online anymore!
        translationList.add("Database");
        translationList.add("Moses");
        modelMap.addAttribute("translationList", translationList);


        modelMap.addAttribute("to","en");
        modelMap.addAttribute("from","de");
        modelMap.addAttribute("prior",0);
        Word w = new Word(0,"-","en");
        modelMap.addAttribute("word",w);
        modelMap.addAttribute("initialWord",w);
        ArrayList<Word> wordList = new ArrayList<>();
        wordList.add(w);
        modelMap.addAttribute("wordList",wordList);
        return "home";
    }

}
