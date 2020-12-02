package Tests;
import org.junit.jupiter.api.Test;
import api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class parentNode_Test {
    @Test
    public void getKeyParentTest(){
        parentNode p=new parentNode(0);
        assertEquals(0,p.getKeyParent());
    }
}
