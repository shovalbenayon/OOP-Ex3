package gui;

import Server.Game_Server;
import Server.game_service;
import algorithms.Graph_Algo;
import dataStructure.*;
import elements.Fruit;
import elements.Robot;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Point3D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class MyGameGUI extends JFrame implements ActionListener, MouseListener, Serializable  {
    private game_service game;
    private Collection<Robot> Robot_col = new LinkedList<>();
    private Collection<Fruit> Fruit_col = new LinkedList<>();
    private DGraph _myDgraph = new DGraph();
    private Graph_Algo _graph_algo = new Graph_Algo(_myDgraph);
    private static Image image;
    private boolean robotf = false;
    private JPanel gamePanel;
    private JButton moveRobot;
    private Thread moveSerialRobot;
    private Thread addRobots;
    private static int robotsNum = 0;
    private static int robotsCounter = 0;
    private BufferedImage appleImage;
    private BufferedImage bananaImage;
    private BufferedImage robotImage;




    /**
     * Init the GUI
     */
    private MyGameGUI()
    {
        initGUI();
    }

    /**
     * Init GUI with existing graph
     * @param gameLevel - graph to init
     */
    public MyGameGUI(int gameLevel)
    {
        gamePanel = new JPanel();
        gamePanel.setPreferredSize(new Dimension(1500, 900));
        moveRobot = new JButton("Move robot");
        gamePanel.add(moveRobot);
        gamePanel.add(Box.createHorizontalGlue());
        this.add(gamePanel, BorderLayout.SOUTH);

        // Robots move thread
        moveSerialRobot = new Thread() {
            public void run() {
                long start = System.currentTimeMillis();
                while (true) {
                    if (System.currentTimeMillis() - start > 10) {
                        try {
                            game.move();
                        } catch (Exception e) {
                            System.out.println("Exception");
                        }

                        start = System.currentTimeMillis();
                        repaint();
                    }
                }
            }
        };

        // Add robots and start thread
        addRobots = new Thread() {
            public void run() {
                while (robotsCounter < robotsNum) { // Choose robots location once (Manual game)
                    String srcNode = JOptionPane.showInputDialog(this, "Enter src node");
                    try {
                        int rNode = Integer.parseInt(srcNode);
                        if (_myDgraph.getNode(rNode) == null) throw new RuntimeException();
                        game.addRobot(rNode);
                        repaint();
                        robotsCounter++;
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

                game.startGame();
                moveSerialRobot.start();
            }
        };

        // Initialize gameGraph
        game = Game_Server.getServer(gameLevel);
        _myDgraph.init(game.getGraph());


        moveRobot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean vFlag = true;
                String rSerial = JOptionPane.showInputDialog("Enter Robot serial ID");
                try {
                    int robotSerial = Integer.parseInt(rSerial);
                    if (robotSerial < 0) throw new RuntimeException();
                    if (robotSerial >= robotsNum) throw new RuntimeException();
                } catch (Exception Ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                    vFlag = false;
                }

                if (vFlag) {
                    String dNode = JOptionPane.showInputDialog("Enter destination node");
                    try {
                        int destNode = Integer.parseInt(dNode);
                        if (_myDgraph.getNode(destNode) == null) throw new RuntimeException();
                        game.chooseNextEdge(Integer.parseInt(rSerial), destNode);
                    } catch (Exception Ex) {
                        JOptionPane.showMessageDialog(null, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        this.getInfoFromString();
        initGUI();

    }



    /**
     * variables to init the GUI window
     */
    private void initGUI()
    {
        this.setSize(this._graph_algo.get_Width()*25, this._graph_algo.get_Height()*15); // set the size of the window according the existing graph
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame mainFrame = new JFrame("Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.add(new MyGameGUI(17));
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
    }

    /**
     * paint the graph
     * @param g graph
     */
    public void paint(Graphics g)
    {
        super.paint(g);
        Collection<node_data> col = this._myDgraph.getV();
        Iterator<node_data> iterNodes = col.iterator();
        Point3D minP = minPoint();
        Point3D maxp = maxPoint();

        while (iterNodes.hasNext()) // draw the Data Nodes
        {
            g.setColor(Color.BLUE);
            g.setFont(new Font("default", Font.BOLD, 18));
            node_data temp = iterNodes.next();
            Point3D PointToScale = ScaleToFrame(temp.getLocation(),minP,maxp);
            g.fillOval((int)PointToScale.x(), (int)PointToScale.y(), 10, 10);
            g.drawString(String.valueOf(temp.getKey()) , (int)PointToScale.x() ,(int)PointToScale.y());

            if (this._myDgraph.getE(temp.getKey()) != null) {

                Collection<edge_data> edgeCol = this._myDgraph.getE(temp.getKey());
                if (edgeCol != null) {
                    Iterator<edge_data> iterEdge = edgeCol.iterator();

                    while (iterEdge.hasNext()) { // draw the Edges nodes
                        {
                            edge_data ed = iterEdge.next();
                            g.setColor(Color.BLACK);
                            Point3D destNodeScaledData = ScaleToFrame(this._myDgraph.getNode(ed.getDest()).getLocation(),minP,maxp);

                            double xSrc = PointToScale.x();
                            double ySrc = PointToScale.y();
                            double xDest = destNodeScaledData.x();
                            double yDest = destNodeScaledData.y();
                            g.drawLine((int) xSrc+5, (int) ySrc+5, (int) xDest+5, (int) yDest+5);
                            
                        }
                    }
                }
            }

        }
        for (Fruit f: this.Fruit_col)
        {
            Point3D fruitScaled = ScaleToFrame(f.getPos(),minP,maxp);
            if (f.getType() == 1) {
                String imageURL = "apple.png";
                image = Toolkit.getDefaultToolkit().getImage(imageURL);
                g.drawImage(image , fruitScaled.ix()-5 , fruitScaled.iy() -5, 30 ,35 , this);
            }
            else{
                String imageURL = "android.png";
                image = Toolkit.getDefaultToolkit().getImage(imageURL);
                g.drawImage(image , fruitScaled.ix()-5, fruitScaled.iy()-5 , 30 ,35 , this);
            }
        }
            for (Robot r : Robot_col) {
                Point3D fruitScaled = ScaleToFrame(r.getCurr_node().getLocation(), minP, maxp);
                String imageURL = "robot.png";
                image = Toolkit.getDefaultToolkit().getImage(imageURL);
                g.drawImage(image, fruitScaled.ix() , fruitScaled.iy() , 40, 40, this);

            
        }

    }

    /**
     * paint the graph by the selected algorithms
     * @param e the algorithms option
     */
    public void actionPerformed(ActionEvent e)
    {
        String str = e.getActionCommand();
        switch (str){
            case "Save To File":
                SaveToFile();
                break;
            case "Start new game":
                StartNewGame();
                break;
            case "Start automatic game":
                StartautoGame();
                break;
        }

    }

    private void StartautoGame() {
    }

    private void StartNewGame() {
        int count  = 0;
        for (Robot r : Robot_col){
            robotf = true;
            count ++;
            JFrame frame = new JFrame();
            String source = JOptionPane.showInputDialog(frame,"Position for robot number " + String.valueOf(count));
            try
            {
                int src = Integer.parseInt(source);
                //(this._myDgraph.getNode(src));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        repaint();
    }

    private void getInfoFromString(){
        try {
            JSONObject line;
            line = new JSONObject(this.game.toString());
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("robots");
            System.out.println(this.game.toString());

            int src_node = 0;  // arbitrary node, you should start at one of the fruits
            for(int a = 0;a<rs;a++) {
                game.addRobot(src_node+a);
            }

            List<String> robotsList = this.game.getRobots();
            Iterator<String> robotsIter = robotsList.iterator();
            while (robotsIter.hasNext()){
                String temp = robotsIter.next();
                elements.Robot robot = new Robot(temp);
                this.Robot_col.add(robot);
            }
            List<String> fruitcol = game.getFruits();
            Iterator<String> fruitsIter = fruitcol.iterator();
            while (fruitsIter.hasNext()){
                String temp = fruitsIter.next();
                Fruit fruit = new Fruit(temp);
                this.Fruit_col.add(fruit);
            }

        } catch (Exception var10) {
            var10.printStackTrace();
        }
    }


    /**
     * save to file option
     */
    private void SaveToFile(){

        JFrame frame = new JFrame();
        FileDialog pathToSave = new FileDialog(frame, "Save the graph", FileDialog.SAVE);
        pathToSave.setVisible(true);
        String filename = pathToSave.getFile();
        if (filename != null)
        {
            this._graph_algo.save(pathToSave.getDirectory()+filename+".txt");
        }
    }
    /**
     * find the maximum point on the graph
     * @return maximum point
     */
    private Point3D minPoint()
    {
        if (this._myDgraph.getV().size() == 0)
        {
            return null;
        }

        double  min_x = Double.MAX_VALUE;
        double  min_y = Double.MAX_VALUE;

        for (node_data n : this._myDgraph.getV())
        {
            if (n.getLocation().x() < min_x)
            {
                min_x = n.getLocation().x();
            }
            if(n.getLocation().y() < min_y)
            {
                min_y = n.getLocation().y();
            }
        }
        return new Point3D(min_x,min_y);
    }

    /**
     * find the minimum point on the graph
     * @return minimum
     */

    private Point3D maxPoint()
    {
        if (this._myDgraph.getV().size() == 0)
        {
            return null;
        }
        double  max_x = Double.MIN_VALUE;
        double  max_y = Double.MIN_VALUE;

        for (node_data n : this._myDgraph.getV())
        {
            if (n.getLocation().x() > max_x)
            {
                max_x = n.getLocation().x();
            }
            if(n.getLocation().y() > max_y)
            {
                max_y = n.getLocation().y();
            }
        }
        return new Point3D(max_x,max_y);
    }

    /**
     * function to scale the nodes to the frame from yael
     * @param location - the dot to scale
     * @param minPoint - the minimum point on the graph
     * @param maxPoint -  the maximum point on the graph
     * @return the new scaled point
     */
    private Point3D ScaleToFrame(Point3D location,Point3D minPoint,Point3D maxPoint)
    {
        double r_min_x = minPoint.x();
        double r_max_x = maxPoint.x();
        double r_min_y = minPoint.y();
        double r_max_y = maxPoint.y();

        double t_min_x = 200;
        double t_max_x = this.getWidth()-200;
        double t_min_y = 200;
        double t_max_y = this.getHeight()-100;

        double x = location.x();
        double y = location.y() ;

        double res_x = ((x-r_min_x)/(r_max_x-r_min_x)) * (t_max_x - t_min_x) +t_min_x;
        double res_y = ((y-r_min_y)/(r_max_y-r_min_y)) * (t_max_y - t_min_y) +t_min_y;

        return new Point3D(res_x,res_y);
    }



    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {


    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
