package matching.sorting;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import components.MatchResult;
import utils.Sorter;

import javax.swing.text.MutableAttributeSet;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sascha on 09.06.17.
 */
public class IntervalSort implements SortStrategy{
    Sorter sorter;
    private double min, max;

    public IntervalSort(double min, double max){
        sorter = new Sorter("Size");
        this.setMax(max);
        this.setMin(min);
    }

    @Override
    public ArrayList<MatchResult> sort(ArrayList<MatchResult> results) {
        ArrayList<MatchResult> sortedResults = new ArrayList<>();
        for (MatchResult mr : results){
            if (mr.getScore() > getMin() && mr.getScore() < getMax()){
                sortedResults.add(mr);
            }

            sorter.matchResultQuickSort(sortedResults);
        }
        return sortedResults;
    }

    @Override
    public double getMin() {
        return min;
    }

    @Override
    public void setMin(double min) {
        this.min = min;
    }

    @Override
    public double getMax() {
        return max;
    }

    @Override
    public void setMax(double max) {
        this.max = max;
    }
}
