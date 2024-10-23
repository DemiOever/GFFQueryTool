package nl.bioinf.alpruis.operation.filterSE;

import java.util.*;

import nl.bioinf.alpruis.Feature;
import nl.bioinf.alpruis.operation.filter_ex_sum.FeatureSummary;
import nl.bioinf.alpruis.operation.filter_ex_sum.FileSummarizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class FileSummarizerTest {

    private Map<String, String> sequences;
    private List<Feature> features;

    @BeforeEach
    public void setUp() {
        sequences = new HashMap<>();
        sequences.put(">seq1", "ATGCATGC");
        sequences.put(">seq2", "GCGCGCGC");
        features = new ArrayList<>();

        // Set testdata
        features.add(new Feature("NC_000067.7", "RefSeq", "region", 1, 195154279, ".", "+", ".",
                Map.of("strain", "C57BL/6J", "genome", "chromosome", "chromosome", "1",
                        "ID", "NC_000067.7:1..195154279", "gbkey", "Src",
                        "Dbxref", "taxon:10090", "Name", "1", "mol_type", "genomic DNA")));

        features.add(new Feature("NC_000067.7", "cmsearch", "gene", 3172239, 3172348, ".", "+", ".",
                Map.of("gene", "Gm26206", "description", "predicted gene, 26206", "ID", "gene-Gm26206",
                        "gene_biotype", "snRNA", "gbkey", "Gene", "Dbxref", "GeneID:115487594,MGI:MGI:5455983", "Name", "Gm26206")));

        features.add(new Feature("NC_000067.7", "cmsearch", "exon", 3172239, 3172348, ".", "+", ".",
                Map.of("product", "U6 spliceosomal RNA", "transcript_id", "XR_004936710.1",
                        "inference", "COORDINATES: profile:INFERNAL:1.1.1", "Parent", "rna-XR_004936710.1",
                        "gene", "Gm26206", "ID", "exon-XR_004936710.1-1", "gbkey", "ncRNA",
                        "Dbxref", "GeneID:115487594,RFAM:RF00026,GenBank:XR_004936710.1,MGI:MGI:5455983")));

        features.add(new Feature("NC_000067.7", "BestRefSeq,Gnomon", "gene", 3269956, 3741733, ".", "-", ".",
                Map.of("gene", "Xkr4", "description", "X-linked Kx blood group related 4",
                        "gene_synonym", "Gm210,mKIAA1889,XRG4", "ID", "gene-Xkr4",
                        "gene_biotype", "protein_coding", "gbkey", "Gene",
                        "Dbxref", "GeneID:497097,MGI:MGI:3528744", "Name", "Xkr4")));

        features.add(new Feature("NC_000067.7", "Gnomon", "exon", 3740775, 3741733, ".", "-", ".",
                Map.of("product", "X-linked Kx blood group related 4, transcript variant X1",
                        "transcript_id", "XM_006495550.5", "Parent", "rna-XM_006495550.5",
                        "gene", "Xkr4", "ID", "exon-XM_006495550.5-1", "gbkey", "mRNA",
                        "Dbxref", "GeneID:497097,GenBank:XM_006495550.5,MGI:MGI:3528744")));

        features.add(new Feature("NC_000067.7", "Gnomon", "exon", 3491925, 3492124, ".", "-", ".",
                Map.of("product", "X-linked Kx blood group related 4, transcript variant X1",
                        "transcript_id", "XM_006495550.5", "Parent", "rna-XM_006495550.5",
                        "gene", "Xkr4", "ID", "exon-XM_006495550.5-2", "gbkey", "mRNA",
                        "Dbxref", "GeneID:497097,GenBank:XM_006495550.5,MGI:MGI:3528744")));
    }

    @Test
    public void testSummarizeFeatures() {
        // Call the summarizeFeatures method
        FileSummarizer summarizer = new FileSummarizer();
        FeatureSummary summary = summarizer.summarizeFeatures(features, sequences);

        // Updated expected feature counts based on the features provided
        Map<String, Integer> expectedFeatureCounts = new HashMap<>();
        expectedFeatureCounts.put("region", 1);  // No introns in the given features
        expectedFeatureCounts.put("gene", 2);    // 3 genes in total
        expectedFeatureCounts.put("exon", 3);    // 3 exons in total

        // Updated expected source counts based on the sources in the features
        Map<String, Integer> expectedSourceCounts = new HashMap<>();
        expectedSourceCounts.put("RefSeq", 1);   // 1 feature with source RefSeq
        expectedSourceCounts.put("cmsearch", 2); // 2 features with source cmsearch
        expectedSourceCounts.put("Gnomon", 2);   // 3 features with source Gnomon
        expectedSourceCounts.put("BestRefSeq,Gnomon", 1);


        //List<String> expectedRegions = Arrays.asList("chr1", "chr2");

        // Assert statements to verify the feature summary results
        assertEquals(2, summary.getCountGenes());
        assertEquals(235943, summary.getAvgLengthGenes());
        assertEquals(3, summary.getForwardStrands());
        assertEquals(3, summary.getReverseStrands());
        assertEquals(List.of("1"), summary.getRegions());
        assertEquals(expectedFeatureCounts, summary.getCountingFeatures());
        assertEquals(expectedSourceCounts, summary.getCountingSources());
        assertEquals(8, summary.getSeqLength());
        assertEquals(75, summary.getPercentageGC(), 0.01);
    }
}
