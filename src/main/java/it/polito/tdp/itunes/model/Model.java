package it.polito.tdp.itunes.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.ALTAdmissibleHeuristic;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private Graph<Album, DefaultWeightedEdge> grafo; 
	private ItunesDAO dao; 
	private Map<Integer, Album> mappaAlbum;
	private List<Album> album; 
	private int n; 
	private List<Album> solMigliore; 
	private int bestNnodiBilancio; 
	
	
	public Model() { 
		
	} 
	
	public void creaGrafo(int n) {
		this.dao = new ItunesDAO();
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class); 
		this.album = dao.getAllAlbumsWTnTracks(n); 
		this.mappaAlbum = new HashMap<>();
		this.n = n; 
		
		
		for(Album a: album) {
			mappaAlbum.put(a.getAlbumId(), a); 
		}
		
		Graphs.addAllVertices(this.grafo, this.album); 
		System.out.println(this.grafo.vertexSet().size()); 
		
		List<CoppieAlbum> coppie = dao.getCoppieAlbum(n, mappaAlbum);
		
		for(CoppieAlbum cc: coppie) {
	//		System.out.println(cc.toString());
		}

		
		
		
		for(CoppieAlbum c: coppie) {
			if(c.getNtracceA1() < c.getNtracceA2()) {
				Graphs.addEdgeWithVertices(this.grafo, c.getA1(), c.getA2(), c.getDiffTracce()); 
			}
			else {
				Graphs.addEdgeWithVertices(this.grafo, c.getA2(), c.getA1(), c.getDiffTracce()); 

			}
		}
	
//		Set<DefaultWeightedEdge> archi = this.grafo.edgeSet(); 
//		for(DefaultWeightedEdge e: archi) {
//			System.out.println(e.toString()+ " peso = "+ this.grafo.getEdgeWeight(e)); 
//		}
		
	}
	
	public List<Album> getAdiacenze(Album vertice){
		Set<Album> vertici = this.grafo.vertexSet(); 
		for(Album a: vertici) {		
			double bilancio = calcolaBilancio(a); 
			a.setBilancio(bilancio);
			}
		
		List<Album> successori = Graphs.successorListOf(this.grafo, vertice) ;
		Collections.sort(successori, new ComparatoreAlbumPerBilancio());
		return successori; 
		}

	
	
	public List<Album> getListaOrdinataAlbum(){
		Set<Album> vertici = this.grafo.vertexSet(); 
		List<Album> result = new LinkedList<>(vertici); 
		
		Collections.sort(result, new ComparatorePerNomeAlbum());
		return result; 
	}
	
	public int getnVertici() {
		return this.grafo.vertexSet().size(); 
	}
	
	public int getnArchi() {
		return this.grafo.edgeSet().size(); 
	}
	
	public int calcolaBilancio(Album vertice) {
		List<Album> successori = Graphs.successorListOf(this.grafo, vertice) ;
		List<Album> predecessori = Graphs.predecessorListOf(this.grafo, vertice);
		int sommaPesiSuccessori = 0; 
		int sommaPesiPredecessori = 0; 
		// calcolo la somma dei pesi dei predecessori di vertice
		for(Album a: predecessori) {
			DefaultWeightedEdge e = this.grafo.getEdge(a, vertice);
			int peso = (int)this.grafo.getEdgeWeight(e); 
			sommaPesiPredecessori = sommaPesiPredecessori+ peso; 
		}
		// calcolo la somma dei pesi dei successori di vertice
		for(Album a: successori) {
			DefaultWeightedEdge e = this.grafo.getEdge(vertice, a);
			int peso = (int)this.grafo.getEdgeWeight(e); 
			sommaPesiSuccessori = sommaPesiSuccessori+ peso; 
		}
		int bilancio = sommaPesiPredecessori-sommaPesiSuccessori; 
		
		return bilancio; 
	}
	
	public List<Album> pre_ricorsione(Album partenza, Album destinazione, int x){
		this.bestNnodiBilancio =0; 
		this.solMigliore = new LinkedList<>();
		Set<Album> visitati = new HashSet<>(); 
		int numNodiBilancio = 0;
		List<Album> parziale = new LinkedList<>(); 
		ricorsione(partenza, destinazione, x, parziale, visitati, numNodiBilancio); 
		
		if(this.solMigliore != null) {
			List<Album> result = new LinkedList<>(this.solMigliore);
			System.out.println(result.size());
			return result; 			
		}else {
			return null; 
		}
	}
	
	
	public void ricorsione(Album partenza, Album destinazione, int x, List<Album> parziale, Set<Album> visitati, int nodiBilancio) {
		// bisogna controllare che il primo nodo (nodo partenza scelto dall'utente) abbia dei successori
		
		List<Album> successori = Graphs.successorListOf(this.grafo, partenza);
		// controllo che la lista.size() != 0
		if(successori.size() != 0) {
			
			for(Album a: successori) {
				if(a.getAlbumId().equals(destinazione.getAlbumId())) {
					// condizione di uscita, si arrivati al nodo destinazione 
					
					
					// capire perchè ad una certa la soluzione migliore sparisce!!!!!!
					if(this.solMigliore.size() ==0) {
						this.solMigliore = parziale; 
						this.bestNnodiBilancio = nodiBilancio;
						return; 
						  
					}else {
						// controllare che la sol sia migliore della precedente memorizzata
						if(nodiBilancio > this.bestNnodiBilancio) {
							this.solMigliore = parziale; 
							this.bestNnodiBilancio = nodiBilancio;
							return;
						}
					}
				}
				else {
					if(!visitati.contains(a)) {
						visitati.add(a);
						DefaultWeightedEdge e = this.grafo.getEdge(partenza, a); 
						double peso = this.grafo.getEdgeWeight(e); 
						if(peso >= x) {
							// condizione che verifica l'incremento dei numNodiBilancio
							boolean flag = false; 
							if(a.getBilancio() > partenza.getBilancio()) {
								nodiBilancio++;
								flag = true; 
							}
							
							parziale.add(a);
							ricorsione(a,destinazione,x,parziale, visitati, nodiBilancio); 
							// backtracking
							parziale.remove(parziale.size()-1);
							visitati.remove(a); 
							
							if(flag) {
								nodiBilancio--; // sistemare questo parametro, non va bene qua 
							}
							
						}else {
							break; 
						}
					}
					else {
						break; 
					}
				}
			} // for iniziale
		}else { 
			return; // capire cosa fare se un nodo non ha successori
		}
		
	}
	
	
	
	
}
