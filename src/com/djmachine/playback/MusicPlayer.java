package com.djmachine.playback;

import java.util.Scanner;

import com.djmachine.queue.MusicQueue;
import com.djmachine.song.Song;

public class MusicPlayer implements Runnable
{
	private MusicQueue queue;
	private Scanner scan;
	
	private enum State
	{
		PLAYING, PAUSED, STOPPED;
	}
	private State currentState;

	public MusicPlayer()
	{
		// Pick a random song and then play
		currentState = State.STOPPED;
		queue = new MusicQueue();
		scan = new Scanner(System.in);
	}
	
	public MusicPlayer(Song song)
	{
		queue = new MusicQueue();
		queue.add(song);
		currentState = State.PLAYING;
	}
	
	public void update()
	{
		switch(currentState)
		{
			case PLAYING:
				
				break;
			case PAUSED:
				
				break;
				
			default:
				System.out.println("ERR: MUSIC PLAYER HAS NO STATE");
			case STOPPED:
				
				break;	
		}
	}

	@Override
	public void run() 
	{
		while(true)
		{
			update();
		}
	}
}
