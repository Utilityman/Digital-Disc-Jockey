package com.djmachine.muiscplayer;

import java.util.Scanner;

import com.djmachine.library.Library;
import com.djmachine.playback.PlaybackThread;
import com.djmachine.queue.MusicQueue;
import com.djmachine.util.CommandLineUtil;

public class MusicPlayer
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
		
		run();
	}

	public void run() 
	{	
		while(running)
		{
			System.out.print(">>> ");
			String input = scan.nextLine();
			parseInput(input);
		}
	}
	
	public void parseInput(String input)
	{
		if(input.equalsIgnoreCase("hello"))
			System.out.println("Hello!");
		if(input.equals("play"))
			play();
		if(input.equalsIgnoreCase("which thread"))
			checkIn();
		if(input.equalsIgnoreCase("pause"))
			pause();
		if(input.equalsIgnoreCase("resume"))
			resume();
		if(input.equalsIgnoreCase("stop"))
			stop();
		if(input.equalsIgnoreCase("next") || input.equalsIgnoreCase("skip"))
			skip();
		if(input.length() > 4)
			if(input.substring(0, 4).equals("find") || input.equalsIgnoreCase("ls"))
			{
				list(input.substring(5));
			}
		if(input.length() > 3)
			if(input.substring(0, 3).equals("add"))
			{
				add(input.substring(4));
			}
		
		if(input.equalsIgnoreCase("show queue"))
			System.out.println("Queue: " + queue);
		
		if(input.equalsIgnoreCase("quit"))
		{
			quit();
		}		
	}

	private void play()
	{
		if(threadToPlay != null)
		{
			if(threadToPlay.isPaused())
			{
				resume();
				return;
			}
		}
		
		if(!threadToPlay.isRunning())
			if(queue.size() > 0)
				{
					threadToPlay = new PlaybackThread(queue);
					Thread thread = new Thread(threadToPlay);
					thread.start();
				}
				else
					System.out.println("You must add songs to the queue first!");
		/*else
		{
			System.out.println("resuming");
			resume();
		}*/
	}
	
	private void pause()
	{
		threadToPlay.pause();
	}
	
	private void resume()
	{
		threadToPlay.resume();
	}
	
	private void stop()
	{		
		System.out.println("STOPPING");
		queue.clear();
		threadToPlay.stop();
		// HARD stop
		threadToPlay = new PlaybackThread();
	}
	
	private void skip()
	{
		threadToPlay.skip();
	}
	
	private void checkIn()
	{
		threadToPlay.acknowledge();
	}
	
	private void list(String input)
	{
		
	}
	
	private void add(String input) 
	{
		boolean success = CommandLineUtil.add(input, queue, library);
		if(!success)
			addUsage();
	}
	

	
	private void quit()
	{
		running = false;

		threadToPlay.stop();
		queue.clear();
		System.exit(42);
	}
	
	private void addUsage()
	{
		System.out.println("ADD USAGE: add \"<artist>\" \"<album\" \"<track\"");
	}
}
