import java.util.*;

import java.util.*;

public class BruteForce {

    public static List<Node> minimumWeightedVertexCover(List<Node> nodes, List<Edge> edges) {
        Set<Node> bestCover = null;
        int bestWeight = Integer.MAX_VALUE;

        int n = nodes.size();
        for (int i = 0; i < (1 << n); i++) {
            Set<Node> subset = new HashSet<>();

            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(nodes.get(j));
                }
            }

            if (isVertexCover(subset, edges)) {
                int weight = subset.stream().mapToInt(Node::getWeight).sum();
                if (weight < bestWeight) {
                    bestWeight = weight;
                    bestCover = new HashSet<>(subset);
                }
            }
        }

        if (bestCover == null) {
            // No valid cover found, return an empty list
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

        TabuGraph g = TabuMain.loadInstance(
                "C:\\Users\\obradovica\\Desktop\\VI projekat\\VI-Projekat\\projekatVI\\instances\\vc_25_80_03.txt");

        List<Node> vertexCover = minimumWeightedVertexCover(g.getNodes(), g.getEdges());

        System.out.println(vertexCover);

        int sum = 0;
        for (Node n : vertexCover) {
            sum += n.getWeight();
        }
        System.out.println(sum);
        System.out.println("Minimum Weighted Vertex Cover: ");
        for (Node node : vertexCover) {
            System.out.println("Node ID: " + node.getId() + ", Weight: " + node.getWeight());
        }

    }
}
