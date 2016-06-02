package com.djmachine.queue;

import com.djmachine.track.Track;

class MusicNode
{
	MusicNode(Track item, MusicNode next)
	{
		this.item = item;
		this.next = next;
	}
	
	Track item;
	MusicNode next;
}
