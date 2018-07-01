package it.polito.tdp.seriea.model;

public class Season {
	
	private int season;
	private String description;
	private int punti; //punti della squadra selezionata
	
	private int annataDoro;
	

	public Season(int season, String description) {
		super();
		this.season = season;
		this.description = description;
		punti=0;
	}

	public int getPunti() {
		return punti;
	}

	public void AddPunti(int punti) {
		this.punti += punti;
	}
	
	public void setAnnataDoro(int massimo) {
		this.annataDoro=massimo;
	}
	
	public int getAnnataDoro() {
		return this.annataDoro;
	}

	/**
	 * @return the season
	 */
	public int getSeason() {
		return season;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param season
	 * the season to set
	 */
	public void setSeason(int season) {
		this.season = season;
	}

	/**
	 * @param description
	 * the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + season;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Season other = (Season) obj;
		if (season != other.season)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return description +" punti:"+punti;
	}


}
