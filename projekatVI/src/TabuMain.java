
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TabuMain {
    private static TabuGraph loadInstance(String filePath) {
        TabuGraph g = new TabuGraph();
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int edgeNumber = 0;
            int TabuGraphSize = Integer.parseInt(bufferedReader.readLine());
            String line = bufferedReader.readLine().trim();
            String[] weights = line.trim().split("\\s+");

            for (int i = 0; i < TabuGraphSize; i++) {
                Node node = new Node(i, Integer.parseInt(weights[i]));
                g.getNodes().add(node);
            }

            for (int i = 0; i < TabuGraphSize; i++) {
                line = bufferedReader.readLine().trim();
                String[] neigbours = line.split("\\s");

                for (int j = i + 1; j < TabuGraphSize; j++) {
                    if (Integer.parseInt(neigbours[j]) == 1) {
                        Node u = g.getNodes().get(i);
                        Node v = g.getNodes().get(j);
                        g.getNodes().get(i).getNeighbours().add(v);
                        g.getNodes().get(j).getNeighbours().add(u);
                        g.getEdges().add(new Edge(u, v));
                        edgeNumber++;
                    }
                }
            }
            g.setEdgeNumber(edgeNumber);
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return g;
    }

    public static void main(String[] args) {
        TabuGraph g = loadInstance(
                "C:\\Users\\obrad\\Desktop\\VI\\VI-Projekat\\projekatVI\\instances\\vc_25_150_04.txt");

        System.out.println(g.getEdges());
        List<Node> result = TabuSearch.vertexCoverTabuSearch(g.getNodes(), g.getEdges());
        System.out.println(result);
        int sum = 0;
        for(Node n : result){
            sum+= n.getWeight();
        }
        System.out.println(sum);

    }
}
