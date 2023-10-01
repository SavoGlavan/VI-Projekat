public class NodeEdgePair {
    private Node node;
    private int edgeNumber;

    public NodeEdgePair(Node node, int edgeNumber) {
        this.node = node;
        this.edgeNumber = edgeNumber;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getEdgeNumber() {
        return edgeNumber;
    }

    public void setEdgeNumber(int edgeNumber) {
        this.edgeNumber = edgeNumber;
    }
}
