package Tests;

import dataStructure.EdgeNode;
import dataStructure.edge_data;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EdgeNodeTest {

    @Test
    public void srcTest(){
        for (int i = 0; i < 10; i++) {
            edge_data eg = new EdgeNode(i , i+1 , i*i);
            assertEquals(i , eg.getSrc());
        }

    }
    @Test
    public void dstTest(){
        for (int i = 0; i < 10; i++) {
            edge_data eg = new EdgeNode(i , i+1 , i*i);
            assertEquals(i+1 , eg.getDest());
        }

    }
    @Test
    public void weightTest(){
        for (int i = 0; i < 10; i++) {
            edge_data eg = new EdgeNode(i , i+1 , i*i);
            assertEquals(i*i , eg.getWeight() , 0.00001);
        }
    }

}
