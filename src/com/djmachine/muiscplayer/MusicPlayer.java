package com.djmachine.muiscplayer;

import java.util.Scanner;

import com.djmachine.library.Library;
import com.djmachine.playback.PlaybackThread;
import com.djmachine.queue.MusicQueue;
import com.djmachine.server.Server;
import com.djmachine.util.CommandLineUtil;

public class MusicPlayer implements Runnable
{
	public enum Mode { CONSOLE, SERVER, CLIENT};
	
	private boolean running;
	private Server server;
	
	private Library library;
	private MusicQueue queue;
	private Scanner scan;
	private PlaybackThread threadToPlay;
	private Mode mode;

	private String response;
	
	public MusicPlayer(Library library, Mode mode)
	{
		this.library = library;
		// Pick a random song and then play
		queue = new MusicQueue();
		scan = new Scanner(System.in);
		running = true;
		threadToPlay = new PlaybackThread();
		this.mode = mode;

	}

	public MusicPlayer(Library library, Mode mode, Server server) 
	{
		this.library = library;
		// Pick a random song and then play
		queue = new MusicQueue();
		scan = new Scanner(System.in);
		running = true;
		threadToPlay = new PlaybackThread();
		this.mode = mode;
		this.server = server;
	}

	public void run() 
	{	
		switch(mode)
		{
			case CONSOLE:
				while(running)
				{
					System.out.print(">>> ");
					String input = scan.nextLine();
					parseInput(input);
				}
				break;
			case SERVER:
				String input = null;
				while(running)
				{
					if(input != null)
					{
						System.out.println("[INFO] Server music player parsing " + input);
						String response = parseInput(input);
						if(!response.equals(input))
							sendAction(response);
					}
				}
				break;
			default:
				System.out.println("[MEGA-SEVERE] How did you not set a mode?!");
				break;
		}

	}
	
	public String parseInput(String input)
	{
		this.response = input;
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
				list(input.substring(5));
			}
		if(input.equalsIgnoreCase("ls"))
			list("*");
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
		return response;		
	}

	private void play()
	{
		if(threadToPlay != null)
		{
			if(threadToPlay.isPaused())
			{
				resume();
				response = "resumed";
				return;
			}
		}
		
		if(!threadToPlay.isRunning())
			if(queue.size() > 0)
				{
					threadToPlay = new PlaybackThread(queue);
					Thread thread = new Thread(threadToPlay);
					thread.start();
					response = "started";
					sendAction("update playing" +"\"" + queue.peek() + "\"");
				}
				else
				{
					if(mode == Mode.CONSOLE)
						System.out.println("You must add songs to the queue first!");
					response = "You must add songs to the queue first!";			
				}
		/*else
		{
			System.out.println("resuming");
			resume();
		}*/
	}
	
	private void pause()
	{
		threadToPlay.pause();
		response = "paused";
	}
	
	private void resume()
	{
		threadToPlay.resume();
		response = "resumed";
	}
	
	private void stop()
	{		
		if(mode == Mode.CONSOLE)
			System.out.println("STOPPING");
		response = "stopped";
		queue.clear();
		threadToPlay.stop();
		// HARD stop
		threadToPlay = new PlaybackThread();
	}
	
	private void skip()
	{
		threadToPlay.skip();
		response = "skipping song";
	}
	
	private void checkIn()
	{
		response = threadToPlay.acknowledge();
	}
	
	private void list(String input)
	{
		boolean success = CommandLineUtil.find(input, library);
		//TODO: Better responses
		response = "listing songs";
		if(!success)
			addUsage();
	}
	
	private void add(String input) 
	{
		boolean success = CommandLineUtil.add(input, queue, library);
		// TODO: Better response
		response = "adding songs";
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
		System.out.println("ADD USAGE: add \"<artist>\" \"<album>\" \"<track>\"");
		response = "ADD USAGE: add \"<artist>\" \"<album\" \"<track>\"";
	}
	
	private void sendAction(String message)
	{
		server.addAction("root", message);
	}
}
