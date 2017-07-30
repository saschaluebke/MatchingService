package utilsTests.evaluationTests;

import org.junit.Test;
import utils.evaluation.EvaluationInputCleaner;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 26.07.17.
 */
public class EvaluationInputCleanerTest {
    static EvaluationInputCleaner eic;

    @Test
    public void evaluation() {
       eic = new EvaluationInputCleaner("/src/main/resources/evaluation/ICD10/icd10InputVersion1.txt");
       eic.cleanInput();
        eic = new EvaluationInputCleaner("/src/main/resources/evaluation/ICD10/icd10InputVersion2.txt");
        eic.cleanInput();
        eic = new EvaluationInputCleaner("/src/main/resources/evaluation/ICD10/icd10InputVersion3.txt");
        eic.cleanInput();
        assertEquals(true, true);
    }
}
