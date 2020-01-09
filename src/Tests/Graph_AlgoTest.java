package Tests;

import java.util.LinkedList;
import java.util.List;
import dataStructure.DataNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import algorithms.Graph_Algo;
import dataStructure.DGraph;
import dataStructure.graph;
import dataStructure.node_data;
import utils.Point3D;
import static org.junit.jupiter.api.Assertions.*;

public class Graph_AlgoTest {
    DGraph myDG  = new DGraph();
    Graph_Algo MYGA = new Graph_Algo();
    @BeforeEach
    void init() {

        myDG.addNode(new DataNode(1, new Point3D(30,50)));
        myDG.addNode(new DataNode(2 , new Point3D(340,100)));
        myDG.addNode(new DataNode(3 , new Point3D(40,200)));
        myDG.addNode(new DataNode(4 , new Point3D(60,270)));
        myDG.addNode(new DataNode(5 , new Point3D(130,450)));
        myDG.addNode(new DataNode(6 , new Point3D(70,240)));
        myDG.connect(1,6,1);
        myDG.connect(1,3,34);
        myDG.connect(2,1,6);
        myDG.connect(2,6,1);
        myDG.connect(5,2,13);
        myDG.connect(4,6,5);
        myDG.connect(6,3,2);
        myDG.connect(6,5,1);

        MYGA.init(myDG);
    }

    @Test
    public void isConnected_test()
    {

        assertFalse(MYGA.isConnected());
        //adding 1 missing edge to make the graph connected

        myDG.connect(3,4,43);

        MYGA.init(myDG);
        assertTrue(MYGA.isConnected());
    }
    @Test
    public void shortestPathTest() {
        myDG.connect(3,4,43);
        MYGA.init(myDG);
        List<node_data> path= MYGA.shortestPath(1, 4);
        node_data temp = path.remove(0);
        node_data temp1 = path.remove(0);
        node_data temp2 = path.remove(0);
        node_data temp3 = path.remove(0);
        assertEquals(temp.getKey(),1);
        assertEquals(temp1.getKey(),6);
        assertEquals(temp2.getKey(),3);
        assertEquals(temp3.getKey(),4);

    }
    @Test
    public void shortestPathDisTest() {
        myDG.connect(3,4,43);
        MYGA.init(myDG);
        assertEquals(15,MYGA.shortestPathDist(1, 2),0.001);
        assertEquals(3,MYGA.shortestPathDist(1, 3),0.001);
        assertEquals(19,MYGA.shortestPathDist(5, 1),0.001);
        assertEquals(46,MYGA.shortestPathDist(1, 4),0.001);


    }
    @Test
    public void TSPTest()
    {
        List<Integer> lst = new LinkedList<>();
        lst.add(1);
        lst.add(6);
        lst.add(5);
        lst.add(2);

        List<node_data> path= MYGA.TSP(lst);
        assertEquals((int)(path.remove(0)).getKey(),1);
        assertEquals((int)(path.remove(0)).getKey(),6);
        assertEquals((int)(path.remove(0)).getKey(),5);
        assertEquals((int)(path.remove(0)).getKey(),2);
    }
    @Test
    public void copyTest()
    {
        graph g_copy = MYGA.copy();
        myDG.removeEdge(1,3);
        assertNotEquals(null,g_copy.getEdge(1, 3));
        assertEquals(g_copy.getEdge(1,6).getWeight() , 1 , 0.0001);
    }

}
