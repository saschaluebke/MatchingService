package matching.distance;

/**
 * No Metric!
 * https://github.com/tdebatty/java-string-similarity/blob/master/src/main/java/info/debatty/java/stringsimilarity/NormalizedLevenshtein.java
 *
 */
public class LevenshteinNormalized implements DistanceStrategy {
    private final Levenshtein l = new Levenshtein();

    @Override
    public double getDistance(String s1, String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 0;
        }

        int m_len = Math.max(s1.length(), s2.length());

        if (m_len == 0) {
            return 0;
        }
        double lev = l.getDistance(s1,s2);
        double result = l.getDistance(s1,s2) /m_len;
        return l.getDistance(s1, s2) / m_len;
    }
}
