package gameClient;

import api.directed_weighted_graph;
import api.edge_data;
import api.game_service;
import gameClient.util.Point3D;
import org.json.JSONObject;

import java.util.List;

public class CL_Pokemon {
	private edge_data _edge;//edge
	private double _value;//value of pok
	private int _type;//type-mark on which edge the pokemon is
	private Point3D _pos;//position of the pokemon
	private double min_dist;
	private int min_ro;
	public CL_Pokemon(Point3D p, int t, double v, double s, edge_data e) {
		_type = t;
	//	_speed = s;
		_value = v;
		set_edge(e);
		_pos = p;
		min_dist = -1;
		min_ro = -1;
	}

	/**
	 * this method return the src of the edge of the pokemon that agent is going to
	 * * @param ag
	 * @param game
	 * @param gg
	 * @return
	 */
	public static int getStartDes(CL_Agent ag,game_service game,directed_weighted_graph gg){
		List<CL_Pokemon> l=Arena.json2Pokemons(game.getPokemons());
		int j=0;
		while(j<l.size()){
			Arena.updateEdge(l.get(j),gg);//update the edges
			j++;
		}
		int i=0;
		while(i<l.size()){
			if(l.get(i).get_edge().getSrc()==ag.getSrcNode()){//if we in the start of the edge
				return l.get(i).get_edge().getDest();
			}
			i++;
		}
		return -1;
	}

	/**
	 * this method get a pokemon of the agent by giving the agent
	 * @param ag
	 * @param game
	 * @param gg
	 * @return
	 */
	public static CL_Pokemon getPokemon(CL_Agent ag, game_service game,directed_weighted_graph gg){
		edge_data e=ag.get_curr_edge();
		List<CL_Pokemon> l=Arena.json2Pokemons(game.getPokemons());
		int j=0;

		while(j<l.size()){
			Arena.updateEdge(l.get(j),gg);//update the edges
			j++;
		}
		int i=0;
		while(i<l.size()){
			if(e==l.get(i).get_edge()) return l.get(i);//if the edge of the pok compatible with the edge that in the agent
			i++;
		}
		return null;
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
}
