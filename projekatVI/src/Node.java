import java.util.ArrayList;
import java.util.Scanner;

public class Node {
    private ArrayList<Node> neighbours;

    int id;
    private int weight;

    public Node(int id, int weight) {
        this.id=id;
        this.weight =weight;
        this.neighbours=new ArrayList<>();
    }

    public ArrayList<Node> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(ArrayList<Node> neighbours) {
        this.neighbours = neighbours;
    }

    public int getId() {
        return id;
    }

       public int getWeight() {
        return weight;
    }
    public String toString(){
        return "Node "+id+" - " + weight;
    }
}
