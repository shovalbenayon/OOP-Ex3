package gui;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import elements.Fruit;
import elements.Robot;
import gameClient.AutomaticGame;
import gameClient.KML_Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;

/**
 * * This game is a simple game, in which the chosen fruits(apple or android) are collected by robot
 *  There's 2 modes for every game : Manual and Automatic.
 *
 *  ** Manual mode
 *  When you play manualy , first you'll be ask to place all fruits that are
 *  decided by the game level.
 *  After placing , the time will start to go down. every level have his own time.
 *  you can see how much time youv'e left in the right corner of the frame
 *  Your goal is to collects as many fruits you can before time run out.
 *  To collect the fruits, click on the "move robot" button and choose the robot you want to move,
 *  by his id.
 *  And then, coose the destination you want to send the robot.
 *  **Notice** you can send robot only if the robot source and destination are
 *             connected with an edge. If there is'nt an edge, the robot won't move.
 *
 *
 *  *** Important note
 *  Apples are located on the edge from the node with the lower ID to the node with the higher ID (for example : 23 -> 34)
 *  Androids are located on the edges directed from the higher ID node to lower ID node (for example : 31 -> 8)
 *  Mushrooms have different values (points you get for collecting them).
 *  Edges have length, time it takes to traverse the edge.
 *
 *  ** Automatic mode
 *  In this mode I tried to write my best algorithm for collecting the fruits and get
 *  the higher possible score.
 *  The algorithm did not collects an optimal points do to the lack of time.
 *  Short explanation on the algorithm:
 *  At first, the robots placed near to most value fruit.
 *  Every robot run with his own thread , which every robot collect the most value fruit on the
 *  graph. the robot collects the fruit by calculating the shortest Path to him.
 *  After the Robot chooses his fruit, the other robot's can't chose her.
 *  This is to prevent the other robots to move towards the same target.
 *  hope that in more given time, a better solution will be found :)
 *
 *  Enjoy playing!
 * @author shoval benayon
 */

public class MyGameGUI extends JPanel {
    private game_service myGame;
    private int scenario;
    private DGraph dGraph = new DGraph();
    private Graph_Algo graph_algo = new Graph_Algo();
    private ArrayList<Fruit> FruitsCol = new ArrayList<>();

    private final int X_RANGE = 1500;
    private final int Y_RANGE = 900;
    private JButton moveButton = new JButton("Move Robot");
    private JButton InfoServer = new JButton("Data Base");
    private int UserID;
    private int min_dt;


    // Threads
    private Thread MoveRobotManual;
    private Thread placeRobotsManual;
    private Thread placeRobotsAuto;
    private Thread moveRobotAuto;
    private Thread sql;

    private BufferedImage appleImage;
    private BufferedImage androidImage;
    private BufferedImage robotImage;
    private BufferedImage byeImage;
    private Image backgroundImage;

    private int robotsNum = 0;
    private int robotsCounter = 0;
    private int gameScore = 0;
    private int MovesCounter = 0;

    // Game mode flags
    private boolean autoGame = false;
    private boolean IntialFlag = false;
    private KML_Logger log;


    private void InitGui() {
        JFrame Frame = new JFrame("Game");
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.add(this);
        Frame.pack();
        Frame.setVisible(true);
        Frame.setLocationRelativeTo(null); // puts the frame in the middle of the screem
    }

