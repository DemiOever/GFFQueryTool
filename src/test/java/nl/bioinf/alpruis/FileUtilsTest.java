package nl.bioinf.alpruis;

import org.junit.jupiter.api.Test;
import java.nio.file.Path;
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

    // Additional test case ideas:
    // - Invalid GFF file without "##gff-version 3"
    // - Invalid FASTA file that doesn't start with ">"
    // - GFF3 file with wrong number of columns (edge case check)
}
