package gameClient;

import api.*;
import gameClient.util.Point3D;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CL_Agent {
		public static final double EPS = 0.0001;
		private static int _count = 0;
		private static int _seed = 3331;
		private int _id;//agent Id
		private geo_location _pos;//agent Position
		private double _speed;//agent speed
		private edge_data _curr_edge;//agent current edge
		private node_data _curr_node;//agent current node
		private directed_weighted_graph _gg;//graph
		private CL_Pokemon _curr_fruit;//the pokemon of the agent
		private long _sg_dt;
		private double _value;//agent grade in the game
		
		
		public CL_Agent(directed_weighted_graph g, int start_node) {
			_gg = g;
			setMoney(0);
			this._curr_node = _gg.getNode(start_node);
			_pos = _curr_node.getLocation();
			_id = -1;
			setSpeed(0);
		}

	/**
	 * this method return the json of agent by giving its Id
	 * @param id
	 * @param game
	 * @return
	 */
	public static String getAgentJason(int id, game_service game) {
		String AllAgents = game.getAgents();

		try {
			JSONObject ttt = new JSONObject(AllAgents);
			JSONArray ags = ttt.getJSONArray("Agents");

			for (int i = 0; i < ags.length(); i++) {//loop on the agents
				JSONObject ag = ags.getJSONObject(i);//take the jsonObject
				JSONObject a = ag.getJSONObject("Agent");//take the information after Agent
				int Id = a.getInt("id");//take agent id
				if (Id == id) {
					return ag.toString();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * this method update the state of the agent in the game
	 * @param json
	 */
		public void update(String json) {
			JSONObject line;
			try {
				// "GameServer":{"graph":"A0","pokemons":3,"agents":1}}
				line = new JSONObject(json);
				JSONObject ttt = line.getJSONObject("Agent");
				int id = ttt.getInt("id");
				if(id==this.getID() || this.getID() == -1) {
					if(this.getID() == -1) {_id = id;}
					double speed = ttt.getDouble("speed");
					String p = ttt.getString("pos");
					Point3D pp = new Point3D(p);
					int src = ttt.getInt("src");
					int dest = ttt.getInt("dest");
					double value = ttt.getDouble("value");
					this._pos = pp;
					this.setCurrNode(src);
					this.setSpeed(speed);
					this.setNextNode(dest);
					this.setMoney(value);
					this.set_curr_edge(_gg.getEdge(src,dest));//update the edge
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		//@Override
		public int getSrcNode() {return this._curr_node.getKey();}
		public String toJSON() {
			int d = this.getNextNode();
			String ans = "{\"Agent\":{"
					+ "\"id\":"+this._id+","
					+ "\"value\":"+this._value+","
					+ "\"src\":"+this._curr_node.getKey()+","
					+ "\"dest\":"+d+","
					+ "\"speed\":"+this.getSpeed()+","
					+ "\"pos\":\""+_pos.toString()+"\""
					+ "}"
					+ "}";
			return ans;	
		}
		private void setMoney(double v) {_value = v;}
	
		public boolean setNextNode(int dest) {
			boolean ans = false;
			int src = this._curr_node.getKey();
			this._curr_edge = _gg.getEdge(src, dest);
			if(_curr_edge!=null) {
				ans=true;
			}
			else {_curr_edge = null;}
			return ans;
		}
		public static boolean DesIdNeeded(game_service game,directed_weighted_graph gg){
			List<CL_Agent> l=Arena.getAgents(game.getAgents(),gg);
			int i=0;
			while(i<l.size()){
				if(l.get(i).getNextNode()==-1){
					return true;
				}
					i++;
			}
			return false;
		}
		public void setCurrNode(int src) {
			this._curr_node = _gg.getNode(src);
		}
		public boolean isMoving() {
			return this._curr_edge!=null;
		}
		public String toString() {
			return toJSON();
		}
		public String toString1() {
			String ans=""+this.getID()+","+_pos+", "+isMoving()+","+this.getValue();	
			return ans;
		}
		public int getID() {
			// TODO Auto-generated method stub
			return this._id;
		}
	
		public geo_location getLocation() {
			// TODO Auto-generated method stub
			return _pos;
		}

		
		public double getValue() {
			// TODO Auto-generated method stub
			return this._value;
		}



		public int getNextNode() {
			int ans = -2;
			if(this._curr_edge==null) {
				ans = -1;}
			else {
				ans = this._curr_edge.getDest();
			}
			return ans;
		}

		public double getSpeed() {
			return this._speed;
		}

		public void setSpeed(double v) {
			this._speed = v;
		}
		public CL_Pokemon get_curr_fruit() {
			return _curr_fruit;
		}
		public void set_curr_fruit(CL_Pokemon curr_fruit) {
			this._curr_fruit = curr_fruit;
		}
		public void set_SDT(long ddtt) {
			long ddt = ddtt;
			if(this._curr_edge!=null) {
				double w = get_curr_edge().getWeight();
				geo_location dest = _gg.getNode(get_curr_edge().getDest()).getLocation();
				geo_location src = _gg.getNode(get_curr_edge().getSrc()).getLocation();
				double de = src.distance(dest);
				double dist = _pos.distance(dest);
				if(this.get_curr_fruit().get_edge()==this.get_curr_edge()) {
					 dist = _curr_fruit.getLocation().distance(this._pos);
				}
				double norm = dist/de;
				double dt = w*norm / this.getSpeed(); 
				ddt = (long)(1000.0*dt);
			}
			this.set_sg_dt(ddt);
		}
		
		public edge_data get_curr_edge() {
			return this._curr_edge;
		}
		public long get_sg_dt() {
			return _sg_dt;
		}
		public void set_sg_dt(long _sg_dt) {
			this._sg_dt = _sg_dt;
		}

		public void set_curr_edge(edge_data e){
			this._curr_edge=e;
		}

	}
