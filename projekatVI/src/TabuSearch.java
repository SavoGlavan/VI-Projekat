import java.util.*;
import java.util.stream.Collectors;

public class TabuSearch {

    // Maximum size of the tabu list.
    private static final int MAX_TABU_TENURE = 15;
    private static final int MIN_TABU_TENURE = 5;
    // Maximum number of iterations without improvement before stopping the search.
    private static final int MAX_ITERATIONS_WITHOUT_IMPROVEMENT = 100;

    // Represents a neighbor solution and the node which was modified to produce
    // this neighbor.
    static class NeighborResult {
        Set<Node> neighbor; // The neighboring solution.
        Node modifiedNode; // The node which was modified (added/removed) to get to this neighbor.

        NeighborResult(Set<Node> neighbor, Node modifiedNode) {
            this.neighbor = neighbor;
            this.modifiedNode = modifiedNode;
        }
    }

    public static List<Node> vertexCoverTabuSearch(List<Node> nodes, List<Edge> edges) {
        // Initial solution using a greedy approach.
        Set<Node> currentSolution = greedyInitialization(edges, nodes);

        // Best solution found so far.
        Set<Node> bestSolution = new HashSet<>(currentSolution);

        // List of recent moves, used to forbid some moves for a limited number of
        // iterations.
        Queue<Node> tabuList = new LinkedList<>();

        int iterationWithoutImprovement = 0;

        // Main search loop.
        while (iterationWithoutImprovement < MAX_ITERATIONS_WITHOUT_IMPROVEMENT) {
            // Find the best neighbor which is not tabu or breaks the tabu but offers a
            // better solution.
            NeighborResult bestNeighborResult = findBestNeighbor(currentSolution, nodes, edges, tabuList, bestSolution);

            // If the new solution is better than the best known, update the best solution.
            if (isNewSolutionBetter(bestNeighborResult.neighbor, bestSolution)) {
                bestSolution = bestNeighborResult.neighbor;
                iterationWithoutImprovement = 0;
            } else {
                iterationWithoutImprovement++;
            }

            // Update the tabu list with the node that was toggled.
            updateTabuList(tabuList, bestNeighborResult.modifiedNode);

            // Move to the best neighbor solution.
            currentSolution = (bestNeighborResult.neighbor != null) ? new HashSet<>(bestNeighborResult.neighbor)
                    : currentSolution;

            if (new Random().nextDouble() < 0.02) {
                Node randomNode = nodes.get(new Random().nextInt(nodes.size()));
                currentSolution = toggleNodeInSet(currentSolution, randomNode);
            }

        }

        // Convert set to list and return.
        return new ArrayList<>(bestSolution);
    }

    // Compute an initial solution using a greedy strategy.
    private static Set<Node> greedyInitialization(List<Edge> edges, List<Node> nodes) {
        // The current vertex cover.
        Set<Node> cover = new HashSet<>();

        // Edges not yet covered by the nodes in 'cover'.
        List<Edge> uncoveredEdges = new ArrayList<>(edges);

        // Node-to-ratio mapping to decide the next best node to add in the cover.
        Map<Node, Double> nodeRatios = getNodeRatios(nodes, edges);

        // While there are edges left uncovered, continue adding nodes.
        while (!uncoveredEdges.isEmpty()) {
            Node bestNode = getBestNode(nodeRatios);
            cover.add(bestNode);
            // Remove edges that are now covered.
            uncoveredEdges.removeIf(edge -> edge.includes(bestNode));
            // Remove the best node from consideration.
            nodeRatios.remove(bestNode);
        }

        return cover;
    }

    // Computes the ratio of weight to the number of edges a node covers. Used in
    // the greedy initialization.
    private static Map<Node, Double> getNodeRatios(List<Node> nodes, List<Edge> edges) {
        return nodes.stream().collect(Collectors.toMap(
                node -> node,
                node -> (double) node.getWeight() / edges.stream().filter(edge -> edge.includes(node)).count()));
    }

    // Returns the node with the minimum weight-to-edges-covered ratio. Used in the
    // greedy initialization.
    private static Node getBestNode(Map<Node, Double> nodeRatios) {
        return nodeRatios.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new RuntimeException("No node found"));
    }

    // Determines if the new solution has a lower total weight than the current best
    // solution.
    private static boolean isNewSolutionBetter(Set<Node> newSolution, Set<Node> bestSolution) {
        return newSolution != null && getTotalWeight(newSolution) < getTotalWeight(bestSolution);
    }

    // Searches for the best neighboring solution.
    private static NeighborResult findBestNeighbor(Set<Node> current, List<Node> nodes, List<Edge> edges,
            Queue<Node> tabuList, Set<Node> bestSolutionEver) {
        Set<Node> bestNeighbor = null;
        Node modifiedNode = null;
        int bestNeighborWeight = Integer.MAX_VALUE;
        int randomNeighborsToExplore = (int) (nodes.size() * 0.8); // e.g., explore 20% of all nodes
        Collections.shuffle(nodes);
        List<Node> subsetOfNodes = nodes.subList(0, Math.min(randomNeighborsToExplore, nodes.size()));

        for (Node node : subsetOfNodes) {
            // Toggle the presence of the node in the current solution to produce a
            // neighbor.
            Set<Node> neighbor = toggleNodeInSet(current, node);

            // Check if the neighbor is valid.
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

    // Toggles the presence of a node in a set (if present, remove it; if absent,
    // add it).
    private static Set<Node> toggleNodeInSet(Set<Node> nodeSet, Node node) {
        Set<Node> toggledSet = new HashSet<>(nodeSet);
        if (toggledSet.contains(node)) {
            toggledSet.remove(node);
        } else {
            toggledSet.add(node);
        }
        return toggledSet;
    }

    // Checks if a solution is a valid neighbor.
    private static boolean isValidNeighbor(Set<Node> neighbor, List<Edge> edges, Node node, Queue<Node> tabuList,
            Set<Node> bestSolutionEver) {
        boolean coversAllEdges = edges.stream()
                .allMatch(edge -> neighbor.contains(edge.getNode1()) || neighbor.contains(edge.getNode2()));

        int neighborWeight = getTotalWeight(neighbor);
        return coversAllEdges && (!tabuList.contains(node) || neighborWeight < getTotalWeight(bestSolutionEver));
    }

    // Computes the total weight of the nodes in a solution.
    private static int getTotalWeight(Set<Node> solution) {
        return solution.stream().mapToInt(Node::getWeight).sum();
    }

    // Adds a node to the tabu list and ensures that the tabu list does not exceed
    // its maximum size.
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