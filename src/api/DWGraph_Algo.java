package api;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import javax.management.NotificationFilter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms{
    private directed_weighted_graph ga;//graph algorithm

    public DWGraph_Algo(){
    }
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
    /**
     *the method initialize all the node_tag to infinity
     */
    private void initializeTag()
    {
        Iterator<node_data> it=this.ga.getV().iterator();
        while(it.hasNext())
        {
            ((NodeData) it.next()).setTagAlgo(Double.POSITIVE_INFINITY);
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

    /**
     * this method return the number of the edges in the graph
     * @return
     */
    private int countEdges(){
        int counter=0;
        Iterator<node_data> it=this.ga.getV().iterator();
        while(it.hasNext()) {
            counter+=this.ga.getE(it.next().getKey()).size();
        }
        return counter;
    }

    /**
     * this method initialize all the tagNodes
     */
    private void initializeTagNode(){
        Iterator<node_data> it=this.ga.getV().iterator();
        while(it.hasNext()){
            Iterator<edge_data> itE=this.ga.getE(it.next().getKey()).iterator();
            while(itE.hasNext()){
                itE.next().setTag(0);
            }
        }
    }
    public int counter=0;

    /**
     * this method inverting
     * @param n
     */
    private void invertingHelp(NodeData n){
        Iterator<node_data> it=n.getNi().values().iterator();
        while (it.hasNext()){
            if(countEdges()==counter) break;//check if we finished
            NodeData curr=(NodeData)it.next();
            if(this.ga.getEdge(curr.getKey(),n.getKey())==null) {//if we did not yet flip the edge from curr to n
                if (!curr.getNi().containsKey(n.getKey())) {//Checks whether a reversal is necessary because in the other case
                    //there is edge also in the opposite direction and then it's doesn't matter
                    if (this.ga.getEdge(n.getKey(), curr.getKey()).getTag() == 0) {
                        n.getNi().remove(curr.getKey(), curr);//exchange the directions by remove from ni
                        curr.getNi().put(n.getKey(), n);//exchange the directions by insert to ni
                        double e = this.ga.getEdge(n.getKey(), curr.getKey()).getWeight();
                        this.ga.getEdge(n.getKey(), curr.getKey()).setTag(1);//set tag to 1 if we flipped this edge
                        counter++;
                    }
                }
            }
            else{
                if (!curr.getNi().containsKey(n.getKey())&&this.ga.getEdge(curr.getKey(),n.getKey()).getTag()!=1) {//Checks whether a reversal is necessary because in the other case
                    //there is edge also in the opposite direction and then it's doesn't matter
                    if (this.ga.getEdge(n.getKey(), curr.getKey()).getTag() == 0) {
                        n.getNi().remove(curr.getKey(), curr);//exchange the directions by remove from ni
                        curr.getNi().put(n.getKey(), n);//exchange the directions by insert to ni
                        double e = this.ga.getEdge(n.getKey(), curr.getKey()).getWeight();
                        this.ga.removeEdge(n.getKey(), curr.getKey());
                        this.ga.connect(curr.getKey(), n.getKey(), e);
                        this.ga.getEdge(n.getKey(), curr.getKey()).setTag(1);//set to 1 if we flipped this edge
                        counter++;
                    }
                }
            }
        }
    }

    private void invertingGraphDirections(directed_weighted_graph g){
        initializeTagNode();
        Iterator<node_data> it=this.ga.getV().iterator();
        while(it.hasNext()){//exchange all directions in graph
            NodeData curr=(NodeData)it.next();
            invertingHelp(curr);
        }
    }

    @Override
    public boolean isConnected() {
        if(this.ga.getV().size()==0) return true;//empty graph is connected
        NodeData src=(NodeData) this.ga.getV().iterator().next();//taking a random node
        int srcKey=src.getKey();
        if(!isConnectedHelp(srcKey)) return false;//if not connected with the original directions it means not connected
        invertingGraphDirections(this.ga);//exchange the directions
        if(!isConnectedHelp(srcKey)){//should be connected after exchange the directions
            invertingGraphDirections(this.ga);
            return false;
        }
        return true;
    }

@Override
public double shortestPathDist(int src, int dest) {
    if(!isConnectedHelp(src, dest)) return -1;
    if(shortestPath(src,dest)==null) return -1;
    return ((NodeData)shortestPath(src,dest).get(shortestPath(src,dest).size()-1)).getTagAlgo();
    //return the tag of the destination node in the list which contain the shortest path
}
    private boolean isConnectedHelp(int src,int dest)
    {
        initializeInfo();//initialize all the info fields
        DWGraph_DS copy = (DWGraph_DS) (copy());//create a copy graph that the algorithm will on it
        LinkedList<node_data> qValues = new LinkedList<>();//create linked list that will storage all nodes that we didn't visit yet
        node_data first = copy.getNode(src);//get the node
        qValues.add(first);
        while (qValues.size() != 0) {
            node_data current = qValues.removeFirst();
            if(current.getKey()==dest) return true;//src is connected to dest
            if (current.getInfo() != null) continue;//if we visit we can skip to the next loop because we have already marked
            current.setInfo("visited");//remark the info
            Collection<node_data> listNeighbors = ((NodeData)current).getNi().values();//create a collection for the neighbors list
            LinkedList<node_data> Neighbors = new LinkedList<>(listNeighbors);//create the neighbors list
            if (Neighbors == null) continue;
            for (node_data n : Neighbors) {
                if (n.getInfo() == null) {//if there is a node we didn't visited yet, we will insert it to the linkedList
                    qValues.add(n);
                }
            }
        }
        return false;
    }
    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if(ga.getNode(src)==null||ga.getNode(dest)==null) return null;//if one of the node not exist
        if(ga.getV().size()==0) return null ;//if the graph has no have nodes
        if(src==dest) {//if its shortest path from node to itself
            ArrayList<node_data> l=new ArrayList<>();
            l.add(ga.getNode(src));
            return l;
        }
        Iterator<node_data> graph=ga.getV().iterator();//iterator on the graph
        HashMap<Integer,NodeTag> tagMap=new HashMap<>();//hash-Map of nodeTags-for the weights in the Dijkstra algorithm
        while (graph.hasNext()){
            node_data n = graph.next();//current node in the graph
            NodeTag nodeTag=new NodeTag(n.getKey(),Double.MAX_VALUE,ga.getE(n.getKey()));//create a node-tag object
            tagMap.put(nodeTag.getKey(),nodeTag);
        }
        HashMap<NodeTag,NodeTag> parent=new HashMap<>();//create hashMap of Parents from
        NodeTag destN = tagMap.get(dest);//take the nodeTag of the destination node
        PriorityQueue<NodeTag> pQ=new PriorityQueue<>(ga.getV().size(),new NodeCompareForQueue());//create priority queue with Priority by nodeCompare
        HashSet<NodeTag> vis= new HashSet<>();//the list of the nodeTag from dest to src
        NodeTag srcN = tagMap.get(src);
        srcN.setTagWeight(0);//the weight from src to itself is 0
        ((NodeData)getGraph().getNode(src)).setTagAlgo(0);//update the tag algo
        pQ.add(srcN);
        while (!pQ.isEmpty()){
            NodeTag cur= pQ.poll();
            if(!vis.contains(cur)){
                vis.add(cur);
                if(cur==destN) break;//if we finished
                for(edge_data e : cur.getE()){//run on the edges
                    if(!vis.contains(tagMap.get(e.getDest()))){
                        if(cur.getTagWeight()+e.getWeight()<tagMap.get(e.getDest()).getTagWeight()){//check if there is new path to the node that more cheap
                            tagMap.get(e.getDest()).setTagWeight(cur.getTagWeight()+e.getWeight());
                            //updte the new tag in tagAlgo for shortestPathDist method
                            ((NodeData)getGraph().getNode(tagMap.get(e.getDest()).getKey())).setTagAlgo(cur.getTagWeight()+e.getWeight());
                            if(e.getDest()==dest){parent.remove(tagMap.get(e.getDest()));}
                            parent.put(tagMap.get(e.getDest()),cur);
                            pQ.add(tagMap.get(e.getDest()));
                        }
                    }
                }
            }
        }
        if(!parent.containsKey(destN)) return null;//if the path from src to dest isn't exist
        ArrayList<node_data> s= new ArrayList<>();
        s.add(ga.getNode(destN.getKey()));
        node_data n =ga.getNode(destN.getKey());
        NodeTag nT= tagMap.get(dest);
        //while for go back from dest to src and get the shortestPath
        while(n.getKey()!=src){
            nT = parent.get(nT);
            n =ga.getNode(nT.getKey());
            s.add(n);
        }
        Collections.reverse(s);//reverse the path to the original frame
        return s;
    }

    /**
     * class represent a comparator for NodeTag class by tagWeights
     */
    private class NodeCompareForQueue implements Comparator<NodeTag> {
        @Override
        public int compare(NodeTag o1, NodeTag o2) {
            if(o1.getTagWeight()<o2.getTagWeight()) return  -1;
            if(o1.getTagWeight()>o2.getTagWeight()) return   1;
            else{ return 0; }
        }
    }

    /**
     * this method return an array list of the Edges in the graph
     * @return
     */
    private ArrayList<edge_data> getEdges(){
        ArrayList<edge_data> arr=new ArrayList<edge_data>();
        int index=0;
        Iterator<node_data> iter=getGraph().getV().iterator();
        while(iter.hasNext()){
            Iterator<edge_data> itEd=getGraph().getE(iter.next().getKey()).iterator();//take the Edges of current node
            while(itEd.hasNext()){
                arr.add(itEd.next());//insert the edge to the arrayList
                index++;
            }
        }
        return arr;
    }

    /**
     * this method return array list of the nodes in the graph
     * @return
     */
    private ArrayList<node_data> getNodes(){
        ArrayList<node_data> arr=new ArrayList<node_data>();
        int index=0;
        Iterator<node_data> iter=getGraph().getV().iterator();
        while(iter.hasNext()){
            arr.add(iter.next());
        }
        return arr;
    }
    @Override
    public boolean save(String file) {
        JsonObject json_object = new JsonObject();//create json object
        JsonArray edges=new JsonArray();//create json array
        ArrayList<edge_data> ed=getEdges();//create array list of all the edges by call to getEdges method
        for(int i=0;i<ed.size();i++){
            JsonObject curr=new JsonObject();//create jasonObject each loop for inserting the information about each edge
            curr.addProperty("src",ed.get(i).getSrc());
            curr.addProperty("w",ed.get(i).getWeight());
            curr.addProperty("dest",ed.get(i).getDest());;
            edges.add(curr);//add the jason object to the jason array of the edges
        }
        JsonArray nodes=new JsonArray();
        ArrayList<node_data> no=getNodes();
        for(int j=0;j<no.size();j++){
            JsonObject curr=new JsonObject();
            curr.addProperty("pos",no.get(j).getLocation().x()+","+no.get(j).getLocation().y()+","+no.get(j).getLocation().z());//take the position and convert to string
            curr.addProperty("id",no.get(j).getKey());
            nodes.add(curr);
        }
        json_object.add("Edges",edges);//insert the edges array to the jason object
        json_object.add("Nodes",nodes);//insert the nodes array to the jason object
        try {
            PrintWriter pw=new PrintWriter(new File(file));
            pw.write(String.valueOf(json_object));//write the jason to the file
            pw.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    @Override
    public boolean load(String file) {

        try {
            GsonBuilder builder=new GsonBuilder();//create the Gson builder
            builder.registerTypeAdapter(directed_weighted_graph.class,new GraphJsonDeserializer());//using the method in GraphJsonDeserializer class to make the json to graph
            Gson gson=builder.create();//create the json

            FileReader reader=new FileReader(file);
            directed_weighted_graph graph =gson.fromJson(reader,directed_weighted_graph.class);//convert the gson to graph from json
            this.ga=graph;//make this.ga to be the graph from the json file
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
