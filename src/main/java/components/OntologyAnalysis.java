package components;

/**
 * Created by sashbot on 05.08.17.
 */
public class OntologyAnalysis {
    private int wordCount,synCount,wordsWithoutSyn;
    private double varianz,averageOfSynPerWord;


    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public int getSynCount() {
        return synCount;
    }

    public void setSynCount(int synCount) {
        this.synCount = synCount;
    }

    public double getVarianz() {
        return varianz;
    }

    public void setVarianz(double varianz) {
        this.varianz = varianz;
    }

    public double getAverageOfSynPerWord() {
        return averageOfSynPerWord;
    }

    public void setAverageOfSynPerWord(double averageOfSynPerWord) {
        this.averageOfSynPerWord = averageOfSynPerWord;
    }

    public int getWordsWithoutSyn() {
        return wordsWithoutSyn;
    }

    public void setWordsWithoutSyn(int wordsWithoutSyn) {
        this.wordsWithoutSyn = wordsWithoutSyn;
    }
}
