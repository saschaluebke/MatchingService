package matching.distance;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by sascha on 12.06.17.
 */
public class Jaccard extends ShingleBased implements DistanceStrategy {

    /**
     * Each input string is converted into a set of n-grams, the Jaccard index is
     * then computed as |V1 inter V2| / |V1 union V2|.
     * Like Q-Gram distance, the input strings are first converted into sets of
     * n-grams (sequences of n characters, also called k-shingles), but this time
     * the cardinality of each n-gram is not taken into account.
     * Distance is computed as 1 - cosine similarity.
     * Jaccard index is a metric distance.
     * @author Thibault Debatty
     */

    public Jaccard(final int k) {
        super(k);
    }

    public Jaccard() {
        super();
    }

    @Override
    public double getDistance(String s1, String s2) {
        return 1.0 - similarity(s1, s2);
    }

    public final double similarity(final String s1, final String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 1;
        }

        Map<String, Integer> profile1 = getProfile(s1);
        Map<String, Integer> profile2 = getProfile(s2);


        Set<String> union = new HashSet<String>();
        union.addAll(profile1.keySet());
        union.addAll(profile2.keySet());

        int inter = profile1.keySet().size() + profile2.keySet().size()
                - union.size();

        return 1.0 * inter / union.size();
    }
}
