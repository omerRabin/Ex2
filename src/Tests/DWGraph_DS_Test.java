package Tests;
import api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class DWGraph_DS_Test {
    private DWGraph_DS graph=new DWGraph_DS();
    @BeforeEach
    public void init() {
        for(int i=0;i<100;i++) {//this for creates 100 nodes
            NodeData n=new NodeData(i);
            graph.addNode(n);
        }
        while(graph.edgeSize()<=1000)//creates 1000 edges
        {
            Random r = new Random();
            int low = 0;
            int high = 99;
            int a = r.nextInt(high-low) + low;
            int b = r.nextInt(high-low) + low;
            graph.connect(a,b,Math.random()*10);
        }
    }
    @Test
    public void getNodeTest()
    {
        int key=graph.getNode(0).getKey();
        assertEquals(0,key);
        assertEquals(graph.getNode(-2),null);//there is no key -2 in the graph right to now
    }
    @Test
    public void getEdgeTest()
    {
        graph.connect(0,1,(double) 5);
        assertEquals( 5,graph.getEdge(0,1).getWeight());
        graph.removeEdge(0,1);
        graph.getEdge(0,1);
        assertEquals(null,graph.getEdge(0,1));
    }
    @Test
    public void addNodeTest()
    {
        boolean flag=false;
        int key=0;
        while(!flag){
            if(graph.getNode(key)==null) {
                NodeData n=new NodeData(key);
                graph.addNode(n);
                flag=true;
            }
            key++;
        }
        assertEquals(true,graph.getV().contains(graph.getNode(key-1)));//check if the key is exist in the graph
    }
    @Test
    public void connectTest()
    {
        graph.connect(2,98,3.14);
        assertEquals(3.14,graph.getEdge(2,98).getWeight());
        assertEquals(null,graph.getEdge(98,2));
        graph.connect(2,98,7);
        assertEquals(7,graph.getEdge(2,98).getWeight());
    }
    @Test
    public void getVTest() {
        Collection copy=graph.getV();
        assertEquals(graph.nodeSize(),copy.size());
    }
    @Test
    public void getEText(){
        int key=0;
        while(true){
            if(graph.getNode(key)==null) {
                NodeData n=new NodeData(key);
                graph.addNode(n);
                break;
            }
            key++;
        }
        NodeData n1=new NodeData(1);
        NodeData checkN=((NodeData)this.graph.getNode(key));
        graph.connect(checkN.getKey(),n1.getKey(),4);
        edge_data e=graph.getEdge(checkN.getKey(),n1.getKey());
        assertEquals(true,graph.getE(checkN.getKey()).contains(e));
    }
    @Test
    public void removeNodeTest()
    {
        NodeData n=new NodeData(1);
        graph.addNode(n);
        graph.connect(1,55,4.55);
        graph.removeNode(1);
        assertEquals(false,graph.getV().contains(graph.getNode(1)));
        assertEquals(null,graph.getEdge(1,55));
    }
    @Test
    public void removeEdge()
    {
        graph.connect(1,2,9.2);
        graph.removeEdge(1,2);
        assertEquals(null,graph.getEdge(1,2));
    }
    @Test
    public void equalsTest(){
        DWGraph_DS g1=new DWGraph_DS(graph);
        boolean eq=graph.equals(g1);
        assertEquals(true,eq);
    }
}
