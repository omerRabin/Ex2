package api;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DWGraph_Algo implements dw_graph_algorithms{
    private directed_weighted_graph ga;//graph algorithm
    @Override
    public void init(directed_weighted_graph g) {
        this.ga=g;
    }
    @Override
    public directed_weighted_graph getGraph() {
        return this.ga;
    }

    @Override
    public directed_weighted_graph copy() {
        directed_weighted_graph copy = new DWGraph_DS(this.ga);//create the copy graph via copy constructor
        return copy;
    }
    private void initializeInfo()
    {
        Iterator<node_data> it=this.ga.getV().iterator();
        while (it.hasNext())
        {
            it.next().setInfo(null);
        }
    }
    private boolean isConnectedHelp(int src)
    {
        initializeInfo();//initialize all the info fields
        DWGraph_DS copy = (DWGraph_DS) (copy());//create a copy graph that the algorithm will on it
        LinkedList<node_data> qValues = new LinkedList<>();//create linked list that will storage all nodes that we didn't visit yet
        node_data first = copy.getNode(src);//get the node
        int countVisited=0;//count the number of nodes we visited
        qValues.add(first);
        while (qValues.size() != 0) {
            node_data current = qValues.removeFirst();
            if (current.getInfo() != null) continue;//if we visit we can skip to the next loop because we have already marked
            countVisited++;
            current.setInfo("visited");//remark the info
            NodeData curr=(NodeData) copy.getNode(current.getKey());
            Collection<node_data> listNeighbors=curr.getNi().values();//create a collection for the neighbors list
            LinkedList<node_data> Neighbors = new LinkedList<>(listNeighbors);//create the neighbors list
            if (Neighbors == null) continue;
            for (node_data n : Neighbors) {
                if (n.getInfo() == null) {//if there is a node we didn't visited yet, we will insert it to the linkedList
                    qValues.add(n);
                }
            }
        }
        if(countVisited==this.ga.getV().size()) return true;//if we visited all nodes
        return false;
    }
    public void invertingHelp(NodeData n){
        Iterator<node_data> it=n.getNi().values().iterator();
        while (it.hasNext()){
            NodeData curr=(NodeData)it.next();
            if(!curr.getNi().containsKey(n.getKey())) {//Checks whether a reversal is necessary
                n.getNi().remove(curr.getKey(),curr);
                curr.getNi().put(n.getKey(),n);
            }
        }
    }
    private void invertingGraphDirections(directed_weighted_graph g){
        Iterator<node_data> it=this.ga.getV().iterator();
        while(it.hasNext()){
            NodeData curr=(NodeData) it.next();
            invertingHelp(curr);
        }
    }
    @Override
    public boolean isConnected() {
        NodeData src=(NodeData) this.ga.getV().iterator().next();
        int srcKey=src.getKey();
        if(!isConnectedHelp(srcKey)) return false;
        invertingGraphDirections(this.ga);
        if(!isConnectedHelp(srcKey)){
            invertingGraphDirections(this.ga);
            return false;
        }
        return true;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
