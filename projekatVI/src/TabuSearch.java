import java.util.*;
import java.util.stream.Collectors;

public class TabuSearch {

    private static final int MAX_TABU_TENURE = 10;

    static class NeighborResult {
        Set<Node> neighbor;
        Node modifiedNode;

        NeighborResult(Set<Node> neighbor, Node modifiedNode) {
            this.neighbor = neighbor;
            this.modifiedNode = modifiedNode;
        }
    }

    private static Set<Node> greedyInitialization(List<Edge> edges, List<Node> nodes) {
        Set<Node> cover = new HashSet<>();
        List<Edge> uncoveredEdges = new ArrayList<>(edges);

        Map<Node, Double> nodeRatios = nodes.stream().collect(Collectors.toMap(
            node -> node,
            node -> (double) node.getWeight() / edges.stream().filter(edge -> edge.includes(node)).count()
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
        Queue<Node> tabuList = new LinkedList<>();

        int iterationWithoutImprovement = 0;

        while (iterationWithoutImprovement < 100) {
            NeighborResult result = findBestNeighbor(currentSolution, nodes, edges, tabuList, bestSolution);
            Set<Node> candidate = result.neighbor;

            if (candidate != null && getTotalWeight(candidate) < getTotalWeight(bestSolution)) {
                bestSolution = candidate;
                iterationWithoutImprovement = 0;
            } else {
                iterationWithoutImprovement++;
            }

            updateTabuList(tabuList, result.modifiedNode);
            currentSolution = (candidate != null) ? new HashSet<>(candidate) : currentSolution;
        }
        return new ArrayList<>(bestSolution);
    }


    private static NeighborResult findBestNeighbor(Set<Node> current, List<Node> nodes, List<Edge> edges, Queue<Node> tabuList, Set<Node> bestSolutionEver) {
        Set<Node> bestNeighbor = null;
        Node modifiedNode = null;
        int bestNeighborWeight = Integer.MAX_VALUE;

        for (Node node : nodes) {
            Set<Node> neighbor = current.contains(node) ? new HashSet<>(current) : new HashSet<>(current);
            if (current.contains(node)) {
                neighbor.remove(node);
            } else {
                neighbor.add(node);
            }

            boolean coversAllEdges = edges.stream()
                .allMatch(edge -> neighbor.contains(edge.getNode1()) || neighbor.contains(edge.getNode2()));

            int neighborWeight = getTotalWeight(neighbor);
            
            if (coversAllEdges && neighborWeight < bestNeighborWeight && (!tabuList.contains(node) || neighborWeight < getTotalWeight(bestSolutionEver))) {
                bestNeighbor = neighbor;
                bestNeighborWeight = neighborWeight;
                modifiedNode = node;
            }
        }

        return new NeighborResult(bestNeighbor, modifiedNode);
    }

    private static int getTotalWeight(Set<Node> solution) {
        return solution.stream().mapToInt(Node::getWeight).sum();
    }

    private static void updateTabuList(Queue<Node> tabuList, Node modifiedNode) {
        if (tabuList != null && modifiedNode != null) {
            tabuList.add(modifiedNode);
            while (tabuList.size() > MAX_TABU_TENURE) {
                tabuList.poll();
            }
        }
    }

}
