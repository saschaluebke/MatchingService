package utils.evaluation;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by sashbot on 26.07.17.
 */
public class EvaluationInputCleaner {
    static String[] specialSigns = {"[", "{", "]", "}", "*", "=", ")", "(", ",", ".", ":", "“", "„"};
    private String path;
    private ArrayList<String> lines;

    public EvaluationInputCleaner(String path) {
        this.path = System.getProperty("user.dir") + path;
        lines = new ArrayList<>();
    }

    public int cleanInput() {
        String encoding = "UTF-8";
        BufferedReader reader1 = null;
        BufferedWriter writer1 = null;
        int count = 0;
        try {

            reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(path), encoding));


            writer1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path + "Cleaned"), encoding));
            String line;
            for (; (line = reader1.readLine()) != null; ) {
                count++;

                line = line.toLowerCase();
                for (String sign : specialSigns) {
                    line = line.replace(sign, "");
                }


                writer1.write(line+System.lineSeparator());
            }

            writer1.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }
}
