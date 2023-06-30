package it.polito.tdp.itunes.model;

import java.util.Comparator;

public class ComparatorePerNomeAlbum implements Comparator<Album> {

	@Override
	public int compare(Album o1, Album o2) {
		return o1.getTitle().compareTo(o2.getTitle());
	}

}
