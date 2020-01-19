package Tests;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import elements.Fruit;
import gameClient.AutomaticGame;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutomaticGameTest {

    @Test
    public void testGetGameFruits() {
        DGraph gameGraph = new DGraph();
        game_service myGame = Game_Server.getServer(0);
        gameGraph.init(myGame.getGraph());
        List<String> fruitsList = myGame.getFruits();
        assertEquals(fruitsList.size(), 1);
        Fruit newFruit = new Fruit(fruitsList.get(0), gameGraph);
        assertEquals(newFruit.getType(), -1); // Banana
    }

    @Test
    public void testGetBestFruit() {
        DGraph gameGraph = new DGraph();
        game_service myGame = Game_Server.getServer(0);
        gameGraph.init(myGame.getGraph());
        ArrayList<Fruit> fruits = new ArrayList<>();
        AutomaticGame.getGameFruits(fruits, gameGraph, myGame);
        assertEquals(fruits.get(0), AutomaticGame.getBestFruit(fruits));
    }

    @Test
    public void removeBestFruit() {
        DGraph gameGraph = new DGraph();
        game_service myGame = Game_Server.getServer(0);
        gameGraph.init(myGame.getGraph());
        ArrayList<Fruit> fruits = new ArrayList<>();
        AutomaticGame.getGameFruits(fruits, gameGraph, myGame);
        AutomaticGame.removeBest(fruits, fruits.get(0));
        assertEquals(fruits.size(), 0);
    }
}
