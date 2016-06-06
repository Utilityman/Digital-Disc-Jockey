package com.djmachine.track;

import com.djmachine.util.TrackData;

public class Track 
{
	public String location;
	public TrackData data;
	public String type;
	
	public Track(String location)
	{
		this.location = location;
		data = TrackData.getTrackData(location);
		int i = location.lastIndexOf('.');
		if(i > 0)
		{
			type = location.substring(i+1);
		}
	}
	
	private Track()
	{
		location = "";
		type = "";
		data = new TrackData();
		data.setAlbum("None");
		data.setArtist("None");
		data.setComposer("None");
		data.setGenre("None");
		data.setTitle("None");
		data.setTrackNumber(0);
	}
	
	public String toString()
	{
		return data.getTitle() + ", " + data.getArtist() + ". " + data.getAlbum() + " [" + data.getGenre() + "]";
	}

	public static Track getEmptyTrack() 
	{
		return new Track();
	}
}
