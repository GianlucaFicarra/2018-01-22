package it.polito.tdp.seriea.model;


import java.util.HashMap;
import java.util.Map;


public class SeasonIdMap {
	
	private Map<Integer, Season> map;
	
	public SeasonIdMap() {
		map = new HashMap<>();
	}

	public Season get(int seasonId) {
		return map.get(seasonId);
	}
	
	public Season get(Season season) {
		Season old = map.get(season.getSeason());
		if (old == null) { 
			map.put(season.getSeason(), season);
			return season;
		}
		return old; 
	}
	
	public void put(Season season, int seasonId) {
		map.put(seasonId, season);
	}
}

