package nl.bioinf.alpruis.operation.filter;

import nl.bioinf.alpruis.Feature;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
/*
* Testing the different filter options with the extra options like contains and delete
* regular without either, with delete only, with contains only and with both.
* */
public class GFFFeatureFunctionsTest {
    public Feature makeFeature () {
        Map<String, String> mapAttr = new LinkedHashMap<>();
        mapAttr.put("ID", "NC_000067.7:1..195154279");
        mapAttr.put("genome","chromosome");
        mapAttr.put("chromosome","1");
        mapAttr.put("gbkey","Src");
        mapAttr.put("Name","1");
        return new Feature("NC_000067.7", "RefSeq", "region", 2, 8, ".", "+", ".", mapAttr);
    }

    @Test
    public void testId() {
        // 1,2, contains, delete, contains and delete
        Feature feature = makeFeature();
        assertTrue(GFFFeatureFunctions.filteringLine(feature, "ID", List.of("NC_000067.7:1..195154279"), false, false));
        assertTrue(GFFFeatureFunctions.filteringLine(feature, "ID", List.of("NC_000067.7:1..195154279", "NC_000067.7"), false, false));
        assertTrue(GFFFeatureFunctions.filteringLine(feature, "ID", List.of("NC_000067.7:1..1951542"), false, true));
        assertTrue(GFFFeatureFunctions.filteringLine(feature, "ID", List.of("NC_000067.7:1..19515428"), true, false)); // because when it isn't found it returns true
        assertTrue(GFFFeatureFunctions.filteringLine(feature, "ID", List.of("NC_000067.7:1..196"), true, true)); // because when it isn't found it returns true

        assertFalse(GFFFeatureFunctions.filteringLine(feature, "ID", List.of("NC_000067.7:1..19515427"), false, false));
        assertFalse(GFFFeatureFunctions.filteringLine(feature, "ID", List.of("NC_000067.7:1..196", "NC_000068.8"), false, false));
        assertFalse(GFFFeatureFunctions.filteringLine(feature, "ID", List.of("NC_000067.7:1..197"), false, true));
        assertFalse(GFFFeatureFunctions.filteringLine(feature, "ID", List.of("NC_000067.7:1..195154279"), true, false)); // because when it needs to be deleted it returns false
        assertFalse(GFFFeatureFunctions.filteringLine(feature, "ID", List.of("NC_000067.7:1..1951542"), true, true)); // because when it needs to be deleted it returns false
    }

    @Test
    public void testType() {
        Feature feature = makeFeature();
        assertTrue(GFFFeatureFunctions.filteringLine(feature, "TYPE", List.of("region"), false, false));
        assertTrue(GFFFeatureFunctions.filteringLine(feature, "TYPE", List.of("gene", "exon"), false, false));
        assertTrue(GFFFeatureFunctions.filteringLine(feature, "TYPE", List.of("gen"), false, true));
        assertTrue(GFFFeatureFunctions.filteringLine(feature, "TYPE", List.of("gene"), true, false)); // because when it isn't found it returns true
        assertTrue(GFFFeatureFunctions.filteringLine(feature, "TYPE", List.of("gene"), true, true)); // because when it isn't found it returns true

        assertFalse(GFFFeatureFunctions.filteringLine(feature, "TYPE", List.of("intron"), false, false));
        assertFalse(GFFFeatureFunctions.filteringLine(feature, "TYPE", List.of("genes", "intron"), false, false));
        assertFalse(GFFFeatureFunctions.filteringLine(feature, "TYPE", List.of("gene"), false, true));
        assertFalse(GFFFeatureFunctions.filteringLine(feature, "TYPE", List.of("gene"), true, false)); // because when it needs to be deleted it returns false
        assertFalse(GFFFeatureFunctions.filteringLine(feature, "TYPE", List.of("gene"), true, true)); // because when it needs to be deleted it returns false

    }

    @Test
    public void testChromosome() {
        Feature feature = makeFeature();
        boolean filterTrue = GFFFeatureFunctions.filteringLine(feature, "CHROMOSOME", List.of("NC_000067.7"), false, false);

    }

    @Test
    public void testRegion() {
        Feature feature = makeFeature();

        boolean filterTrue = GFFFeatureFunctions.filteringLine(feature, "REGION", List.of("5", "10"), false, true);
        boolean filterFalse = GFFFeatureFunctions.filteringLine(feature, "REGION", List.of("1", "10"), false, false);

    }

    @Test
    public void testSource() {

    }

}
