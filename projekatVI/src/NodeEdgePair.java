public class NodeEdgePair implements Comparable<NodeEdgePair>{
    private Node node;
    private int edgeNumber;
    private float ratio;



    public NodeEdgePair(Node node, int edgeNumber) {
        this.node = node;
        this.edgeNumber = edgeNumber;
        if(edgeNumber!=0)
        this.ratio=(float)(this.node.getWeight()*1.5)/(this.edgeNumber*2);
        else this.ratio=Float.POSITIVE_INFINITY;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
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

    @Override
    public int compareTo(NodeEdgePair n) {
        if(this.ratio<n.ratio)
            return 1;
        else if (this.ratio==n.ratio)
            return 0;
        else return -1;
    }

    public String toString(){
        return this.node+" "+ this.ratio + " "+ this.edgeNumber;
    }
}
