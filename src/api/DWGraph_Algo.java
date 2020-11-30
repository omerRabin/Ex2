package api;

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

    @Override
    public boolean isConnected() {
        return false;
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
