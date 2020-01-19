package Tests;

import Server.Game_Server;
import Server.game_service;
import elements.Fruit;
import elements.Robot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FruitTest {
    Collection<Fruit> fruits_col = new LinkedList<>();
    @BeforeEach
    void setUp() {
        game_service game =  Game_Server.getServer(4);
        String g = game.getGraph();
        List<String> fruits = game.getFruits();

        for (String fruit : fruits){
            Fruit newFruit = new Fruit(fruit);
            fruits_col.add(newFruit);
        }

    }

    @Test
    void getPos() {
        for (Fruit f:
             fruits_col) {
            f.getPos();

        }
    }

    @Test
    void getValue() {
        for (Fruit f:
                fruits_col) {
            f.getValue();

        }
    }

    @Test
    void getType() {
        for (Fruit f:
                fruits_col) {
            f.getType();

        }
    }

    @Test
    void getEdge() {
        for (Fruit f:
                fruits_col) {
            f.getEdge();

        }
    }
}