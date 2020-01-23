package gui;

import Server.Game_Server;
import gameClient.SimpleDB;
import gameClient.Student;
import jdk.nashorn.internal.scripts.JO;
import utils.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DataBaseGUI extends JFrame implements ActionListener  {
    private JTable MainTable;
    private boolean startFlag = true;
    private int UserID ;
    Object[][] Info;
    private boolean fromGame = true;

	public DataBaseGUI() {
        String id = JOptionPane.showInputDialog("Enter your id number" );
        try {
            this.UserID= Integer.parseInt(id);
            Game_Server.login(this.UserID);

        } catch (Exception Ex) {
            JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
        }
        InitGui();
        fromGame = false;
	}

    public DataBaseGUI(int id) {
	    this.UserID= id;
	    Game_Server.login(this.UserID);
        InitGui();
    }

    /**
     * Init the GUI frame
     */
	private void InitGui(){
        this.setSize(600, 800); // set the size of the window according the existing graph

        if (fromGame) {
          //  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        MenuBar menuBar = new MenuBar();

        Menu File = new Menu("Options"); //main's manu
        menuBar.add(File);
        this.setMenuBar(menuBar);

        MenuItem ranking = new MenuItem("Global ranking");
        ranking.addActionListener(this);

        MenuItem bestOfMe = new MenuItem("Best ranking levels");
        bestOfMe.addActionListener(this);

        MenuItem myLevel = new MenuItem("My Level");
        myLevel.addActionListener(this);

        MenuItem total = new MenuItem("My Totals games in the server");
        total.addActionListener(this);
        MenuItem all = new MenuItem("My data base");
        all.addActionListener(this);

        File.add(ranking);
        File.add(bestOfMe);
        File.add(myLevel);
        File.add(total);
        File.add(all);

        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }

    /**
     * Paint the data for the user
     * @param g = graphics
     */
    public void paint(Graphics g) {
        super.paint(g);
        // Headers for the table
        if (startFlag) {
            JOptionPane.showMessageDialog(this,"Enter on options menu!");
            startFlag = false;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
	    if (e.getActionCommand() == "Global ranking"){
            Object[] stageSelection = {"0", "1", "3", "5", "9", "11", "13", "16", "19", "20", "23"};
            String initialStage = "0";
            Object selectedStage = JOptionPane.showInputDialog(null, "Choose",
                    "Main menu", JOptionPane.QUESTION_MESSAGE, null, stageSelection, initialStage);

            ArrayList<Student> arr = SimpleDB.getRankingForLevel(Integer.parseInt((String) selectedStage));
            Range stat =SimpleDB.Ranking(arr,this.UserID);
            JOptionPane.showMessageDialog(this, "Ranking in class for level " + selectedStage + " : " + stat.get_min() +" from " + stat.get_max());
        }


        if (e.getActionCommand() == "Best ranking levels"){
            String[] columns = {"Game Scenario", "Best Score"};
            JFrame newFrame = new JFrame();
            newFrame.setSize(400 ,500);
            newFrame.setVisible(true);
            newFrame.setLocationRelativeTo(null);
            newFrame.setTitle("Bast Scores");
            Info = SimpleDB.bestScores(String.valueOf(this.UserID));
            JTable newtable = new JTable(Info, columns);
            newFrame.add(new JScrollPane(newtable));
        }
        if (e.getActionCommand() == "My Level"){
            JOptionPane.showMessageDialog(this, "My Max level is : " + SimpleDB.showMaxLevel(String.valueOf(this.UserID)));
        }
        if (e.getActionCommand() == "My Totals games in the server"){
            JOptionPane.showMessageDialog(this , "Your total games in the server until now : "+SimpleDB.HowMuch(this.UserID));
        }

        if (e.getActionCommand() == "My data base"){
            String[] columns = new String[] {
                    "Number og game", "Id", "Level", "Score", "Moves", "Date"
            };

            this.setTitle("Data Table");
            Info = SimpleDB.printUserLog(this.UserID);
            MainTable = new JTable(Info, columns);
            this.add(new JScrollPane(MainTable));
        }

    }

}
