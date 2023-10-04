
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
        // C:\Users\obradovica\Desktop\VI
        // projekat\VI-Projekat\projekatVI\instances\vc_10_10_01.txt
        for (int i = 1; i < 6; i++) {
            TabuGraph g = loadInstance(
                    "C:\\Users\\obradovica\\Desktop\\VI projekat\\VI-Projekat\\projekatVI\\instances\\vc_10_10_0" + i
                            + ".txt");
            List<Node> result = TabuSearch.vertexCoverTabuSearch(g.getNodes(), g.getEdges());
            int sum = 0;
            for (Node n : result) {
                sum += n.getWeight();
            }
            System.out.println("instanca vc_10_10_0: " + sum);
        }
        System.out.println("-----------------");
        for (int i = 1; i < 6; i++) {
            TabuGraph g = loadInstance(
                    "C:\\Users\\obradovica\\Desktop\\VI projekat\\VI-Projekat\\projekatVI\\instances\\vc_10_30_0" + i
                            + ".txt");
            List<Node> result = TabuSearch.vertexCoverTabuSearch(g.getNodes(), g.getEdges());
            int sum = 0;
            for (Node n : result) {
                sum += n.getWeight();
            }
            System.out.println("instanca vc_10_30_0: " + sum);
        }

        System.out.println("-----------------");
        for (int i = 1; i < 6; i++) {
            TabuGraph g = loadInstance(
                    "C:\\Users\\obradovica\\Desktop\\VI projekat\\VI-Projekat\\projekatVI\\instances\\vc_50_100_0" + i
                            + ".txt");
            List<Node> result = TabuSearch.vertexCoverTabuSearch(g.getNodes(), g.getEdges());
            int sum = 0;
            for (Node n : result) {
                sum += n.getWeight();
            }
            System.out.println("instanca vc_50_100_0: " + sum);
        }

        System.out.println("-----------------");
        for (int i = 1; i < 6; i++) {
            TabuGraph g = loadInstance(
                    "C:\\Users\\obradovica\\Desktop\\VI projekat\\VI-Projekat\\projekatVI\\instances\\vc_50_250_0" + i
                            + ".txt");
            List<Node> result = TabuSearch.vertexCoverTabuSearch(g.getNodes(), g.getEdges());
            int sum = 0;
            for (Node n : result) {
                sum += n.getWeight();
            }
            System.out.println("instanca vc_50_250_0: " + sum);
        }

        System.out.println("-----------------");
        for (int i = 1; i < 6; i++) {
            TabuGraph g = loadInstance(
                    "C:\\Users\\obradovica\\Desktop\\VI projekat\\VI-Projekat\\projekatVI\\instances\\vc_300_1000_0" + i
                            + ".txt");
            List<Node> result = TabuSearch.vertexCoverTabuSearch(g.getNodes(), g.getEdges());
            int sum = 0;
            for (Node n : result) {
                sum += n.getWeight();
            }
            System.out.println("instanca vc_300_1000_0: " + sum);
        }

        System.out.println("-----------------");
        for (int i = 1; i < 6; i++) {
            TabuGraph g = loadInstance(
                    "C:\\Users\\obradovica\\Desktop\\VI projekat\\VI-Projekat\\projekatVI\\instances\\vc_300_5000_0" + i
                            + ".txt");
            List<Node> result = TabuSearch.vertexCoverTabuSearch(g.getNodes(), g.getEdges());
            int sum = 0;
            for (Node n : result) {
                sum += n.getWeight();
            }
            System.out.println("instanca vc_300_5000_0: " + sum);
        }

        // System.out.println(g.getEdges());
        // List<Node> result = TabuSearch.vertexCoverTabuSearch(g.getNodes(),
        // g.getEdges());
        // System.out.println(result);
        // int sum = 0;
        // for (Node n : result) {
        // sum += n.getWeight();
        // }
        // System.out.println(sum);

    }
}
