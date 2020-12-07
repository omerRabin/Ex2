package gameClient;
import api.*;

import Server.Game_Server_Ex2;
import api.game_service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Server.Game_Server_Ex2;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ex2 implements Runnable{
    private static GameFrame _win;//for graphics
    private static Arena _ar;//for //for represent the state of the scenario
    private static int scenario;
    public static void main(String[] a) {
         //scenario=Integer.parseInt(a[1]);//check how to use it later
        Thread client = new Thread(new Ex2());
        client.start();
    }
    @Override
    public void run() {
       // game_service game = Game_Server_Ex2.getServer(scenario); //get the scenario
        game_service game = Game_Server_Ex2.getServer(2); //get the scenario

        //	int id = 211510631;
        //	game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
        directed_weighted_graph gg;
            GsonBuilder builder=new GsonBuilder();//create the Gson builder
            builder.registerTypeAdapter(directed_weighted_graph.class,new GraphJsonDeserializer());//using the method in GraphJsonDeserializer class to make the json to graph
            Gson gson=builder.create();//create the json
            gg=gson.fromJson(g,directed_weighted_graph.class);//convert the gson to graph from json
           init(game);
           game.startGame();
          _win.setTitle("Ex2 - OOP: My Solution "+game.toString());
          //----------------------------------------------------------------- from here i need to change everyThing
        int ind=0;
        long dt=100;

        while(game.isRunning()) {
            moveAgants(game, gg);
            try {
                if(ind%3==0) {_win.repaint();}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);

    }
    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg;
        GsonBuilder builder=new GsonBuilder();//create the Gson builder
        builder.registerTypeAdapter(directed_weighted_graph.class,new GraphJsonDeserializer());//using the method in GraphJsonDeserializer class to make the json to graph
        Gson gson=builder.create();//create the json
        gg=gson.fromJson(g,directed_weighted_graph.class);//convert the gson to graph from json

        _ar = new Arena();//create arena object
        _ar.setGraph(gg);//set arena to work on gg
        _ar.setPokemons(Arena.json2Pokemons(fs));//insert the pokemons to the arena
        _win = new GameFrame("Ex2 Game Window");//create window game
        _win.setSize(1000, 700);
        _win.update(_ar);//initialize the arena and update the window
        _win.show();//open the window on the screen
        String info = game.toString();//get the info about the graph-like num of agents and pokemons
        JSONObject line;
        try {
            int index=0;
            line = new JSONObject(info);//create Json Object contains the graph info
            JSONObject data = line.getJSONObject("GameServer");//take the info that GameServer contain
            int numOfAgents = data.getInt("agents");//take the number of agents in the graph
            System.out.println(info);
            System.out.println(game.getPokemons());
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());//crate a pokemon list from the jason
            cl_fs=sortPokemons(cl_fs);//sort the pokemons
            for (int a = 0; a < cl_fs.size(); a++) {//inserting pokemons to the area(on edges)
                Arena.updateEdge(cl_fs.get(a), gg);
            }
            for(int a = 0;a<numOfAgents;a++){//inserting agents to the area(on nodes)
                /* //---------------------i dont know if i need to use in getType in my implement so i left boaz code in document
                //********************************think how to insert efficiently the agents
                int ind = a%cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
                game.addAgent(nn);
                 */

                edge_data e=cl_fs.get(index).get_edge();//take an edge that there is pokemon on it
                game.addAgent(e.getSrc());
                index++;
            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }

    /**
     * This method Sort in ascending order the pokemons by their distance from agent(the edges weights)
     * @return
     */

    private ArrayList<CL_Pokemon> sortPokemons(ArrayList<CL_Pokemon> pokemons){
        ArrayList<CL_Pokemon> arr=new ArrayList<CL_Pokemon>();
        int index=0;
        while(!pokemons.isEmpty()) {
            CL_Pokemon bestP=pokemons.remove(index);
            int internalIndex=0;
            while (!pokemons.isEmpty()) {
                if(bestP.get_edge().getWeight()>pokemons.get(internalIndex).get_edge().getWeight()){
                    bestP=pokemons.get(internalIndex);
                }
                    internalIndex++;
            }
            index++;
            arr.add(bestP);
        }
        return arr;
    }

    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen by shortestPath Algorithm
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        //***********************************check if its impossible to involve threads here-no only in main!!!
        String agents=game.getAgents();
        int index=0;
        ArrayList<CL_Agent> agentsList= (ArrayList<CL_Agent>) Arena.getAgents(agents,gg);
        while(!agentsList.isEmpty()){
            if(agentsList.get(index).get_curr_edge()==agentsList.get(index).get_curr_fruit().get_edge()) {//the case that we exist in the edge that has pokemon
                if (agentsList.get(index).get_curr_fruit().getLocation().close2equals(agentsList.get(index).getLocation())) {//the agent is close enough to the pokemon
                    String lg = game.move();//doing move
                    List<CL_Agent> log = Arena.getAgents(lg, gg);
                    _ar.setAgents(log);//update the agents on the arena
                    String fs = game.getPokemons();
                    List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
                    _ar.setPokemons(ffs);//update the new pokemons to the edges
                    //********************************************check if break here improves the algorithm!
                }
            }
            NodeData des=((NodeData)gg.getNode(agentsList.get(index).get_curr_edge().getDest()));
            if(agentsList.get(index).getLocation().distance(des.getLocation())<0.0001){//the agent is close enough to destination node
                int id = agentsList.get(index).getID();
                int dest = agentsList.get(index).getNextNode();
                int src = agentsList.get(index).getSrcNode();
                double v = agentsList.get(index).getValue();
                if(dest==-1){
                    DWGraph_Algo ga=new DWGraph_Algo();
                    ga.init(gg);

                    String ps = game.getPokemons();//string of pokemons
                    List<CL_Pokemon> psList = Arena.json2Pokemons(ps);//pokemons list
                    double bestDes=Double.POSITIVE_INFINITY;
                    int i=0;
                    while (!psList.isEmpty()){
                        NodeData n=((NodeData)gg.getNode(src));
                        if(ga.shortestPathDist(src,psList.get(i).get_edge().getSrc())+ (psList.get(i).get_edge().getWeight()*
                                ((psList.get(i).getLocation().distance(n.getLocation()))/
                                        (n.getLocation().distance(((NodeData)gg.getNode(des.getKey())).getLocation())))) <bestDes){
                            //this if check if the sum of weights from src to pokemon is smallest than min
                            bestDes=ga.shortestPathDist(src,psList.get(i).get_edge().getSrc());
                            dest=psList.get(i).get_edge().getSrc();
                                    i++;
                        }
                        //************It should be added that once a Pokemon is assigned to an agent the other agents cannot be assigned to it
                    }
                        game.chooseNextEdge(agentsList.get(index).getID(), dest);
                        System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest);
                }
            }
                index++;
        }

    }
}
