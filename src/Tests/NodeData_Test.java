package Tests;

import api.DWGraph_DS;
import api.NodeData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class NodeData_Test {
    @Test
    public void getEdgeTest(){
        DWGraph_DS g=new DWGraph_DS();
        NodeData n0=new NodeData(0);
        NodeData n1=new NodeData(1);
        g.addNode(n0);
        g.addNode(n1);
        g.connect(n0.getKey(),n1.getKey(),9.12);
        assertEquals(9.12,n0.getEdge(1).getWeight());
        assertEquals(null,n1.getEdge(0));
    }
}
