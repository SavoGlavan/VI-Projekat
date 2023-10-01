import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    private static Graph loadInstance(String filePath) {
        Graph g = new Graph();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int edgeNumber=0;
            int graphSize = Integer.parseInt(bufferedReader.readLine());
            String line=bufferedReader.readLine().trim();
            String[] weights = line.trim().split("\\s+");


            for (int i = 0; i < graphSize; i++) {
                Node node = new Node(i, Integer.parseInt(weights[i]));
                g.getNodes().add(node);
            }

            for (int i = 0; i < graphSize; i++) {
                line=bufferedReader.readLine().trim();
                String[] neigbours = line.split("\\s");

                for (int j = i+1; j < graphSize; j++) {
                    if(Integer.parseInt(neigbours[j])==1){
                        g.getNodes().get(i).getNeighbours().add(g.getNodes().get(j));
                        g.getNodes().get(j).getNeighbours().add(g.getNodes().get(i));
                        edgeNumber++;
                    }
                }
            }
            g.setEdgeNumber(edgeNumber);
            bufferedReader.close();
            fileReader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return g;
    }

    public static void main(String[] args) {
        Graph g=loadInstance("C:\\Users\\Kmica\\IdeaProjects\\projekatVI\\instances\\vc_10_10_01.txt");
        System.out.println(g.getNodes());

        for(Node n: g.getNodes()){
            System.out.println(n.getNeighbours());
        }
        System.out.println(g.getEdgeNumber());
        System.out.println("Minimal vertex Cover");
        System.out.print(GRASP.findMinimalVertexCover(g));
    }
}
