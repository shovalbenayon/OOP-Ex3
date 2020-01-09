package gui;


import algorithms.Graph_Algo;
import dataStructure.*;
import utils.Point3D;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.List;
import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Graph_GUI extends JFrame implements ActionListener, MouseListener,Serializable
{

    private graph Dgraph = new DGraph();
    private Graph_Algo graph_algo = new Graph_Algo();
    private ArrayList<node_data> SP= new ArrayList<>();


    /**
     * Init the GUI
     */
    private Graph_GUI()
    {
        initGUI();
    }

    /**
     * Init GUI with existing graph
     * @param g - graph to init
     */
    public Graph_GUI(graph g )
    {
        this.Dgraph = g;
        this.graph_algo.init(g);
        initGUI();

    }

    /**
     * variables to init the GUI window
     */
    private void initGUI()
    {
        this.setSize(this.graph_algo.get_Width(), this.graph_algo.get_Height()); // set the size of the window according the existing graph
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MenuBar menuBar = new MenuBar();

        Menu File = new Menu("File"); //main's manu
        menuBar.add(File);
        this.setMenuBar(menuBar);

        Menu Functions = new Menu("Graph Algo Functions");//main's manu
        menuBar.add(Functions);
        this.setMenuBar(menuBar);

        Menu DGraph1 = new Menu("DGraph Functions");//main's manu
        menuBar.add(DGraph1);
        this.setMenuBar(menuBar);

        MenuItem IsConnected = new MenuItem("Is Connected");
        IsConnected.addActionListener(this);

        MenuItem ShortestPathDist = new MenuItem("Shortest Path Dist");
        ShortestPathDist.addActionListener(this);

        MenuItem ShortestPath = new MenuItem("Shortest Path");
        ShortestPath.addActionListener(this);

        MenuItem TSP = new MenuItem("TSP");
        TSP.addActionListener(this);

        MenuItem SaveToFile = new MenuItem("Save To File");
        SaveToFile.addActionListener(this);

        MenuItem InitFromFile = new MenuItem("Init From File");
        InitFromFile.addActionListener(this);

        MenuItem Nodes_Size = new MenuItem("Nodes Size");
        Nodes_Size.addActionListener(this);

        MenuItem Edges_size = new MenuItem("Edges size");
        Edges_size.addActionListener(this);

        MenuItem connect = new MenuItem("Connect");
        connect.addActionListener(this);

        MenuItem remove = new MenuItem("Remove Edge");
        remove.addActionListener(this);

        MenuItem remove_node = new MenuItem("Remove Node");
        remove_node.addActionListener(this);

        File.add(SaveToFile);
        File.add(InitFromFile);

        DGraph1.add(Nodes_Size);
        DGraph1.add(Edges_size);
        DGraph1.add(connect);
        DGraph1.add(remove);
        DGraph1.add(remove_node);


        Functions.add(ShortestPath);
        Functions.add(ShortestPathDist);
        Functions.add(TSP);
        Functions.add(IsConnected);

        this.addMouseListener(this);
    }

    /**
     * paint the graph
     * @param g graph
     */
    public void paint(Graphics g)
    {
        super.paint(g);
        Collection<node_data> col = Dgraph.getV();
        Iterator<node_data> iterNodes = col.iterator();

        while (iterNodes.hasNext()) // draw the Data Nodes
        {
            g.setColor(Color.BLUE);
            node_data temp = iterNodes.next();
            Point3D p = new Point3D(temp.getLocation());
            g.fillOval((int)p.x(), (int)p.y(), 10, 10);
            g.drawString(String.valueOf(temp.getKey()) , (int)p.x() ,(int)p.y());
        }

        iterNodes = col.iterator();

        while (iterNodes.hasNext()){
            node_data tempNode = iterNodes.next();
            Collection<edge_data> edgeCol = this.Dgraph.getE(tempNode.getKey());
            if (edgeCol !=null) {
                Iterator<edge_data> iterEdge = edgeCol.iterator();

                while (iterEdge.hasNext()) { // draw the Edges nodes
                    {
                        edge_data ed = iterEdge.next();
                        node_data start = this.Dgraph.getNode(ed.getSrc());
                        node_data end = this.Dgraph.getNode(ed.getDest());
                        g.setColor(Color.RED);

                        g.drawLine((int) end.getLocation().x()+5, (int) end.getLocation().y()+5,
                                (int) start.getLocation().x()+5, (int) start.getLocation().y()+5);
                        String weight = String.valueOf(ed.getWeight());


                        //draw the direction of the Edge
                        double directionX = ((end.getLocation().x()+2) * 8 + (start.getLocation().x())+2 )/ 9;
                        double  directionY = ((end.getLocation().y()+2 )* 8 + (start.getLocation().y())+2) / 9;
                        g.setColor(Color.YELLOW);
                        g.fillOval((int) directionX, (int) directionY, 10, 10);

                        g.setColor(Color.BLACK);
                        g.drawString(weight, (int) ((start.getLocation().x() + end.getLocation().x()) / 2), (int) (start.getLocation().y() + end.getLocation().y()) / 2);
                    }
                }
            }

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
            case "Shortest Path Dist":
                ShortestPathDist();
                break;
            case "Is Connected":
                IsConnected();
                break;
            case "Init From File":
                InitFromFile();
                break;
            case "Shortest Path":
                 ShortestPath();
                break;
            case "Save To File":
                SaveToFile();
                break;
            case "TSP":
                TSP();
                break;
            case "Edges size":
                EdgesSize();
                break;
            case "Nodes Size":
                NodesSize();
                break;
            case "Connect":
                connect();
                break;
            case "Remove Node":
                RemoveNode();
                break;
            case "Remove Edge":
                RemoveEdge();
                break;
        }

    }

    /**
     * connect between 2 Nodes
     */
    private void connect() {

        JFrame frame = new JFrame();
        String source = JOptionPane.showInputDialog(frame,"Source Node");
        String dest = JOptionPane.showInputDialog(frame,"Destination Node");
        String weight = JOptionPane.showInputDialog(frame , "Weight of the Edge");
        try
        {
            int src = Integer.parseInt(source);
            int des = Integer.parseInt(dest);
            double wet = Double.parseDouble(weight);
            this.Dgraph.connect(src , des, wet);
            this.graph_algo.init(this.Dgraph);
            repaint();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * remove edge from the graph
     */
    private void RemoveEdge() {

        JFrame frame = new JFrame();
        String source = JOptionPane.showInputDialog(frame,"Source Node");
        String dest = JOptionPane.showInputDialog(frame,"Destination Node");

        try
        {
            int src = Integer.parseInt(source);
            int des = Integer.parseInt(dest);

            this.Dgraph.removeEdge(src, des);
            this.graph_algo.init(this.Dgraph);
            repaint();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * remove node from the graph
     */
    private void RemoveNode() {

        JFrame frame = new JFrame();
        String source = JOptionPane.showInputDialog(frame,"Node To Remove");
        try
        {
            int src = Integer.parseInt(source);
            this.Dgraph.removeNode(src);
            this.graph_algo.init(this.Dgraph);
            repaint();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void TSP(){

        Graph_GUI TS = new Graph_GUI();
        graph temp_gr = new DGraph();
        List<node_data> TSPans = new LinkedList<>();
        JFrame frame = new JFrame("TSP");
        String insert = JOptionPane.showInputDialog(frame,"Insert List of Node (separated by spaces)");

        try {
            ArrayList<Integer> NodesInt = new ArrayList<Integer>();
            //Splitting by space and inserting to a list of targets:
            String[] input = insert.split(" ");
            for (int i = 0; i < input.length; i++) {
                int temp = Integer.parseInt(input[i]);
                NodesInt.add(temp);
            }
            StringBuilder output = new StringBuilder();
            TSPans = graph_algo.TSP(NodesInt);
            if (TSPans == null) {
                JOptionPane.showMessageDialog(frame, "No Path between 2 nodes", "TSP ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else {
                output.append("The path is: \n");
                for (int i = 0; i < TSPans.size(); i++) {
                    output.append(TSPans.get(i).getKey() + "");
                    if (i != TSPans.size() - 1)
                        output.append("->");
                }
                JOptionPane.showMessageDialog(frame, output + "\n enter ok to see the graph", "TSP", JOptionPane.INFORMATION_MESSAGE);
                frame.setVisible(false);
            }

        } catch (Exception e1) {
            JOptionPane.showMessageDialog(frame, e1.getMessage(), "TSP", JOptionPane.ERROR_MESSAGE);
        }

        if (TSPans != null) {
            Iterator<node_data> iterNodes = TSPans.iterator();
            ArrayList<Integer> NodesArray = new ArrayList<>();
            while (iterNodes.hasNext()) {
                node_data temp = iterNodes.next();
                temp_gr.addNode(temp);
                NodesArray.add(temp.getKey());
            }
            for (int i = 0 ; i < NodesArray.size() -1; i++){

                node_data cur = temp_gr.getNode(NodesArray.get(i));
                node_data next = temp_gr.getNode(NodesArray.get(i+1));
                double weight = this.Dgraph.getEdge(cur.getKey(), next.getKey()).getWeight();
                temp_gr.connect(cur.getKey(), next.getKey(), weight);

            }
        }

        else
        {
            JOptionPane.showMessageDialog(frame, "char not valid ");
        }

        TS.Dgraph= temp_gr;
        TS.graph_algo.init(temp_gr);
        TS.initGUI();
        TS.setVisible(true);
        TS.repaint();

        repaint();


    }

    /**
     * shortest path drawing, open a window with the path of the nodes and then open a new window with the
     * drawing og the new graph . only with the shortest path
     */
    private void ShortestPath(){

        Graph_GUI SP_Graph = new Graph_GUI();
        DGraph dg = new DGraph();

        List<node_data> NodesPath = new ArrayList<>();

        JFrame frame = new JFrame();
        String source = JOptionPane.showInputDialog(frame,"Source Node");
        String dest = JOptionPane.showInputDialog(frame,"Destination Node");
        try
        {
            int src = Integer.parseInt(source);
            int des = Integer.parseInt(dest);
            double NodesPathDouble = this.graph_algo.shortestPathDist(src , des);
            if (NodesPathDouble == Double.POSITIVE_INFINITY) {
                JOptionPane.showMessageDialog(frame, "Not Valid Path between 2 Nodes ");
                return;
            }
            else {
                StringBuilder pathNodes = new StringBuilder("The path is: \n");
                NodesPath =  this.graph_algo.shortestPath(src, des);
                for (int i = 0 ; i< NodesPath.size() ; i++) {
                    pathNodes.append(NodesPath.get(i).getKey());
                    if (i != NodesPath.size() -1)
                        pathNodes.append("->");
                }
                JOptionPane.showMessageDialog(frame , pathNodes + "\n enter ok to see the graph");


            }

        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        if (NodesPath != null) {
            Iterator<node_data> iterNodes = NodesPath.iterator();
            ArrayList<Integer> NodesArray = new ArrayList<>();
            while (iterNodes.hasNext()) {
                node_data temp = iterNodes.next();
                dg.addNode(temp);
                NodesArray.add(temp.getKey());
            }
            for (int i = 0 ; i < NodesArray.size() -1; i++){

                node_data cur = dg.getNode(NodesArray.get(i));
                node_data next = dg.getNode(NodesArray.get(i+1));
                double weight = this.Dgraph.getEdge(cur.getKey(), next.getKey()).getWeight();
                dg.connect(cur.getKey(), next.getKey(), weight);

            }
        }

        else
        {
            JOptionPane.showMessageDialog(frame, "char not valid ");
        }

        SP_Graph.Dgraph= dg;
        SP_Graph.graph_algo.init(dg);
        SP_Graph.initGUI();
        SP_Graph.setVisible(true);
        SP_Graph.repaint();

        repaint();

    }

    /**
     * shortest path value, open a window with the value
     */
    private void ShortestPathDist(){

        JFrame frame = new JFrame();
        double shortestpath ;
        String start = JOptionPane.showInputDialog(frame,"Source Node");
        String finish = JOptionPane.showInputDialog(frame,"Destination Node");
        try
        {
            int src = Integer.parseInt(start);
            int des = Integer.parseInt(finish);

            shortestpath = this.graph_algo.shortestPathDist(src, des);

            if(shortestpath != Double.POSITIVE_INFINITY)
            {
                JOptionPane.showMessageDialog(frame, "The shortest Path distance between "+src +" and "+ des +" is: " + shortestpath);
            }

            else
            {
                JOptionPane.showMessageDialog(frame, "Not Valid Path between 2 Nodes");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        repaint();

    }

    /**
     * open a new window and show if the graph is connected or not. false or true answer
     */
    private void IsConnected(){

        JFrame frame = new JFrame();
        boolean ifConnected = this.graph_algo.isConnected();

        JOptionPane.showMessageDialog(frame , "The graph is all connected? \n" + ifConnected);

    }

    /**
     * open a new window and showing the nodes size
     */
    private void NodesSize(){
        JFrame frame = new JFrame();
        int size = this.graph_algo.getnodeSize();

        JOptionPane.showMessageDialog(frame , "Nodes size is : \n" + size);

    }

    /**
     * open a new window and showing the Edges size
     */

    private void EdgesSize(){
        JFrame frame = new JFrame();
        int size = this.graph_algo.getedgeSize();
        JOptionPane.showMessageDialog(frame , "Edges size is : \n" + size);

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
            this.graph_algo.save(pathToSave.getDirectory()+filename+".txt");
        }
    }

    /**
     * init the graph from file
     */
    private void InitFromFile(){

        JFrame frameToChoose = new JFrame("Test");
        frameToChoose.setLayout(new FlowLayout());
        frameToChoose.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            this.graph_algo.init(selectedFile.getPath());
            this.Dgraph = this.graph_algo.copy();
            repaint();
        }
        frameToChoose.pack();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        Point3D p = new Point3D(x,y);
        node_data new_node = new DataNode(this.Dgraph.nodeSize()+1 , p);
        this.Dgraph.addNode(new_node);
        this.graph_algo.init(this.Dgraph);
        repaint();

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
