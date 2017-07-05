package matching.iterate;

import components.MatchResult;
import components.Word;
import matching.distance.DistanceStrategy;

import java.util.ArrayList;

public interface IterateStrategy {
    void setWordFromDB(Word wordFromDB);
    void setSearchString(String searchString);
    ArrayList<MatchResult> getMatchList(DistanceStrategy distanceStrategy);
}
