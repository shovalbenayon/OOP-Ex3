package Tests;

import dataStructure.DataNode;
import dataStructure.EdgeNode;
import dataStructure.edge_data;
import dataStructure.node_data;
import org.junit.Test;
import utils.Point3D;

import static org.junit.Assert.assertEquals;

public class DataNodeTest {

    @Test
    public void keyTest(){
        for (int i = 0; i < 10; i++) {
            Point3D p = new Point3D(i,i);
            node_data nd = new DataNode(i , i+1 , p);
            assertEquals(i , nd.getKey());
        }

    }
    @Test
    public void LocationTest(){
        for (int i = 0; i < 10; i++) {
            Point3D p = new Point3D(i,i);
            node_data nd = new DataNode(i , i+1 , p);
            assertEquals(p , nd.getLocation());
        }

    }
    @Test
    public void weightTest(){
        for (int i = 0; i < 10; i++) {
            Point3D p = new Point3D(i,i);
            node_data nd = new DataNode(i , i+1 , p);
            assertEquals(i+1 , nd.getWeight(), 0.0001);
        }
    }

    @Test
    public void tagTest(){
        for (int i = 0; i < 10; i++) {
            Point3D p = new Point3D(i,i);
            node_data nd = new DataNode(i , i+1 , p , "", i);
            assertEquals(i , nd.getTag());
        }
    }

    @Test
    public void InfoTest(){
        for (int i = 0; i < 10; i++) {
            String temp ="";
            temp += i;
            Point3D p = new Point3D(i,i);
            node_data nd = new DataNode(i , i+1 , p , temp, i);
            assertEquals(temp , nd.getInfo());
        }
    }

}
