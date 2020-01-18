package gameClient;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import elements.Fruit;
import elements.Robot;
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
    private DGraph gameGraph = new DGraph();
    private Graph_Algo autoGameGraph = new Graph_Algo();

    private final int X_RANGE = 1500;
    private final int Y_RANGE = 900;
    private JPanel gamePanel;
    JFrame Frame = new JFrame("Game");

    // Manual game fields
    private Thread manualMoveRobot;
    private Thread manualChooseLocation;
    private JButton moveButton = new JButton("Move Robot");

    // Automatic game fields
    private Thread autoChooseLocation;
    private Thread autoMoveRobot;
    private ArrayList<Fruit> FruitsCol = new ArrayList<>();
    private Collection<Robot> RobotCol = new LinkedList<>();

    //Images for the frame
    private BufferedImage appleImage;
    private BufferedImage androidImage;
    private BufferedImage robotImage;
    private Image backgroundImage;

    private static int robotsNum = 0;
    private static int robotsCounter = 0;
    private int gameScore = 0;

    // Game mode flags
    private boolean autoMode = false;
    private boolean modeFlag = false;


    public void InitGui() {
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.add(new MyGameGUI(this.gameNumber));
        Frame.pack();
        Frame.setVisible(true);
        Frame.setLocationRelativeTo(null); // puts the frame in the middle of the screem
    }

    public MyGameGUI(int gameNumber) {
        this.gameNumber = gameNumber;
        gamePanel = new JPanel();
        gamePanel.setPreferredSize(new Dimension(X_RANGE, Y_RANGE));
        this.gamePanel.add(moveButton);
        this.add(gamePanel, BorderLayout.SOUTH);
        backgroundImage = Toolkit.getDefaultToolkit().createImage("Background.jpg");
        Threads();

        myGame = Game_Server.getServer(gameNumber);
        gameGraph.init(myGame.getGraph());
        getRobotsFromString();

        try {
            File apple = new File("apple.png");
            appleImage = ImageIO.read(apple);
            File android= new File("android.png");
            androidImage = ImageIO.read(android);
            File robot = new File("robot.png");
            robotImage = ImageIO.read(robot);
            File background = new File("background.jpg");
            backgroundImage = ImageIO.read(background);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(backgroundImage, 5, 50, 1500, 900, null);

        Collection<node_data> nodesCol = gameGraph.getV();
        double minX = getMinX(nodesCol);
        double maxX = getMaxX(nodesCol);
        double minY = getMinY(nodesCol);
        double maxY = getMaxY(nodesCol);

        //drawing the nodes
        for (node_data node : nodesCol) {
            g.setColor(Color.PINK);
            g.setFont(new Font("default", Font.BOLD, 15));
            double scaledX = scale(node.getLocation().x(), minX, maxX, 100, (double)X_RANGE - 100);
            double scaledY = scale(node.getLocation().y() ,minY, maxY, 100, (double)Y_RANGE - 100);
            g.fillOval((int)scaledX, (int)scaledY, 10, 10);
            g.drawString(String.valueOf(node.getKey()), (int)scaledX, (int)scaledY - 5);

            //drawing the edges
            Collection<edge_data> edgesCol = gameGraph.getE(node.getKey());
            for (edge_data edge : edgesCol) {
                node_data dest = gameGraph.getNode(edge.getDest());
                Point3D destLocation = dest.getLocation();

                g.setColor(Color.BLACK);
                double scaledXDest = scale(destLocation.x(), minX, maxX, 100, (double)X_RANGE - 100);
                double scaledYDest = scale(destLocation.y(), minY, maxY, 100, (double)Y_RANGE - 100);
                g.drawLine((int)scaledX + 5, (int)scaledY + 5, (int)scaledXDest + 5, (int)scaledYDest + 5);
            }
        }

        // Fruits drawl
        List<String> fruitsList = myGame.getFruits();
        for (String tempFruit : fruitsList) {
            Fruit newFruit = new Fruit(tempFruit, gameGraph);
            this.FruitsCol.add(newFruit);
            double fruitX = newFruit.getPos().x();
            double fruitY = newFruit.getPos().y();
            fruitX = scale(fruitX, minX, maxX, 100, X_RANGE - 100);
            fruitY = scale(fruitY, minY, maxY, 100, Y_RANGE - 100);
            if (newFruit.getType() == 1)
                g.drawImage(appleImage, (int)fruitX - 5, (int)fruitY - 5, 20, 25, this);
            else
                g.drawImage(androidImage, (int)fruitX - 5, (int)fruitY - 5, 20, 25, this);
        }

        // Robots drawl
        List<String> robotsList = myGame.getRobots();
        for (String tempRobot : robotsList) {
            Robot newRobot = new Robot(tempRobot);
            this.RobotCol.add(newRobot);
            double robotX = newRobot.getPos().x();
            double robotY = newRobot.getPos().y();
            robotX = scale(robotX, minX, maxX, 100, X_RANGE - 100);
            robotY = scale(robotY, minY, maxY, 100, Y_RANGE - 100);
            g.drawImage(robotImage, (int)robotX - 20, (int)robotY - 20, 40, 40, this);
        }

        // Remaining time drawl
        g.setFont(new Font("Helvetica", Font.BOLD, 15));
        g.setColor(Color.BLACK);
        long timeToEnd = myGame.timeToEnd() / 1000;
        if (timeToEnd < 10) g.setColor(Color.RED);
        if (timeToEnd < 1) g.drawString("GAME OVER", X_RANGE - 100, 20);
        else g.drawString("Remaining time : " +timeToEnd, X_RANGE - 150, 20);

        // Total score drawl
        g.setColor(Color.BLACK);
        g.drawString("Total game score : " + gameScore, X_RANGE - 1490, 20);

        if (!modeFlag) {
            try {
                Image img = robotImage;
                Image newimg = img.getScaledInstance(130, 150,  java.awt.Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(newimg);

                Object[] options = {"Manual game!",
                        "Automatic game!"};
                int dialogButton = JOptionPane.showOptionDialog(null, "Which game would you like to play?", "Manual or Automatic", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, icon, options, options[0]);
                if (dialogButton == JOptionPane.YES_OPTION) {
                    autoMode = false;
                    //move button available only when user playing on manual
                    moveButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean vFlag = true;

                            String[] options = new String[robotsNum];
                            for (int i = 0 ; i < robotsNum ; i++){
                                options[i] = String.valueOf(i);
                            }

                            String idRobot = (String) JOptionPane.showInputDialog(null,"Enter id number of the robot you want to move" ,"Move",JOptionPane.QUESTION_MESSAGE,icon , options , options[0]);
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
                                    if (gameGraph.getNode(destinationNode) == null) throw new RuntimeException();
                                    myGame.chooseNextEdge(Integer.parseInt(idRobot), destinationNode);
                                } catch (Exception Ex) {
                                    JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    });

                }


                else {
                    autoMode = true;
                    autoGameGraph.init(gameGraph);
                    moveButton.setVisible(false);
                }

                modeFlag = true;
            } catch (HeadlessException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!autoMode) {
                if (!manualChooseLocation.isAlive() && !manualMoveRobot.isAlive())
                    manualChooseLocation.start();
            }

            else {
                if (!autoChooseLocation.isAlive() && !autoMoveRobot.isAlive())
                    autoChooseLocation.start();
            }

        }


    }

    /**
     *
     * @param data denote some data to be scaled
     * @param r_min the minimum of the range of your data
     * @param r_max the maximum of the range of your data
     * @param t_min the minimum of the range of your desired target scaling
     * @param t_max the maximum of the range of your desired target scaling
     * @return
     */

    private double scale(double data, double r_min, double r_max, double t_min, double t_max) {
        double res = ((data - r_min) / (r_max-r_min)) * (t_max - t_min) + t_min;
        return res;
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
    private void getRobotsFromString(){
        try {
            JSONObject line;
            line = new JSONObject(this.myGame.toString());
            JSONObject server = line.getJSONObject("GameServer");
            robotsNum = server.getInt("robots");

        } catch (Exception var10) {
            var10.printStackTrace();
        }
    }


    public void Threads(){
        // Manual game threads
        manualChooseLocation = new Thread() {
            public void run() {
                while (robotsCounter < robotsNum) {
                    String src = JOptionPane.showInputDialog("Enter Position for Robot Number " + robotsCounter );
                    try {
                        int source = Integer.parseInt(src);
                        if (gameGraph.getNode(source) == null) throw new RuntimeException();
                        myGame.addRobot(source);
                        repaint();
                        robotsCounter++;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                myGame.startGame();
                manualMoveRobot.start();
            }
        };

        manualMoveRobot = new Thread() {
            public void run() {
                JSONObject getManualGameScore;
                long start = System.currentTimeMillis();
                while (myGame.isRunning()) {
                    if (System.currentTimeMillis() - start > 20) {
                        try {
                            getManualGameScore = new JSONObject(myGame.toString());
                            JSONObject manualGameScore = getManualGameScore.getJSONObject("GameServer");
                            gameScore = manualGameScore.getInt("grade");
                            myGame.move();
                        } catch (Exception e) {
                            System.out.println("Exception");
                        }

                        start = System.currentTimeMillis();
                        repaint();
                    }
                }
            }
        };

        // Automatic game threads
        autoChooseLocation = new Thread() {
            public void run() {
                while (robotsCounter < robotsNum) {
                    Fruit bestFruit = getBestFruit(FruitsCol);

                    // Choose the best location based on the greatest fruits values
                    int autoSrcNode;
                    if (bestFruit.getType() == 1)
                        autoSrcNode = bestFruit.getEdge().getSrc(); // Apple
                    else
                        autoSrcNode = bestFruit.getEdge().getDest(); // android
                    myGame.addRobot(autoSrcNode);

                    FruitsCol = removeBest(FruitsCol, bestFruit);
                    repaint();
                    robotsCounter++;
                }

                myGame.startGame();
                autoMoveRobot.start();
            }
        };

        autoMoveRobot = new Thread() {
            public void run() {
                JSONObject getAutoGameScore;
                long start = System.currentTimeMillis();
                while (myGame.isRunning()) {
                    if (System.currentTimeMillis() - start > 20) {
                        try {
                            FruitsCol.clear();
                            FruitsCol = getGameFruits(FruitsCol, gameGraph, myGame);
                            getAutoGameScore = new JSONObject(myGame.toString());
                            JSONObject autoGameScore = getAutoGameScore.getJSONObject("GameServer");
                            gameScore = autoGameScore.getInt("grade");

                            for (String autoRobot : myGame.getRobots()) {
                                Robot currentRobot = new Robot(autoRobot);
                                if (currentRobot.getDestination() == -1) {
                                    currentRobot.setDestination(getNext(FruitsCol, gameGraph, currentRobot.getSource())) ;
                                    myGame.chooseNextEdge(currentRobot.getID(), currentRobot.getDestination());
                                }
                            }
                            myGame.move();
                        } catch (Exception e) {
                        }

                        start = System.currentTimeMillis();
                        repaint();
                    }
                }
            }
        };

    }
    public static ArrayList<Fruit> getGameFruits(ArrayList<Fruit> gameFruits, DGraph gameGraph, game_service myGame) {
        List<String> fruitsList = myGame.getFruits();
        for (String myFruit : fruitsList)
            gameFruits.add(new Fruit(myFruit, gameGraph));

        return gameFruits;
    }

    public static Fruit getBestFruit(ArrayList<Fruit> gameFruits) {
        Fruit bestFruit = gameFruits.get(0);
        for (Fruit myFruit : gameFruits)
            if (myFruit.getValue() > bestFruit.getValue())
                bestFruit = myFruit;

        return bestFruit;
    }

    public static ArrayList<Fruit> removeBest(ArrayList<Fruit> gameFruits, Fruit delFruit) {
        for (Fruit myFruit : gameFruits) {
            if (delFruit.getValue() == myFruit.getValue()) {
                gameFruits.remove(myFruit);
                return gameFruits;
            }
        }
        return gameFruits;
    }

    public static int getNext(ArrayList<Fruit> gameFruits, DGraph gameGraph, int srcNode) {
        Graph_Algo myGraph = new Graph_Algo();
        myGraph.init(gameGraph);
        List<Integer> robotPath = new LinkedList<>();
        robotPath.add(srcNode);
        double maxProfit = 0;
        int nextNode = srcNode;

        for (Fruit myFruit : gameFruits) {
            double rToFruitSrc = myGraph.shortestPathDist(srcNode, myFruit.getEdge().getSrc());
            double rToFruitDest = myGraph.shortestPathDist(srcNode, myFruit.getEdge().getDest());

            if (myFruit.getType() == -1) { // Banana
                if (myGraph.shortestPathDist(srcNode, myFruit.getEdge().getDest()) == 0) {
                    int ret = myFruit.getEdge().getSrc(); // Collect banana from destination to source
                    gameFruits.remove(myFruit);
                    return ret;
                }

                else if (myFruit.getValue() / rToFruitDest > maxProfit) {
                    maxProfit = myFruit.getValue() / rToFruitDest;
                    nextNode = myFruit.getEdge().getDest();
                }
            }

            else if (myFruit.getType() == 1) { // Apple
                if (myGraph.shortestPathDist(srcNode, myFruit.getEdge().getSrc()) == 0) {
                    int ret2 = myFruit.getEdge().getDest();
                    //return myFruit.getEdge().getDest(); // Collect apple from source to destination
                    gameFruits.remove(myFruit);
                    return ret2;
                }
                else if (myFruit.getValue() / rToFruitSrc > maxProfit) {
                    maxProfit = myFruit.getValue() / rToFruitSrc;
                    nextNode = myFruit.getEdge().getSrc();
                }
            }
        }

        robotPath.add(nextNode);
        List<node_data> greedyPath = myGraph.TSP(robotPath);
        return greedyPath.get(1).getKey();
    }
}