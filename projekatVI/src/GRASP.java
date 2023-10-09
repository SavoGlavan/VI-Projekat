import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class GRASP {
    private static ArrayList<NodeEdgePair>minVertexCover;
    private static ArrayList<NodeEdgePair> candidates=new ArrayList<>();

    public static int vertexCoverWeight;

    public static int vertexCoverEdgeNumber;

    public static ArrayList<Node> extractNodes(ArrayList<NodeEdgePair> list){
        ArrayList<Node> nodes=new ArrayList<>();
        for(NodeEdgePair nodeEdgePair:list){
            nodes.add(nodeEdgePair.getNode());
        }
        return nodes;
    }
    private static int calculateEdgeNumber(ArrayList<Node> vertexCover, ArrayList<Node> neigbours){
        int edge_number=0;
        for(Node neigbour:neigbours){
            if(!vertexCover.contains(neigbour))
                edge_number++;
        }
        return edge_number;
    }
    private static CoverInfo recomputeCandiateEdgeCoverage(ArrayList<NodeEdgePair> candidateSolution){
        int edge_sum=0;
        int weight_sum=0;
        Collections.sort(candidateSolution);
        Collections.reverse(candidateSolution);
        ArrayList<NodeEdgePair> newCover=new ArrayList<>();
        NodeEdgePair best=candidateSolution.get(0);
        newCover.add(new NodeEdgePair(best.getNode(), best.getNode().getNeighbours().size()));
        candidateSolution.remove(best);
        edge_sum+=best.getNode().getNeighbours().size();
        weight_sum+=best.getNode().getWeight();
        ArrayList<Node>nodes=extractNodes(candidateSolution);
        for(Node n:nodes){
            int edgeNumber=calculateEdgeNumber(extractNodes(newCover),n.getNeighbours());
            if(edgeNumber==0)
                continue;
            newCover.add(new NodeEdgePair(n, edgeNumber));
            edge_sum+=edgeNumber;
            weight_sum+=n.getWeight();
        }
        candidateSolution=newCover;
        return new CoverInfo(candidateSolution,weight_sum,edge_sum);
    }
    private static int recomputeEdgeCoverage(){
        int sum=0;
        Collections.sort(minVertexCover);
        Collections.reverse(minVertexCover);
        ArrayList<NodeEdgePair> newCover=new ArrayList<>();
        NodeEdgePair best=minVertexCover.get(0);
        newCover.add(new NodeEdgePair(best.getNode(), best.getNode().getNeighbours().size()));
        minVertexCover.remove(best);
        sum+=best.getNode().getNeighbours().size();
        ArrayList<Node>nodes=extractNodes(minVertexCover);
        for(Node n:nodes){
            int edgeNumber=calculateEdgeNumber(extractNodes(newCover),n.getNeighbours());
            if(edgeNumber==0)
                continue;
            newCover.add(new NodeEdgePair(n, edgeNumber));
            sum+=edgeNumber;
        }
        minVertexCover=newCover;
        return sum;
    }
    private static ArrayList<NodeEdgePair> updateCandidateEdgeNumbers(ArrayList<NodeEdgePair> candidateSolution, ArrayList<NodeEdgePair> candidates){
        ArrayList<NodeEdgePair> updatedCandidates=new ArrayList<>();
        ArrayList<Node> candidateNodes=extractNodes(candidates);
        ArrayList<Node> vertexNodes=extractNodes(candidateSolution);
        for(Node candidate:candidateNodes){
            int edgeNumber=calculateEdgeNumber(vertexNodes, candidate.getNeighbours());
            updatedCandidates.add(new NodeEdgePair(candidate,edgeNumber));

        }
        candidates=updatedCandidates;

        return candidates;
    }
    private static void updateEdgeNumbers(){
        ArrayList<NodeEdgePair> updatedCandidates=new ArrayList<>();
        ArrayList<Node> candidateNodes=extractNodes(candidates);
        ArrayList<Node> vertexNodes=extractNodes(minVertexCover);
        for(Node candidate:candidateNodes){
            int edgeNumber=calculateEdgeNumber(vertexNodes, candidate.getNeighbours());
            updatedCandidates.add(new NodeEdgePair(candidate,edgeNumber));

        }
        candidates=updatedCandidates;
    }

    private static void init(ArrayList<Node> nodes){
        Node first=null;
        NodeEdgePair best=null;
        float bestRatio=Float.POSITIVE_INFINITY;
        //Greedy inicijalizacija
        for(Node n:nodes){
            float ratio=(float) (n.getWeight()*1.5)/(n.getNeighbours().size()*2);
            if(ratio<bestRatio){
                first=n;
                bestRatio=ratio;
            }
            if(ratio==bestRatio){
                if(n.getNeighbours().size()>first.getNeighbours().size()){
                    first=n;
                }
            }
        }
        minVertexCover.add(new NodeEdgePair(first, first.getNeighbours().size()));
        nodes.remove(first);

        //Inijalizacija kandidat skupa
        for(Node n:nodes){
            if(n.getNeighbours().size()==0){
                continue;
            }
            else{
                int edgeNumber=calculateEdgeNumber(extractNodes(minVertexCover), n.getNeighbours());
                candidates.add(new NodeEdgePair(n,edgeNumber));
            }

        }
    }
    private static NodeEdgePair findBestCandidate(){
        //Najbolji kandidat se bira nasumično od 25% najboljih kandidata
        Random r=new Random();
        Collections.sort(candidates);
        Collections.reverse(candidates);
        if(candidates.get(0).getRatio()==Float.POSITIVE_INFINITY){
            return null;
        }
        int range=candidates.size()/4;
        if(range==0)
            range=1;
        int index=r.nextInt(range);
        NodeEdgePair bestCandidate=candidates.get(index);
        minVertexCover.add(bestCandidate);
        candidates.remove(bestCandidate);
        vertexCoverEdgeNumber = recomputeEdgeCoverage();

        vertexCoverWeight+=bestCandidate.getNode().getWeight();
        return bestCandidate;
    }
    private static void localSearch(){
        Random r=new Random();
        int candidateCoverTotalWeight=vertexCoverWeight;
        int candidateCoverEdgeNumber=vertexCoverEdgeNumber;

        for(int i=0;i<50;i++){
            if(candidates.isEmpty())
                return;
            ArrayList<NodeEdgePair> candidateSolution=new ArrayList<>(minVertexCover);
            ArrayList<NodeEdgePair> potentialCandidates=new ArrayList<>(candidates);
            //Izbacivanje nasumičnog cvora
            int index=r.nextInt(candidateSolution.size());
            NodeEdgePair removedPair=candidateSolution.get(index);
            candidateCoverTotalWeight-=removedPair.getNode().getWeight();
            candidateCoverEdgeNumber-=removedPair.getEdgeNumber();
            candidateSolution.remove(removedPair);
            potentialCandidates=updateCandidateEdgeNumbers(candidateSolution, potentialCandidates);

            Collections.sort(potentialCandidates);
            Collections.reverse(potentialCandidates);

            if(potentialCandidates.get(0).getRatio()==Float.POSITIVE_INFINITY)
                break;
            //Dodavanje nasumičnog cvora
            index=r.nextInt(potentialCandidates.size());
            NodeEdgePair addedPair=potentialCandidates.get(index);
            while(addedPair.getRatio()==Float.POSITIVE_INFINITY){
                index=r.nextInt(potentialCandidates.size());
                addedPair=potentialCandidates.get(index);
            }
            candidateSolution.add(addedPair);
            CoverInfo info=recomputeCandiateEdgeCoverage(candidateSolution);
            candidateCoverEdgeNumber=info.getCoverEdgeNumber();
            candidateCoverTotalWeight= info.getCoverTotalWeight();
            candidateSolution=info.getVertexCover();

            //Provjera da li je kandidate rješenje bolje
            float ratioCover= (float) (vertexCoverWeight*1.5)/(vertexCoverEdgeNumber*2);
            float ratioCandidate=(float) (candidateCoverTotalWeight*1.5)/(candidateCoverEdgeNumber*2);
            if(ratioCandidate<ratioCover && candidateCoverEdgeNumber>=vertexCoverEdgeNumber){
                minVertexCover=candidateSolution;
                potentialCandidates.remove(addedPair);
                potentialCandidates.add(removedPair);
                candidates=potentialCandidates;
                vertexCoverWeight=candidateCoverTotalWeight;
                vertexCoverEdgeNumber=candidateCoverEdgeNumber;
                updateEdgeNumbers();
                vertexCoverEdgeNumber=recomputeEdgeCoverage();
                i=0;
            }

        }


    }
    public static ArrayList<NodeEdgePair>findMinimalVertexCover(Graph g){
        minVertexCover=new ArrayList<>();
        candidates=new ArrayList<>();
        vertexCoverWeight=0;
        vertexCoverEdgeNumber=0;
        init(g.getNodes());

        while (vertexCoverEdgeNumber!=g.getEdgeNumber()){
            NodeEdgePair bestCandidate=findBestCandidate();
            if(bestCandidate==null){
                break;
            }
            localSearch();
        }
        System.out.println("Nodes: "+minVertexCover);
        System.out.println("Total weight: "+vertexCoverWeight);
        return minVertexCover;
    }
}
