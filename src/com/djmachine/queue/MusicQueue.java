package com.djmachine.queue;

import java.util.ArrayList;

import com.djmachine.track.Track;

public class MusicQueue 
{
	private MusicNode head; 
	private MusicNode tail;
	private MusicQueue previousSongs;
	
	public MusicQueue()
	{
		head = null;
		tail = null;
		previousSongs = new MusicQueue(-1);
	}
	
	public MusicQueue(int num)
	{
		head = null;
		tail = null;
	}
	
	public Track getNext()
	{
		if(size() == 1)
		{
			MusicNode returnSong = head;
			head = null;
			
			previousSongs.add(returnSong.item);
			return returnSong.item;
		}
		else
		{
			MusicNode returnSong = head;
			head = head.next;
			
			previousSongs.add(returnSong.item);
			return returnSong.item;
		}
	}
	
	public void add(Track song)
	{
		if(head == null)
		{
			MusicNode node = new MusicNode(song, null);
			head = node;
			tail = node;
		}
		else
		{
			MusicNode node = new MusicNode(song, null);
			tail.next = node;
			tail = node;
		}
	}
	
	public void add(ArrayList<Track> tracks)
	{
		for(int i = 0; i < tracks.size(); i++)
			add(tracks.get(i));
	}

	public int size() 
	{
		if(head == null)
			return 0;
		
		int count = 1;
		MusicNode tmp = head;
		while(tmp.next != null)
		{
			count++;
			tmp = tmp.next;
		}
		return count;
	}

	public void clear()
	{
		head = null;
	}

	public boolean hasTracks() 
	{
		if(head == null)
			return false;
		return true;
	}
}
