package com.djmachine.test;

import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

import net.sourceforge.jaad.mp4.MP4Container;
import net.sourceforge.jaad.mp4.api.MetaData;
import net.sourceforge.jaad.mp4.api.Movie;
import net.sourceforge.jaad.mp4.api.Protection;
import net.sourceforge.jaad.mp4.api.Track;

public class DataDemoM4A 
{
	public static void main(String[] args) { 
		//long startTime = System.currentTimeMillis();

		try { 
			final String file; 
			file = "res/Intro.m4a";

			final MP4Container cont = new MP4Container(new RandomAccessFile(file, "r")); 
			final Movie movie = cont.getMovie(); 
			System.out.println("Song:"); 

				final List<Track> tracks = movie.getTracks(); 
				Track t; 
				for (int i = 0; i < tracks.size(); i++) { 
					t = tracks.get(i); 
					System.out.println("\tTrack " + i + ": " + t.getCodec() + " (language: " + t.getLanguage() 
							+ ", created: " + t.getCreationTime() + ")"); 

					final Protection p = t.getProtection(); 
					if (p != null) 
						System.out.println("\t\tprotection: " + p.getScheme()); 
				} 

				if (movie.containsMetaData()) { 
					System.out.println("\tMetadata:"); 
					final Map<MetaData.Field<?>, Object> data = movie.getMetaData().getAll(); 
					for (MetaData.Field<?> key : data.keySet()) { 
						if (key.equals(MetaData.Field.COVER_ARTWORKS)) { 
							final List<?> l = (List<?>) data.get(MetaData.Field.COVER_ARTWORKS); 
							System.out.println("\t\t" + l.size() + " Cover Artworks present"); 
						} else 
							System.out.println("\t\t" + key.getName() + " = " + data.get(key)); 
					} 
				} 

				final List<Protection> protections = movie.getProtections(); 
				if (protections.size() > 0) { 
					System.out.println("\tprotections:"); 
					for (Protection p : protections) { 
						System.out.println("\t\t" + p.getScheme()); 
					} 
				} 

		} catch (Exception e) { 
			e.printStackTrace(); 
			System.err.println("error while reading file: " + e.toString()); 
		} 		
	} 
}
