package utils.ontology;

import components.Relation;
import components.Word;
import jxl.write.WriteException;
import utils.evaluation.ExcelWriter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sashbot on 05.08.17.
 */
public class OntologyAnalysis {
    private ArrayList<Word> allWords;
    private ArrayList<Relation> allRelations;
    private String name;
    private int wordsWithoutSyn;

    public OntologyAnalysis(String name, ArrayList<Word> allWords, ArrayList<Relation> allRelations){
        this.allRelations = allRelations;
        this.allWords = allWords;
        this.name = name;
    }

    private int getMaxSyn(int[] histogram){
        int maxSyn=0;
        for(int syn : histogram){
            if(syn>maxSyn){
                maxSyn = syn;
            }
        }
        return maxSyn;
    }

    public void printHistogramm(int[] histogram){
        ExcelWriter ew = new ExcelWriter("/out/"+name+".xls",name);
        ew.addLabel(0,0,"WordCount: ");
        ew.addNumber(1,0,getWordCount());
        ew.addLabel(2,0,"AverageSynPerWord:");
        ew.addLabel(3,0,String.valueOf(getAverageOfSynPerWord()));
        ew.addLabel(4,0,"Varianz:");
        ew.addLabel(5,0,String.valueOf(getVariance()));
       // ew.addLabel(6,0,"WordWithoutSynonym:");
       // ew.addNumber(7,0,getWordsWithoutSyn());

        for(int i=0; i<histogram.length;i++){
            ew.addNumber(0,i+2,i);
        }

        for(int i=0;i<histogram.length;i++){
            ew.addNumber(1,i+2,histogram[i]);
        }

        try {
            ew.write();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<Word> getAllWords() {
        return allWords;
    }

    public ArrayList<Relation> getAllRelations() {
        return allRelations;
    }

    public int[] scanOntologyForSynonyms() {
        int[] histogram = new int[1];
        int count=0;
        for (Word w : allWords) {
            count++;
            if(count%50 == 0){
                System.out.println(count +" of "+allWords.size());
            }


            int synCount = getSynCount(w);
            if (synCount >= histogram.length) {
                int[] tmp = histogram;
                histogram = new int[synCount + 1];
                for (int i = 0; i < tmp.length; i++) {
                    histogram[i] = tmp[i];
                }
                histogram[synCount] = histogram[synCount] + 1;
            } else {
                histogram[synCount] = histogram[synCount] + 1;
            }
        }
        return histogram;
    }

    public int getWordCount() {
        return allWords.size();
    }

    public int getSynCount(Word word) {
        int wordIndex = word.getId();
        Word w = allWords.get(wordIndex-1);
        int synCount=0;
        for(Relation r : allRelations){
            if (r.getIdFrom() == w.getId()){
                synCount++;
            }
        }

        return synCount;
    }

    public double getVariance() {
        double variance = 0;
        double varianceTmp = 0;
        double averageSynPerWord = getAverageOfSynPerWord();
        for(Word w : allWords){
            int synCount = getSynCount(w);

            varianceTmp = varianceTmp + (synCount - averageSynPerWord) * (synCount - averageSynPerWord);
        }
        variance = varianceTmp/(getWordCount()-1);
        return variance;
    }

    public double getAverageOfSynPerWord() {
        double synCounts=0;
        for(Word w : allWords){
            synCounts = synCounts +getSynCount(w);
        }
        return synCounts/(double)getWordCount();
    }

    public int getWordsWithoutSyn() {
        wordsWithoutSyn=0;
        for(Word w : allWords){
            if (getSynCount(w)==0){
                wordsWithoutSyn++;
            }
        }
        return wordsWithoutSyn;
    }
}
