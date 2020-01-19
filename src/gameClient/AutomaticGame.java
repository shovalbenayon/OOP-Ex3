package gameClient;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.node_data;
import elements.Fruit;


public class AutomaticGame {

    /**
     * finds game fruits
     * @param fruitsCol - the array in the beginning - empty array
     * @param dGraph - the dgraph og the game
     * @param Game - the game
     * @return - the new array after update and contain all the fruits
     */
    public static ArrayList<Fruit> getGameFruits(ArrayList<Fruit> fruitsCol, DGraph dGraph, game_service Game) {
        List<String> fruitsList = Game.getFruits();
        for (String myFruit : fruitsList)
            fruitsCol.add(new Fruit(myFruit, dGraph));

        return fruitsCol;
    }

    /**
     *Finding the Most valuable fruit
     * @param fruitsCol - fruits collection
     * @param Game - the game
     * @return the Most valuable fruit
     */
    public static Fruit getBestFruit(ArrayList<Fruit> fruitsCol, game_service Game) {
        Fruit bestFruit = fruitsCol.get(0);
        for (Fruit myFruit : fruitsCol)
            if (myFruit.getValue() > bestFruit.getValue())
                bestFruit = myFruit;

        return bestFruit;
    }

    /**
     * This metod remove the fruit from the collection
     * @param fruitsCol - fruits
     * @param Game - the game
     * @param fruit - the fruit to delete
     * @return
     */
    public static ArrayList<Fruit> removeBest(ArrayList<Fruit> fruitsCol, game_service Game, Fruit fruit) {
        for (Fruit myFruit : fruitsCol) {
            if (fruit.getValue() == myFruit.getValue()) {
                fruitsCol.remove(myFruit);
                return fruitsCol;
            }
        }
        return fruitsCol;
    }


    /**
     * finding the next node to go
     * @param fruitsCol
     * @param dGraph
     * @param Game
     * @param source
     * @return
     */
    public static int getNext(ArrayList<Fruit> fruitsCol, DGraph dGraph, game_service Game, int source) {
        Graph_Algo myGraph = new Graph_Algo();
        myGraph.init(dGraph);
        List<Integer> robotPath = new LinkedList<>();
        robotPath.add(source);
        double maxProfit = 0;
        int nextNode = source;

        for (Fruit myFruit : fruitsCol) {
            double rToFruitSrc = myGraph.shortestPathDist(source, myFruit.getEdge().getSrc());
            double rToFruitDest = myGraph.shortestPathDist(source, myFruit.getEdge().getDest());

            if (myFruit.getType() == -1) { // Android
                if (myGraph.shortestPathDist(source, myFruit.getEdge().getDest()) == 0) {
                    int ret = myFruit.getEdge().getSrc(); // Collect android from destination to source
                    fruitsCol.remove(myFruit);
                    return ret;
                }

                else if (myFruit.getValue() / rToFruitDest > maxProfit) {
                    maxProfit = myFruit.getValue() / rToFruitDest;
                    nextNode = myFruit.getEdge().getDest();
                }
            }

            else if (myFruit.getType() == 1) { // Apple
                if (myGraph.shortestPathDist(source, myFruit.getEdge().getSrc()) == 0) {
                    int ret2 = myFruit.getEdge().getDest();
                    fruitsCol.remove(myFruit);
                    return ret2;
                }
                else if (myFruit.getValue() / rToFruitSrc > maxProfit) {
                    maxProfit = myFruit.getValue() / rToFruitSrc;
                    nextNode = myFruit.getEdge().getSrc();
                }
            }
        }

        robotPath.add(nextNode);
        List<node_data> greedyPath = myGraph.TSP(robotPath);
        return greedyPath.get(1).getKey();
    }
}