package it.polito.tdp.PremierLeague.model;

public class Adiacenza {

	Player p1;
	Player p2;
	double costo;
	public Adiacenza(Player p1, Player p2, double costo) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.costo = costo;
	}
	public Player getP1() {
		return p1;
	}
	public void setP1(Player p1) {
		this.p1 = p1;
	}
	public Player getP2() {
		return p2;
	}
	public void setP2(Player p2) {
		this.p2 = p2;
	}
	public double getCosto() {
		return costo;
	}
	public void setCosto(double costo) {
		this.costo = costo;
	}
	
	
}
