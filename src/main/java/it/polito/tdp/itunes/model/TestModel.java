package it.polito.tdp.itunes.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		Model m = new Model(); 
		
		m.creaGrafo(24);
		Album v = new Album(73, "Unplugged");
		for(Album a: m.getAdiacenze(v)) {
		//	System.out.println(a.getTitle() +" "+a.getBilancio() +"\n");
		}
		Album a1 = new Album(251, "The Office, Season 3");
		Album a2 = new Album(141, "Greatest Hits");

		
		List<Album> ricors = m.pre_ricorsione(a1, a2, 4); 
		
		System.out.println("RICORSIONE "+ricors);

	}

}
