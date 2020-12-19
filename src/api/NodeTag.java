package api;

import java.util.Collection;

public class NodeTag {
    private int key;
    private double tagWeight;
    Collection<edge_data> e;
    public NodeTag(int key, double tagWeight,Collection<edge_data> edges){
        this.key=key;
        this.tagWeight=tagWeight;
        this.e=edges;
    }
    public int getKey(){
        return this.key;
    }
    public double getTagWeight(){
        return this.tagWeight;
    }
    public Collection<edge_data> getE(){
        return this.e;
    }
    public void setTagWeight(double tagWeight){
        this.tagWeight=tagWeight;
    }
}
