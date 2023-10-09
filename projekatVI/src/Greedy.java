import java.util.ArrayList;

public class Greedy {

    private static NodeEdgePair findBestCandidate(ArrayList<Node> vertexCover, ArrayList<Node> candidates ){
        Node best=null;
        float best_score=Float.POSITIVE_INFINITY;
        NodeEdgePair bestPair=new NodeEdgePair(null, 0);
        for(Node node:candidates){
            int edge_number=0;
            for(Node neigbour:node.getNeighbours()){
                if(!vertexCover.contains(neigbour))
                    edge_number++;
            }
            if (edge_number==0){
                continue;
            }
            float score=node.getWeight()/edge_number;
            if(score<best_score){
                bestPair=new NodeEdgePair(node,edge_number);
                best_score=score;
            }
        }
        return bestPair;
    }
    public static ArrayList<Node>findMinimalVertexCover(Graph g){
        ArrayList<Node>minVertexCover=new ArrayList<>();
        ArrayList<Node> candidates=g.getNodes();
        int coveredEdgeNumber=0;
        while (coveredEdgeNumber!=g.getEdgeNumber()){
            NodeEdgePair bestCandidate=findBestCandidate(minVertexCover, candidates);
            minVertexCover.add(bestCandidate.getNode());
            candidates.remove(bestCandidate.getNode());
            coveredEdgeNumber+=bestCandidate.getEdgeNumber();
        }

        return minVertexCover;
    }
}
