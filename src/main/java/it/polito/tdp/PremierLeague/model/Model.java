package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge>grafo;
	private Map<Integer,Player>idMap;
	
	public Model() {
		dao= new PremierLeagueDAO();
		
		idMap= new HashMap<Integer,Player>();
		dao.listAllPlayers(idMap);
	}
	
	public void creaGrafo(Match m) {
		grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		// aggiungo vertici
		//giocatori che hanno partecipato alla partita
		Graphs.addAllVertices(grafo, this.dao.getVertici(m,idMap));
		
		//aggiungo archi
		for( Adiacenza a: dao.getAdiacenze(m, idMap)) {
			if(a.getCosto()>=0) {
				//p1 migliore di p2
				if(grafo.containsVertex(a.getP1())&& grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(grafo, a.getP1(),a.getP2(),a.getCosto());
				}
			}
			else {
				if(grafo.containsVertex(a.getP1())&& grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(grafo, a.getP2(),a.getP1(),(-1)*a.getCosto());
				}
			}
		}
	}
	
	public int getVertici() {
		return this.grafo.vertexSet().size();
	}
	public int getArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<Match> getTuttiMatch(){
		List<Match> matches= dao.listAllMatches();
		Collections.sort(matches, new Comparator<Match>() {

			@Override
			public int compare(Match o1, Match o2) {
				return o1.matchID-o2.matchID;
			}	
		});
		
		return matches;
	}
	

	public GiocatoreMigliore getMigliore() {
		if(grafo == null) {
			return null;
		}
		
		Player best = null;
		Double maxDelta = (double) Integer.MIN_VALUE;
		
		for(Player p : this.grafo.vertexSet()) {
			// calcolo la somma dei pesi degli archi uscenti
			double pesoUscente = 0.0;
			for(DefaultWeightedEdge edge : this.grafo.outgoingEdgesOf(p)) {
				pesoUscente = pesoUscente+ this.grafo.getEdgeWeight(edge);
			}
			
			// calcolo la somma dei pesi degli archi entranti
			double pesoEntrante = 0.0;
			for(DefaultWeightedEdge edge : this.grafo.incomingEdgesOf(p)) {
				pesoEntrante = pesoEntrante + this.grafo.getEdgeWeight(edge);
			}
			
			double delta = pesoUscente - pesoEntrante;
			if(delta > maxDelta) {
				best = p;
				maxDelta = delta;
			}
		}
		
		return new GiocatoreMigliore (best,maxDelta);
		
	}
	public Graph<Player,DefaultWeightedEdge> getGrafo() {
		return this.grafo;
	}
}
