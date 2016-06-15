package com.djmachine.track;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;

import net.sourceforge.jaad.mp4.MP4Container;
import net.sourceforge.jaad.mp4.api.MetaData;
import net.sourceforge.jaad.mp4.api.Movie;

public class TrackData
{
	private String genre;
	private int track_number;
	private String title;
	private String composer;
	private String album;
	private String artist;

	// Currently only gets m4a track data
	public static TrackData getTrackData(String file)
	{
		TrackData trackData = new TrackData();

		MP4Container cont;

		try 
		{
			cont = new MP4Container(new RandomAccessFile(file, "r"));
			final Movie movie = cont.getMovie();

			if (movie.containsMetaData()) {
				final Map<MetaData.Field<?>, Object> data = movie.getMetaData()
						.getAll();
				for (MetaData.Field<?> key : data.keySet()) 
				{
					if (key.equals(MetaData.Field.COVER_ARTWORKS)) 
					{
						//final List<?> l = (List<?>) data.get(MetaData.Field.COVER_ARTWORKS);
						//System.out.println("\t\t" + l.size() + " Cover Artworks present");
					} 
					else
					{
						//System.out.println("\t\t" + key.getName() + " = " + data.get(key));
						if(key.getName().equals("Genre"))
							trackData.genre = (String) data.get(key);
						if(key.getName().equals("Track Number"))
							trackData.track_number = (Integer) data.get(key);
						if(key.getName().equals("Title"))
							trackData.title = (String) data.get(key);
						if(key.getName().equals("Composer"))
							trackData.composer = (String) data.get(key);
						if(key.getName().equals("Album"))
							trackData.album = (String) data.get(key);
						if(key.getName().equals("Artist"))
							trackData.artist = (String) data.get(key);
						
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(file + " not found!");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return trackData;
	}
	
	
	public String getGenre() {
		return genre;
	}


	public void setGenre(String genre) {
		this.genre = genre;
	}


	public int getTrackNumber() {
		return track_number;
	}


	public void setTrackNumber(int track_number) {
		this.track_number = track_number;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getComposer() {
		return composer;
	}


	public void setComposer(String composer) {
		this.composer = composer;
	}


	public String getAlbum() {
		return album;
	}


	public void setAlbum(String album) {
		this.album = album;
	}


	public String getArtist() {
		return artist;
	}


	public void setArtist(String artist) {
		this.artist = artist;
	}


	public String toString()
	{
		return title + ", " + artist + ". " + album + " [" + genre + "]";
	}
}
