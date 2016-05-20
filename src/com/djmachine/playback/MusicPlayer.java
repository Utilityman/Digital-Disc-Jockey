package com.djmachine.playback;

import com.djmachine.queue.MusicQueue;
import com.djmachine.song.Song;

public class MusicPlayer 
{
	private MusicQueue queue;
	
	
	private enum State
	{
		PLAYING, PAUSED, STOPPED;
	}
	private State currentState;

	public MusicPlayer()
	{
		// Pick a random song and then play
		currentState = State.PLAYING;
	}
	
	public MusicPlayer(Song song)
	{
		queue.add(song);
		currentState = State.PLAYING;
	}
}
