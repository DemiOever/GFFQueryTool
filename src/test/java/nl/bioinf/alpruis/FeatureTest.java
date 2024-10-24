package nl.bioinf.alpruis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FeatureTest {

    private String seqId;
    private String source;
    private String type;
    private int start;
    private int end;
    private String score;
    private String strand;
    private String phase;
    private Map<String, String> attributes;
    private List<String> children;

    @BeforeEach
    void setUp() {
        // Initialize the test data
        String seqId = "NC_088708.1";
        String source = "NCBI RefSeq GCF_963576615.1-RS_2024_08";
        String type = "RefSeq";
        int start = 1;
        int end = 79350317;
        String strand = "+";
    }

    @Test
    void featureTest() {
        Feature features = new Feature(
                seqId, source, type, start, end, score, strand, phase, attributes);

        assertEquals(seqId, features.getSeqId());
        assertEquals(source, features.getSource());
        assertEquals(type, features.getType());
        assertEquals(start, features.getStart());
        assertEquals(end, features.getEnd());
        assertEquals(strand, features.getStrand());
        assertEquals(attributes, features.getAttributes());
    }
}
