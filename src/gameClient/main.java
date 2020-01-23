package gameClient;


import Server.Game_Server;
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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {
        MyGameGUI game;
        DataBaseGUI scoresGui;

        Object[] selectionMenu = {"Play", "Data Base"};
        String initialChoice = "Play";
        Object selectedChoice = JOptionPane.showInputDialog(null, "Choose",

                "Main menu", JOptionPane.QUESTION_MESSAGE, null, selectionMenu, initialChoice);

        if (selectedChoice.equals("Play")) game = new MyGameGUI();

        if (selectedChoice.equals("Data Base")){
            scoresGui = new DataBaseGUI();

        }

    }


}
