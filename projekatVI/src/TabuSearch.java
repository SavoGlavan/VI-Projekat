import java.util.*;
import java.util.stream.Collectors;

public class TabuSearch {

    private static final int MAX_TABU_TENURE = 15;
    private static final int MIN_TABU_TENURE = 5;
    private static final int MAX_ITERATIONS_WITHOUT_IMPROVEMENT = 100;

    static class NeighborResult {
        Set<Node> neighbor;
        Node modifiedNode;

        NeighborResult(Set<Node> neighbor, Node modifiedNode) {
            this.neighbor = neighbor;
            this.modifiedNode = modifiedNode;
        }
    }

    public static List<Node> vertexCoverTabuSearch(List<Node> nodes, List<Edge> edges) {
        Set<Node> currentSolution = greedyInitialization(edges, nodes);
        Set<Node> bestSolution = new HashSet<>(currentSolution);
        Queue<Node> tabuList = new LinkedList<>();
        int iterationWithoutImprovement = 0;

        while (iterationWithoutImprovement < MAX_ITERATIONS_WITHOUT_IMPROVEMENT) {
            NeighborResult bestNeighborResult = findBestNeighbor(currentSolution, nodes, edges, tabuList, bestSolution);
            if (isNewSolutionBetter(bestNeighborResult.neighbor, bestSolution)) {
                bestSolution = bestNeighborResult.neighbor;
                iterationWithoutImprovement = 0;
            } else {
                iterationWithoutImprovement++;
            }
            updateTabuList(tabuList, bestNeighborResult.modifiedNode);
            currentSolution = (bestNeighborResult.neighbor != null) ? new HashSet<>(bestNeighborResult.neighbor) : currentSolution;
            if (new Random().nextDouble() < 0.02) {
                Node randomNode = nodes.get(new Random().nextInt(nodes.size()));
                currentSolution = toggleNodeInSet(currentSolution, randomNode);
            }
        }
        return new ArrayList<>(bestSolution);
    }

    private static Set<Node> greedyInitialization(List<Edge> edges, List<Node> nodes) {
        Set<Node> cover = new HashSet<>();
        List<Edge> uncoveredEdges = new ArrayList<>(edges);
        Map<Node, Double> nodeRatios = getNodeRatios(nodes, edges);
        while (!uncoveredEdges.isEmpty()) {
            Node bestNode = getBestNode(nodeRatios);
            cover.add(bestNode);
            uncoveredEdges.removeIf(edge -> edge.includes(bestNode));
            nodeRatios.remove(bestNode);
        }
        return cover;
    }

    private static Map<Node, Double> getNodeRatios(List<Node> nodes, List<Edge> edges) {
        return nodes.stream().collect(Collectors.toMap(
                node -> node,
                node -> (double) node.getWeight() / edges.stream().filter(edge -> edge.includes(node)).count()));
    }

    private static Node getBestNode(Map<Node, Double> nodeRatios) {
        return nodeRatios.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException());
    }

    private static boolean isNewSolutionBetter(Set<Node> newSolution, Set<Node> bestSolution) {
        return newSolution != null && getTotalWeight(newSolution) < getTotalWeight(bestSolution);
    }

    private static NeighborResult findBestNeighbor(Set<Node> current, List<Node> nodes, List<Edge> edges,
            Queue<Node> tabuList, Set<Node> bestSolutionEver) {
        Set<Node> bestNeighbor = null;
        Node modifiedNode = null;
        int bestNeighborWeight = Integer.MAX_VALUE;
        int randomNeighborsToExplore = (int) (nodes.size() * 0.45);
        Collections.shuffle(nodes);
        List<Node> subsetOfNodes = nodes.subList(0, Math.min(randomNeighborsToExplore, nodes.size()));

        for (Node node : subsetOfNodes) {
            Set<Node> neighbor = toggleNodeInSet(current, node);
            if (isValidNeighbor(neighbor, edges, node, tabuList, bestSolutionEver)) {
                int neighborWeight = getTotalWeight(neighbor);
                if (neighborWeight < bestNeighborWeight) {
                    bestNeighbor = neighbor;
                    bestNeighborWeight = neighborWeight;
                    modifiedNode = node;
                }
            }
        }
        return new NeighborResult(bestNeighbor, modifiedNode);
    }

    private static Set<Node> toggleNodeInSet(Set<Node> nodeSet, Node node) {
        Set<Node> toggledSet = new HashSet<>(nodeSet);
        if (toggledSet.contains(node)) {
            toggledSet.remove(node);
        } else {
            toggledSet.add(node);
        }
        return toggledSet;
    }

    private static boolean isValidNeighbor(Set<Node> neighbor, List<Edge> edges, Node node, Queue<Node> tabuList,
            Set<Node> bestSolutionEver) {
        boolean coversAllEdges = edges.stream()
                .allMatch(edge -> neighbor.contains(edge.getNode1()) || neighbor.contains(edge.getNode2()));
        int neighborWeight = getTotalWeight(neighbor);
        return coversAllEdges && (!tabuList.contains(node) || neighborWeight < getTotalWeight(bestSolutionEver));
    }

    private static int getTotalWeight(Set<Node> solution) {
        return solution.stream().mapToInt(Node::getWeight).sum();
    }

    private static void updateTabuList(Queue<Node> tabuList, Node modifiedNode) {
        if (tabuList != null && modifiedNode != null) {
            tabuList.add(modifiedNode);
            int randomTabuTenure = new Random().nextInt(MAX_TABU_TENURE - MIN_TABU_TENURE + 1) + MIN_TABU_TENURE;
            while (tabuList.size() > randomTabuTenure) {
                tabuList.poll();
            }
        }
    }
}
