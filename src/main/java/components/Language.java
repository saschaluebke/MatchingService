package components;

/**
 * Created by Saschbot on 13.03.2017.
 */
public class Language {
    private String name;
    private String fullName;





    public Language(String name, String fullName){
        this.name = name;
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }



}
