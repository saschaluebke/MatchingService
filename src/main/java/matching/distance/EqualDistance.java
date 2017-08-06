package matching.distance;

/**
 * Created by sashbot on 06.08.17.
 */
public class EqualDistance implements DistanceStrategy {
    @Override
    public double getDistance(String searchString, String dbString) {
       if(searchString.equals(dbString)){
           return 0;
       }else{
           return 1;
       }
    }
}
