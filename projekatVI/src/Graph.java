
import java.util.ArrayList;

public class Graph {
    private ArrayList<Node> nodes;
    private ArrayList<Node> minVertexCover;

    private int edgeNumber;
    public Graph() {
        this.nodes = new ArrayList<>();
        this.minVertexCover = new ArrayList<>();
        this.edgeNumber=0;
    }

    public int getEdgeNumber() {
        return edgeNumber;
    }

    public void setEdgeNumber(int edgeNumber) {
        this.edgeNumber = edgeNumber;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<Node> getMinVertexCover() {
        return minVertexCover;
    }

    public void setMinVertexCover(ArrayList<Node> minVertexCover) {
        this.minVertexCover = minVertexCover;
    }
}
