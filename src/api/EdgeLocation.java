package api;

public class EdgeLocation implements edge_location{
    private double ratio;
    private edge_data edge;
    public EdgeLocation(double ratio,edge_data edge){
        this.ratio=ratio;
        this.edge=edge;
    }
    @Override
    public edge_data getEdge() {
        return this.edge;
    }

    @Override
    public double getRatio() {
        return this.ratio;
    }
}
