package com.djmachine.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class DataDemoMP3 
{
	public static void main(String[] args)
	{
		Mp3File mp3file = null;
		try {
			mp3file = new Mp3File("Bit Rush.mp3");
		} catch (UnsupportedTagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
		System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
		System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
		System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
		System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
		System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));
		
		if (mp3file.hasId3v2Tag()) {
			  ID3v2 id3v2Tag = mp3file.getId3v2Tag();
			  System.out.println("Track: " + id3v2Tag.getTrack());
			  System.out.println("Artist: " + id3v2Tag.getArtist());
			  System.out.println("Title: " + id3v2Tag.getTitle());
			  System.out.println("Album: " + id3v2Tag.getAlbum());
			  System.out.println("Year: " + id3v2Tag.getYear());
			  System.out.println("Genre: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
			  System.out.println("Comment: " + id3v2Tag.getComment());
			  System.out.println("Composer: " + id3v2Tag.getComposer());
			  System.out.println("Publisher: " + id3v2Tag.getPublisher());
			  System.out.println("Original artist: " + id3v2Tag.getOriginalArtist());
			  System.out.println("Album artist: " + id3v2Tag.getAlbumArtist());
			  System.out.println("Copyright: " + id3v2Tag.getCopyright());
			  System.out.println("URL: " + id3v2Tag.getUrl());
			  System.out.println("Encoder: " + id3v2Tag.getEncoder());
			  byte[] albumImageData = id3v2Tag.getAlbumImage();
			  if (albumImageData != null) {
			    System.out.println("Have album image data, length: " + albumImageData.length + " bytes");
			    System.out.println("Album image mime type: " + id3v2Tag.getAlbumImageMimeType());
			  }
		}
		if (mp3file.hasId3v2Tag()) {
		  ID3v2 id3v2Tag = mp3file.getId3v2Tag();
		  byte[] imageData = id3v2Tag.getAlbumImage();
		  if (imageData != null) {
		    //String mimeType = id3v2Tag.getAlbumImageMimeType();
		    // Write image to file - can determine appropriate file extension from the mime type
		    RandomAccessFile file = null;
			try {
				file = new RandomAccessFile("album-artwork", "rw");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    try {
				file.write(imageData);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  }
		}
	}
}
