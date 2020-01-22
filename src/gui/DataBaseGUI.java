package gui;

import gameClient.SimpleDB;
import gameClient.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DataBaseGUI extends JFrame implements ActionListener , Runnable {
    private JTable MainTable;
    private boolean startFlag = true;
    Object[][] Info;

	public DataBaseGUI() {
        InitGui();
	}

    /**
     * Init the GUI frame
     */
	private void InitGui(){
        this.setSize(600, 800); // set the size of the window according the existing graph
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        File.add(ranking);
        File.add(bestOfMe);
        File.add(myLevel);

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
            JOptionPane.showMessageDialog(this,"Wait a few minutes , the table data is loading..");
            startFlag = false;
        }
        String[] columns = new String[] {
                "Number og game", "Id", "Level", "Score", "Moves", "Date"
        };

        this.setTitle("Data Table");
        Info = SimpleDB.printUserLog(316150861);
        MainTable = new JTable(Info, columns);
        this.add(new JScrollPane(MainTable));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	    if (e.getActionCommand() == "Global ranking"){
            Object[] stageSelection = {"0", "1", "3", "5", "9", "11", "13", "16", "19", "20", "23"};
            String initialStage = "0";
            Object selectedStage = JOptionPane.showInputDialog(null, "Choose",
                    "Main menu", JOptionPane.QUESTION_MESSAGE, null, stageSelection, initialStage);

            ArrayList<Student> arr = SimpleDB.getRankingForLevel(Integer.parseInt((String) selectedStage));
            JOptionPane.showMessageDialog(this, "Ranking in class for level " + selectedStage + " : " +SimpleDB.Ranking(arr,316150861));
        }


        if (e.getActionCommand() == "Best ranking levels"){
            String[] columns = {"Game Scenario", "Best Score"};
            JFrame newFrame = new JFrame();
            newFrame.setSize(400 ,500);
            newFrame.setVisible(true);
            newFrame.setLocationRelativeTo(null);
            newFrame.setTitle("Bast Scores");
            Info = SimpleDB.bestScores("316150861");
            JTable newtable = new JTable(Info, columns);
            newFrame.add(new JScrollPane(newtable));
        }
        if (e.getActionCommand() == "My Level"){
            JOptionPane.showMessageDialog(this, "My Max level is : " + SimpleDB.showMaxLevel("316150861"));
        }

    }

    @Override
    public void run() {
        InitGui();
    }
}
