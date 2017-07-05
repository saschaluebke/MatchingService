package matching.sorting;

import components.MatchResult;

import java.util.ArrayList;

public interface SortStrategy {
    public ArrayList<MatchResult> sort(ArrayList<MatchResult> results);
    public double getMin();
    public void setMin(double min);
    public double getMax();
    public void setMax(double max);
}
