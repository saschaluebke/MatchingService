package matching.distance;

/**
 * Created by sashbot on 06.08.17.
 */
public class SubstringDistance implements DistanceStrategy {
    @Override
    public double getDistance(String searchString, String dbString) {
        int index;
        if(searchString.length()>dbString.length()){
            index = searchString.indexOf(dbString);
        }else{
            index = dbString.indexOf(searchString);
        }
        if (index>-1){
            return 0;
        }else{
            return 1;
        }
    }
}
