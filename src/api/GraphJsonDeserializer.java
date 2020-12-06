package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class GraphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {

    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject=jsonElement.getAsJsonObject();
        directed_weighted_graph graph=new DWGraph_DS();
        JsonArray EdgesJsonObject=jsonObject.get("Edges").getAsJsonArray();
        JsonArray NodesJsonObject=jsonObject.get("Nodes").getAsJsonArray();
        for(JsonElement je:EdgesJsonObject){
            node_data n=new NodeData(je.getAsJsonObject().get("src").getAsInt());
            edge_data edge=new EdgeData(n.getKey(),je.getAsJsonObject().get("dest").getAsInt(),
                    je.getAsJsonObject().get("w").getAsDouble(),null,0);
            ((NodeData)n).getEdges().put(edge.getDest(),edge);
            graph.addNode(n);
            graph.connect(n.getKey(),edge.getDest(),edge.getWeight());
        }
        for(JsonElement jn:NodesJsonObject){
            String position=jn.getAsJsonObject().get("pos").getAsString();
            geo_location pos=disassemble(position);
            node_data n=new NodeData(pos,jn.getAsJsonObject().get("id").getAsInt());
            graph.addNode(n);
        }
        return graph;
    }
    private geo_location disassemble(String str){
        double x,y,z;
        int indexFirstComma = 0,indexSecondComma=0,counter=0;
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)==','){
                if(counter==0){
                    indexFirstComma=i;
                }
                if(counter==1){
                    indexSecondComma=i;
                }
                counter++;
            }
        }
        geo_location pos=new GeoLocation(Double.parseDouble(str.substring(0,indexFirstComma)),
                Double.parseDouble(str.substring(indexFirstComma+1,indexSecondComma)),
                Double.parseDouble(str.substring(indexSecondComma+1)));
        return pos;
    }
}
