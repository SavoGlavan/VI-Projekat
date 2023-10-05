import java.lang.reflect.Array;
import java.util.ArrayList;

import ilog.concert.*;
import ilog.cplex.*;

public class CPLEX {

    public static void main(String[] args) {
    	
    	   for (int i = 1; i < 6; i++) {
    		   try {
    	            IloCplex cplex = new IloCplex();
    	         
    	            TabuGraph g = TabuMain.loadInstance(
    	                    "C:\\Users\\obrad\\Desktop\\VI\\VI-Projekat\\projekatVI\\instances\\vc_10_30_0" + i
                            + ".txt");
    	            int numVertices = g.getNodes().size();
    	            double[] weights = new double[g.getNodes().size()];
    	            for (int j = 0; j < weights.length; j++) {
    					weights[j] = g.getNodes().get(j).getWeight()+ 0.0;
    				}
    	             int[][] edges = new int [g.getEdges().size()][2];
    	             
    	             for (int j = 0; j < g.getEdges().size(); j++) {
    					edges[j][0] = g.getEdges().get(j).getNode1().getId();
    					edges[j][1] = g.getEdges().get(j).getNode2().getId();
    				}
    	           
    	                      
    	            IloNumVar[] x = cplex.boolVarArray(numVertices);

    	            cplex.addMinimize(cplex.scalProd(weights, x));

    	            for (int[] edge : edges) {
    	                int u = edge[0];
    	                int v = edge[1];
    	                cplex.addGe(cplex.sum(x[u], x[v]), 1);
    	            }

    	            if (cplex.solve()) {
    	                System.out.println("result: " + cplex.getObjValue());
    	                for (int j = 0; j < numVertices; j++) {
    	                   System.out.println("x[" + j + "] = " + cplex.getValue(x[j]));
    	                }
    	            } else {
    	                System.out.println("Model not solved");
    	            }

    	            cplex.end();
    	        } catch (IloException e) {
    	            e.printStackTrace();
    	        }
    				}
      
    }
}