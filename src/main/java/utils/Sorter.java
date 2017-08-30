package utils;

import components.MatchResult;
import components.MatchResultSet;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by sascha on 12.06.17.
 */
public class Sorter {
    public String mode;

    public Sorter(String mode){
        this.mode = mode;
    }

    public MatchResultSet matchResultSetQuicksort(MatchResultSet mrs){
      ArrayList<ArrayList<MatchResult>> matchResults = mrs.getMatchResults();
        quicksort2(matchResults, 0,matchResults.size()-1);
      return mrs;
    }

     public ArrayList<MatchResult> matchResultQuickSort(ArrayList<MatchResult> results){
        quicksort(results,0,results.size()-1);
        return results;
    }

    private void quicksort2(ArrayList<ArrayList<MatchResult>> list, int from, int to) {
        if (from <= to) {
            int pivot = from;
            int left = from + 1;
            int right = to;
            double pivotValue = getListValue2(list,pivot);
            while (left <= right) {
                // left <= to -> limit protection
                while (left <= to && pivotValue < getListValue2(list,left)) {
                    left++;
                }
                // right > from -> limit protection
                while (right > from && pivotValue >= getListValue2(list,right)) {
                    right--;
                }
                if (left < right) {
                    Collections.swap(list, left, right);
                }
            }
            Collections.swap(list, pivot, left - 1);
            quicksort2(list, from, right - 1);
            quicksort2(list, right + 1, to);
        }
    }


    private void quicksort(ArrayList<MatchResult> list, int from, int to) {
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

     private double getListValue(ArrayList<MatchResult> list, int pivot){
        if (mode.equals("SCORE")){
            return list.get(pivot).getScore();
        }else if (mode.equals("SIZE")){
            return list.get(pivot).getSourceSize();
        }

       return 0;
    }

    private double getListValue2(ArrayList<ArrayList<MatchResult>> list, int pivot){
        if (mode.equals("SCORE")){
            return list.get(pivot).get(0).getScore();
        }else if (mode.equals("SIZE")){
            int target = list.get(pivot).get(0).getTargetSize();
            int source = list.get(pivot).get(0).getSourceSize();
            if(target > source){
                return 100-(target-source);
            }else{
                return 100-(source-target);
            }
        }

        return 0;
    }
}
