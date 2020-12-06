package api;

import com.google.gson.*;

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
    private int countEdges(){
        int counter=0;
        Iterator<node_data> it=this.ga.getV().iterator();
        while(it.hasNext()) {
            counter+=this.ga.getE(it.next().getKey()).size();
        }
        return counter;
    }
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
        if(shortestPath(src,dest)==null) return -1;
        return ((NodeData)shortestPath(src,dest).get(shortestPath(src,dest).size()-1)).getTagAlgo();
        //return the tag of the destination node in the list which contain the shortest path
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        int counter=0;//counter for index of listPath
        List<node_data> listPath=new ArrayList<node_data>();//The reverse list that is returned
        List<node_data> listResult=new ArrayList<node_data>();//the returned list
        if(!DijkstraHelp(src,dest)) return null;//if there is no path from src to dest
        if(src==dest) {
            listPath.add(this.ga.getNode(src));
            return listPath;
        }
        //the other case:
        node_data d=this.ga.getNode(dest);
        listPath.add(counter,d);//insert the dest in order to go from destination to source
        directed_weighted_graph gCopy=copy();
        Iterator<node_data> it=gCopy.getV().iterator();//run on the whole graph
        while(true){
            if(((NodeData)listPath.get(counter)).getP().keyParent==src){
                listPath.add(gCopy.getNode(((NodeData)listPath.get(counter)).getP().keyParent));//if we finish->exit loop
                break;
            }
            listPath.add(gCopy.getNode(((NodeData)listPath.get(counter)).getP().keyParent));//go to parent to restore the path
            counter++;
        }
        for(int i=listPath.size()-1;i>=0;i--){
            listResult.add(listPath.size()-i-1,listPath.get(i));//reverse the list
        }
        return listResult;
    }
    //class Entry for enable the priority queue to storage node info objects and sort by the minimum key
    private class Entry implements Comparable<Entry> {
        private node_data node;//info-node
        private double tag;//tag of node
        //constructor
        public Entry(node_data node, double tag) {
            this.tag = tag;
            this.node = node;
        }
        /**
         * compre to between tags for implementation Comparable
         * @param other
         * @return
         */
        @Override
        public int compareTo(Entry other) {
            if(this.tag>other.tag) return 1;
            if(this.tag==other.tag) return 0;
            return -1;
        }
    }
    /**
     * this method change each tag to the distance between src to the mode
     * @param src
     * @param dest
     * @return
     */
    public boolean DijkstraHelp(int src, int dest) {
        PriorityQueue<Entry>queue=new PriorityQueue();//queue storages the nodes that we will visit by the minimum tag
        //WGraph_DS copy = (WGraph_DS) (copy());//create a copy graph that the algorithm will on it
        initializeTag();
        initializeInfo();
        node_data first=this.ga.getNode(src);
        ((NodeData)first).setTagAlgo(0);//distance from itself=0
        queue.add(new Entry(first,((NodeData)first).getTagAlgo()));
        while(!queue.isEmpty()) {
            Entry pair=queue.poll();
            node_data current= pair.node;
            if(current.getKey()==dest) return true;
            if (current.getInfo() != null) continue;//if we visit we can skip to the next loop because we have already marked
            current.setInfo("visited");//remark the info
            Collection<node_data> listNeighbors=((NodeData)current).getNi().values();//create a collection for the neighbors list
            LinkedList<node_data> Neighbors = new LinkedList<>(listNeighbors);//create the neighbors list
            if (Neighbors == null) continue;
            for(node_data n:Neighbors) {
                //check if there is another path from src that shortest(with direction from current to n)
                if(((NodeData)n).getTagAlgo()>ga.getEdge(current.getKey(),n.getKey()).getWeight()+((NodeData)current).getTagAlgo())
                {
                    ((NodeData)n).setTagAlgo(((NodeData)current).getTagAlgo() + ga.getEdge(
                            current.getKey(),n.getKey()).getWeight());//compute the new tag
                    parentNode p=new parentNode(current.getKey());//create the parent
                    ((NodeData) n).setP(p);//save the parent
                }
                queue.add(new Entry(n,n.getTag()));//add to the queue
            }
        }
        return false;
    }
    private ArrayList<edge_data> getEdges(){
        ArrayList<edge_data> arr=new ArrayList<edge_data>();
        int index=0;
        Iterator<node_data> iter=getGraph().getV().iterator();
        while(iter.hasNext()){
            Iterator<edge_data> itEd=getGraph().getE(iter.next().getKey()).iterator();
            while(itEd.hasNext()){
                arr.add(itEd.next());
                index++;
            }
        }
        return arr;
    }
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
        JsonObject json_object = new JsonObject();
        JsonArray edges=new JsonArray();
        ArrayList<edge_data> ed=getEdges();
        for(int i=0;i<ed.size();i++){
            JsonObject curr=new JsonObject();
            curr.addProperty("src",ed.get(i).getSrc());
            curr.addProperty("w",ed.get(i).getWeight());
            curr.addProperty("dest",ed.get(i).getDest());;
            edges.add(curr);
        }
        JsonArray nodes=new JsonArray();
        ArrayList<node_data> no=getNodes();
        for(int j=0;j<no.size();j++){
            JsonObject curr=new JsonObject();
            curr.addProperty("pos",no.get(j).getLocation().x()+","+no.get(j).getLocation().y()+","+no.get(j).getLocation().z());
            curr.addProperty("id",no.get(j).getKey());
            nodes.add(curr);
        }
        json_object.add("Edges",edges);
        json_object.add("Nodes",nodes);
        try {
            PrintWriter pw=new PrintWriter(new File(file));
            pw.write(String.valueOf(json_object));
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
            GsonBuilder builder=new GsonBuilder();
            builder.registerTypeAdapter(directed_weighted_graph.class,new GraphJsonDeserializer());
            Gson gson=builder.create();

            FileReader reader=new FileReader(file);
            directed_weighted_graph graph =gson.fromJson(reader,directed_weighted_graph.class);
            this.ga=graph;
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
