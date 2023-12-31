import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {


    @Override
    public int compare(Node a, Node b) {
        return a.getWeight() - b.getWeight();
    }
}
