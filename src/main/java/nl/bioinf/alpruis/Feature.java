package nl.bioinf.alpruis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a feature in a GFF3 file, such as a gene or exon.
 * The class stores information about a feature's sequence ID, source, type, start and end positions,
 * score, strand, phase, and additional attributes.
 * It also tracks hierarchical relationships between features, such as parent-child relationships.
 */
public class Feature {
    private final String seqId;
    private final String source;
    private final String type;
    private final int start;
    private final int end;
    private final String score;
    private final String strand;
    private final String phase;
    private final Map<String, String> attributes;
    private List<String> children;

    /**
     * Constructs a Feature object with the provided information.
     *
     * @param seqId      the sequence ID on which this feature is located (e.g., chromosome or contig).
     * @param source     the source that created this feature (e.g., a prediction program).
     * @param type       the type of the feature (e.g., gene, exon).
     * @param start      the start position of the feature on the sequence.
     * @param end        the end position of the feature on the sequence.
     * @param score      the score associated with this feature (can be "." if not applicable).
     * @param strand     the strand direction: "+" for forward, "-" for reverse.
     * @param phase      the phase for features with a coding sequence, typically 0, 1, or 2.
     * @param attributes additional attributes as a map of key-value pairs (e.g., ID, Parent).
     */
    public Feature(String seqId, String source, String type, int start, int end, String score, String strand, String phase, Map<String, String> attributes) {
        this.seqId = seqId;
        this.source = source;
        this.type = type;
        this.start = start;
        this.end = end;
        this.score = score;
        this.strand = strand;
        this.phase = phase;
        this.attributes = attributes;
        this.children = new ArrayList<String>();
    }

    /**
     * Gets the sequence ID (e.g., chromosome or contig) where this feature is located.
     *
     * @return the sequence ID.
     */
    public String getSeqId() {
        return seqId;
    }

    /**
     * Gets the source that created this feature (e.g., a prediction program).
     *
     * @return the source of the feature.
     */
    public String getSource() {
        return source;
    }

    /**
     * Gets the type of this feature (e.g., gene, exon).
     *
     * @return the type of the feature.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the start position of this feature on the sequence.
     *
     * @return the start position.
     */
    public int getStart() {
        return start;
    }

    /**
     * Gets the end position of this feature on the sequence.
     *
     * @return the end position.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Gets the strand direction of this feature: "+" for forward strand, "-" for reverse strand.
     *
     * @return the strand direction.
     */
    public String getStrand() {
        return strand;
    }

    /**
     * Gets the attributes of this feature as a map of key-value pairs.
     *
     * @return the map of attributes.
     */
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * Retrieves the unique ID of the feature from its attributes.
     *
     * @return the ID of the feature.
     */
    public String getID() {
        return attributes.get("ID");
    }

    /**
     * Retrieves the chromosome (or sequence ID) this feature is located on from its attributes.
     *
     * @return the chromosome or sequence ID.
     */
    public String getChromosome() {
        return attributes.get("chromosome");
    }

    /**
     * Retrieves the parent ID of this feature from its attributes, indicating hierarchical relationships.
     *
     * @return the parent ID of the feature, or null if not applicable.
     */
    public String getParentID() {
        return attributes.get("Parent");
    }

    /**
     * Adds a child feature ID to this feature's list of children.
     *
     * @param child the ID of the child feature.
     */
    public void addChild(String child) {
        children.add(child);
    }

    /**
     * Retrieves the list of child features associated with this feature.
     *
     * @return the list of child feature IDs.
     */
    public List<String> getChildren() {
        return children;
    }

    /**
     * Provides a string representation of this feature, including all of its details and attributes.
     *
     * @return a string describing this feature.
     */
    @Override
    public String toString() {
        return "Feature{" +
                "seqID='" + seqId + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", score='" + score + '\'' +
                ", strand='" + strand + '\'' +
                ", phase='" + phase + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    /**
     * Provides a string representation of this feature in GFF3 format, including all of its details and attributes.
     *
     * @return a string describing this feature.
     */
    public String toGffFormat() {
        return seqId + '\t' +
                source + '\t' +
                type + '\t' +
                start + '\t' +
                end +'\t' +
                score +'\t' +
                strand +'\t' +
                phase +'\t' +
                atributesToString()
                ;
    }

    /**
     * Provides a string representation of this feature in CSV format, including all of its details and attributes.
     *
     * @return a string describing this feature.
     */
    public String toCsvFormat() {
        return seqId + ',' +
                source + ',' +
                type + ',' +
                start + ',' +
                end +',' +
                score +',' +
                strand +',' +
                phase +',' +
                atributesToString();
    }
    /**
     * Provides a string representation of the attributes for CSV and GFF3 format.
     *
     * @return a string describing this feature.
     */
    public String atributesToString() { //TODO thr last ; shouldn't be there (lvl 1)
        StringBuilder sb = new StringBuilder();
        for (String attr : attributes.keySet()) {
            String value = attributes.get(attr);
            sb.append(attr + "=" + value + ";");
        }
        return sb.toString();
    }
}