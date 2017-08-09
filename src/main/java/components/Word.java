package components;

import java.util.ArrayList;

public class Word {
    private String name;
    private int id;
    private String description="-";     //To distinguish homonyms
    private String language;
    private int prior=0;
    private int count=0;

    public Word(int id,String name, String language){
        setWord(name);
        setLanguage(language);
        setId(id);
    }

    public Word(int id,String name, String description, String language){
        setWord(name);
        setLanguage(language);
        setId(id);
        setDescription(description);
    }

    public int getPrior() {
        return prior;
    }

    public void setPrior(int prior) {
        this.prior = prior;
    }


    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getName() {
        return name;
    }
    public void setWord(String name) {
        this.name = name;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<String> getVarialbes(){
        ArrayList<String> variables = new ArrayList<>();
        variables.add("name"); variables.add(getName());
        variables.add("id"); variables.add(Integer.toString(getId()));
        variables.add("description"); variables.add(getDescription());
        variables.add("language"); variables.add(getLanguage());
        variables.add("cout"); variables.add(Integer.toString(getCount()));
        return variables;
    }

    @Override
    public String toString(){
        return getName();
    }
}