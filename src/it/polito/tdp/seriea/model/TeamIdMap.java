package it.polito.tdp.seriea.model;

import java.util.HashMap;
import java.util.Map;

public class TeamIdMap {
	
	
	private Map<String, Team> map;
	
	public TeamIdMap() {
		map = new HashMap<>();
	}

	public Team get(String teamId) {
		return map.get(teamId);
	}
	
	public Team get(Team team) {
		Team old = map.get(team.getTeam());
		if (old == null) { 
			map.put(team.getTeam(), team);
			return team;
		}
		return old; 
	}
	
	public void put(Team team, String teamId) {
		map.put(teamId, team);
	}
}
