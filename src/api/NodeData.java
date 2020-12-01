package api;

import java.util.HashMap;

public class NodeData implements node_data{
    private geo_location pos;//node position
    private int id;//key
    private HashMap<Integer, node_data> ni;//all the neighbors of node
    private HashMap<Integer,edge_data> Edges;//all edges of node
    private double weight;//node weight
    private String info;//node info
    private int tag;//node tag
    private double tagAlgo;//tag for Dijkstra algorithm
    private parentNode p;//parent node
    public NodeData(geo_location pos,int id, HashMap<Integer,edge_data> Edges,HashMap<Integer, node_data> ni,
                    double weight,String info,int tag,double tagAlgo,parentNode p){
        this.pos=pos;
        this.id=id;
        this.Edges=Edges;
        this.ni=ni;
        this.weight=weight;
        this.info=info;
        this.tag=tag;
        this.tagAlgo=tagAlgo;
        this.p=p;
    }
    public NodeData(int id){
        this.pos=new GeoLocation();
        this.id=id;
        this.Edges=new HashMap<Integer, edge_data>();
        this.ni=new HashMap<Integer, node_data>();
        this.weight=0;
        this.info=null;
        this.tag=0;
        this.tagAlgo=0;
        this.p=null;
    }
    public edge_data getEdge(int des){ return this.Edges.get(des);
    }
    public HashMap<Integer,edge_data> getEdges(){
        return this.Edges;
    }
    public HashMap<Integer, node_data> getNi(){
        return this.ni;
    }
    @Override
    public int getKey() {
        return this.id;
    }

    @Override
    public geo_location getLocation() {
        return this.pos;
    }

    @Override
    public void setLocation(geo_location p) {
        this.pos=p;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight=w;
    }

    @Override
    public String getInfo() {
        return this.info;
    }

    @Override
    public void setInfo(String s) {
        this.info=s;
    }

    @Override
    public int getTag() {
        return this.tag;
    }

    @Override
    public void setTag(int t) {
        this.tag=t;
    }
    public double getTagAlgo(){return this.tagAlgo; }
    public void setTagAlgo(double tagAlgo){
        this.tagAlgo=tagAlgo;
    }
    public parentNode getP(){
        return this.p;
    }
    public void setP(parentNode p){
        this.p=p;
    }
}
