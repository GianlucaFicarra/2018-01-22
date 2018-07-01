package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	
	private List<Team> teams;
	private List<Season> season;
	private List<Match> incontri;
	
	private TeamIdMap squadreMap;
	private SeasonIdMap stagioniMap;
	private MatchIdMap matchesIdMap;
	//per creare oggetto match nel dao mi serve oggetto SEASON e TEAM, devo creare delle loro mappe

	private List<Season> stagioniTeam;
	List<Season> best=  new ArrayList(); //soluz ricorsione
	
	private SimpleDirectedWeightedGraph<Season, DefaultWeightedEdge> graph;
	
	public Model() {
	dao= new SerieADAO();
	

	squadreMap = new TeamIdMap();
	teams = dao.listTeams(squadreMap);
	
	stagioniMap = new SeasonIdMap();
	season = dao.listAllSeasons(stagioniMap);
	
	matchesIdMap = new MatchIdMap();
	incontri = dao.listMatches(stagioniMap, squadreMap, matchesIdMap);
	
	}


	public List<Team> getSquadre() {
		return teams;
	}
	

	public List<Season> puntiClassifica(Team team) {
		
		stagioniTeam= dao.seasonsTeam(team, stagioniMap); //vertici grafo e stagioni dove gioca la squadra

		System.out.println(incontri);
			for(Match m: incontri) {
				
					Team teamHome = m.getHomeTeam();
					Team teamAway = m.getAwayTeam();
					Season stagioneTeam=m.getSeason();
					
					if(teamHome.equals(team) || teamAway.equals(team)) {
					
						switch (m.getFtr()) { //carattere che indica l'esito della partita
						case "H":
							if(teamHome.equals(team))
								stagioneTeam.AddPunti(3);
							break;
							
						case "A":
							if(teamAway.equals(team))
								stagioneTeam.AddPunti(3);
							break;
							
						case "D":
							stagioneTeam.AddPunti(1);
							break;
							
						default:
							throw new IllegalArgumentException("Errore interno: risultato non valido = " + m.getFtr());
			
						}
			      }
			}
		
		return season;
		
		/* II MODO SFRUTTO IL DAO
		List<Season> vittorie = dao.putVittorie(team, stagioniMap);
		List<Season> results = dao.putPareggi(team, stagioniMap, vittorie);
		
		return results;
		*/
	}

	

	public List<Season> getSeason() {
		return season;
	}

	
	
	public void creaGrafo() {
		graph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici sono le stagioni dove ha giocato la partita
		//-->stagioniTeam già popolata
		
		//creo grafo
		Graphs.addAllVertices(this.graph, stagioniTeam);
		
		for(Season s1: graph.vertexSet()) {
			for(Season s2: graph.vertexSet()) {
				if(!s1.equals(s2) && !graph.containsEdge(s2, s1) && !graph.containsEdge(s1, s2)) {
					
					//L’arco deve essere pesato con la differenza tra i punteggi delle due stagioni
					//arco orientato nella direzione della stagione in cui la squadra ha fatto più punti
					int peso;
					int pti1 = s1.getPunti();
					int pti2 = s2.getPunti();
					if(pti1 > pti2) {
						peso = pti1 - pti2;
						Graphs.addEdge(this.graph, s2, s1, peso);
					} else if(pti2 > pti1) {
						peso = pti2 - pti1;
						Graphs.addEdge(this.graph, s1, s2, peso);
					}
				}
	
			}
		}
	}


	public Season annataOro() {
		Season oro=null;
		int best=-1;
		int punti;
		
		for(Season s: stagioniTeam) { //stagioni del tea sopra scelto
			punti=0;
			for(DefaultWeightedEdge d: graph.incomingEdgesOf(s)) {
				punti+= graph.getEdgeWeight(d);
			}
			for(DefaultWeightedEdge d : graph.outgoingEdgesOf(s)) {
				punti -= graph.getEdgeWeight(d);
			}
			
			if(punti > best) {
				best = punti;
				oro = s;
			}
		}
		
		if(oro != null) { //se ho definito una migliore
			oro.setAnnataDoro(best);
		}
		return oro;
	}


	public void getCammino() {
		
		//un passo= aggiunta stagione consecutiva con punteggio migliore
		List<Season> parziale=  new LinkedList(); //list di soluz parziali
		
		/*for(Season s: stagioniTeam) {
			parziale.add(s);
			cerca(parziale, s, best);
			parziale.remove(parziale.size()-1);
		}*/
		Season iniziale = stagioniTeam.get(0); // parto dal vertice piu vecchio temporalmente
		System.out.println("Stagione inizale"+iniziale.toString());
		parziale.add(iniziale);
		cerca(parziale, iniziale);
		
	}


	private void cerca(List<Season> parziale, Season corrente) {
		
		//controllo se ho migliorato il cammino
		/*if(parziale.isEmpty() || parziale.size()>best.size()) {
			best= new ArrayList<>(parziale); //deepcoy
		}
		
		
		for(Season successivo:  Graphs.successorListOf(this.graph, corrente)) {
   
			if(!parziale.contains(successivo) && successivo.getPunti()>corrente.getPunti()) {
				parziale.add(successivo);
				cerca(parziale, successivo, best);
				parziale.remove(parziale.size()-1);
				//parziale.remove(successivo);
			}
			
		
		}*/
		// condizione di terminazione
				if(parziale.size() > best.size()) {
					best = new ArrayList<>(parziale);
				}
				
			
				Season ultimo = parziale.get(parziale.size()-1);
				List<Season> vicini = Graphs.successorListOf(this.graph, ultimo);
				
				for(Season vicino : vicini) {
					if(vicino.getPunti() > ultimo.getPunti()) {
						parziale.add(vicino);
						cerca(parziale, vicino);
						parziale.remove(parziale.size()-1);
					}
					
				}
		
	}

	public List<Season> getBestCammino() {
		return this.best;
	}
	
}
