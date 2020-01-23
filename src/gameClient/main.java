package gameClient;


import dataStructure.DGraph;
import dataStructure.DataNode;
import dataStructure.graph;
import gui.DataBaseGUI;
import gui.Graph_GUI;
import gui.MyGameGUI;
import utils.Point3D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {

       //MyGameGUI games = new MyGameGUI(20);
        // MyGameGUI games = new MyGameGUI(-31);
       // draw_million();
        DataBaseGUI h = new DataBaseGUI();



    }
    public static void draw_million(){
        graph myDG = new DGraph();
        for (int i = 0; i < 1000000 ; i++) {
            myDG.addNode(new DataNode(i, new Point3D(Math.random()*i,Math.random()*(i+1))));

        }
        for (int i = 0; i < 99999; i++) {
            myDG.connect(i,i+1,1);

        }
        Graph_GUI window = new Graph_GUI(myDG);



    }




}
