package nl.bioinf.alpruis;

import java.util.*;

import nl.bioinf.alpruis.operation.filter_ex_sum.FeatureSummary;
import nl.bioinf.alpruis.operation.filter_ex_sum.FileSummarizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

//public class FileSummarizerTest {
//
//    @Test
//    void averageLengthTest() {
//        Map<String, String> sequence = new HashMap<>();
//        sequence.put("first", "AGCTGGCTGA");
//
//        // Call method
//        Long actualAverage = FileSummarizer.averageLength(sequence);
//
//        // Expected
//        Long expectedAverage = 10L;
//
//        // Assert
//        assertEquals(expectedAverage, actualAverage);
//    }
//
//    @Test
//    void gettingGcPercentageTest() {
//        Map<String, String> seq = new HashMap<>();
//        seq.put("chr1", "AGCTGGCTGA");
//
//        // Call method
//        double actualPercentage = FileSummarizer.gettingGcPercentage(seq);
//
//        // Expected
//        double expectedPercentage = 60.0;
//
//        assertEquals(expectedPercentage, actualPercentage);
//    }
//
//    @Test
//    void summarizeFeaturesTest() {
//        // New ArrayList
//        List<Feature> features = new ArrayList<>();
//        Map<String, String> sequence = new HashMap<>();
//
//        // New Map for attributes and add attributes to featureAttributes
//        Map<String, String> featureAttributes = new HashMap<>();
//        featureAttributes.put("ID", "Chromosome");
//
//        // Put some items in features and sequence
//        features.add(new Feature(
//                "gene", "source1", "chr1", 100, 200, ".", "+", "1", featureAttributes));
//        sequence.put("chr1", "AGCTGGCTGA");
//
//        // Call summarizeFeature
//        FeatureSummary result = FileSummarizer.summarizeFeatures(features, sequence);
//
//        // Assert
//        assertEquals(0, result.getCountGenes());
//        assertEquals(0, result.getAvgLengthGenes());
//        assertEquals(1, result.getForwardStrands());
//        assertEquals(0, result.getReverseStrands());
//        //assertTrue(result.getRegions().contains("chr1"));
//    }
//}

public class FileSummarizerTest {

    private Map<String, String> sequences;
    private List<Feature> features;

    @BeforeEach
    public void setUp() {
        sequences = new HashMap<>();
        sequences.put(">seq1", "ATGCATGC");
        sequences.put(">seq2", "GCGCGCGC");
        features = new ArrayList<>();
        Map<String, String> featureAttributes = new HashMap<>();

        // Set testdata
        featureAttributes.put("ID", "Chromosome");
        features.add(new Feature("gene", "sourceA", "gene", 100, 200, ".", "+", "0", featureAttributes));
        features.add(new Feature("gene", "sourceA", "gene", 300, 500, ".", "-", "1", featureAttributes));
        features.add(new Feature("exon", "sourceB", "exon", 150, 180, ".", "+", "2", featureAttributes));
        features.add(new Feature("intron", "sourceC", "intron", 0, 1000, ".", "-", "0", featureAttributes));
        features.add(new Feature("exon", "sourceC", "gene", 0, 1500, ".", "+", "1", featureAttributes));
    }

    @Test
    public void testSummarizeFeatures() {
        // Call the summarizeFeatures method
        FeatureSummary summary = FileSummarizer.summarizeFeatures(features, sequences);

        // Expected values based on the setup data
        Map<String, Integer> expectedFeatureCounts = new HashMap<>();
        expectedFeatureCounts.put("intron", 1);
        expectedFeatureCounts.put("gene", 3);
        expectedFeatureCounts.put("exon", 1);

        Map<String, Integer> expectedSourceCounts = new HashMap<>();
        expectedSourceCounts.put("sourceA", 2);
        expectedSourceCounts.put("sourceB", 1);
        expectedSourceCounts.put("sourceC", 2);

        //List<String> expectedRegions = Arrays.asList("chr1", "chr2");

        // Assert statements to verify the feature summary results
        assertEquals(3, summary.getCountGenes());
        assertEquals(600, summary.getAvgLengthGenes());
        assertEquals(3, summary.getForwardStrands());
        assertEquals(2, summary.getReverseStrands());
        //assertEquals("ch", summary.getRegions());
        assertEquals(expectedFeatureCounts, summary.getCountingFeatures());
        assertEquals(expectedSourceCounts, summary.getCountingSources());
        assertEquals(8, summary.getSeqLength());
        assertEquals(75, summary.getPercentageGC(), 0.01);

    }
}
