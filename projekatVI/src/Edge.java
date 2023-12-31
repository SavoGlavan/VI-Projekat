class Edge {
    private Node node1;
    private Node node2;

    public Edge(Node node1, Node node2) {
        this.node1 = node1;
        this.node2 = node2;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public boolean includes(Node node) {
        return node1.equals(node) || node2.equals(node);
    }
    public String toString(){
        return node1+" - " + node2;
    }
}
