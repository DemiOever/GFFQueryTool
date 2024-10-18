package nl.bioinf.alpruis;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileUtilsTest {

    @Test
    void testValidGFFFile() {
        // Mock a valid GFF3 file path
        Path validGFFFile = Path.of("src/test/resources/valid_gff.gff");

        // Call fileValidator method
        boolean isValid = FileUtils.fileValidator(validGFFFile);

        // Assert the file is valid
        assertTrue(isValid, "The valid GFF3 file should be recognized as valid.");
    }

    @Test
    void testValidFastaFile() {
        // Mock a valid FASTA file path
        Path validFastaFile = Path.of("src/test/resources/valid_fasta.fasta");

        // Call fileValidator method
        boolean isValid = FileUtils.fileValidator(validFastaFile);

        // Assert the file is valid
        assertTrue(isValid, "The valid FASTA file should be recognized as valid.");
    }

    @Test
    void testInvalidGFFFileColumns() {
        // Mock a valid GFF3 file path
        Path validGFFFile = Path.of("src/test/resources/invalid_gff_columns.gff");

        // Call fileValidator method
        boolean isValid = FileUtils.fileValidator(validGFFFile);

        // Assert the file is valid
        assertFalse(isValid, "The valid GFF3 file should be recognized as invalid.");
    }

    @Test
    void testInvalidGFFFileVersion() {
        // Mock a valid GFF3 file path
        Path validGFFFile = Path.of("src/test/resources/invalid_gff_version.gff");

        // Call fileValidator method
        boolean isValid = FileUtils.fileValidator(validGFFFile);

        // Assert the file is valid
        assertFalse(isValid, "The valid GFF3 file should be recognized as invalid.");
    }

    @Test
    void testInvalidFastaFileContent() {
        // Mock a valid FASTA file path
        Path validFastaFile = Path.of("src/test/resources/invalid_fasta_content.fasta");

        // Call fileValidator method
        boolean isValid = FileUtils.fileValidator(validFastaFile);

        // Assert the file is valid
        assertFalse(isValid, "The valid FASTA file should be recognized as invalid.");
    }

    @Test
    void testInvalidFastaFileHeader() {
        // Mock a valid FASTA file path
        Path validFastaFile = Path.of("src/test/resources/invalid_fasta_header.fasta");

        // Call fileValidator method
        boolean isValid = FileUtils.fileValidator(validFastaFile);

        // Assert the file is valid
        assertFalse(isValid, "The valid FASTA file should be recognized as invalid.");
    }

    @Test
    void ValidSequenceMakerTest() {
        // Valid file path
        Path validSequenceFile = Path.of("src/test/resources/valid_fasta.fasta");

        // Call sequenceMaker
        Map<String, String> map= FileUtils.sequenceMaker(validSequenceFile);

        // Assert the file is valid
        assertNotNull(map, "The map should not be null.");
        assertFalse(map.isEmpty(), "The map should not be empty.");
    }

    @Test
    void InvalidSequenceMakerTest() {
        // Invalid file path
        Path invalidSequenceFile = Path.of("src/test/resources/invalid_fasta_header.fasta");

        // Call sequenceMaker
        Map<String, String> map= FileUtils.sequenceMaker(invalidSequenceFile);

        // Assert the file is invalid
        assertNotNull(map, "The map should not be null.");
        assertTrue(map.isEmpty(), "The map should not be empty.");
    }

    // Additional test case ideas:
    // - Invalid GFF file without "##gff-version 3"
    // - Invalid FASTA file that doesn't start with ">"
    // - GFF3 file with wrong number of columns (edge case check)
}
