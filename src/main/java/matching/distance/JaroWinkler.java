package matching.distance;

import java.util.Arrays;

/**
 * Keine Metric!
 * https://github.com/tdebatty/java-string-similarity/blob/master/src/main/java/info/debatty/java/stringsimilarity/JaroWinkler.java
 */

public class JaroWinkler implements DistanceStrategy {

    private static final double DEFAULT_THRESHOLD = 0.7;
    private static final int THREE = 3;
    private static final double JW_COEF = 0.1;
    private final double threshold;

    /**
     * Instantiate with default threshold (0.7).
     *
     */
    public JaroWinkler() {
        this.threshold = DEFAULT_THRESHOLD;
    }


    @Override
    public double getDistance(String searchString,String dbString) {
        return 1.0 - similarity(searchString, dbString);
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

        int[] mtp = matches(s1, s2);
       // System.out.println(Arrays.toString(mtp));
        float m = mtp[0];
        if (m == 0) {
            return 0f;
        }
        double j = ((m / s1.length() + m / s2.length() + (m - mtp[1]) / m))
                / THREE;
        double jw = j;

        if (j > getThreshold()) {
            jw = j + Math.min(JW_COEF, 1.0 / mtp[THREE]) * mtp[2] * (1 - j);
        }
        return jw;
    }

    private int[] matches(final String s1, final String s2) {
        String max, min;
        if (s1.length() > s2.length()) {
            max = s1;
            min = s2;
        } else {
            max = s2;
            min = s1;
        }
        int range = Math.max(max.length() / 2 - 1, 0);
        int[] match_indexes = new int[min.length()];
        Arrays.fill(match_indexes, -1);
        boolean[] match_flags = new boolean[max.length()];
        int matches = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            char c1 = min.charAt(mi);
            for (int xi = Math.max(mi - range, 0),
                 xn = Math.min(mi + range + 1, max.length());
                 xi < xn;
                 xi++) {
                if (!match_flags[xi] && c1 == max.charAt(xi)) {
                    match_indexes[mi] = xi;
                    match_flags[xi] = true;
                    matches++;
                    break;
                }
            }
        }
        char[] ms1 = new char[matches];
        char[] ms2 = new char[matches];
        for (int i = 0, si = 0; i < min.length(); i++) {
            if (match_indexes[i] != -1) {
                ms1[si] = min.charAt(i);
                si++;
            }
        }
        for (int i = 0, si = 0; i < max.length(); i++) {
            if (match_flags[i]) {
                ms2[si] = max.charAt(i);
                si++;
            }
        }
        int transpositions = 0;
        for (int mi = 0; mi < ms1.length; mi++) {
            if (ms1[mi] != ms2[mi]) {
                transpositions++;
            }
        }
        int prefix = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            if (s1.charAt(mi) == s2.charAt(mi)) {
                prefix++;
            } else {
                break;
            }
        }
        int[] result = new int[]{matches, transpositions / 2, prefix, max.length()};
        return result;
    }


    /**
     * Instantiate with given threshold to determine when Winkler bonus should
     * be used.
     * Set threshold to a negative value to get the Jaro distance.
     * @param threshold
     */
    public JaroWinkler(final double threshold) {
        this.threshold = threshold;
    }

    /**
     * Returns the current value of the threshold used for adding the Winkler
     * bonus. The default value is 0.7.
     *
     * @return the current value of the threshold
     */
    public final double getThreshold() {
        return threshold;
    }
}
