package Tests;

import com.sun.corba.se.impl.orbutil.graph.NodeData;
import dataStructure.*;
import org.junit.Test;
import utils.Point3D;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;


public class DGraphTest {

    @Test
   public void getNode() {
     DGraph d = new DGraph();
     int j = 1;
     for(int i = 1; i < 10 ; i++ ) { //the key can't be 0
          Point3D p = new Point3D(j , j , 0);
          DataNode n = new DataNode(i , i*i, p , "", i);
          d.addNode(n);
          assertEquals(d.getNode(i), n);
          j++;

     }
    }


    @Test
    public void getEdge() {
     DGraph d = new DGraph();
     int j = 1;
     for(int i = 1; i < 10 ; i++ ) { //the key can't be 0
      Point3D p = new Point3D(j , j , 0);
      DataNode n = new DataNode(i , i*i, p );
      d.addNode(n);
      j++;

     }
     for (int i = 1; i <9 ; i++) {
      EdgeNode e = new EdgeNode(i, i+1 , i*i);
      d.connect(i , i+1 , i*i);
      assertEquals(d.getEdge(i , i+1 ).getWeight() , e.getWeight() , 0.000001);
     }

    }

    @Test
    public void addNode() {
     DGraph dg = new DGraph();
     Point3D p1 = new Point3D(1,2,0);
     Point3D p2 = new Point3D(35,7,0);
     Point3D p3 = new Point3D(3,8,0);
     Point3D p4 = new Point3D(6,1,0);
     Point3D p5 = new Point3D(-4,-3,0);
     node_data nd1 = new DataNode(1 , 7 , p1 ,"firstNode" , 0);
     node_data nd2 = new DataNode(2 , 3 , p2 , "second node" , 0);
     node_data nd3 = new DataNode(3 , 5 , p3);
     node_data nd4 = new DataNode(4 , 13 , p4);
     node_data nd5 = new DataNode(5, 6 , p5);

     dg.addNode(nd1);
     dg.addNode(nd2);
     dg.addNode(nd3);
     dg.addNode(nd4);
     dg.addNode(nd5);
    }

    @Test
    public void connect() {
     DGraph dg = new DGraph();
     Point3D p1 = new Point3D(1,2,0);
     Point3D p2 = new Point3D(35,7,0);
     Point3D p3 = new Point3D(3,8,0);
     node_data nd1 = new DataNode(1 , 7 , p1 ,"firstNode" , 0);
     node_data nd2 = new DataNode(2 , 3 , p2 , "second node" , 0);
     node_data nd3 = new DataNode(3 , 5 , p3 , "third node" , 0);

     dg.addNode(nd1);
     dg.addNode(nd2);
     dg.addNode(nd3);
     dg.connect(1,2,9999);
     dg.connect(1,3,8888);
     dg.connect(2,1,7777);
     dg.connect(3,1,6666);
     dg.connect(2,3,5555);
     dg.connect(3,2,4444);


     assertEquals(2 , dg.getE(3).size());
     assertEquals(2 , dg.getE(1).size());
     assertEquals(2 , dg.getE(2).size());

    }

    @Test
    public void getV() {
     DGraph dg = new DGraph();
     Collection<DataNode> col = new LinkedList<>();
     for (int i = 0; i < 10 ; i++) {
      DataNode d =  new DataNode(i);
      col.add(d);
      dg.addNode(d);
     }
     if (!dg.getV().containsAll(col) )
      fail();
    }

    @Test
    public void getE() {
     DGraph dg = new DGraph();

     for (int i = 0; i < 10 ; i++) {
      DataNode d =  new DataNode(i);
      dg.addNode(d);

     }
     int size = 1;
     Collection<EdgeNode> col = new LinkedList<>();
     for (int i = 0 ; i < 9 ; i++) {
      dg.connect(i , i+1 , i);
      dg.connect(i+1 , i , i);
      EdgeNode e1 = new EdgeNode(i , i+1 , i);
      EdgeNode e2 = new EdgeNode(i+1 , i , i);
      col.add(e1);
      col.add(e2);
      size += dg.getE(i).size();
     }

     assertEquals(size , col.size());

    }