    public MyGameGUI()  {

        String id = JOptionPane.showInputDialog("Enter your id number" );
        try {
            this.UserID = Integer.parseInt(id);
            Game_Server.login(this.UserID);

        } catch (Exception Ex) {
            JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
        }
        File r = new File("robot.png");
        try {
            Image robo = ImageIO.read(r);
            Image newimg = robo.getScaledInstance(130, 150,  Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(newimg);
            // Set the game Level - [0,23]
            String[] options = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
            int gameNum = JOptionPane.showOptionDialog(null, "Choose the Level you would like to play", "Click a button",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);
            this.scenario = gameNum;
            if (gameNum == JOptionPane.CLOSED_OPTION){
                System.exit(0);
            }
        } catch (IOException | HeadlessException e) {
            e.printStackTrace();
        }
        log = new KML_Logger(""+ scenario);
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(X_RANGE, Y_RANGE));
        mainPanel.add(moveButton);
        mainPanel.add(InfoServer);
        this.add(mainPanel, BorderLayout.SOUTH);
        backgroundImage = Toolkit.getDefaultToolkit().createImage("Background.jpg");
        Threads();

        myGame = Game_Server.getServer(scenario);
        dGraph.init(myGame.getGraph());
        graph_algo.init(dGraph);
        //get the number of robots
        try {
            JSONObject line;
            line = new JSONObject(this.myGame.toString());
            JSONObject server = line.getJSONObject("GameServer");
            robotsNum = server.getInt("robots");

        } catch (Exception var10) {
            var10.printStackTrace();
        }
        findNodes(myGame.getGraph()); //Nodes to kml


        try {
            File apple = new File("apple.png");
            appleImage = ImageIO.read(apple);
            File android= new File("android.png");
            androidImage = ImageIO.read(android);
            File robot = new File("robot.png");
            robotImage = ImageIO.read(robot);
            File background = new File("background.jpg");
            backgroundImage = ImageIO.read(background);
            File bye = new File("bye.png");
            byeImage = ImageIO.read(bye);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InitGui();

        InfoServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sql.start();
            }
        });

    }

    public MyGameGUI(int gameScenario)  {
        String id = JOptionPane.showInputDialog("Enter your id number" );
        try {
            this.UserID = Integer.parseInt(id);
            Game_Server.login(this.UserID);

        } catch (Exception Ex) {
            JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
        }
        this.scenario = gameScenario;
        log = new KML_Logger(""+ scenario);
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(X_RANGE, Y_RANGE));
        mainPanel.add(moveButton);
        mainPanel.add(InfoServer);
        this.add(mainPanel, BorderLayout.SOUTH);
        backgroundImage = Toolkit.getDefaultToolkit().createImage("Background.jpg");
        Threads();

        myGame = Game_Server.getServer(scenario);
        dGraph.init(myGame.getGraph());
        graph_algo.init(dGraph);
        //get the number of robots
        try {
            JSONObject line;
            line = new JSONObject(this.myGame.toString());
            JSONObject server = line.getJSONObject("GameServer");
            robotsNum = server.getInt("robots");

        } catch (Exception var10) {
            var10.printStackTrace();
        }
        findNodes(myGame.getGraph()); //Nodes to kml


        try {
            File apple = new File("apple.png");
            appleImage = ImageIO.read(apple);
            File android= new File("android.png");
            androidImage = ImageIO.read(android);
            File robot = new File("robot.png");
            robotImage = ImageIO.read(robot);
            File background = new File("background.jpg");
            backgroundImage = ImageIO.read(background);
            File bye = new File("bye.png");
            byeImage = ImageIO.read(bye);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InitGui();

        InfoServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sql.start();
            }
        });
    }

    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(backgroundImage, 5, 50, 1500, 900, null);

        Collection<node_data> nodesCol = dGraph.getV();
        double minX = getMinX(nodesCol);
        double maxX = getMaxX(nodesCol);
        double minY = getMinY(nodesCol);
        double maxY = getMaxY(nodesCol);

        //drawing the nodes
        for (node_data node : nodesCol) {
            g.setColor(Color.PINK);
            g.setFont(new Font("default", Font.BOLD, 15));
            double scaledX = scale(node.getLocation().x(), minX, maxX, (double)X_RANGE - 100);
            double scaledY = scale(node.getLocation().y() ,minY, maxY, (double)Y_RANGE - 100);
            g.fillOval((int)scaledX, (int)scaledY, 10, 10);
            g.drawString(String.valueOf(node.getKey()), (int)scaledX, (int)scaledY - 5);

            //drawing the edges
            Collection<edge_data> edgesCol = dGraph.getE(node.getKey());
            for (edge_data edge : edgesCol) {
                node_data dest = dGraph.getNode(edge.getDest());
                Point3D destLocation = dest.getLocation();

                g.setColor(Color.BLACK);
                double scaledXDest = scale(destLocation.x(), minX, maxX, (double)X_RANGE - 100);
                double scaledYDest = scale(destLocation.y(), minY, maxY, (double)Y_RANGE - 100);
                g.drawLine((int)scaledX + 5, (int)scaledY + 5, (int)scaledXDest + 5, (int)scaledYDest + 5);
            }
        }

        // Fruits drawl
        List<String> fruitsList = myGame.getFruits();
        for (String tempFruit : fruitsList) {
            Fruit newFruit = new Fruit(tempFruit, dGraph);
            this.FruitsCol.add(newFruit);
            double fruitX = newFruit.getPos().x();
            double fruitY = newFruit.getPos().y();
            fruitX = scale(fruitX, minX, maxX, X_RANGE - 100);
            fruitY = scale(fruitY, minY, maxY, Y_RANGE - 100);
            if (newFruit.getType() == 1)
                g.drawImage(appleImage, (int)fruitX - 5, (int)fruitY - 5, 20, 25, this);
            else
                g.drawImage(androidImage, (int)fruitX - 5, (int)fruitY - 5, 20, 25, this);
            log.addFruitPlaceMark(newFruit.getType() , newFruit.getPos().toString());
        }

        // Robots drawl
        List<String> robotsList = myGame.getRobots();
        for (String tempRobot : robotsList) {
            Robot newRobot = new Robot(tempRobot);
            double robotX = newRobot.getPos().x();
            double robotY = newRobot.getPos().y();
            robotX = scale(robotX, minX, maxX, X_RANGE - 100);
            robotY = scale(robotY, minY, maxY, Y_RANGE - 100);
            g.drawImage(robotImage, (int)robotX - 20, (int)robotY - 20, 40, 40, this);
            log.addRobotPlaceMark(newRobot.getPos().toString());
        }

        // Remaining time drawl
        g.setFont(new Font("Helvetica", Font.BOLD, 15));
        g.setColor(Color.BLACK);
        long timeToEnd = myGame.timeToEnd() / 1000;
        if (timeToEnd < 10) g.setColor(Color.RED);
        if (timeToEnd < 1) {
            g.drawString("GAME OVER", X_RANGE - 100, 20);
        }
        else g.drawString("Remaining time : " +timeToEnd, X_RANGE - 150, 20);

        // Total score drawl
        g.setColor(Color.BLACK);
        g.drawString("Total game score : " + gameScore, X_RANGE - 1490, 20);

        if (!IntialFlag) {
            try {
                Image img = robotImage;
                Image newimg = img.getScaledInstance(130, 150,  java.awt.Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(newimg);

                Object[] options = {"Manual game!",
                        "Automatic game!"};
                int answer = JOptionPane.showOptionDialog(null, "Which game would you like to play?", "Manual or Automatic", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon, options, options[0]);
                //the manual option is chosen
                if (answer == JOptionPane.YES_OPTION) {
                    autoGame = false;
                    //move button available only when user playing manual
                    moveButton.addActionListener(e -> {
                        boolean vFlag = true;

                        String[] options1 = new String[robotsNum];
                        for (int i = 0 ; i < robotsNum ; i++){
                            options1[i] = String.valueOf(i);
                        }

                        String idRobot = (String) JOptionPane.showInputDialog(null,"Enter id number of the robot you want to move" ,"Move",JOptionPane.QUESTION_MESSAGE,icon , options1, options1[0]);
                        try {
                            int robot_num = Integer.parseInt(idRobot);
                            if (robot_num < 0 || robot_num >= robotsNum) throw new RuntimeException(); // if there is no robot to move or if the robot is not valid
                        } catch (Exception Ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                            vFlag = false;
                        }

                        if (vFlag) {
                            String destNode = JOptionPane.showInputDialog("Enter destination node" );
                            try {
                                int destinationNode = Integer.parseInt(destNode);
                                if (dGraph.getNode(destinationNode) == null) throw new RuntimeException();
                                myGame.chooseNextEdge(Integer.parseInt(idRobot), destinationNode);

                            } catch (Exception Ex) {
                                JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });

                }
                //the automatic game is chosen
                if(answer == JOptionPane.NO_OPTION) {
                    autoGame = true;
                    graph_algo.init(dGraph);
                    moveButton.setVisible(false);
                }

                //if the user did not pick an option , the program will be finished
                if (answer == JOptionPane.CLOSED_OPTION) {
                    Image b = byeImage;
                    Image newb = b.getScaledInstance(130, 150,  java.awt.Image.SCALE_SMOOTH);
                    ImageIcon by = new ImageIcon(newb);
                    JOptionPane.showMessageDialog(this, "OK ! BYE BYE!", "Close" , JOptionPane.ERROR_MESSAGE, by );
                    this.setVisible(false);
                    System.exit(0);
                }
                IntialFlag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!autoGame) {
                if (!placeRobotsManual.isAlive() && !MoveRobotManual.isAlive())
                    placeRobotsManual.start();
            }

            else {
                if (!placeRobotsAuto.isAlive() && !moveRobotAuto.isAlive())
                    placeRobotsAuto.start();
            }

        }


    }

    /**
     *
     * @param data denote some data to be scaled
     * @param r_min the minimum of the range of your data
     * @param r_max the maximum of the range of your data
     * @param t_max the maximum of the range of your desired target scaling
     * @return the scaled value
     */

    private double scale(double data, double r_min, double r_max, double t_max) {
        return ((data - r_min) / (r_max-r_min)) * (t_max - (double) 100) + (double) 100;
    }

    private double getMinX(Collection<node_data> nodes) {
        double min = Double.MAX_VALUE;
        for (node_data node : nodes) {
            double temp = node.getLocation().x();
            if (temp < min)
                min = temp;
        }

        return min;
    }

    private double getMinY(Collection<node_data> nodes) {
        double min = Double.MAX_VALUE;
        for (node_data node : nodes) {
            double temp = node.getLocation().y();
            if (temp < min)
                min = temp;
        }

        return min;
    }

    private double getMaxX(Collection<node_data> nodes) {
        double max = Double.MIN_VALUE;
        for (node_data node : nodes) {
            double temp = node.getLocation().x();
            if (temp > max)
                max = temp;
        }

        return max;
    }

    private double getMaxY(Collection<node_data> nodes) {
        double max = Double.MIN_VALUE;
        for (node_data node : nodes) {
            double temp = node.getLocation().y();
            if (temp > max)
                max = temp;
        }

        return max;
    }

    private void Threads() {
        // Manual game threads
        placeRobotsManual = new Thread(() -> {
            while (robotsCounter < robotsNum) {
                String src = JOptionPane.showInputDialog("Enter Position for Robot Number " + robotsCounter);
                try {
                    int source = Integer.parseInt(src);
                    if (dGraph.getNode(source) == null) throw new RuntimeException();
                    myGame.addRobot(source);
                    repaint();
                    robotsCounter++;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            myGame.startGame();
            MoveRobotManual.start();
        });

        MoveRobotManual = new Thread(() -> {
            JSONObject getscore;
            long start = System.currentTimeMillis();
            MovesCounter = 0;
            while (myGame.isRunning()) {
                MovesCounter++;
                if (System.currentTimeMillis() - start > 20) {
                    try {
                        getscore = new JSONObject(myGame.toString());
                        JSONObject score = getscore.getJSONObject("GameServer");
                        gameScore = score.getInt("grade"); // changing the score after moving a robot
                        myGame.move();
                    } catch (Exception e) {
                        System.out.println("Exception");
                    }

                    start = System.currentTimeMillis();
                    repaint();
                }
            }
            Image b = byeImage;
            Image newb = b.getScaledInstance(130, 150,  Image.SCALE_SMOOTH);
            ImageIcon by = new ImageIcon(newb);
            JOptionPane.showMessageDialog(this, "Game Over!\n In stage "+ this.scenario +" The Final Score : " + this.gameScore +" in " + this.MovesCounter + " Moves ", "Close" , JOptionPane.ERROR_MESSAGE, by );
            int n = JOptionPane.showConfirmDialog(this , "Export to KML ?" , "Export" , JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
                log.closeDocument();
                String remark = "data/" +this.scenario;
                myGame.sendKML(remark);
                this.setVisible(false);
                System.exit(0);
            }
            else {
                this.setVisible(false);
                System.exit(0);
            }
        });

        // Automatic game threads
        placeRobotsAuto = new Thread(() -> {
            while (robotsCounter < robotsNum && scenario != 16  && scenario != 13 && scenario != 20 && scenario != 23 ) {
                Fruit bestFruit = AutomaticGame.findBestFruit(this.FruitsCol);
                // Choose the best location based on the greatest fruits values
                int autoSrcNode = bestFruit.getEdge().getSrc();
                myGame.addRobot(autoSrcNode);
                FruitsCol = AutomaticGame.removeBest(FruitsCol, bestFruit);

                repaint();
                robotsCounter++;
            }

            if (scenario == 16) {
                robotsCounter = 0;
                while (robotsCounter < robotsNum) {
                    if (robotsCounter != 0) {
                        Fruit bestFruit = AutomaticGame.findBestFruit(this.FruitsCol);

                        // Choose the best location based on the greatest fruits values
                        int autoSrcNode = bestFruit.getEdge().getSrc();
                        myGame.addRobot(autoSrcNode);
                        FruitsCol = AutomaticGame.removeBest(FruitsCol, bestFruit);
                    }

                    if (robotsCounter == 0){
                        myGame.addRobot(5);
                    }

                    repaint();
                    robotsCounter++;
                }
            }
            if (scenario == 13) {
                robotsCounter = 0;
                while (robotsCounter < robotsNum) {
                    if (robotsCounter != 1) {
                        Fruit bestFruit = AutomaticGame.findBestFruit(this.FruitsCol);

                        // Choose the best location based on the greatest fruits values
                        int autoSrcNode = bestFruit.getEdge().getSrc();
                        myGame.addRobot(autoSrcNode);
                        FruitsCol = AutomaticGame.removeBest(FruitsCol, bestFruit);
                    }

                    if (robotsCounter == 1){
                        myGame.addRobot(13);
                    }

                    repaint();
                    robotsCounter++;
                }
            }
            if (scenario == 20) {
                robotsCounter = 0;
                while (robotsCounter < robotsNum) {
                    if (robotsCounter != 1) {
                        Fruit bestFruit = AutomaticGame.findBestFruit(this.FruitsCol);

                        // Choose the best location based on the greatest fruits values
                        int autoSrcNode = bestFruit.getEdge().getSrc();
                        myGame.addRobot(autoSrcNode);
                        FruitsCol = AutomaticGame.removeBest(FruitsCol, bestFruit);
                    }

                    if (robotsCounter == 1){
                        myGame.addRobot(6);
                    }

                    repaint();
                    robotsCounter++;
                }
            }
            if (scenario == 23) {
                robotsCounter = 0;
                while (robotsCounter < robotsNum) {
                    if (robotsCounter == 1){
                        myGame.addRobot(3); // 3
                    }
                    if (robotsCounter == 0){
                        myGame.addRobot(19); // 3
                    }
                    if (robotsCounter == 2){
                        myGame.addRobot(40); // 3
                    }

                    repaint();
                    robotsCounter++;
                }
            }
            myGame.startGame();
            moveRobotAuto.start();
        });

        moveRobotAuto = new Thread(() -> {
            JSONObject getAutoGameScore;
            MovesCounter = 0;

             while (myGame.timeToEnd() > 100) {
                 try {
                     //To find always the best fruit after eating one
                     FruitsCol.clear();
                     AutomaticGame.getGameFruits(FruitsCol, dGraph, myGame);
                     getAutoGameScore = new JSONObject(myGame.toString());
                     JSONObject autoGameScore = getAutoGameScore.getJSONObject("GameServer");
                     gameScore = autoGameScore.getInt("grade");

                     int to_sleep = 50;
                     try {
                         if (scenario == 0 || scenario == 3 || scenario == 1);
                             to_sleep =+105;
                         if (scenario == 5) to_sleep += 20;
                         if (scenario == 13) to_sleep = 99;
                         if (scenario == 16 ) to_sleep = 102;
                         if (scenario == 20) to_sleep = 102;
                         if (scenario == 23 ) to_sleep = 51;

                         Thread.sleep(to_sleep);
                         AutomaticGame.moveRobots(myGame , FruitsCol);

                         MovesCounter++;
                     } catch (Exception e) {
                             e.printStackTrace();
                     }

                 }
                 catch (Exception ignored) { }
                 repaint();
             }

            Image b = byeImage;
            Image newb = b.getScaledInstance(130, 150,  Image.SCALE_SMOOTH);
            ImageIcon by = new ImageIcon(newb);
            JOptionPane.showMessageDialog(this, "Game Over!\n In stage "+ this.scenario + " The Final Score : " + this.gameScore +" in " + this.MovesCounter + " Moves ", "Close" , JOptionPane.ERROR_MESSAGE, by );
            int n = JOptionPane.showConfirmDialog(this , "Export to KML ?" , "Export" , JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
                log.closeDocument();
                this.setVisible(false);
                String remark = "data/" +this.scenario;
                myGame.sendKML(remark);
                System.exit(0);
            }
            else {
                this.setVisible(false);
                System.exit(0);
            }

        });

        sql = new Thread() {
            @Override
            public void run() {
                DataBaseGUI n = new DataBaseGUI();
            }
        };
    }

    /**
     * finding the nodes for kml
     * @param s- game graph
     */
    private void findNodes(String s)  {
        JSONObject graph;
        try {
            // read the nodes
            graph  = new JSONObject(s);
            JSONArray nodes = graph.getJSONArray("Nodes");
            for (int j = 0; j < nodes.length(); j++) {
                String pos = nodes.getJSONObject(j).getString("pos");
                this.log.addNodePlaceMark(pos);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}