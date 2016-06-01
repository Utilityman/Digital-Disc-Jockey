package com.djmachine.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

import net.sourceforge.jaad.mp4.MP4Container;
import net.sourceforge.jaad.mp4.api.MetaData;
import net.sourceforge.jaad.mp4.api.Movie;

public class TrackData
{
	
	public String genre;
	public int track_number;
	public String title;
	public String composer;
	public String album;
	public String artist;

	// Currently only gets m4a track data
	public static TrackData getTrackDataAsJSON(String file)
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
						final List<?> l = (List<?>) data.get(MetaData.Field.COVER_ARTWORKS);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return trackData;
	}
	
	public String toString()
	{
		return title + ", " + artist + ". " + album + " [" + genre + "]";
	}
}
