package api;

public class EdgeData implements edge_data{
    private int src;//source of edge
    private int dest;//destination of edge
    private double w;//Edge weight
    private String info;//Edge info
    private int tag;//Edge tag
    public EdgeData(int src,int dest,double w,String info,int tag){
        this.src=src;
        this.dest=dest;
        this.w=w;
        this.info=info;
        this.tag=tag;
    }
    @Override
    public int getSrc() {
        return this.src;
    }

    @Override
    public int getDest() {
        return this.dest;
    }

    @Override
    public double getWeight() {
        return this.w;
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
