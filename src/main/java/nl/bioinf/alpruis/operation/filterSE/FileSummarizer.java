package nl.bioinf.alpruis.operation.filterSE;

import nl.bioinf.alpruis.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to provide summary statistics for biological sequence files and GFF3 features.
 * This class summarizes sequences and features such as gene counts, strand directions, and GC content.
 */
public class FileSummarizer {
    private static final Logger logger = LogManager.getLogger(FileSummarizer.class.getName());

    /**
     * Calculates the average length of the sequences in a given map of FASTA sequences.
     *
     * @param sequence a map containing FASTA headers as keys and their corresponding sequences as values.
     * @return the average sequence length.
     */
    private static long averageLength(Map<String,String> sequence){
        long sum = 0;
        for (Map.Entry<String, String> entry : sequence.entrySet()) {
            sum += entry.getValue().length();
        }
        return sum/sequence.size();
    }

    /**
     * Calculates the GC content percentage in the provided sequences.
     * The GC content is determined by counting the occurrences of G and C in the sequences.
     *
     * @param seq a map containing FASTA headers as keys and their corresponding sequences as values.
     * @return the GC percentage as a double.
     */
    private static double gettingGcPercentage(Map<String, String> seq) {
        // gc-percentage
        // input: String
        // return: percentage = int
        Pattern pattern = Pattern.compile("[*C|G]");
        int count = 0;
        long length = 0;
        for (Map.Entry<String, String> entry : seq.entrySet()) {
            length += entry.getValue().length();
            Matcher matcher = pattern.matcher(entry.getValue().toUpperCase());
            while (matcher.find()) {
                count++;
            }
        }
        return count / (double) length * 100;
    }

    /**
     * Summarizes the features from a list of GFF3 features and FASTA sequences,
     * providing statistics such as gene count, average gene length, strand direction counts,
     * and the types of features present.
     *
     * @param features a list of Feature objects parsed from a GFF3 file.
     * @param sequence a map of sequences from a FASTA file.
     * @return a FeatureSummary object containing various statistics about the features and sequences.
     */
    public FeatureSummary summarizeFeatures(List<Feature> features, Map<String, String> sequence) {
        Map<String, Integer> countingFeatures = new LinkedHashMap<>();
        Map<String, Integer> countingSources = new LinkedHashMap<>();

        List<String> regions = new ArrayList<>();

        int countGenes = 0;
        long lengthGenes = 0;
        int forwardStrands = 0;
        int reverseStrands = 0;
        int unknownStrands = 0;

        for (Feature feature : features) {
            countingFeatures.put(feature.getType(), countingFeatures.getOrDefault(feature.getType(), 0) + 1);
            countingSources.put(feature.getSource(), countingSources.getOrDefault(feature.getSource(), 0) + 1);


            if (Objects.equals(feature.getType(), "gene")) {
                countGenes++;
                long length = feature.getEnd() - feature.getStart();
                lengthGenes += length;
            }

            String strand = feature.getStrand();
            switch (strand) {
                case "+" -> forwardStrands++;
                case "-" -> reverseStrands++;
                case "." -> unknownStrands++;
            }

            if (Objects.equals(feature.getType(), "region")) {
                regions.add(feature.getSeqId());
            }
        }
        logger.warn("In the strand column are {} found as empty(not forward nor reverse).", unknownStrands);
        long avgLengthGenes = countGenes > 0 ? lengthGenes / countGenes : 0;

        return new FeatureSummary(averageLength(sequence), gettingGcPercentage(sequence), countingFeatures, countingSources, regions, countGenes, avgLengthGenes, forwardStrands, reverseStrands);
    }
}
