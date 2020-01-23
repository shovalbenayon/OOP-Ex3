package gameClient;

import java.util.ArrayList;
import java.util.List;

import elements.Fruit;
import org.json.JSONException;
import org.json.JSONObject;

import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;

/**
 *This class represent an Automatic game
 * @author shoval benayon
 *
 */
public class AutomaticGame {
    //	public static game_service game;
    private static DGraph dgraph;
    private static Graph_Algo graph_algo;
    static List<Fruit> fruits = new ArrayList<>();

    /**
     * finds game fruits
     * @param fruitsCol - the array in the beginning - empty array
     * @param dGraph - the dgraph og the game
     * @param Game - the game
     */
    public static void getGameFruits(ArrayList<Fruit> fruitsCol, DGraph dGraph, game_service Game) {
        List<String> fruitsList = Game.getFruits();
        for (String myFruit : fruitsList)
            fruitsCol.add(new Fruit(myFruit, dGraph));

    }


    /**
     * This method finds the best fruit from the collection
     * @param FruitsCol - the collection of fruit
     * @return - The best fruit
     */
    public static Fruit findBestFruit(ArrayList<Fruit> FruitsCol) { // find an edge with fruit to put the robot in the node
        Fruit bestFruit = FruitsCol.get(0);
        for (Fruit myFruit : FruitsCol)
            if (myFruit.getValue() > bestFruit.getValue())
                bestFruit = myFruit;

        return bestFruit;
    }

    /**
     * This method remove the best fruit from the collection so the next robot won't go there
     * @param fruits - The collection of fruit
     * @param fruit - The best fruit
     * @return - The fruits collection after deleting the best fruit
     */
    public static ArrayList<Fruit> removeBest(ArrayList<Fruit> fruits, Fruit fruit){
        for (Fruit myFruit : fruits) {
            if (fruit.getValue() == myFruit.getValue()) {
                fruits.remove(myFruit);
                return fruits;
            }
        }
        return fruits;
    }
    /**
     * This method move the robots to the next fruit using shortest path algorithms
     * @param game - The game
     * @param fruits - The fruits collection from the game
     */
    public static game_service moveRobots(game_service game, List<Fruit> fruits) {
        AutomaticGame.fruits = fruits;
        dgraph = new DGraph();
        dgraph.init(game.getGraph());
        AutomaticGame.graph_algo = new Graph_Algo(dgraph);
        List<String> log = game.move();
        if (log != null) {
            for (int i = 0; i < log.size(); i++) {
                String robot_json = log.get(i);
                try {
                    JSONObject line = new JSONObject(robot_json);
                    JSONObject ttt = line.getJSONObject("Robot");
                    int robotID = ttt.getInt("id");
                    int src = ttt.getInt("src");
                    int dest = ttt.getInt("dest");
                    if (dest == -1) {
                        edge_data nextEdge = ClosestEdge(src, AutomaticGame.fruits);
                        List<node_data> nodesPath = graph_algo.shortestPath(src, nextEdge.getSrc());
                        if (nodesPath == null) {
                            game.chooseNextEdge(robotID, nextEdge.getDest());
                        } else {
                            for (node_data n : nodesPath) {
                                dest = n.getKey();
                                game.chooseNextEdge(robotID, dest);
                            }
                            game.chooseNextEdge(robotID, nextEdge.getDest());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return game;
    }

    /**
     * this function find the closet edge with a  in shortest path by using
     * "shortest path" algorithm from the class "graph_algo"and return this edge.
     * @param robotPos the current position of the robot
     * @param fruits the list of the current fruits
     * @return the closest edge with a fruit.
     */

    private static edge_data ClosestEdge(int robotPos, List<Fruit> fruits) { // give the edge with the fruit with the	shortest path.
        double minPath = Double.POSITIVE_INFINITY;
        int bestSrc = robotPos;
        int bestDest = robotPos;
        double temp = -1;
        for (int i = 0; i < fruits.size(); i++) {
            Fruit currentFruit = fruits.get(i);
            edge_data e = dgraph.getEdge(currentFruit.getEdge().getSrc() , currentFruit.getEdge().getDest());
            temp = graph_algo.shortestPathDist(robotPos, e.getSrc());
            if ( temp < minPath) {
                minPath = temp;
                bestSrc = e.getSrc();
                bestDest = e.getDest();
            }
        }
        return dgraph.getEdge(bestSrc, bestDest);
    }

}