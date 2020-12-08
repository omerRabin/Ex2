package api;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GraphJsonDeserializer implements JsonDeserializer<directed_weighted_graph> {
//class for making graph object from json
    @Override
    public directed_weighted_graph deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject=jsonElement.getAsJsonObject();//create jason Object
        directed_weighted_graph graph=new DWGraph_DS();//the graph that we create
        JsonArray EdgesJsonObject=jsonObject.get("Edges").getAsJsonArray();//an array of the edges
        JsonArray NodesJsonObject=jsonObject.get("Nodes").getAsJsonArray();//an array of the nodes
        for(JsonElement j:NodesJsonObject){//Adding the nodes to the graph only by nodes
            node_data n=new NodeData(j.getAsJsonObject().get("id").getAsInt());//create the node
            graph.addNode(n);//adding the node to the graph
        }
        for(JsonElement je:EdgesJsonObject){//loop for edges
            node_data n=new NodeData(je.getAsJsonObject().get("src").getAsInt());//create the current node in the loop
            edge_data edge=new EdgeData(n.getKey(),je.getAsJsonObject().get("dest").getAsInt(),//create the current edge in the loop
                    je.getAsJsonObject().get("w").getAsDouble(),null,0);
            ((NodeData)n).getEdges().put(edge.getDest(),edge);//insert the edge to the edges list of the node
            graph.addNode(n);//insert the node to the graph
            graph.connect(n.getKey(),edge.getDest(),edge.getWeight());//create the edge in the graph
        }
        for(JsonElement jn:NodesJsonObject){//loop for nodes
            String position=jn.getAsJsonObject().get("pos").getAsString();//take the position of the current node
            geo_location pos=disassemble(position);//create the pos object by disassemble the string to the properties of pos object
            node_data n=new NodeData(pos,jn.getAsJsonObject().get("id").getAsInt());//create the node
            graph.addNode(n);//adding the node to the graph
        }
        return graph;
    }
    private geo_location disassemble(String str){
        double x,y,z;
        int indexFirstComma = 0,indexSecondComma=0,counter=0;
        for(int i=0;i<str.length();i++){//loop on the str and mark where is x,y,z
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
        geo_location pos=new GeoLocation(Double.parseDouble(str.substring(0,indexFirstComma)),//parsing x y z in string to double
                Double.parseDouble(str.substring(indexFirstComma+1,indexSecondComma)),
                Double.parseDouble(str.substring(indexSecondComma+1)));
        return pos;
    }
}
