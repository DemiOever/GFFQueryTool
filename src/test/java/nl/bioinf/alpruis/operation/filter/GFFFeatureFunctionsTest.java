package nl.bioinf.alpruis.operation.filter;

import nl.bioinf.alpruis.Feature;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GFFFeatureFunctionsTest {
    @Test
    public void test() {
        Map<String, String> mapAttr = new HashMap<String, String>();
        mapAttr.put("ID", "NC_000067.7:1..195154279");
        mapAttr.put("genome","chromosome");
        mapAttr.put("chromosome","1");
        mapAttr.put("gbkey","Src");
        mapAttr.put("Name","1");
        Feature feature = new Feature("NC_000067.7", "RefSeq", "region", 2, 8, ".", "+", ".", mapAttr);

        boolean filterTrue = GFFFeatureFunctions.filteringLine(feature, "REGION", List.of("5", "10"), false, true);
        boolean filterTrue2 = GFFFeatureFunctions.filteringLine(feature, "CHROMOSOME", List.of("NC_000067.7"), false, false);

        assertTrue(filterTrue);
        assertTrue(filterTrue2);

        boolean filterFalse = GFFFeatureFunctions.filteringLine(feature, "REGION", List.of("1", "10"), false, false);
        assertTrue(filterFalse);
    }

}
