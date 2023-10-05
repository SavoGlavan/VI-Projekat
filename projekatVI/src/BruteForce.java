import java.util.*;

public class BruteForce {

    public static List<Node> minimumWeightedVertexCover(List<Node> nodes, List<Edge> edges) {
        Set<Node> bestCover = null;
        long bestWeight = Long.MAX_VALUE;
        
        int n = nodes.size();
        for (long i = 0; i < (1L << n); i++) {
            Set<Node> subset = new HashSet<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1L << j)) != 0) {
                    subset.add(nodes.get(j));
                }
            }

            if (isVertexCover(subset, edges)) {
                long weight = subset.stream().mapToLong(Node::getWeight).sum();
                if (weight < bestWeight) {
                    bestWeight = weight;
                    bestCover = new HashSet<>(subset);
                }
            }
        }

        if (bestCover == null) {
            return new ArrayList<>();
        }

        List<Node> aList = new ArrayList<>(bestCover);

        return aList;
    }

    private static boolean isVertexCover(Set<Node> subset, List<Edge> edges) {
        for (Edge edge : edges) {
            if (!subset.contains(edge.getNode1()) && !subset.contains(edge.getNode2())) {
                return false;
            }
        }
        return true;
    }
    public static void main(String[] args) {
        
        for (int i = 1; i < 6; i++) {
            TabuGraph g = TabuMain.loadInstance(
                    "C:\\Users\\obrad\\Desktop\\VI\\VI-projekat\\projekatVI\\instances\\vc_50_100_0" + i
                            + ".txt");
            List<Node> result = minimumWeightedVertexCover(g.getNodes(), g.getEdges());
            int sum = 0;
            for (Node n : result) {
                sum += n.getWeight();
            }
            System.out.println("instanca vc_50_100_0_" + i+ ": " +sum);
            
        }
          for (int i = 1; i < 6; i++) {
            TabuGraph g = TabuMain.loadInstance(
                    "C:\\Users\\obrad\\Desktop\\VI\\VI-projekat\\projekatVI\\instances\\vc_50_250_0" + i
                            + ".txt");
            List<Node> result = minimumWeightedVertexCover(g.getNodes(), g.getEdges());
            int sum = 0;
            for (Node n : result) {
                sum += n.getWeight();
            }
            System.out.println("instanca vc_50_250_0_" + i+ ": " +sum);
        
        }
    }
}
