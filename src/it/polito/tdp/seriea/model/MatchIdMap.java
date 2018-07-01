package it.polito.tdp.seriea.model;

import java.util.HashMap;
import java.util.Map;

public class MatchIdMap {
	
	private Map<Integer, Match> map;
	
	public MatchIdMap() {
		map = new HashMap<>();
	}

	public Match get(int matchId) {
		return map.get(matchId);
	}
	
	public Match get(Match match) {
		Match old = map.get(match.getId());
		if (old == null) { 
			map.put(match.getId(), match);
			return match;
		}
		return old; 
	}
	
	public void put(Match match, int matchId) {
		map.put(matchId, match);
	}
}
