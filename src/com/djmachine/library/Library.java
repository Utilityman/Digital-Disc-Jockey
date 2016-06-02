package com.djmachine.library;

import java.util.ArrayList;

import com.djmachine.track.Track;

public class Library 
{
	private ArrayList<Track> musicLibrary;
	private ArrayList<String> artists;
	private ArrayList<String> albums;
	private ArrayList<String> genres;
	
	public Library()
	{
		musicLibrary = new ArrayList<Track>();
		artists = new ArrayList<String>();
		albums = new ArrayList<String>();
		genres = new ArrayList<String>();
	}
	
	public boolean addTrack(Track track)
	{
		if(!albums.contains(track.data.getAlbum()))
			addAlbum(track.data.getAlbum());
		if(!artists.contains(track.data.getArtist()))
			addArtist(track.data.getArtist());
		if(!genres.contains(track.data.getGenre()))
			addGenre(track.data.getGenre());
		
		return musicLibrary.add(track);
	}
	
	public boolean addArtist(String artist)
	{
		return artists.add(artist);
	}
	
	public boolean addAlbum(String album)
	{
		return albums.add(album);
	}
	
	public boolean addGenre(String genre)
	{
		return genres.add(genre);
	}
	
	public void tracksToStandardOut()
	{
		for(int i = 0; i < musicLibrary.size(); i++)
			System.out.println(musicLibrary.get(i));
	}
	
	// TODO: Not very random
	public Track getRandomTrack()
	{
		return musicLibrary.get(1);
	}
	
}
