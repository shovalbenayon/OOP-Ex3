package Tests;

import algorithms.Graph_Algo;
import dataStructure.*;
import gui.Graph_GUI;
import org.junit.Test;
import utils.Point3D;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Graph_GUITests
{

    @Test
    public void testDraw(){
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
        graph graph = new DGraph();
        graph.addNode(v1);
        graph.addNode(v2);
        graph.addNode(v3);
        graph.addNode(v4);
        graph.addNode(v5);

        graph.addNode(v6);
        graph.addNode(v7);
        graph.addNode(v8);
        graph.addNode(v9);
        graph.addNode(v10);

        graph.connect(v1.getKey(), v3.getKey(), 6);
        graph.connect(v1.getKey(), v4.getKey(), 10);
        graph.connect(v3.getKey(), v4.getKey(), 9);
        graph.connect(v4.getKey(), v2.getKey(), 3);
        graph.connect(v3.getKey(), v5.getKey(), 1);

        graph.connect(v2.getKey(), v3.getKey(), 5);
        graph.connect(v2.getKey(), v5.getKey(), 1);

        graph.connect(v6.getKey(), v7.getKey(), 4);
        graph.connect(v9.getKey(), v7.getKey(), 18);
        graph.connect(v10.getKey(), v8.getKey(), 16);

        graph.connect(v5.getKey(), v7.getKey(), 5);
        graph.connect(v4.getKey(), v8.getKey(), 11);
        graph.connect(v10.getKey(), v6.getKey(), 5);
        graph.connect(v9.getKey(), v2.getKey(), 11);
        graph.connect(v7.getKey(), v9.getKey(), 11);

        Graph_GUI window = new Graph_GUI(graph);
        window.setVisible(true);
    }

    @Test
    public void testDraw2(){
        graph myDG = new DGraph();
        myDG.addNode(new DataNode(1, new Point3D(30,50)));
        myDG.addNode(new DataNode(2 , new Point3D(340,100)));
        myDG.addNode(new DataNode(3 , new Point3D(40,200)));
        myDG.addNode(new DataNode(4 , new Point3D(60,270)));
        myDG.addNode(new DataNode(5 , new Point3D(130,450)));
        myDG.addNode(new DataNode(6 , new Point3D(70,240)));
        myDG.connect(1,6,1);
        myDG.connect(1,3,34);
        myDG.connect(2,1,6);
        myDG.connect(2,6,1);
        myDG.connect(5,2,13);
        myDG.connect(4,6,5);
        myDG.connect(6,3,2);
        myDG.connect(6,5,1);

        Graph_GUI window = new Graph_GUI(myDG);
        window.setVisible(true);

    }
}
