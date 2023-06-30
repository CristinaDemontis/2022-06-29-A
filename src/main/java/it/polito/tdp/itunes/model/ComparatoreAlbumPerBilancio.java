package it.polito.tdp.itunes.model;

import java.util.Comparator;

public class ComparatoreAlbumPerBilancio implements Comparator<Album>{

	@Override
	public int compare(Album o1, Album o2) {
		return -(int)(o1.getBilancio()-o2.getBilancio());
	}

}
