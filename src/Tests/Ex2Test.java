package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import algorithms.*;
import dataStructure.*;
import utils.*;
import gui.*;

import javax.swing.*;


/**
 * EX2 Structure test:
 * 1. make sure your code compile with this dummy test (DO NOT Change a thing in this test).
 * 2. the only change require is to run your GUI window (see line 64).
 * 3. EX2 auto-test will be based on such file structure.
 * 4. Do include this test-case in you submitted repository, in folder Tests (under src).
 * 5. Do note that all the required packages are imported - do NOT use other
 *
 * @author boaz.benmoshe
 *
 */
class Ex2Test {
    private static graph _graph;
    private static graph_algorithms _alg;
    public static final double EPS = 0.001;
    private static Point3D min = new Point3D(0,0,0);
    private static Point3D max = new Point3D(100,100,0);
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
        _graph = tinyGraph();
    }

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void testConnectivity() {
        _alg = new Graph_Algo(_graph);
        boolean con = _alg.isConnected();
        if(!con) {
            fail("shoulbe be connected");
        }
    }
    @Test
    void testgraph() {
        boolean ans = drawGraph(_graph);
        assertTrue(ans);
    }

    private static graph tinyGraph() {
        graph ans = new DGraph();
        return ans;
    }

    boolean drawGraph(graph g) {
        node_data v1 = new DataNode(1, new Point3D(25,90,0));
        node_data v2 = new DataNode(2, new Point3D(340,300,0));
        node_data v3 = new DataNode(3,  new Point3D(24,300,0));
        node_data v4 = new DataNode(4, new Point3D(68,100,0));
        node_data v5 = new DataNode(5, new Point3D(500,600,0));

        _graph.addNode(v1);
        _graph.addNode(v2);
        _graph.addNode(v3);
        _graph.addNode(v4);
        _graph.addNode(v5);

        _graph.connect(v1.getKey(), v3.getKey(), 6);
        _graph.connect(v1.getKey(), v4.getKey(), 10);
        _graph.connect(v3.getKey(), v4.getKey(), 9);
        _graph.connect(v4.getKey(), v2.getKey(), 3);
        _graph.connect(v3.getKey(), v5.getKey(), 1);

        _graph.connect(v2.getKey(), v3.getKey(), 5);
        _graph.connect(v2.getKey(), v5.getKey(), 1);
        Graph_GUI gui = new Graph_GUI(_graph);
        gui.setVisible(true);

        return true;

    }

}