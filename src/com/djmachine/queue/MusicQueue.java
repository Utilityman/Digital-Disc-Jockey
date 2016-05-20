package com.djmachine.queue;

import com.djmachine.song.Song;

public class MusicQueue 
{
	private MusicNode head; 
	private MusicNode tail;
	private MusicQueue previousSongs;
	
	public MusicQueue()
	{
		head = null;
		previousSongs = new MusicQueue();
	}
	
	public Song getNext()
	{
		MusicNode returnSong = head;
		head = head.next;
		
		previousSongs.add(head.item);
		return returnSong.item;
	}
	
	public void add(Song song)
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
