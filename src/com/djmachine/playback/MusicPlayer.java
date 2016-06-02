package com.djmachine.playback;

import java.util.Scanner;

import com.djmachine.library.Library;
import com.djmachine.queue.MusicQueue;

public class MusicPlayer implements Runnable
{
	private Library library;
	private MusicQueue queue;
	private Scanner scan;
	
	private enum State
	{
		PLAYING, PAUSED, STOPPED;
	}
	private State currentState;

	public MusicPlayer(Library library)
	{
		this.library = library;
		// Pick a random song and then play
		currentState = State.STOPPED;
		queue = new MusicQueue();
		scan = new Scanner(System.in);
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
		System.out.println("here");
		PlaybackThread threadToPlay = new PlaybackThread(library.getRandomTrack());
		Thread thread = new Thread(threadToPlay);
		thread.start();
		System.out.println("Meanwhile I can do this");
		while(true)
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Still going...");
		}

	}
}
