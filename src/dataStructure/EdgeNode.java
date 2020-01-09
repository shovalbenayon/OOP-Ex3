package dataStructure;

import java.io.Serializable;

public class EdgeNode implements edge_data, Serializable {

    private int Source;
    private int Destination;
    private double Weight;
    private String Info;
    private int Tag;

    public EdgeNode(){
        this.Source = 0;
        this.Weight = 0;
        this.Destination = 0;
        this.Info = null;
        this.Tag = 0;
    }

    public EdgeNode(int source , int dest , double weight, String info , int tag){
        this.Source = source;
        if (weight >= 0)
            this.Weight = weight;
        this.Destination = dest;
        this.Info = info;
        this.Tag = tag;
    }

    public EdgeNode(EdgeNode oth){
        this.Source = oth.Source;
        this.Destination = oth.Destination;
        if (oth.Weight >= 0)
            this.Weight = oth.Weight;
        this.Info = oth.Info;
        this.Tag = oth.Tag;
    }

    public EdgeNode(int src, int dest, double w) {
        this.Source = src;
        this.Destination = dest;
        if (w >= 0)
            this.Weight = w;
        this.Info = null;
        this.Tag = 0;
    }

    public EdgeNode(int src, int dest) {
        this.Source = src;
        this.Destination = dest;
        this.Weight = 0;
        this.Info = null;
        this.Tag = 0;
    }

    @Override
    public int getSrc() {
        return this.Source;
    }

    @Override
    public int getDest() {
        return this.Destination;
    }

    @Override
    public double getWeight() {
        return this.Weight;
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
        return this.Tag;
    }

    @Override
    public void setTag(int t) {
        this.Tag = t;
    }
}
