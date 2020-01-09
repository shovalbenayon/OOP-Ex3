package dataStructure;

import com.sun.corba.se.impl.orbutil.graph.NodeData;
import utils.Point3D;

import javax.xml.soap.Node;
import java.io.Serializable;

public class DataNode implements node_data, Serializable, Comparable {
    private int Key;
    private double Weight;
    private Point3D Location;
    private String Info;
    private int tag;

    public DataNode(int i, Point3D p){
        this.Key = i;
        this.Weight = 0;
        this.Location = p;
        this.Info = null;
        this.tag = 0;
    }
    public DataNode(int key){
        this.Key = key;
        this.Weight = 0;
        this.Location =null;
        this.Info = "";
        this.tag = 0;
    }

    public DataNode(int key , double weight , Point3D l , String info , int tag){
        this.Key = key;
        this.Weight = weight;
        this.Location = new Point3D(l.x() , l.y() , l.z());
        this.Info = info;
        this.tag = tag;
    }
    public DataNode(int key , double weight , Point3D l ){
        this.Key = key;
        this.Weight = weight;
        this.Location = new Point3D(l.x() , l.y() , l.z());
        this.Info = "";
        this.tag = 0;
    }

    public DataNode(DataNode oth){
        this.Key = oth.Key;
        this.tag = oth.tag;
        this.Weight = oth.Weight;
        this.Info = oth.Info;
        this.Location= new Point3D(oth.Location);
    }

    public DataNode(Point3D p) {
        this.Key = 0;
        this.Weight = 0;
        this.Location = new Point3D(p.x() , p.y() , p.z());
        this.Info = "";
        this.tag = 0;
    }

    @Override
    public int getKey() {
        return this.Key;
    }

    @Override
    public Point3D getLocation() {
        return this.Location;
    }

    @Override
    public void setLocation(Point3D p) {
        this.Location = new Point3D(p.x() , p.y() , p.z());
    }

    @Override
    public double getWeight() {
        return this.Weight;
    }

    @Override
    public void setWeight(double w) {
        this.Weight = w;
    }

    @Override
    public String getInfo() {
        return this.Info;
    }

    @Override
    public void setInfo(String s) {
        this.Info = s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag = t;
    }

    @Override
    public int compareTo(Object o) {
        if (this.getWeight() > ((DataNode)o).getWeight())
            return 1;
        return 0;
    }
}
