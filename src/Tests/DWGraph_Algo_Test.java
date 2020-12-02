package Tests;
import api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DWGraph_Algo_Test {
    private DWGraph_DS graph=new DWGraph_DS();//graph DS for be init into graphA
    private DWGraph_Algo graphA=new DWGraph_Algo();//Graph on which the algorithms will be run
    @BeforeEach
    public void init()
    {
        for(int i=0;i<100;i++) {
            NodeData n=new NodeData(i);
            graph.addNode(n);
        }
        while(graph.edgeSize()<=1000)
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
    public void initTest()
    {
        graphA.init(graph);
        assertEquals(true,graphA.getGraph().equals(graph));//equals of java
    }
    @Test
    public void getGraphTest()
    {
        graphA.init(graph);
        DWGraph_DS g1=(DWGraph_DS) graphA.getGraph();
        assertEquals(true,graph.equals(g1));//equals of java
    }
    @Test
    public void CopyTest() {
        graphA.init(graph);
        DWGraph_DS a;
        a=(DWGraph_DS) graphA.copy();
        boolean resultCopy=graphA.getGraph().equals(a);//my equals method check if two graph is deeply equal
        assertEquals(true,resultCopy);
    }
    @Test
    public void isConnectedTest(){

        DWGraph_DS g1=new DWGraph_DS();
        NodeData n1=new NodeData(1);
        NodeData n2=new NodeData(2);
        NodeData n3=new NodeData(3);
        g1.addNode(n1);
        g1.addNode(n2);
        g1.connect(1,2,3.33);
        g1.connect(2,1,3.33);
        graphA.init(g1);
        assertEquals(true,graphA.isConnected());//check on connected graph
        g1.removeEdge(1,2);
        graphA.isConnected();
        assertEquals(false,graphA.isConnected());//disconnected graph because we deleted an essential edge

        DWGraph_DS g2=new DWGraph_DS();
        graphA.init(g2);
        assertEquals(true,graphA.isConnected());//empty graph is connected

        DWGraph_DS g3=new DWGraph_DS();
        g3.addNode(n3);
        graphA.init(g3);
        assertEquals(true,graphA.isConnected());//graph with only one node is connected

        //more complicated case
        DWGraph_DS g4=new DWGraph_DS();
        NodeData n4=new NodeData(4);
        NodeData n5=new NodeData(5);
        NodeData n6=new NodeData(6);

        g4.addNode(n4);
        g4.addNode(n5);
        g4.addNode(n6);
        g4.connect(4,5,4);
        g4.connect(5,6,6);
        g4.connect(6,4,3);
        DWGraph_Algo g4A=new DWGraph_Algo();
        g4A.init(g4);
        assertEquals(true,g4A.isConnected());
        //-------------------------------------------
        DWGraph_DS g5=new DWGraph_DS();
        NodeData n7=new NodeData(7);
        NodeData n8=new NodeData(8);
        NodeData n9=new NodeData(9);
        g5.addNode(n7);
        g5.addNode(n8);
        g5.addNode(n9);
        g5.connect(7,8,3);
        g5.connect(9,7,4);
        g5.connect(9,8,6);
        DWGraph_Algo g5A=new DWGraph_Algo();
        g5A.init(g5);
        assertEquals(false,g5A.isConnected());
        //----------------------------------------------
        DWGraph_DS g6=new DWGraph_DS();
        NodeData n10=new NodeData(10);
        NodeData n11=new NodeData(11);
        NodeData n12=new NodeData(12);
        g5.addNode(n10);
        g5.addNode(n11);
        g5.addNode(n12);
        g5.connect(10,11,3);
        g5.connect(10,12,4);
        g5.connect(11,10,6);
        g5.connect(11,12,3);
        g5.connect(12,10,4);
        g5.connect(12,11,6);
        DWGraph_Algo g6A=new DWGraph_Algo();
        g6A.init(g6);
        assertEquals(true,g6A.isConnected());
    }
    @Test
    public void DijkstraHelpTest() {
        DWGraph_DS g = new DWGraph_DS();
        for (int i = 0; i < 7; i++) {
            NodeData n=new NodeData(i);
            g.addNode(n);
        }
        g.connect(0, 1, 4);
        g.connect(0, 4, 2);
        g.connect(0, 5, 8);
        g.connect(1, 2, 7);
        g.connect(1, 4, 1);
        g.connect(3, 4, 4);
        g.connect(6, 3, 9);
        g.connect(5, 3, 3);
        graphA.init(g);
        assertEquals(true, graphA.DijkstraHelp(0, 3));
        assertEquals(true,11==((NodeData)graphA.getGraph().getNode(3)).getTagAlgo());
        assertEquals(true, graphA.DijkstraHelp(0, 4));
        assertEquals(true,2==((NodeData)graphA.getGraph().getNode(4)).getTagAlgo());


    }
    @Test
    public void shortestPathTest() {
        DWGraph_DS g=new DWGraph_DS();
        for(int i=0;i<7;i++){
            NodeData n=new NodeData(i);
            g.addNode(n);
        }
        g.connect(0,1,4);
        g.connect(0,4,2);
        g.connect(0,5,3);
        g.connect(1,2,7);
        g.connect(1,4,1);
        g.connect(3,4,4);
        g.connect(6,3,9);
        g.connect(5,3,3);
        g.connect(0,3,7);
        graphA.init(g);
        assertEquals(graphA.shortestPath(0,4).get(1).getKey(),4);
        assertEquals(graphA.shortestPath(0,3).get(2).getKey(),3);

    }
    @Test
    public void shortestPathDistTest(){
        DWGraph_DS g=new DWGraph_DS();
        for(int i=0;i<7;i++){
            NodeData n=new NodeData(i);
            g.addNode(n);
        }
        g.connect(0,1,4);
        g.connect(0,4,2);
        g.connect(0,5,8);
        g.connect(1,2,7);
        g.connect(1,4,1);
        g.connect(3,4,4);
        g.connect(6,3,9);
        g.connect(5,3,3);
        graphA.init(g);
        assertEquals(2,graphA.shortestPathDist(0,4));
    }

}
