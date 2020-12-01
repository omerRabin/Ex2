package api;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class DWGraph_DS implements directed_weighted_graph{
    private int MC;//mode counter-count number of changes on graph
    private int edges;//number of edges in graph
    private int nodeSize;//number of nodes in graph
    private HashMap<Integer, node_data> graph;//Hash-Map structure represents the graph
    public DWGraph_DS(){
        this.edges=0;
        this.nodeSize=0;
        this.MC=0;
        this.graph=new HashMap<Integer, node_data>();
    }
    public DWGraph_DS(int edges,int nodeSize,HashMap<Integer, node_data> graph){
        this.MC=0;
        this.edges=edges;
        this.nodeSize=nodeSize;
        this.graph=graph;
    }
    public DWGraph_DS(directed_weighted_graph g)
    {
        this.MC=0;
        this.edges=g.edgeSize();
        this.nodeSize=g.nodeSize();
        this.graph=new HashMap<Integer, node_data>();
        Iterator<node_data> it=g.getV().iterator();
        while(it.hasNext())//loop the nodes in graph g and copy the nodes to the new copy object with unique keys
        {
            node_data current=it.next();
            int currentKey=current.getKey();
            this.graph.put(currentKey,current);
        }
    }

    @Override
    public node_data getNode(int key) {
        if(this.graph.containsKey(key)) return this.graph.get(key);//get the node by get function of HashMap
        return null;
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        if(graph.containsKey(src)) {
            NodeData n = (NodeData) getNode(src);
            return n.getEdge(dest);
        }
        return null;
    }

    @Override
    public void addNode(node_data n) {
        int key=n.getKey();
        if(graph.containsKey(key)) return;
        this.graph.put(key,n);//using the put function of HashMap to insert the node in O(1)
        this.MC++;
        this.nodeSize++;
    }

    @Override
    public void connect(int src, int dest, double w) {
        if(src==dest)return;
        NodeData n1=(NodeData) getNode(src);
        NodeData n2=(NodeData) getNode(dest);
        EdgeData e=new EdgeData(src,dest,w,null,0);
                n1.getNi().put(dest, n2);//put des node in neighbors list of n1
                n1.getEdges().put(dest, e);//put edge between n1 to n2 in edges structures
                this.MC++;
                this.edges++;
    }

    @Override
    public Collection<node_data> getV() {
        return this.graph.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        NodeData n=(NodeData) getNode(node_id);
        return n.getEdges().values();
    }

    @Override
    public node_data removeNode(int key) {
        node_data removalNode=(getNode(key));//get the node by its key
        if(removalNode!=null) {
            Iterator<node_data> it = getV().iterator();
            while (it.hasNext()) {//loop on all graph
                NodeData current = (NodeData) it.next();
                if (current.getNi().containsKey(key)) {
                    current.getNi().remove(key, removalNode);//remove from ni
                    current.getEdges().remove(key, removalNode);//remove the edge
                    this.edges--;
                }
            }
            this.MC++;
            this.nodeSize--;
            this.graph.remove(key, removalNode);//delete the node from the graph for finish the process
        }
        return removalNode;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        NodeData n=(NodeData) getNode(src);
        if(n.getNi().containsKey(dest)){
            n.getNi().remove(dest,getNode(dest));//remove from ni
            edge_data e=n.getEdge(dest);
            n.getEdges().remove(dest,(EdgeData)e);//remove edge
            return e;
        }
        return null;
    }

    @Override
    public int nodeSize() {
        return this.nodeSize;
    }

    @Override
    public int edgeSize() {
        return this.edges;
    }

    @Override
    public int getMC() {
        return this.MC;
    }
    public boolean equals(Object ot){
        if(ot==null || !(ot instanceof DWGraph_DS)){return false;}
        Iterator<node_data> it= ((DWGraph_DS) ot).getV().iterator();
        if( ((DWGraph_DS) ot).edgeSize()!=this.edgeSize()||
                ((DWGraph_DS) ot).nodeSize()!=this.nodeSize) return false;//try to fail if one of the fields is not equal to ga fields
        while(it.hasNext()){//check if the graph structures are equal
            if(!containsNode(it.next())) return false;
        }
        return true;
    }
    private boolean containsNode(node_data n){
        Iterator<node_data> it=this.getV().iterator();
        while (it.hasNext()){
            if(it.next().getKey()==n.getKey()) return true;//if the keys are equal its enough
        }
        return false;
    }
}
