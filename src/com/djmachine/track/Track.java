package com.djmachine.track;

import com.djmachine.util.TrackData;

public class Track 
{
	public String location;
	public TrackData data;
	
	public Track(String location)
	{
		this.location = location;
		data = TrackData.getTrackData(location);
	}
	
	public String toString()
	{
		return data.getTitle() + ", " + data.getArtist() + ". " + data.getAlbum() + " [" + data.getGenre() + "]";
	}
}
