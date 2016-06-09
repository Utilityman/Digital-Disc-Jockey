package com.djmachine.server;

import java.io.PrintStream;
import java.util.ArrayList;

/**
 * Sends messages to the client while the main server thread just reads from the client
 * @author jmackin
 *
 */
public class ServerMessageWriter implements Runnable
{
	private PrintStream toClient;
	
	private boolean running;
	
	private static final String MESSAGE = "[SERVER] Standard Response";
	private ArrayList<String> messages;
	
	public ServerMessageWriter(PrintStream toClient) 
	{
		this.toClient = toClient;
		messages = new ArrayList<String>();
	}

	@Override
	public void run() 
	{
		running = true;
		
		while(running)
		{
			if(messages.isEmpty())
				toClient.println(MESSAGE);
			else
			{
				// Write pushed message and then 
				toClient.println(messages.get(0));
				messages.remove(0);
			}
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

}
