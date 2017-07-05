package matching.distance;


/**
 * http://heim.ifi.uio.no/%7Edanielry/StringMetric.pdf
 * https://github.com/tdebatty/java-string-similarity/blob/master/src/main/java/info/debatty/java/stringsimilarity/MetricLCS.java
 *
 */
public class LongestCommonSub implements  DistanceStrategy {
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
        return 1.0
                - (1.0 * length(s1, s2))
                / m_len;
    }

    public final int length(final String s1, final String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        /* function LCSLength(X[1..m], Y[1..n])
         C = array(0..m, 0..n)
         for i := 0..m
         C[i,0] = 0
         for j := 0..n
         C[0,j] = 0
         for i := 1..m
         for j := 1..n
         if X[i] = Y[j]
         C[i,j] := C[i-1,j-1] + 1
         else
         C[i,j] := max(C[i,j-1], C[i-1,j])
         return C[m,n]
         */
        int s1_length = s1.length();
        int s2_length = s2.length();
        char[] x = s1.toCharArray();
        char[] y = s2.toCharArray();

        int[][] c = new int[s1_length + 1][s2_length + 1];

        for (int i = 1; i <= s1_length; i++) {
            for (int j = 1; j <= s2_length; j++) {
                if (x[i - 1] == y[j - 1]) {
                    c[i][j] = c[i - 1][j - 1] + 1;

                } else {
                    c[i][j] = Math.max(c[i][j - 1], c[i - 1][j]);
                }
            }
        }

        return c[s1_length][s2_length];
    }
}
