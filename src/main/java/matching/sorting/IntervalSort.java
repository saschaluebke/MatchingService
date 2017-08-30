package matching.sorting;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import components.MatchResult;
import components.MatchResultSet;
import utils.Sorter;

import javax.swing.text.MutableAttributeSet;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sascha on 09.06.17.
 */
public class IntervalSort implements SortStrategy{
    Sorter sorter;

    public IntervalSort(){
        sorter = new Sorter("SIZE");
    }

    @Override
    public ArrayList<MatchResult> sort(ArrayList<MatchResult> results) {

        if(results.size()>1){
            sorter.matchResultQuickSort(results);
        }

        return results;
    }

    @Override
    public MatchResultSet sortList(MatchResultSet mrs) {
        sorter.matchResultSetQuicksort(mrs);
        return mrs;
    }

}
