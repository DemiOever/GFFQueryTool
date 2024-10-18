package nl.bioinf.alpruis;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
// TODO Sets making aswell because might need to use when using exit condition and list has to be updated(??)
public class OptionsProcessor {

    private Path inputGffFile;
    private Map<String, String> sequence;
    private boolean validate;
    private boolean summary;
    private boolean delete;
    private boolean extended;
    private Path outputFile;
    private Map<String, List<String>> finalListFilter;

    public OptionsProcessor(Path inputGffFile, Map<String, String> sequence, boolean validate, boolean summary,
                                  boolean delete, boolean extended, Path outputFile, Map<String, List<String>> finalListFilter) {
        this.inputGffFile = inputGffFile;
        this.sequence = sequence;
        this.validate = validate;
        this.summary = summary;
        this.delete = delete;
        this.extended = extended;
        this.outputFile = outputFile;
        this.finalListFilter = finalListFilter;
    }

    // Getters and setters for each field

    public Path getInputGffFile() {
        return inputGffFile;
    }

    public Map<String, String> getSequence() {
        return sequence;
    }

    public boolean isValidate() {
        return validate;
    }

    public boolean isSummary() {
        return summary;
    }

    public boolean isDelete() {
        return delete;
    }

    public boolean isExtended() {
        return extended;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public Map<String, List<String>> getListFilter() {
        return finalListFilter;
    }
}
