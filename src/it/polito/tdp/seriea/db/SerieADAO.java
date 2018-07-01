package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.MatchIdMap;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.SeasonIdMap;
import it.polito.tdp.seriea.model.Team;
import it.polito.tdp.seriea.model.TeamIdMap;

public class SerieADAO {

	public List<Season> listAllSeasons(SeasonIdMap stagioniMap) {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(stagioniMap.get(new Season(res.getInt("season"), res.getString("description"))));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams(TeamIdMap squadreMap) {
		String sql = "SELECT team FROM teams";
		
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(squadreMap.get(new Team(res.getString("team"))));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Match> listMatches(SeasonIdMap stagioniMap, TeamIdMap squadreMap, MatchIdMap matchesIdMap) {
		String sql = "select match_id as id, Season as s, HomeTeam as h, "
				+ "AwayTeam as a, FTHG as goalh, FTAG as goala, FTR as result " + 
				"from matches";
		
		List<Match> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Match(res.getInt("id"), stagioniMap.get(res.getInt("s")), 
						squadreMap.get(res.getString("h")), squadreMap.get(res.getString("a")),
						res.getInt("goalh"), res.getInt("goala"), res.getString("result")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Season> seasonsTeam(Team squadra, SeasonIdMap seasonMap) {
		String sql = "select distinct Season " + 
				"from matches " + 
				"where (HomeTeam = ? or AwayTeam = ?)";
		
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, squadra.getTeam());
			st.setString(2, squadra.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(seasonMap.get(res.getInt("Season")));
			}		

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	//I MODO PUNTEGGI: USO MODEL
	public List<Match> getMatchTeam(Team team, TeamIdMap squadreMap, SeasonIdMap stagioniMap ) {
		String sql = "select match_id as id, Season as s, HomeTeam as h, "
				+ "AwayTeam as a, FTHG as goalh, FTAG as goala, FTR as result " + 
				"FROM matches " + 
				"WHERE (HomeTeam=? OR AwayTeam=?) ";
		
		List<Match> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet res = st.executeQuery();

			//per creare oggetto match mi serve oggetto SEASON e TEAM, devo creare delle loro mappe
			result.add(new Match(res.getInt("id"), stagioniMap.get(res.getInt("s")),
					squadreMap.get(res.getString("h")), squadreMap.get(res.getString("a")),
					res.getInt("goalh"), res.getInt("goala"), res.getString("result")));

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	//II MODO PUNTEGGI: USO DAO
    public List<Season> putVittorie(Team team, SeasonIdMap seasonMap) {
		
		String sql = "select count(FTR) as vittorie, Season as stagione " + 
				"from matches " + 
				"where (HomeTeam = ? " + 
				"and FTR = 'H') OR (AwayTeam = ? " + 
				"and FTR = 'A') " + 
				"group by Season";
		
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s = seasonMap.get(res.getInt("stagione"));
				int punteggio = res.getInt("vittorie")*3;
				s.AddPunti(punteggio); // aggiungo il punteggio 
				result.add(s);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Season> putPareggi(Team team, SeasonIdMap seasonMap, List<Season> seasons) {
		
		String sql = "select count(FTR) as pareggi, Season as stagione " + 
				"from matches " + 
				"where (HomeTeam = ? OR AwayTeam = ?) " + 
				"and FTR = 'D' " + 
				"group by Season";
		
		
		Connection conn = DBConnect.getConnection();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s = seasonMap.get(res.getInt("stagione"));
				int pareggi = res.getInt("pareggi");
				s.AddPunti(pareggi);
				if(!seasons.contains(s)) {
					seasons.add(s);
				}
			}		

			conn.close();
			return seasons;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


}
