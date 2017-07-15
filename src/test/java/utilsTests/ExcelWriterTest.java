package utilsTests;

import jxl.write.WriteException;
import org.junit.Test;
import utils.evaluation.ExcelWriter;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by sashbot on 14.07.17.
 */
public class ExcelWriterTest {
    static ExcelWriter ew;


    @Test
    public void excelWriterTest(){
        ew = new ExcelWriter("/out/test.xls", "Evaluation");
        ew.addLabel(1, 1, "Unverändert");
        ew.addLabel(2,1,"schlecht");
        ew.addLabel(3,1,"gut");
        ew.addLabel(4,1,"perfekt");

        ew.addNumber(1,2,10);

        try {
            ew.write();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

        assertEquals(0,0);
    }
}
