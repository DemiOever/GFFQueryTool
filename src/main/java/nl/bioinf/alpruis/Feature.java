package nl.bioinf.alpruis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public String getID() {
        return attributes.get("ID");
    }

    public String getChromosome() {
        return attributes.get("chromosome");
    }

    public String getSeqId() {
        return seqId;
    }

    public String getSource() {
        return source;
    }

    public String getType() {
        return type;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getStrand() {
        return strand;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getParentID() {
        return attributes.get("Parent");
    }

    public void addChild(String child) {
        children.add(child);
    }

    public List<String> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return  "Feature{" +
                "seqID='" + seqId +
                "', source='" + source +
                "', type='" + type +
                "', start=" + start +
                ", end=" + end +
                ", score='" + score +
                "', strand='" + strand +
                "', phase='" + phase +
                "', attributes=" + attributes +
                ", children=" + children +
                "}";
    }


}
