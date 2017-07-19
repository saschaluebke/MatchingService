package components;

/**
 * Created by sashbot on 18.07.17.
 */
public class SimpleTranslationResult implements TranslationResult {

    Word input;

    public SimpleTranslationResult(Word input){
     this.input = input;
    }

    @Override
    public Word getInput() {
        return input;
    }
}
