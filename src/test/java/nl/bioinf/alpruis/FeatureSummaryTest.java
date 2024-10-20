package nl.bioinf.alpruis;

import java.util.*;

import nl.bioinf.alpruis.operation.filter_ex_sum.FeatureSummary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class FeatureSummaryTest {

        private Long seqLength;
        private double percentageGC;
        private Map<String, Integer> countingFeatures;
        private Map<String, Integer> countingSources;
        private List<String> regions;
        private int countGenes;
        private long avgLengthGenes;
        private int forwardStrands;
        private int reverseStrands;

    private FeatureSummary featureSummary;

    @BeforeEach
    void setUp() {
        Long seqLength = 1000L;
        Long percentageGC = 50L;
        Map<String, Integer> countingFeatures = new HashMap<>();
        countingFeatures.put("exon", 20);
        countingFeatures.put("intron", 10);

        Map<String, Integer> countingSources = new HashMap<>();
        countingSources.put("sourceA", 15);
        countingSources.put("sourceB", 5);

        List<String> regions = Arrays.asList("region1", "region2", "region3");
        int countGenes = 5;
        long avgLengthGenes = 300;
        int forwardStrands = 10;
        int reverseStrands = 8;

        featureSummary = new FeatureSummary(seqLength, percentageGC, countingFeatures, countingSources, regions, countGenes, avgLengthGenes, forwardStrands, reverseStrands);
    }

    @Test
    public void toStringTest() {
        String expectedString = "FeatureSummary{avgSeqLength=1000, AvgPercentageGC=50.0, countingFeatures={intron=10, exon=20}, countingSources={sourceA=15, sourceB=5}, countGenes=5, avgLengthGenes=300, forwardStrands=10, reverseStrands=8, regions=[region1, region2, region3]}";
        assertEquals(expectedString, featureSummary.toString(),  "The toString output should match the expected summary.");
    }
}
