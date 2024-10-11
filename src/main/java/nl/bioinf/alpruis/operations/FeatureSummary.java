package nl.bioinf.alpruis.operations;

import java.util.List;
import java.util.Map;

public class FeatureSummary {
    private long seqLength;
    private double percentageGC;
    private Map<String, Integer> countingFeatures;
    private List<String> regions;
    private int countGenes;
    private long avgLengthGenes;
    private int forwardStrands;
    private int reverseStrands;

    public FeatureSummary(long seqLength, double percentageGC, Map<String, Integer> countingFeatures, List<String> regions, int countGenes, long avgLengthGenes, int forwardStrands, int reverseStrands) {
        this.seqLength = seqLength;
        this.percentageGC = percentageGC;
        this.countingFeatures = countingFeatures;
        this.regions = regions;
        this.countGenes = countGenes;
        this.avgLengthGenes = avgLengthGenes;
        this.forwardStrands = forwardStrands;
        this.reverseStrands = reverseStrands;
    }

    // Getters for each field if needed
    public double getPercentageGC() {
        return percentageGC;
    }

    public long getSeqLength() {
        return seqLength;
    }

    public Map<String, Integer> getCountingFeatures() {
        return countingFeatures;
    }

    public List<String> getRegions() {
        return regions;
    }

    public int getCountGenes() {
        return countGenes;
    }

    public long getAvgLengthGenes() {
        return avgLengthGenes;
    }

    public int getForwardStrands() {
        return forwardStrands;
    }

    public int getReverseStrands() {
        return reverseStrands;
    }

    @Override
    public String toString() {
        return "FeatureSummary{" +
                "seqLength=" + seqLength +
                ", percentageGC=" + percentageGC +
                ", countingFeatures=" + countingFeatures +
                ", countGenes=" + countGenes +
                ", avgLengthGenes=" + avgLengthGenes +
                ", forwardStrands=" + forwardStrands +
                ", reverseStrands=" + reverseStrands +
                ", regions=" + regions +
                '}';
    }
}
