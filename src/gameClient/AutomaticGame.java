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
 *
 *
 * @author
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


    public static Fruit findFirstPos(ArrayList<Fruit> FruitsCol) { // find an edge with fruit to put the robot in the node
        Fruit bestFruit = FruitsCol.get(0);
        for (Fruit myFruit : FruitsCol)
            if (myFruit.getValue() > bestFruit.getValue())
                bestFruit = myFruit;

        return bestFruit;
    }
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
     * the main function , to move the robot with the server to the next edge in shortest path.
     * the moves and the time left is printing.
     * @param game the game from the server
     * @param gg the graph of the game
     * @param fruits the current fruits in the game
     */
    public static game_service moveRobots(game_service game, DGraph gg, List<Fruit> fruits) {
        AutomaticGame.fruits = fruits;
        dgraph = new DGraph();
        dgraph.init(game.getGraph());
        AutomaticGame.graph_algo = new Graph_Algo(dgraph);
        List<String> log = game.move();
        if (log != null) {
            long time = game.timeToEnd();
            for (int i = 0; i < log.size(); i++) {
                String robot_json = log.get(i);
                try {
                    JSONObject line = new JSONObject(robot_json);
                    JSONObject ttt = line.getJSONObject("Robot");
                    String pos = ttt.getString("pos");
                    int robotID = ttt.getInt("id");
                    int src = ttt.getInt("src");
                    int dest = ttt.getInt("dest");
                    if (dest == -1) {
                        edge_data e = nextEdge(src, AutomaticGame.fruits);
                        List<node_data> nodes = graph_algo.shortestPath(src, e.getSrc());
                        if (nodes == null) {
                            game.chooseNextEdge(robotID, e.getDest());
                        } else {
                            for (node_data n : nodes) {
                                dest = n.getKey();
                                game.chooseNextEdge(robotID, dest);
                            }
                            game.chooseNextEdge(robotID, e.getDest());
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

    private static edge_data nextEdge(int robotPos, List<Fruit> fruits) { // give the edge with the fruit with the	shortest path.
        double minPath = Double.POSITIVE_INFINITY;
        int bestSrc = robotPos;
        int bestDest = robotPos;
        double temp = -1;
        int indF = -1;
        for (int i = 0; i < fruits.size(); i++) {
            edge_data e = findEdgeFruit(dgraph, fruits.get(i));
            temp = graph_algo.shortestPathDist(robotPos, e.getSrc());
            if ( temp < minPath) {
                minPath = temp;
                bestSrc = e.getSrc();
                bestDest = e.getDest();
            }
        }
        //	autoGame.fruits.remove(indF);
        return dgraph.getEdge(bestSrc, bestDest);
    }

    /**
     * this function find an edge with a given fruit, by compare the adding distance from 2 nodes to the fruit, and the distance between the 2 nodes.
     * the function return the right edge considering the type of the fruit (banana for down , apple for up edge).
     * @param graph the graph of the game
     * @param fr the current fruit on the game to check
     * @return the edge with the given fruit.
     */
    public static edge_data findEdgeFruit(DGraph graph, Fruit fr) {
        int src = 0;
        int dest = 0;

        for (node_data n : graph.getV()) {
            for (edge_data e : graph.getE(n.getKey())) {
                double dFruit = (Math.sqrt(Math.pow(n.getLocation().x() - fr.getPos().x(), 2)
                        + Math.pow(n.getLocation().y() - fr.getPos().y(), 2)))
                        + Math.sqrt(Math.pow(graph.getNode(e.getDest()).getLocation().x() - fr.getPos().x(), 2)
                        + Math.pow(graph.getNode(e.getDest()).getLocation().y() - fr.getPos().y(), 2));
                double dNodes = (Math
                        .sqrt(Math.pow(n.getLocation().x() - graph.getNode(e.getDest()).getLocation().x(), 2)
                                + Math.pow(n.getLocation().y() - graph.getNode(e.getDest()).getLocation().y(), 2)));
                double highNode = graph.getNode(e.getSrc()).getLocation().y()
                        - graph.getNode(e.getDest()).getLocation().y();
                if (Math.abs(dNodes - dFruit) < 0.000001) {
                    if (fr.getType() == -1) { // if its andoroid
                        if (highNode < 1) { // if the direction is from lower id node to higher id node
                            src = graph.getNode(e.getSrc()).getKey();
                            dest = graph.getNode(e.getDest()).getKey();
                        } else {
                            src = graph.getNode(e.getDest()).getKey();
                            dest = graph.getNode(e.getSrc()).getKey();
                        }
                    } else { // if its apple
                        if (highNode > 1) { // if the direction is from higher id node to lower id node
                            src = graph.getNode(e.getSrc()).getKey();
                            dest = graph.getNode(e.getDest()).getKey();
                        } else {
                            src = graph.getNode(e.getDest()).getKey();
                            dest = graph.getNode(e.getSrc()).getKey();

                        }
                    }
                }
            }
        }
        return graph.getEdge(src, dest);
    }

}