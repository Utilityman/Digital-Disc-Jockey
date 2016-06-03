package com.djmachine.playback;

import java.util.Scanner;

import com.djmachine.library.Library;
import com.djmachine.queue.MusicQueue;

public class MusicPlayer implements Runnable
{
	private boolean running;
	
	private Library library;
	private MusicQueue queue;
	private Scanner scan;
	private PlaybackThread threadToPlay;

	public MusicPlayer(Library library)
	{
		this.library = library;
		// Pick a random song and then play
		queue = new MusicQueue();
		scan = new Scanner(System.in);
		running = true;
		threadToPlay = new PlaybackThread();
	}

	@Override
	public void run() 
	{
		queue.add(library.randDump(library.size()));
		
		while(running)
		{
			System.out.print(">>> ");
			String input = scan.nextLine();
			parseInput(input);
		}
	}
	
	public void parseInput(String input)
	{
		if(input.equals("play"))
		{
			if(queue.size() > 0)
			{
				threadToPlay = new PlaybackThread(queue);
				Thread thread = new Thread(threadToPlay);
				thread.start();
			}
			else
				System.out.println("You must add songs to the queue first!");
		}
		if(input.equalsIgnoreCase("hello"))
			System.out.println("Hello!");
		if(input.equalsIgnoreCase("which thread"))
			threadToPlay.acknowledge();
		if(input.equalsIgnoreCase("pause"))
			threadToPlay.pause();
		if(input.equalsIgnoreCase("resume"))
			threadToPlay.resume();
		if(input.equalsIgnoreCase("stop"))
		{		
			System.out.println("STOPPING");
			queue.clear();
			threadToPlay.stop();
			// HARD stop
			threadToPlay = new PlaybackThread(queue);
		}
		if(input.equalsIgnoreCase("next") || input.equalsIgnoreCase("skip"))
		{
			threadToPlay.skip();
		}
		if(input.equalsIgnoreCase("quit"))
			running = false;
		if(input.length() == 4)
			if(input.substring(0, 4).equals("find"))
			{
				System.out.println("Implementing find");
			}
		if(input.length() == 3)
			if(input.substring(0, 3).equals("add"))
			{
				System.out.println("Implementing add");
			}
		
		if(input.equalsIgnoreCase("quit"))
		{
			threadToPlay.stop();
			queue.clear();
			System.exit(42);
		}
			
	}
}
