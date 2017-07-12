package evaluation;

import org.junit.Test;
import utils.ICD10EvaluationMaker;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 10.07.17.
 */
public class ICD10EvaluationMakerTest {
    ICD10EvaluationMaker em;

    @Test
    public void makeInputFiles(){
        String input = "/src/main/resources/evaluation/ICD10/icd10.txt";
        String output = "/src/main/resources/evaluation/ICD10/icd10InputVersion1.txt";
        em = new ICD10EvaluationMaker(input,output);
        ArrayList<Integer> lines = new ArrayList<>();
        for(int i=0; i<3 ;i++){
            em.setOutput("/src/main/resources/evaluation/ICD10/icd10InputVersion"+(i+1)+".txt");
            lines.add(em.generateICDFile(400+i));
        }


        assertEquals(179,(int)lines.get(0));
        assertEquals(178,(int)lines.get(1));
        assertEquals(178,(int)lines.get(2));
    }
}
