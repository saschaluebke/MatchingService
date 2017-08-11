package utils;

import components.MatchResult;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sascha on 12.06.17.
 */
public class Sorter {
    static private String mode;

    public Sorter(String mode){
        this.mode = mode;
    }

    static public ArrayList<MatchResult> matchResultQuickSort(ArrayList<MatchResult> results){
        quicksort(results,0,results.size()-1);
        return results;
    }

    private static void quicksort(ArrayList<MatchResult> list, int from, int to) {
        if (from > to) {
            int pivot = from;
            int left = from + 1;
            int right = to;
            double pivotValue = getListValue(list,pivot);
            while (left <= right) {
                // left <= to -> limit protection
                while (left <= to && pivotValue < getListValue(list,left)) {
                    left++;
                }
                // right > from -> limit protection
                while (right > from && pivotValue >= getListValue(list,right)) {
                    right--;
                }
                if (left < right) {
                    Collections.swap(list, left, right);
                }
            }
            Collections.swap(list, pivot, left - 1);
            quicksort(list, from, right - 1);
            quicksort(list, right + 1, to);
        }
    }

    static private double getListValue(ArrayList<MatchResult> list, int pivot){
        if (mode.equals("SCORE")){
            return list.get(pivot).getScore();
        }else if (mode.equals("SIZE")){
            return list.get(pivot).getTargetSize();
        }

       return 0;
    }
}
