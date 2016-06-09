package com.djmachine.client;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientMessageReader implements Runnable
{
	private static final String RESPONSE = "[SERVER] Standard Response";
	
	private BufferedReader fromServer;
	public boolean running;
	
	public ClientMessageReader(BufferedReader fromServer) 
	{
		running = true;
		this.fromServer = fromServer;
	}

	public void run() 
	{
		String inFromServer;

		try
		{
			while((inFromServer = fromServer.readLine()) != null && running)
			{
				if(inFromServer.equals(RESPONSE))
					System.out.println(inFromServer);
				else
					System.out.println(inFromServer);
				System.out.print(">>> ");
				
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
