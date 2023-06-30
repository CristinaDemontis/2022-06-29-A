package it.polito.tdp.itunes.model;

public class CoppieAlbum {
	
	private Album a1; 
	private Album a2; 
	private int ntracceA1; 
	private int ntracceA2;
	private int diffTracce;
	public CoppieAlbum(Album a1, Album a2, int ntracceA1, int ntracceA2, int diffTracce) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.ntracceA1 = ntracceA1;
		this.ntracceA2 = ntracceA2;
		this.diffTracce = diffTracce;
	}
	public Album getA1() {
		return a1;
	}
	public Album getA2() {
		return a2;
	}
	public int getNtracceA1() {
		return ntracceA1;
	}
	public int getNtracceA2() {
		return ntracceA2;
	}
	public int getDiffTracce() {
		return diffTracce;
	}
	@Override
	public String toString() {
		return "a1=" + a1 + ", a2=" + a2 + ", ntracceA1=" + ntracceA1 + ", ntracceA2=" + ntracceA2
				+ ", diffTracce=" + diffTracce ;
	} 
	
	


}
