package nl.bioinf.alpruis.operations;

import java.util.List;
import java.util.Map;

/**
 * The FeatureSummary class provides a summary of various statistics related to genomic features,
 * such as sequence length, GC content, feature counts, gene counts, strand orientation, and more.
 */
public class FeatureSummary {
    private Long seqLength;
    private double percentageGC;
    private Map<String, Integer> countingFeatures;
    private Map<String, Integer> countingSources;
    private List<String> regions;
    private int countGenes;
    private long avgLengthGenes;
    private int forwardStrands;
    private int reverseStrands;

    /**
     * Constructor for the FeatureSummary class.
     *
     * @param seqLength The length of the sequence.
     * @param percentageGC The percentage of GC content in the sequence.
     * @param countingFeatures A map of feature types and their respective counts.
     * @param regions A list of regions in the sequence.
     * @param countGenes The number of genes in the sequence.
     * @param avgLengthGenes The average length of the genes in the sequence.
     * @param forwardStrands The number of features located on the forward strand.
     * @param reverseStrands The number of features located on the reverse strand.
     */
    public FeatureSummary(Long seqLength, double percentageGC, Map<String, Integer> countingFeatures, Map<String, Integer> countingSources, List<String> regions, int countGenes, long avgLengthGenes, int forwardStrands, int reverseStrands) {
        this.seqLength = seqLength;
        this.percentageGC = percentageGC;
        this.countingFeatures = countingFeatures;
        this.countingSources = countingSources;
        this.regions = regions;
        this.countGenes = countGenes;
        this.avgLengthGenes = avgLengthGenes;
        this.forwardStrands = forwardStrands;
        this.reverseStrands = reverseStrands;
    }

    /**
     * Returns a string representation of the FeatureSummary, summarizing key statistics.
     *
     * @return A string containing the sequence length, GC content, feature counts, gene statistics, and strand orientation.
     */
    @Override
    public String toString() {
        return "FeatureSummary{" +
                "avgSeqLength=" + seqLength +
                ", AvgPercentageGC=" + percentageGC +
                ", countingFeatures=" + countingFeatures +
                ", countingSources=" + countingSources +
                ", countGenes=" + countGenes +
                ", avgLengthGenes=" + avgLengthGenes +
                ", forwardStrands=" + forwardStrands +
                ", reverseStrands=" + reverseStrands +
                ", regions=" + regions +
                '}';
    }
}
