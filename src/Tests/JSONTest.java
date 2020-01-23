package Tests;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import elements.Robot;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JSONTest {

    private game_service game = Game_Server.getServer(23);
    private DGraph dGraph = new DGraph(game.getGraph());;
    private List<Robot> RobotsCol = new ArrayList<>();

    @Test
    public void CheckNodesAndEDges(){
        assertEquals(dGraph.getV().size() , 48);
        assertEquals(dGraph.getE(1).size() , 5);
        assertEquals(dGraph.getE(2).size() , 4);
        assertEquals(dGraph.getE(3).size() , 4);
        assertEquals(dGraph.getE(4).size() , 3);
        assertEquals(dGraph.getE(5).size() , 3);
        assertEquals(dGraph.getE(6).size() , 3);
    }

    @Test
    public void CheckData(){
        JSONObject ForFruits;
        int Fruits_num = 0;
        int Robots_num = 0;
        try {
            ForFruits = new JSONObject(game.toString());
            JSONObject fruits = ForFruits.getJSONObject("GameServer");
            Fruits_num = fruits.getInt("fruits");
            Robots_num = fruits.getInt("robots");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals(Fruits_num, 6);
        assertEquals(Robots_num, 3);

    }
}
