package matching.sorting;

import components.MatchResult;
import components.MatchResultSet;
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
        ArrayList<MatchResult> sortedResults = sorter.matchResultQuickSort(results);
        return sortedResults;
    }

    @Override
    public MatchResultSet sortList(MatchResultSet mrs) {
       return  sorter.matchResultSetQuicksort(mrs);
    }

}
