package Tests;


import algorithms.Graph_Algo;
import gui.Graph_GUI;
import dataStructure.DGraph;
import dataStructure.DataNode;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;

import java.io.IOException;
import java.util.LinkedList;

public class main {
    public static void main(String[] args) throws IOException {
        node_data v1 = new DataNode(1, new Point3D(25,90,0));
        node_data v2 = new DataNode(2, new Point3D(340,300,0));
        node_data v3 = new DataNode(3,  new Point3D(24,300,0));
        node_data v4 = new DataNode(4, new Point3D(68,100,0));
        node_data v5 = new DataNode(5, new Point3D(500,600,0));

        node_data v6 = new DataNode(6, new Point3D(100,300,0));
        node_data v7 = new DataNode(7, new Point3D(190,200,0));
        node_data v8 = new DataNode(8,  new Point3D(390,400,0));
        node_data v9 = new DataNode(9, new Point3D(300,450,0));
        node_data v10 = new DataNode(10, new Point3D(300,550,0));
        graph grap = new DGraph();
        grap.addNode(v1);
        grap.addNode(v2);
        grap.addNode(v3);
        grap.addNode(v4);
        grap.addNode(v5);

        grap.addNode(v6);
        grap.addNode(v7);
        grap.addNode(v8);
        grap.addNode(v9);
        grap.addNode(v10);

        grap.connect(v1.getKey(), v3.getKey(), 6);
        grap.connect(v1.getKey(), v4.getKey(), 10);
        grap.connect(v3.getKey(), v4.getKey(), 9);
        grap.connect(v4.getKey(), v2.getKey(), 3);
        grap.connect(v3.getKey(), v5.getKey(), 1);

        grap.connect(v2.getKey(), v3.getKey(), 5);
        grap.connect(v2.getKey(), v5.getKey(), 1);

        grap.connect(v6.getKey(), v7.getKey(), 4);
        grap.connect(v9.getKey(), v7.getKey(), 18);
        grap.connect(v10.getKey(), v8.getKey(), 16);

        grap.connect(v5.getKey(), v7.getKey(), 5);
        grap.connect(v4.getKey(), v8.getKey(), 11);
        grap.connect(v10.getKey(), v6.getKey(), 5);
        grap.connect(v9.getKey(), v2.getKey(), 11);
//        grap.connect(v7.getKey(), v9.getKey(), 11);
//        grap.connect(v1.getKey() , v6.getKey() , 7);
//        grap.connect(v1.getKey() , v10.getKey() , 7);
//        grap.connect(v2.getKey() , v1.getKey() , 9);
//        grap.connect(v8.getKey() , v1.getKey() , 2);

//        Graph_Algo ga = new Graph_Algo(grap);
//        LinkedList<Integer> nodesPath =new LinkedList<>();
//        nodesPath.add(2);
//        nodesPath.add(10);
//        LinkedList<node_data> Path = (LinkedList<node_data>) ga.TSP(nodesPath);
//
//        System.out.println(Path.isEmpty());
////
        Graph_GUI window = new Graph_GUI(grap);
        window.setVisible(true);
//        Window window = new Window();
//        window.setVisible(true);
//        Point3D a = new Point3D(25,90,0);
//        Point3D b = new Point3D(100,300,0);
//        Point3D c = new Point3D(190,200,0);
//        Point3D d = new Point3D(390,400,0);
//        Point3D h = new Point3D(500,600,0);
//        Point3D f = new Point3D(300,550,0);
//        Point3D g = new Point3D(300,450,0);
//        //Point3D h = new Point3D(300,550,0);
//
//        node_data v1 = new DataNode(1, a);
//        node_data v2 = new DataNode(2, b);
//        node_data v3 = new DataNode(3, c);
//        node_data v4 = new DataNode(4, d);
//        node_data v5 = new DataNode(5, h);
//        node_data v6 = new DataNode(6, f);
//        node_data v7 = new DataNode(7, g);
//        //node_data v8 = new Node(8, h);
//
//        graph grap = new DGraph();
//
//        grap.addNode(v1);
//        grap.addNode(v2);
//        grap.addNode(v3);
//        grap.addNode(v4);
//        grap.addNode(v5);
//        grap.addNode(v6);
//        grap.addNode(v7);
//        //	grap.addNode(v8);
//        grap.connect(v1.getKey(), v2.getKey(), 6);
//        grap.connect(v2.getKey(), v3.getKey(), 10);
//        grap.connect(v3.getKey(), v4.getKey(), 9);
//        grap.connect(v4.getKey(), v5.getKey(), 3);
//        grap.connect(v5.getKey(), v1.getKey(), 1);
//        grap.connect(v1.getKey(), v3.getKey(), 5);
//        grap.connect(v3.getKey(), v5.getKey(), 6);
//        grap.connect(v2.getKey(), v4.getKey(), 1);
//        grap.connect(v2.getKey(), v6.getKey(), 4);
//        grap.connect(v4.getKey(), v7.getKey(), 3);
//        grap.connect(v7.getKey(), v6.getKey(), 1);
//        grap.connect(v7.getKey(), v5.getKey(), 6);
//        grap.connect(v2.getKey(), v7.getKey(), 1);
//        grap.connect(v6.getKey(), v5.getKey(), 1);
//        grap.connect(v4.getKey(), v6.getKey(), 3);
//
//        Graph_GUI window = new Graph_GUI(grap);
//        window.setVisible(true);
//        repaint();






    }
}
