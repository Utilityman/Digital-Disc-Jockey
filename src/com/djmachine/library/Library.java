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
		if(!albums.contains(track.getAlbum()))
			addAlbum(track.getAlbum());
		if(!artists.contains(track.getArtist()))
			addArtist(track.getArtist());
		if(!genres.contains(track.getGenre()))
			addGenre(track.getGenre());
		
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
		return musicLibrary.get(0);
	}

	/**
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
			if(musicLibrary.get(i).getTitle().equals(input))
			{
				return musicLibrary.get(i);
			}
		}
		return null;
	}

	public boolean hasArtist(String artist) 
	{
		for(int i = 0; i < artists.size(); i++)
			if(artists.get(i).equals(artist))
				return true;
		return false;
	}

	public ArrayList<Track> getArtistTracks(String artist) 
	{
		ArrayList<Track> tracks = new ArrayList<Track>();
		
		for(int i = 0; i < musicLibrary.size(); i++)
			if(musicLibrary.get(i).getArtist().equalsIgnoreCase(artist))
				tracks.add(musicLibrary.get(i));
		
		// Auto shuffling when adding, probably will change it to only shuffle when specified
		long seed = System.nanoTime();
		Collections.shuffle(tracks, new Random(seed));
			
		return tracks;
	}

	public boolean hasAlbum(String album)
	{
		for(int i = 0; i < albums.size(); i++)
			if(albums.get(i).equals(album))
				return true;
		return false;
	}

	public ArrayList<Track> getAlbumTracks(String album) 
	{
		ArrayList<Track> tracks = new ArrayList<Track>();
		
		for(int i = 0; i < musicLibrary.size(); i++)
			if(musicLibrary.get(i).getAlbum().equals(album))
				tracks.add(musicLibrary.get(i));
		
		// Auto shuffling when adding, probably will change it to only shuffle when specified
		long seed = System.nanoTime();
		Collections.shuffle(tracks, new Random(seed));
		
		return tracks;
	}

	public boolean hasTrack(String substring) 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < musicLibrary.size(); i++)
			sb.append(musicLibrary.get(i) + "\n");
		
		return sb.toString();
	}
	
}
