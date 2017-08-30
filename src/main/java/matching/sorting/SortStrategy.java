package matching.sorting;

import components.MatchResult;
import components.MatchResultSet;

import java.util.ArrayList;

public interface SortStrategy {
    public ArrayList<MatchResult> sort(ArrayList<MatchResult> results);
    MatchResultSet sortList(MatchResultSet mrs);
}
