package gameClient;
import api.*;

import Server.Game_Server_Ex2;
import api.game_service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import Server.Game_Server_Ex2;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Ex2 implements Runnable{
    private static GameFrame _win;//for graphics
    private static Arena _ar;//for //for represent the state of the scenario
    private static int scenario;
    private static int id=-1;
    public static long timeToEnd;
    public static void LevelChooser(Thread client) {
        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(350, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);

        panel.setLayout(null);

        JLabel idlabel = new JLabel("Enter ID");
        idlabel.setBounds(10, 20, 80, 25);
        panel.add(idlabel);

        JTextField userID = new JTextField(20);
        userID.setBounds(100, 20, 165, 25);
        panel.add(userID);

        JLabel label = new JLabel("Enter level");
        label.setBounds(10, 50, 80, 25);
        panel.add(label);

        JTextField userText = new JTextField(20);
        userText.setBounds(100, 50, 165, 25);
        panel.add(userText);

        JLabel choose = new JLabel("No level chosen");
        choose.setBounds(100, 110, 300, 25);
        panel.add(choose);

        JButton button = new JButton("Choose");
        button.setBounds(10, 80, 80, 25);
        panel.add(button);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                try {
                    scenario = Integer.parseInt(userText.getText());
                    id = Integer.parseInt(userID.getText());
                    choose.setText("Chosen level: " + scenario + " with the ID:"+ id);
                    client.start();
                }catch(Exception e) {
                    choose.setText("Invalid Input");
                }
            }
        });


        frame.setVisible(true);
    }


    public static void main(String[] a) {
            if (a.length > 0) {
                try {
                    id = Integer.parseInt(a[0]);
                    scenario = Integer.parseInt(a[1]);
                    Thread client = new Thread(new Ex2());
                    client.start();
                } catch (NumberFormatException e) {
                    System.err.println("Argument" + a[0] + " must be an integer.");
                }
            }
            //scenario=Integer.parseInt(a[1]);
            Thread client = new Thread(new Ex2());
            LevelChooser(client);
            //client.start();
        }
    @Override
    public void run() {
        game_service game = Game_Server_Ex2.getServer(scenario); //get the scenario
        //game_service game = Game_Server_Ex2.getServer(8); //get the scenario return to 8 when the pokemon is close to agent but not on the right edge its collect the second pokemon and then come into loop on the edge
        //int id = 211510631;
        if (game == null) {//the case we try to run a scenario that not exist
            Thread client = new Thread(new Ex2());
            LevelChooser(client);
        } else {
            game.login(id);
            String g = game.getGraph();
            String pks = game.getPokemons();
            directed_weighted_graph gg;
            GsonBuilder builder = new GsonBuilder();//create the Gson builder
            builder.registerTypeAdapter(directed_weighted_graph.class, new GraphJsonDeserializer());//using the method in GraphJsonDeserializer class to make the json to graph
            Gson gson = builder.create();//create the json
            gg = gson.fromJson(g, directed_weighted_graph.class);//convert the gson to graph from json
            init(game);
            game.startGame();
            _win.setTitle("Ex2 - OOP: My Solution " + game.toString());
            int ind = 0;
            long dt = 100;
            while (game.isRunning()) {
                timeToEnd=game.timeToEnd();
                boolean wasMoved = false;
                List<CL_Agent> AgentsList = Arena.getAgents(game.getAgents(), gg);
                int k = 0;
                while (k < AgentsList.size()) {
                    CL_Agent currAg = AgentsList.get(k);
                    if (currAg.getSpeed() > 3) dt = 90;
                    int dest = currAg.getNextNode();
                    currAg.set_curr_fruit(Arena.getPokemon(dest, game, gg));

                    if (currAg.get_curr_fruit() != null) {//it means we in the edge of the pokemon
                        //------------
                        if (currAg.get_curr_fruit().getLocation().distance(currAg.getLocation()) < 0.01) {//if the agent is close enough to pokemon
                            moveAgants(game, gg);
                            wasMoved = true;
                        }

                    }

                    k++;
                }
                if (!wasMoved) {
                    moveAgants(game, gg);
                }

                try {
                    if (ind % 3 == 0) {
                        _win.repaint();
                    }
                    Thread.sleep(dt);
                    ind++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!game.isRunning()) {
                Thread client = new Thread(new Ex2());
                LevelChooser(client);
            }
            String res = game.toString();

            System.out.println(res);
        }
    }


    /**
     * this method locate the agents in strategy way on the arena in the first-Time
     * @param game
     */
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
            Arena.updateEdge(cl_fs.get(i),gg);
            i++;
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
            System.out.println(game.getGraph());
            cl_fs=sortPokemons(cl_fs);//sort the pokemons
            for (int a = 0; a < cl_fs.size(); a++) {//inserting pokemons to the area(on edges)
                Arena.updateEdge(cl_fs.get(a), gg);
            }
            for(int a = 0;a<numOfAgents;a++){//inserting agents to the area(on nodes)
                CL_Pokemon c = cl_fs.get(a);
                int nn = c.get_edge().getDest();
                int ns=c.get_edge().getSrc();
                // cases of the locations of the pokemon
                if(nn>ns&& c.getType()>0){
                    game.addAgent(ns);
                }
                else if(nn<ns&&c.getType()>0){
                    game.addAgent(nn);
                }
                else if(nn>ns&&c.getType()<0){
                    game.addAgent(nn);
                }
                else if(nn<ns&&c.getType()<0){
                    game.addAgent(ns);
                }
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
        while(!pokemons.isEmpty()) {
            CL_Pokemon pok=minPokemonHelp(pokemons);//find the pokemons by weights
            arr.add(pok);
            pokemons.remove(pok);
        }
        return arr;
    }

    /**
     * this method find the pokemon with the minimum weight
     * @param pokemons
     * @return
     */
    private CL_Pokemon minPokemonHelp(ArrayList<CL_Pokemon> pokemons){
        int i=0;
        CL_Pokemon bestP=pokemons.get(i);
        while(i<pokemons.size()) {
            if(pokemons.get(i).get_edge().getWeight()<bestP.get_edge().getWeight()){//if there is new bestPok
                bestP=pokemons.get(i);
            }
            i++;
        }
        return bestP;
    }

    /**
     * this method return agent by giving it's Id
     * @param id
     * @param game
     * @param gg
     * @return
     */
    private static CL_Agent getAgent(int id,game_service game,directed_weighted_graph gg){
        List<CL_Agent> l=Arena.getAgents(game.getAgents(),gg);
        int i=0;
        while(i<l.size()){
            if(l.get(i).getID()==id) return l.get(i);
            i++;
        }
        return null;
    }

    /**
     *this method return the closet pokemon the the agent ag-using shortestPath algorithm
     * @param ag
     * @param game
     * @param gg
     * @return
     */
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
            sum = ga.shortestPathDist(ag.getSrcNode(), ps.get(i).get_edge().getSrc());

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

    /**
     * the method return the closet pokemon to agent with the id in parameter
     * @param game
     * @param id
     * @param gg
     * @return
     */
    private static CL_Agent takePokemon(game_service game,int id,directed_weighted_graph gg){
        CL_Agent ag=getAgent(id,game,gg);
        CL_Pokemon bestP=bestPok(ag,game,gg);
        if(ag!=null){
            ag.set_curr_fruit(bestP);//update the pokemon in the agent
        }
        return ag;
    }
    /**
     * the method move the agents in the first time
     * @param game
     * @param gg
     */
    private static void moveInit(game_service game,directed_weighted_graph gg){
        String agents = game.getAgents();
        ArrayList<CL_Agent> agentsList = (ArrayList<CL_Agent>) Arena.getAgents(agents, gg);//give the agent list
        int index1 = 0;
        while (index1 < agentsList.size()) {
            CL_Agent currAg = agentsList.get(index1);
            game.chooseNextEdge(currAg.getID(),CL_Pokemon.getStartDes(currAg,game,gg));//choose the next node
            index1++;
        }
    }
    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen by shortestPath Algorithm
     * @param game
     * @param gg
     * @param
     */
    public static int count=0;
    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        //***********************************check if its impossible to involve threads here-no only in main!!!
        String agents = game.getAgents();
        int index1 = 0;

        ArrayList<CL_Agent> agentsList = (ArrayList<CL_Agent>) Arena.getAgents(agents, gg);
        while (index1 < agentsList.size()) {
            CL_Agent currAg = agentsList.get(index1);

            currAg.update(CL_Agent.getAgentJason(currAg.getID(), game));//update the edge-if there is
            if (currAg.getNextNode() == -1) {//we arrived to new node
                int id = currAg.getID();
                if(count==0) {
                    moveInit(game, gg);
                    List<CL_Agent> la=Arena.getAgents(game.getAgents(),gg);
                    for(int i=0;i<la.size();i++) {
                        la.get(i).update(CL_Agent.getAgentJason(la.get(i).getID(), game));//update edge
                        la.get(i).set_curr_fruit(CL_Pokemon.getPokemon(la.get(i), game, gg));//update pokemon
                    }
                }
                currAg = takePokemon(game, id, gg);//take the closet pokemon the agent
                count++;//conut++  for no go into the move init again
                DWGraph_Algo ga = new DWGraph_Algo();
                ga.init(gg);
                directed_weighted_graph gCopy=ga.copy();
                ga.init(gCopy);

                double v = currAg.getValue();
                int dest;

                if(currAg.getSrcNode()!=currAg.get_curr_fruit().get_edge().getSrc()) {//we go to src
                    dest= ga.shortestPath(currAg.getSrcNode(),
                            currAg.get_curr_fruit().get_edge().getSrc()).get(1).getKey();
                }
                else {
                    dest = ga.shortestPath(currAg.getSrcNode(),
                            currAg.get_curr_fruit().get_edge().getDest()).get(1).getKey();//we in the edge-go to destination
                }

                game.chooseNextEdge(currAg.getID(), dest);//take the next node in the path-that is the neighbor
                currAg.update(CL_Agent.getAgentJason(currAg.getID(), game));//update the new edge-if there is
                //-------------------------------------
                List<CL_Agent> Log = Arena.getAgentsPerAg(game.getAgents(), CL_Agent.getAgentJason(currAg.getID(), game), gg);//update the egent in the list agents in game
                Log = Arena.getAgents(game.getAgents(), gg);
                _ar.setAgents(Log);//update the agents on the arena
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest);
                //=======================================================================
            }
            else {// give the current edge
                //----------------------------
                currAg.update(CL_Agent.getAgentJason(currAg.getID(), game));//update the edge-if there is
            }
            index1++;
        }
        String lg = game.move();//doing move
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);//update the agents on the arena
        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        _ar.setPokemons(ffs);//update the new pokemons to the edges
    }
}