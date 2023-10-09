
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class TabuMain {
    public static TabuGraph loadInstance(String filePath) {
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
                "C:\\Users\\obrad\\Desktop\\VI\\VI-Projekat\\projekatVI\\instances\\vc_10_10_03.txt");
        int sum = 0;
        int bestRes = Integer.MAX_VALUE;
        int worseRes = 0;
        for (int i = 0; i < 10; i++) {
            List<Node> result = TabuSearch.vertexCoverTabuSearch(g.getNodes(), g.getEdges());

            int single = 0;
            for (Node n : result) {
                sum += n.getWeight();
                single += n.getWeight();
            }

            if (single < bestRes) {
                bestRes = single;
            }
            if (single > worseRes) {
                worseRes = single;
            }
        }

        double avg = sum / 10.0;

        System.err.println("avg:");
        System.out.println(avg);
        System.err.println("best:");
        System.out.println(bestRes);
        System.err.println("worst:");
        System.out.println(worseRes);

    }
}