    @Test
    public void removeNode() {
     DGraph dg = new DGraph();
     Point3D p1 = new Point3D(1,2,0);
     Point3D p2 = new Point3D(35,7,0);
     Point3D p3 = new Point3D(3,8,0);
     Point3D p4 = new Point3D(6,1,0);
     Point3D p5 = new Point3D(-4,-3,0);
     node_data nd1 = new DataNode(1 , 7 , p1 ,"firstNode" , 0);
     node_data nd2 = new DataNode(2 , 3 , p2 , "second node" , 0);
     node_data nd3 = new DataNode(3 , 5 , p3);
     node_data nd4 = new DataNode(4 , 13 , p4);
     node_data nd5 = new DataNode(5, 6 , p5);

     dg.addNode(nd1);
     dg.addNode(nd2);
     dg.addNode(nd3);
     dg.addNode(nd4);
     dg.addNode(nd5);

     for (int i = 1; i < 5; i++) {
      dg.connect(i , i+1 , i);
     }

     dg.removeNode(3);
     assertEquals(dg.getNode(3) , null);
     dg.removeNode(1);
     assertEquals(dg.getNode(1) , null);
     dg.removeNode(2);
     dg.removeNode(4);
     dg.removeNode(5);
     assertEquals(dg.nodeSize() , 0);
    }

    @Test
    public void removeEdge() {
     DGraph d = new DGraph();
     int j=1;
     for (int i =1 ; i<10 ;i++){
      Point3D p = new Point3D(j, j, j);
      DataNode n = new DataNode(i , p);
      d.addNode(n);
     }


     for(int i =1 ; i <d.nodeSize() ;i++) {
      if ( d.nodeSize() > 1+i) {
       d.connect(i, i+1, i);
      }
     }
     assertEquals(d.edgeSize() , 7);
     for(int i =1 ; i <d.nodeSize()-1 ;i++) {
      if (d.nodeSize() > 1 + i) {
       d.removeEdge(i, i + 1);
      }
     }
     assertEquals(d.getEdge( 1, 2) , null);
     assertEquals(d.getEdge( 2, 3) , null);
     assertEquals(d.getEdge( 3, 4) , null);
     assertEquals(d.getEdge( 4, 5) , null);
     assertEquals(d.getEdge( 5, 6) , null);
     assertEquals(d.getEdge( 6, 7) , null);
     assertEquals(d.getEdge( 7, 8) , null);
     assertEquals(d.getEdge( 8, 9) , null);
     assertEquals(d.getEdge( 9, 10) , null);
    }

    @Test
    public void nodeSize() {
     DGraph d = new DGraph();
     int j=1;
     for (int i =1 ; i<10 ;i++){
      Point3D p = new Point3D(j, j, j);
      DataNode n = new DataNode(i , p );
      d.addNode(n);
     }
     if (d.nodeSize() != 9) {
      fail();
     }
    }

    @Test
    public void edgeSize() {
     DGraph d = new DGraph();
     int j=1;
     for (int i =1 ; i<10 ;i++){
      Point3D p = new Point3D(j, j, j);
      DataNode n = new DataNode(i , p);
      d.addNode(n);
     }
     for(int i =1 ; i <d.nodeSize() ;i++) {
      d.connect(i, i+1, i);
     }
     if(d.edgeSize()!=8) {
      fail("edge size is not correct");
     }
    }

    @Test
    public void getMC() {
     DGraph d = new DGraph();
     int j=1;
     for (int i =1 ; i<10 ;i++){
      Point3D p = new Point3D(j, j, j);
      DataNode n = new DataNode(i , p);
      d.addNode(n);
     }
     for(int i =1 ; i <d.nodeSize() ;i++) {
      d.connect(i, i+1, i);
     }

     assertEquals(d.getMC() , 17);
    }
}