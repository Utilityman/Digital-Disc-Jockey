package com.djmachine.playback;

import java.io.File;
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
			if(input.substring(0, 4).equals("find"))
			{
				find(input);
			}
		if(input.length() > 3)
			if(input.substring(0, 3).equals("add"))
			{
				add(input.substring(4));
			}
		
		if(input.equalsIgnoreCase("quit"))
		{
			quit();
		}		
	}

	private void play()
	{
		//if(threadToPlay.isRunning())		
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
		threadToPlay = new PlaybackThread(queue);
	}
	
	private void skip()
	{
		threadToPlay.skip();
	}
	
	private void checkIn()
	{
		threadToPlay.acknowledge();
	}
	
	private void find(String input)
	{
		
	}
	
	private void add(String input) 
	{
		if(input.equalsIgnoreCase("*"))
		{
			System.out.println("[INFO] Adding the entire library to the queue");
			queue.add(library.randDump(library.size()));
		}

		int firstQuote = input.indexOf("\"");
		int secondQuote = getNextQuotePos(firstQuote, input);
		firstQuote++;
		
		String resourceDirectory = System.getProperty("user.dir") + "/res/";
		System.out.println(firstQuote + " " + secondQuote);
		System.out.println(input.substring(firstQuote, secondQuote));
		File file = new File(resourceDirectory + input.substring(firstQuote, secondQuote));
		if(file.isDirectory())
			System.out.println("Progress!");
		
		System.out.println("Queue: " + queue);

	}
	
	private int getNextQuotePos(int firstQuote, String input)
	{
		for(int i = firstQuote + 1; i < input.length() - firstQuote; i++)
		{
			if(input.charAt(i) == '"')
			{
				System.out.println("found it");
				return i;
			}
		}	
		
		return -1;
	}
	
	private void quit()
	{
		running = false;

		threadToPlay.stop();
		queue.clear();
		System.exit(42);
	}
}
