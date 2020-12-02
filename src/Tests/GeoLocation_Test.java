package Tests;
import org.junit.jupiter.api.Test;
import api.GeoLocation;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class GeoLocation_Test {
    @Test
    public void distance_Test(){
        GeoLocation g1=new GeoLocation(1,20,0);
        GeoLocation g2=new GeoLocation(1,10,0);
        assertEquals(10,g1.distance(g2));
    }
}
