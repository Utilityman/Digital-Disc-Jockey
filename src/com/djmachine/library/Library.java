package com.djmachine.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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

	/*
	 * 
	 */
	public ArrayList<Track> randDump(int i) 
	{
		ArrayList<Track> dumpList = new ArrayList<Track>();
		
		dumpList = musicLibrary;
		
		long seed = System.nanoTime();
		Collections.shuffle(dumpList, new Random(seed));
		
		return dumpList;
	}

	public int size() 
	{
		return musicLibrary.size();
	}

	public Track getTrack(String input) 
	{
		for(int i = 0; i < musicLibrary.size(); i++)
		{
			if(musicLibrary.get(i).location.equals(input))
			{
				return musicLibrary.get(i);
			}
		}
		return null;
	}
	
}
