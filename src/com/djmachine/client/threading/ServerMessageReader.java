package com.djmachine.client.threading;

import java.io.BufferedReader;
import java.io.IOException;

import com.djmachine.client.Client;

public class ServerMessageReader implements Runnable
{	
	private Client client;
	private BufferedReader fromServer;
	public boolean running;
	
	public ServerMessageReader(Client client, BufferedReader fromServer) 
	{
		running = true;
		this.client = client;
		this.fromServer = fromServer;
	}

	public void run() 
	{
		String inFromServer;

		try
		{
			while(running && (inFromServer = fromServer.readLine()) != null)
			{
				System.out.println(inFromServer);
				client.addAction(inFromServer);
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public boolean isRunning()
	{
		return running;
	}

}
