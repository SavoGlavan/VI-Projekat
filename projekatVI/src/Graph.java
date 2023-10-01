import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Graph {
    private ArrayList<Node> nodes;
    private ArrayList<Node> minVertexCover;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.minVertexCover = new ArrayList<>();
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
