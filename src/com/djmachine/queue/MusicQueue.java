package com.djmachine.queue;

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
		MusicNode returnSong = head;
		head = head.next;
		
		previousSongs.add(head.item);
		return returnSong.item;
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
}
