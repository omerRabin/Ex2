package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.List;

public class CL_Pokemon {
	private edge_data _edge;
	private double _value;
	private int _type;
	private Point3D _pos;
	private double min_dist;
	private int min_ro;
	private boolean isDest;
	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
	//	_speed = s;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
	}
	public static CL_Pokemon getPokemon(CL_Agent ag, game_service game,directed_weighted_graph gg){
		edge_data e=ag.get_curr_edge();
		List<CL_Pokemon> l=Arena.json2Pokemons(game.getPokemons());
		int j=0;

		while(j<l.size()){
			Arena.updateEdge(l.get(j),gg);
			j++;
		}
		int i=0;
		while(i<l.size()){
			if(e==l.get(i).get_edge()) return l.get(i);
			i++;
		}
		return null;
	}
	public static CL_Pokemon init_from_json(String json) {
		CL_Pokemon ans = null;
		try {
			JSONObject p = new JSONObject(json);
			int id = p.getInt("id");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	public String toString() {return "F:{v="+_value+", t="+_type+"}";}
	public edge_data get_edge() {
		return _edge;
	}

	public void set_edge(edge_data _edge) {
		this._edge = _edge;
	}

	public Point3D getLocation() {
		return _pos;
	}
	public int getType() {return _type;}
//	public double getSpeed() {return _speed;}
	public double getValue() {return _value;}

	public double getMin_dist() {
		return min_dist;
	}

	public void setMin_dist(double mid_dist) {
		this.min_dist = mid_dist;
	}

	public int getMin_ro() {
		return min_ro;
	}

	public void setMin_ro(int min_ro) {
		this.min_ro = min_ro;
	}
	public boolean getIsDest(){
		return this.isDest;
	}
	public void setIsDest(boolean flag){
		this.isDest=flag;
	}
}
