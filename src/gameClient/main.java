package gameClient;


import gui.MyGameGUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {

        File background = new File("robot.png");
        Image robo = ImageIO.read(background);
        Image newimg = robo.getScaledInstance(130, 150,  java.awt.Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(newimg);
        // Set the game Level - [0,23]
        String[] options = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24"};
        int gameNum = JOptionPane.showOptionDialog(null, "Choose the Level you would like to display", "Click a button",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);
        if (gameNum<0) gameNum=0;
        MyGameGUI games = new MyGameGUI(gameNum);

    }



}
