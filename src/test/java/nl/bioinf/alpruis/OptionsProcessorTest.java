package nl.bioinf.alpruis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OptionsProcessorTest {

    private Path inputGffFile;
    private Map<String, String> sequence;
    private boolean validate;
    private boolean summary;
    private boolean delete;
    private boolean extended;
    private Path outputFile;
    private Map<String, List<String>> finalListFilter;
    private List<String> listAttribute;
    private boolean contains;

    @BeforeEach
    void setUp() {
        // Initialize the test data
        Path inputGffFile = Path.of("src/test/resources/valid_gff.gff");
        sequence = new LinkedHashMap<>();
        sequence.put("seq1", "ATCG");
        boolean validate = true;
        boolean summary = false;
        boolean delete = true;
        boolean extended = false;
        Path outputFile = Path.of("output.txt");
        boolean contains = false;

        finalListFilter = new LinkedHashMap<>();
        finalListFilter.put("filter1", Arrays.asList("value1", "value2"));

        Object listAttribute = Arrays.asList("attribute1", "attribute2");
    }

    @Test
    void optionsProcessorTest() {
        OptionsProcessor optionsProcessor = new OptionsProcessor(
                inputGffFile, sequence, validate, summary, delete, extended, outputFile, finalListFilter, contains);

        assertEquals(inputGffFile, optionsProcessor.getInputGffFile());
        assertEquals(sequence, optionsProcessor.getSequence());
        assertEquals(validate, optionsProcessor.isValidate());
        assertEquals(summary, optionsProcessor.isSummary());
        assertEquals(delete, optionsProcessor.isDelete());
        assertEquals(extended, optionsProcessor.isExtended());
        assertEquals(outputFile, optionsProcessor.getOutputFile());
        assertEquals(finalListFilter, optionsProcessor.getListFilter());
        assertEquals(contains, optionsProcessor.getContains());
    }
}
