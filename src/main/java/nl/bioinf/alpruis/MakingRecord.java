package nl.bioinf.alpruis;

import java.util.HashMap;
import java.util.Map;

public class MakingRecord {
    private String seqId;
    private String source;
    private String type;
    private int start;
    private int end;
    private String score;
    private String strand;
    private String phase;
    private Map<String, String> attributes;

    public MakingRecord(String seqId, String source, String type, int start, int end, String score, String strand, String phase, String attributesStr) {
        this.seqId = seqId;
        this.source = source;
        this.type = type;
        this.start = start;
        this.end = end;
        this.score = score;
        this.strand = strand;
        this.phase = phase;
        this.attributes = parseAttributes(attributesStr);
    }

    private Map<String, String> parseAttributes(String attributesStr) {
        Map<String, String> attributes = new HashMap<>();

        return new HashMap<>();
    }

    public String getSeqId() { return seqId; }
    public String getType() { return type; }
    public int getStart() { return start; }
    public int getEnd() { return end; }
    public String getStrand() { return strand; }
    public Map<String, String> getAttributes() { return attributes; }

    @Override
    public String toString() {
        return String.format("Feature[seqId=%s, type=%s, start=%d, end=%d]", seqId, type, start, end);
    }
}
