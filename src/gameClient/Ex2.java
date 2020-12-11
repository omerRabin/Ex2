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
         //scenario=Integer.parseInt(a[1]);
        Thread client = new Thread(new Ex2());
        client.start();
    }
    @Override
    public void run() {
       // game_service game = Game_Server_Ex2.getServer(scenario); //get the scenario
        game_service game = Game_Server_Ex2.getServer(0); //get the scenario

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
        //not create the new version of graph-with nodes on positions
        GsonBuilder builder=new GsonBuilder();//create the Gson builder
        builder.registerTypeAdapter(directed_weighted_graph.class,new GraphJsonDeserializer());//using the method in GraphJsonDeserializer class to make the json to graph
        Gson gson=builder.create();//create the json
        gg=gson.fromJson(g,directed_weighted_graph.class);//convert the gson to graph from json

        _ar = new Arena();//create arena object
        _ar.setGraph(gg);//set arena to work on gg
        ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());//crate a pokemon list from the jason
        int i=0;
        while(i<cl_fs.size()){//update the edges that the pokemons exist
            Arena.updateEdge(cl_fs.get(i++),gg);
        }
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
            cl_fs=sortPokemons(cl_fs);//sort the pokemons
            for (int a = 0; a < cl_fs.size(); a++) {//inserting pokemons to the area(on edges)
                Arena.updateEdge(cl_fs.get(a), gg);
            }
            for(int a = 0;a<numOfAgents;a++){//inserting agents to the area(on nodes)
                //********************************think how to insert efficiently the agents
                int ind = a%cl_fs.size();
                CL_Pokemon c = cl_fs.get(ind);
                int nn = c.get_edge().getDest();
                if(c.getType()<0 ) {nn = c.get_edge().getSrc();}
                game.addAgent(nn);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }

    /**
     * This method Sort in ascending order the pokemons by their distance from agent(the edges weights)
     * @return
     */
//*************must to update this method. After boaz will answer me if there is importance to the weights. if there is, we need to sort not from src to des, but according to
    //weight from src of edge to pokemon position!(like i did yet, but check if its valid)
    private ArrayList<CL_Pokemon> sortPokemons(ArrayList<CL_Pokemon> pokemons){
        ArrayList<CL_Pokemon> arr=new ArrayList<CL_Pokemon>();
        while(!pokemons.isEmpty()) {
            CL_Pokemon pok=minPokemonHelp(pokemons);
            arr.add(pok);
            pokemons.remove(pok);
        }
        return arr;
    }
private CL_Pokemon minPokemonHelp(ArrayList<CL_Pokemon> pokemons){
        int i=0;
    CL_Pokemon bestP=pokemons.get(i);
    while(i<pokemons.size()) {
        if(pokemons.get(i).get_edge().getWeight()<bestP.get_edge().getWeight()){
            bestP=pokemons.get(i);
        }
        i++;
    }
        return bestP;
}
//-----------------------------------------------------------------------
    private static CL_Agent getAgent(int id,game_service game,directed_weighted_graph gg){
        List<CL_Agent> l=Arena.getAgents(game.getAgents(),gg);
        int i=0;
        while(i<l.size()){
            if(l.get(i).getID()==id) return l.get(i);
            i++;
        }
        return null;
    }
    //-------------------------------------------------------------------
    private static CL_Pokemon bestPok(CL_Agent ag,game_service game,directed_weighted_graph gg){
        ArrayList<CL_Pokemon> ps=Arena.json2Pokemons(game.getPokemons());
        CL_Pokemon bestP=null;
        double bestShortestPath=Double.POSITIVE_INFINITY;
        DWGraph_Algo ga=new DWGraph_Algo();
        int i=0;
        ga.init(gg);
        for (int a = 0; a < ps.size(); a++) {//inserting pokemons to the area(on edges)
            Arena.updateEdge(ps.get(a), gg);
        }
        double sum=0;
        while(i<ps.size()){
            if(ag.getSrcNode()==ps.get(i).get_edge().getDest()) {
                i++;
                continue;
            }
            sum=ga.shortestPathDist(ag.getSrcNode(),ps.get(i).get_edge().getDest());
            if(sum==-1) {
                i++;
                continue;
            }
                if (sum < bestShortestPath) {
                    bestShortestPath = sum;
                    bestP = ps.get(i);
                }
            i++;
        }
        return bestP;
    }
//------------------------------------------------------------------------
    private static CL_Agent takePokemon(game_service game,int id,directed_weighted_graph gg){
    CL_Agent ag=getAgent(id,game,gg);
    CL_Pokemon bestP=bestPok(ag,game,gg);
    if(ag!=null){
        ag.set_curr_fruit(bestP);
    }
    return ag;
    }
    //------------------------------------------------------------------
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
        int index1=0;
        //ArrayList<CL_Pokemon> pokEat=//***********check how to make sure that two agents dont go to the same pokemon
        ArrayList<CL_Agent> agentsList= (ArrayList<CL_Agent>) Arena.getAgents(agents,gg);
        int counterNotCome=0;
        while (index1<agentsList.size()) {
            CL_Agent currAg = agentsList.get(index1);
            //----------------
            //currAg.update(CL_Agent.getAgentJason(currAg.getID(), game));//update the new edge-if there is
            //----------------
            if (currAg.getNextNode() == -1) {
                int id = currAg.getID();

                currAg=takePokemon(game, id, gg);
                DWGraph_Algo ga = new DWGraph_Algo();
                ga.init(gg);
                double v = currAg.getValue();
                int dest = ga.shortestPath(currAg.getSrcNode(),
                        currAg.get_curr_fruit().get_edge().getDest()).get(1).getKey();
                //******check if it Does not contradict the first inserting of the agents
                game.chooseNextEdge(currAg.getID(), dest);//take the next node in the path-that is the neighbor
                currAg.update(CL_Agent.getAgentJason(currAg.getID(), game));//update the new edge-if there is
                //-------------------------------------
                List<CL_Agent> Log = Arena.getAgentsPerAg(game.getAgents(),CL_Agent.getAgentJason(currAg.getID(),game), gg);//update the egent in the list agents in game
                Log=Arena.getAgents(game.getAgents(),gg);

                _ar.setAgents(Log);//update the agents on the arena
                //-----------------------------------------
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
                //=======================================================================
            }

            else{// give the current edge
                //----------------------------
                currAg.update(CL_Agent.getAgentJason(currAg.getID(), game));//update the new edge-if there is
            }
            currAg.set_curr_fruit(CL_Pokemon.getPokemon(currAg,game,gg));//update the pokemon
                if (currAg.get_curr_edge() == currAg.get_curr_fruit().get_edge()) {//the case that we exist in the edge that has pokemon
                    if (currAg.get_curr_fruit().getLocation().close2equals(currAg.getLocation())) {//the agent is close enough to the pokemon
                        String lg = game.move();//doing move
                        List<CL_Agent> log = Arena.getAgents(lg, gg);
                        _ar.setAgents(log);//update the agents on the arena
                        String fs = game.getPokemons();
                        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
                        _ar.setPokemons(ffs);//update the new pokemons to the edges
                        //********************************************check if break here improves the algorithm and if it will work and not stop everything (if no one is close enough to pokemon)!
                    } else {
                        counterNotCome++;
                    }
            }
            index1++;
        }
        if(counterNotCome==agentsList.size()) {
            String lg = game.move();//doing move
                List<CL_Agent> log = Arena.getAgents(lg, gg);
                _ar.setAgents(log);//update the agents on the arena
                String fs = game.getPokemons();
                List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
                _ar.setPokemons(ffs);//update the new pokemons to the edges
        }
        }
}
