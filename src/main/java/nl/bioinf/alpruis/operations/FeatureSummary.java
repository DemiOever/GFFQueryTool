package nl.bioinf.alpruis.operations;

import java.util.List;
import java.util.Map;

public class FeatureSummary {
    private Long seqLength;
    private double percentageGC;
    private Map<String, Integer> countingFeatures;
    private List<String> regions;
    private int countGenes;
    private long avgLengthGenes;
    private int forwardStrands;
    private int reverseStrands;

    public FeatureSummary(Long seqLength, double percentageGC, Map<String, Integer> countingFeatures, List<String> regions, int countGenes, long avgLengthGenes, int forwardStrands, int reverseStrands) {
        this.seqLength = seqLength;
        this.percentageGC = percentageGC;
        this.countingFeatures = countingFeatures;
        this.regions = regions;
        this.countGenes = countGenes;
        this.avgLengthGenes = avgLengthGenes;
        this.forwardStrands = forwardStrands;
        this.reverseStrands = reverseStrands;
    }

    @Override
    public String toString() {
        return "FeatureSummary{" +
                "avgSeqLength=" + seqLength +
                ", AvgPercentageGC=" + percentageGC +
                ", countingFeatures=" + countingFeatures +
                ", countGenes=" + countGenes +
                ", avgLengthGenes=" + avgLengthGenes +
                ", forwardStrands=" + forwardStrands +
                ", reverseStrands=" + reverseStrands +
                ", regions=" + regions +
                '}';
    }
}
