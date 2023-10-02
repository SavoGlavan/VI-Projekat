
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TabuGraph {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    private ArrayList<Node> minVertexCover;

    private int edgeNumber;

    public TabuGraph() {
        this.edges = new ArrayList<>();
        this.nodes = new ArrayList<>();
        this.minVertexCover = new ArrayList<>();
        this.edgeNumber = 0;
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

    public ArrayList<Edge> getEdges() {
        return edges;
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
