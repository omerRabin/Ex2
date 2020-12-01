package api;

public class GeoLocation implements geo_location {
    private double x;//x coordinate
    private double y;//y coordinate
    private double z;//z coordinate
    public GeoLocation(double x,double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public GeoLocation(){
        this.x=0;
        this.y=0;
        this.z=0;
    }
    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(geo_location g) {
        return Math.sqrt(Math.pow(g.x()-this.x,2)+Math.pow(g.y()-this.y,2));
    }
}
