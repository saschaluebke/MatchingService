package utilsTests.fileReaderTest;

import database.DBHelper;
import database.dbStrategy.simpleStrategy.SynonymStrategy;
import org.junit.BeforeClass;
import org.junit.Test;
import utils.ontology.OntologyAnalysis;

import static org.junit.Assert.assertEquals;

public class OntologyAnalysisTest {
    static DBHelper dbh;

    @BeforeClass
    public static void onceExecutedBeforeAll() {
    dbh = new DBHelper(new SynonymStrategy());

    }

    @Test
    public void varianceTest(){
        OntologyAnalysis ov = new OntologyAnalysis("Openthesaurus",dbh.getAllWords("de"),dbh.getAllRelations("de","de"));
        assertEquals(6,ov.getAverageOfSynPerWord(),1.0);
        assertEquals(66,ov.getVariance(),1.0);

    }

    @Test
    public void onlyOpenThesarus(){
        OntologyAnalysis ov = new OntologyAnalysis("Openthesaurus",dbh.getAllWords("de"),dbh.getAllRelations("de","de"));
        int[] histogram = ov.scanOntologyForSynonyms();
        ov.printHistogramm(histogram);
        assertEquals(92,histogram.length);

    }

    @Test
    public void onlyWordNet(){
        OntologyAnalysis ov = new OntologyAnalysis("WordNet",dbh.getAllWords("en"),dbh.getAllRelations("en","en"));
        int[] histogram = ov.scanOntologyForSynonyms();
        ov.printHistogramm(histogram);
        assertEquals(19,histogram.length);
        assertEquals(19,histogram[15]);
        assertEquals(328,histogram[0]);

    }

    @Test
    public void onlyNCI(){
        OntologyAnalysis ov = new OntologyAnalysis("NCI",dbh.getAllWords("en"),dbh.getAllRelations("en","en"));
        int[] histogram = ov.scanOntologyForSynonyms();
        ov.printHistogramm(histogram);
        assertEquals(92,histogram.length);

    }

    @Test
    public void onlySpecialist(){
        OntologyAnalysis ov = new OntologyAnalysis("Specialist",dbh.getAllWords("en"),dbh.getAllRelations("en","en"));
        int[] histogram = ov.scanOntologyForSynonyms();
        ov.printHistogramm(histogram);
        assertEquals(19,histogram.length);
        assertEquals(19,histogram[15]);
        assertEquals(328,histogram[0]);

    }



}
