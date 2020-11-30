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
    public NodeData(geo_location pos,int id, HashMap<Integer,edge_data> Edges,HashMap<Integer, node_data> ni,
                    double weight,String info,int tag){
        this.pos=pos;
        this.id=id;
        this.Edges=Edges;
        this.ni=ni;
        this.weight=weight;
        this.info=info;
        this.tag=tag;
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
}
