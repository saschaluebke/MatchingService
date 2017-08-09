package matching.sorting;

import components.MatchResult;
import utils.Sorter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sascha on 09.06.17.
 */
public class ScoreSort implements SortStrategy {
    Sorter sorter;

    public ScoreSort(){
        sorter = new Sorter("SCORE");
    }

    @Override
    public ArrayList<MatchResult> sort(ArrayList<MatchResult> results) {
        ArrayList<MatchResult> sortedResults = new ArrayList<>();

        return sorter.matchResultQuickSort(results);
    }

    /**
     * Interval of min max size makes no sense when only sorting by score!
     */
    @Override
    public double getMin() {
        return 0;
    }

    @Override
    public void setMin(double min) {

    }

    @Override
    public double getMax() {
        return 0;
    }

    @Override
    public void setMax(double max) {

    }
}
