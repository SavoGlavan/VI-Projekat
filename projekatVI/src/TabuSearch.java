import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Function;




public class TabuSearch {

    private static final int TABU_TENURE = 10;
    private static Set<Node> greedyInitialization(List<Edge> edges, List<Node> nodes) {
        Set<Node> cover = new HashSet<>();
        List<Edge> uncoveredEdges = new ArrayList<>(edges);
        
        Map<Node, Double> nodeRatios = nodes.stream().collect(Collectors.toMap(
            node -> node,
            node -> (double) node.getWeight() / edges.stream().filter(edge -> edge.getNode1().equals(node) || edge.getNode2().equals(node)).count()
        ));
    
        while (!uncoveredEdges.isEmpty()) {
            Node bestNode = nodeRatios.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException("No node found"));
    
            cover.add(bestNode);
            uncoveredEdges = uncoveredEdges.stream()
                .filter(edge -> !cover.contains(edge.getNode1()) && !cover.contains(edge.getNode2()))
                .collect(Collectors.toList());
    
            nodeRatios.remove(bestNode);
        }
    
        return cover;
    }
    
 

    public static List<Node> vertexCoverTabuSearch(List<Node> nodes, List<Edge> edges) {
        if (nodes == null || edges == null) {
            throw new IllegalArgumentException("Nodes or edges cannot be null");
        }

        Set<Node> currentSolution = greedyInitialization(edges, nodes);
        Set<Node> bestSolution = new HashSet<>(currentSolution);
        List<Node> tabuList = new ArrayList<>();

        int iterationWithoutImprovement = 0;

        while (iterationWithoutImprovement < 100) {
            Set<Node> candidate = findBestNeighbor(currentSolution, nodes, edges, tabuList);

            if (candidate != null && getTotalWeight(candidate) < getTotalWeight(bestSolution)) {
                bestSolution = candidate;
                iterationWithoutImprovement = 0;
            } else {
                iterationWithoutImprovement++;
            }

            updateTabuList(tabuList, candidate);
            currentSolution = (candidate != null) ? new HashSet<>(candidate) : currentSolution;
        }
        List<Node> aList = new ArrayList<Node>(bestSolution);

        return aList;
    }

    private static Set<Node> findBestNeighbor(Set<Node> current, List<Node> nodes, List<Edge> edges, List<Node> tabuList) {
        Set<Node> bestNeighbor = new HashSet<>(current);

        for (Node node : nodes) {
            Set<Node> newSolution = new HashSet<>(current);

            if (current.contains(node)) {
                newSolution.remove(node);
            } else {
                newSolution.add(node);
            }

            boolean coversAllEdges = edges.stream()
                .allMatch(edge -> newSolution.contains(edge.getNode1()) || newSolution.contains(edge.getNode2()));

            if (coversAllEdges && !tabuList.contains(node) && getTotalWeight(newSolution) < getTotalWeight(bestNeighbor)) {
                bestNeighbor = newSolution;
            }
        }

        return bestNeighbor.equals(current) ? null : bestNeighbor;
    }

    private static int getTotalWeight(Set<Node> solution) {
        return (solution != null) ? solution.stream().mapToInt(Node::getWeight).sum() : Integer.MAX_VALUE;
    }

    private static void updateTabuList(List<Node> tabuList, Set<Node> candidate) {
        if (tabuList != null && candidate != null) {
            tabuList.addAll(candidate);
            while (tabuList.size() > TABU_TENURE) {
                tabuList.remove(0);
            }
        }
    }

    public static void main(String[] args) {
        Node central = new Node(1, 5);
        Node node2 = new Node(2, 2);
        Node node3 = new Node(3, 3);
        Node node4 = new Node(4, 4);
        Node node5 = new Node(5, 2);
        Node node6 = new Node(6, 1);
        Node node7 = new Node(7, 3);
    
        List<Node> nodes = Arrays.asList(central, node2, node3, node4, node5, node6, node7);
    
        List<Edge> edges = Arrays.asList(
            new Edge(central, node2),
            new Edge(central, node3),
            new Edge(central, node4),
            new Edge(central, node5),
            new Edge(central, node6),
            new Edge(central, node7)
        );
    
        List<Node> result = vertexCoverTabuSearch(nodes, edges);
        System.out.println(result.stream().map(Node::getId).collect(Collectors.toList()));
    }
    
}
