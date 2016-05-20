package com.djmachine.queue;

import com.djmachine.song.Song;

class MusicNode
{
	MusicNode(Song item, MusicNode next)
	{
		this.item = item;
		this.next = next;
	}
	
	Song item;
	MusicNode next;
}
