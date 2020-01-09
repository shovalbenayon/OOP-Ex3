package dataStructure;

import oop_utils.OOP_Point3D;
import utils.Point3D;

public class clientRobot {

    private int _id;
    private Point3D _pos;
    private double _speed;
    private edge_data _curr_edge;
    private node_data _curr_node;
    private double _money;

    public clientRobot(String s) {
        String []splits = s.split(":");

        int id = intFromString(splits[2]);
        double val = valueFromString(splits[3]);
        int src = intFromString(splits[4]);
        int dst = intFromString(splits[5]);
        int speed = intFromString(splits[6]);
        Point3D pos = posFromString(splits[7]);

        this._money = val;
        this._id = id;
        this._curr_node =  new DataNode(src);
        this._curr_edge = new EdgeNode(src, dst);
        this._speed = speed;
        this._pos = new Point3D(pos.x() , pos.y() , pos.z());

    }
    public int intFromString(String s){
        String id = s.substring(0 , s.indexOf(","));
        return Integer.parseInt(id);
    }
    public double valueFromString(String s){
        String val = s.substring(0 , s.indexOf(","));
        return Double.parseDouble(val);
    }


    public Point3D posFromString(String s){
        s = s.substring(1 , s.length() -3);
        String[] split = s.split(",");
        double x= Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Double.parseDouble(split[2]);
        return new Point3D(x, y, z);
    }
    public int getID() {
        return this._id;

    }

    public Point3D getLocation() {
        return this._pos;
    }


    public double getMoney() {
        return this._money;
    }


    public double getSpeed() {
        return this._speed;
    }


    public void setSpeed(double v) {
        this._speed = v;
    }

    public node_data getNode(){
        return this._curr_node;
    }
    public edge_data getEdge(){
        return this._curr_edge;
    }



}
