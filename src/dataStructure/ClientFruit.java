package dataStructure;

import Server.fruits;
import Server.robot;
import oop_utils.OOP_Point3D;
import utils.Point3D;

public class ClientFruit implements fruits {
    private OOP_Point3D _pos;
    private double _value;
    private int _type;
    private edge_data _edge;

    public ClientFruit (Point3D p , double v , int t , edge_data ed ){
        this._pos = new OOP_Point3D(p.x() , p.y() , p.z());
        this._type = t;
        this._value = v;
        this._edge = ed;

    }

    public ClientFruit(String s) {
        String []splits = s.split(":");

        double val = valueFromString(splits[2]);
        int type = typeFromString(splits[3]);
        Point3D pos = posFromString(splits[4]);
        this._value = val;
        this._type = type;
        this._pos = new OOP_Point3D(pos.x() , pos.y() , pos.z());

    }

    public double valueFromString(String s){
        String val = s.substring(0 , s.indexOf(","));
        return Double.parseDouble(val);
    }

    public int typeFromString(String s){
        String type = s.substring(0 , s.indexOf(","));
        return Integer.parseInt(type);
    }

    public Point3D posFromString(String s){
        s = s.substring(1 , s.length() -3);
        String[] split = s.split(",");
        double x= Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Double.parseDouble(split[2]);
        return new Point3D(x, y, z);
    }


    @Override
    public OOP_Point3D getLocation() {
        return this._pos;
    }

    @Override
    public double getValue() {
        return _value;
    }

    @Override
    public double grap(robot robot, double v) {
        return 0;
    }

    @Override
    public int getType() {
        return this._type;
    }
}
