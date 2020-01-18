package elements;

import dataStructure.DataNode;
import dataStructure.EdgeNode;
import dataStructure.edge_data;
import dataStructure.node_data;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

public class Robot {

    private int id;
    private Point3D pos;
    private double speed;
    private edge_data curr_edge;
    private node_data curr_node;
    private double money;

    public Robot(String s) {
        JSONObject jsonRobot ;
        try {
            jsonRobot = new JSONObject(s);
            JSONObject robot = jsonRobot.getJSONObject("Robot");
            this.id = robot.getInt("id");
            this.money = robot.getDouble("value");
            this.speed = robot.getDouble("speed");
            String []splits = robot.getString("pos").split(",");
            double x = Double.parseDouble(splits[0]);
            double y = Double.parseDouble(splits[1]);
            double z = Double.parseDouble(splits[2]);
            this.pos = new Point3D(x, y, z);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public Robot() {
        this.money = 0;
        this.id = 0;
        this.curr_node = null;
        this.curr_edge = null;
        this.speed = 0;
        this.pos = null;
    }

    public int getID() { return this.id; }

    public int getId() {
        return id;
    }

    public Point3D getPos() { return pos; }

    public edge_data getCurr_edge() {
        return curr_edge;
    }

    public node_data getCurr_node() {
        return curr_node;
    }




}
