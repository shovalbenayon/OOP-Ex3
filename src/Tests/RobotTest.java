package Tests;

import Server.Game_Server;
import Server.game_service;
import elements.Robot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {
    Collection<Robot> robots_col = new LinkedList<>();
    @BeforeEach
    void setUp() {
        game_service game =  Game_Server.getServer(4);

        List<String> robots = game.getRobots();
        for (String robot: robots) {
            Robot newRobot = new Robot(robot);
            robots_col.add(newRobot);
        }
    }

    @Test
    void getId() {
        for (Robot r:
             robots_col) {
            r.getId();
        }
    }

    @Test
    void getPos() {
        for (Robot r:
                robots_col) {
            r.getPos();
        }
    }


    @Test
    void getSource() {
        for (Robot r:
                robots_col) {
            r.getSource();
        }
    }

    @Test
    void getDestination() {
        for (Robot r:
                robots_col) {
            r.getDestination();
        }
    }

    @Test
    void setSource() {
        for (int i = 0; i < 10; i++) {
            for (Robot r:
                    robots_col) {
                r.setSource(i);
                assertEquals(r.getSource() , i);
            }

        }

    }

    @Test
    void setDestination() {
        for (int i = 0; i <10 ; i++) {
            for (Robot r:robots_col) {
                r.setDestination(i);
                assertEquals(r.getDestination(),i);
            }
        }
    }
}