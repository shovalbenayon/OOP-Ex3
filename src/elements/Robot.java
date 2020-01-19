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
    private int source;
    private int destination;
    private Point3D pos;
    private double speed;

    public Robot(String s) {
        JSONObject jsonRobot ;
        try {
            jsonRobot = new JSONObject(s);
            JSONObject robot = jsonRobot.getJSONObject("Robot");
            this.id = robot.getInt("id");
            this.speed = robot.getDouble("speed");
            this.source = robot.getInt("src");
            this.destination = robot.getInt("dest");
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
        this.id = 0;
        this.speed = 0;
        this.pos = null;
    }

    public int getID() { return this.id; }

    public int getId() {
        return id;
    }

    public Point3D getPos() { return pos; }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }






}
