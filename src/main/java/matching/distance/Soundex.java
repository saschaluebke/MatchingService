package matching.distance;

public class Soundex implements DistanceStrategy {

    private String getSoundex(String s){
        char[] x = s.toUpperCase().toCharArray();
        char firstLetter = x[0];

        // convert letters to numeric code
        for (int i = 0; i < x.length; i++) {
            switch (x[i]) {

                case 'B':
                case 'F':
                case 'P':
                case 'V':
                    x[i] = '1';
                    break;

                case 'C':
                case 'G':
                case 'J':
                case 'K':
                case 'Q':
                case 'S':
                case 'X':
                case 'Z':
                    x[i] = '2';
                    break;

                case 'D':
                case 'T':
                    x[i] = '3';
                    break;

                case 'L':
                    x[i] = '4';
                    break;

                case 'M':
                case 'N':
                    x[i] = '5';
                    break;

                case 'R':
                    x[i] = '6';
                    break;

                default:
                    x[i] = '0';
                    break;
            }
        }

        // remove duplicates
        String output = "" + firstLetter;
        for (int i = 1; i < x.length; i++)
            if (x[i] != x[i-1] && x[i] != '0')
                output += x[i];

        // pad with 0's or truncate
        output = output + "0000";
        return output.substring(0, 4);
    }

    @Override
    public double getDistance(String searchString, String dbString) {
        searchString = getSoundex(searchString);
        dbString = getSoundex(dbString);

        int len0 = searchString.length() + 1;
        int len1 = dbString.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (searchString.charAt(i - 1) == dbString.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }


}
