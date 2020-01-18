package elements;

import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import java.util.Collection;

public class Fruit  {
    private Point3D pos;
    private double value;
    private int type;
    private edge_data edge;



    public Fruit (Point3D p , double v , int t , edge_data ed ){
        this.pos = new Point3D(p.x() , p.y() , p.z());
        this.type = t;
        this.value = v;
        this.edge = ed;

    }

    public Fruit(){
        this.edge = null;
        this.value = 0;
        this.type = 0;
        this.pos = null;
    }

    public Fruit(String s)  {
        JSONObject getfruit ;
        try {
            getfruit = new JSONObject(s);
            JSONObject jsonFruit = getfruit.getJSONObject("Fruit");

            this.value = jsonFruit.getDouble("value");
            this.type = jsonFruit.getInt("type");
            String []splits = jsonFruit.getString("pos").split(",");
            double x = Double.parseDouble(splits[0]);
            double y = Double.parseDouble(splits[1]);
            double z = Double.parseDouble(splits[2]);
            this.pos = new Point3D(x, y, z);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Fruit(String JSONString, DGraph gameGraph) {
        JSONObject getFruit;
        try {
            getFruit = new JSONObject(JSONString);
            JSONObject jsonFruit = getFruit.getJSONObject("Fruit");
            this.value = jsonFruit.getDouble("value");
            this.type = jsonFruit.getInt("type");
            String[] posArray = jsonFruit.getString("pos").split(",");
            double x = Double.parseDouble(posArray[0]);
            double y = Double.parseDouble(posArray[1]);
            double z = Double.parseDouble(posArray[2]);
            this.pos = new Point3D(x, y, z);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.edge = setEdge(gameGraph);
    }

    public Point3D getPos() {
        return pos;
    }


    public double getValue() {
        return value;
    }


    public int getType() {
        return type;
    }


    public edge_data getEdge() {
        return edge;
    }

    private edge_data setEdge(DGraph gameGraph) {
        double epsilon = 0.000001;
        double edgeDist = 0;
        double srcToFruitDist = 0, fruitToDestDist = 0, fruitDist = 0;

        Collection<node_data> nodesCol = gameGraph.getV();
        for (node_data nD : nodesCol) {
            Collection<edge_data> edgesCol = gameGraph.getE(nD.getKey());
            for (edge_data eD : edgesCol) {
                double xDist = nD.getLocation().x() - gameGraph.getNode(eD.getDest()).getLocation().x();
                double yDist = nD.getLocation().y() - gameGraph.getNode(eD.getDest()).getLocation().y();
                edgeDist = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));

                double xSrcToFruit = nD.getLocation().x() - this.getPos().x();
                double ySrcToFruit = nD.getLocation().y() - this.getPos().y();
                double xFruitToDest = this.getPos().x() - gameGraph.getNode(eD.getDest()).getLocation().x();
                double yFruitToDest = this.getPos().y() - gameGraph.getNode(eD.getDest()).getLocation().y();
                srcToFruitDist = Math.sqrt(Math.pow(xSrcToFruit, 2) + Math.pow(ySrcToFruit, 2));
                fruitToDestDist = Math.sqrt(Math.pow(xFruitToDest, 2) + Math.pow(yFruitToDest, 2));
                fruitDist = srcToFruitDist + fruitToDestDist;

                if (Math.abs(edgeDist - fruitDist) < epsilon) return eD;
            }
        }

        return null;
    }


}
