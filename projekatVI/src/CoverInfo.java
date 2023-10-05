import java.util.ArrayList;

public class CoverInfo {
    private ArrayList<NodeEdgePair> vertexCover;
    private int coverTotalWeight;
    private int coverEdgeNumber;

    public CoverInfo(ArrayList<NodeEdgePair> vertexCover, int coverTotalWeight, int coverEdgeNumber) {
        this.vertexCover = vertexCover;
        this.coverTotalWeight = coverTotalWeight;
        this.coverEdgeNumber = coverEdgeNumber;
    }

    public ArrayList<NodeEdgePair> getVertexCover() {
        return vertexCover;
    }

    public void setVertexCover(ArrayList<NodeEdgePair> vertexCover) {
        this.vertexCover = vertexCover;
    }

    public int getCoverTotalWeight() {
        return coverTotalWeight;
    }

    public void setCoverTotalWeight(int coverTotalWeight) {
        this.coverTotalWeight = coverTotalWeight;
    }

    public int getCoverEdgeNumber() {
        return coverEdgeNumber;
    }

    public void setCoverEdgeNumber(int coverEdgeNumber) {
        this.coverEdgeNumber = coverEdgeNumber;
    }
}
