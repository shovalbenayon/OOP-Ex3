package gui;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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

public class MyGameGUI extends JPanel {
    private game_service myGame;
    private int gameNumber;
    private DGraph dGraph = new DGraph();
    private Graph_Algo graph_algo = new Graph_Algo();
    private ArrayList<Fruit> FruitsCol = new ArrayList<>();

    private final int X_RANGE = 1500;
    private final int Y_RANGE = 900;
    private JButton moveButton = new JButton("Move Robot");

    // Threads
    private Thread MoveRobotManual;
    private Thread placeRobotsManual;
    private Thread placeRobotsAuto;
    private Thread moveRobotAuto;

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
        File r = new File("robot.png");
        try {
            Image robo = ImageIO.read(r);
            Image newimg = robo.getScaledInstance(130, 150,  Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(newimg);
            // Set the game Level - [0,23]
            String[] options = {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
            int gameNum = JOptionPane.showOptionDialog(null, "Choose the Level you would like to play", "Click a button",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, icon, options, options[0]);
            this.gameNumber = gameNum;
            if (gameNum == JOptionPane.CLOSED_OPTION){
                System.exit(0);
            }
        } catch (IOException | HeadlessException e) {
            e.printStackTrace();
        }
        log = new KML_Logger(""+gameNumber);
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(X_RANGE, Y_RANGE));
        mainPanel.add(moveButton);
        this.add(mainPanel, BorderLayout.SOUTH);
        backgroundImage = Toolkit.getDefaultToolkit().createImage("Background.jpg");
        Threads();

        myGame = Game_Server.getServer(gameNumber);
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
                                MovesCounter++;
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
            Image newb = b.getScaledInstance(130, 150,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon by = new ImageIcon(newb);
            JOptionPane.showMessageDialog(this, "Game Over!\n In stage "+ this.gameNumber+" The Final Score : " + this.gameScore +" in " + this.MovesCounter + " Moves ", "Close" , JOptionPane.ERROR_MESSAGE, by );
            int n = JOptionPane.showConfirmDialog(this , "Export to KML ?" , "Export" , JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
                log.closeDocument();
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
            while (robotsCounter < robotsNum) {
                //gets the most valuable fruit
                Fruit bestFruit = AutomaticGame.getBestFruit(FruitsCol);
                // Choose the best location based on the greatest fruits values
                int autoSrcNode;
                //If the direction is from lower id node to higher id node so place the robot in the source so the robot can eat the fruit
                if (bestFruit.getType() == 1)
                    autoSrcNode = bestFruit.getEdge().getSrc(); // Apple

                //else , opposite
                else
                    autoSrcNode = bestFruit.getEdge().getDest(); // android
                myGame.addRobot(autoSrcNode);

                AutomaticGame.removeBest(FruitsCol, bestFruit);
                repaint();
                robotsCounter++;
            }

            myGame.startGame();
            moveRobotAuto.start();
        });

        moveRobotAuto = new Thread(() -> {
            JSONObject getAutoGameScore;
            long start = System.currentTimeMillis();
            MovesCounter = 0;
            while (myGame.isRunning()) {
                if (System.currentTimeMillis() - start > 20) {
                    try {
                        //To find always the best fruit after eating one
                        FruitsCol.clear();
                        AutomaticGame.getGameFruits(FruitsCol, dGraph, myGame);
                        getAutoGameScore = new JSONObject(myGame.toString());
                        JSONObject autoGameScore = getAutoGameScore.getJSONObject("GameServer");
                        gameScore = autoGameScore.getInt("grade");

                        for  (String autoRobot : myGame.getRobots()) {
                            Robot currentRobot = new Robot(autoRobot);
                            //If the robot do not have destination , he will go to the best node to collect a fruit
                            if (currentRobot.getDestination() == -1) {
                                currentRobot.setDestination(AutomaticGame.getNext(FruitsCol, dGraph, currentRobot.getSource()));
                                myGame.chooseNextEdge(currentRobot.getID(), currentRobot.getDestination());
                                MovesCounter++;
                            }
                        }
                        myGame.move();
                    } catch (Exception ignored) {
                    }

                    start = System.currentTimeMillis();
                    repaint();
                }
            }
            Image b = byeImage;
            Image newb = b.getScaledInstance(130, 150,  java.awt.Image.SCALE_SMOOTH);
            ImageIcon by = new ImageIcon(newb);
            JOptionPane.showMessageDialog(this, "Game Over!\n In stage "+ this.gameNumber+ " The Final Score : " + this.gameScore +" in " + this.MovesCounter + " Moves ", "Close" , JOptionPane.ERROR_MESSAGE, by );
            int n = JOptionPane.showConfirmDialog(this , "Export to KML ?" , "Export" , JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
                log.closeDocument();
                this.setVisible(false);
                System.exit(0);
            }
            else {
                this.setVisible(false);
                System.exit(0);
            }

        });

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