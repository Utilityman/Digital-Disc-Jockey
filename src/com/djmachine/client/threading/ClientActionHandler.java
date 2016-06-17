package com.djmachine.client.threading;

import com.djmachine.client.Client;

public class ClientActionHandler implements Runnable 
{
	private String action;
	private Client client;
	
	public ClientActionHandler(Client client, String inFromServer) 
	{
		this.client = client;
		this.action = inFromServer;
	}

	@Override
	public void run() 
	{
		if(action.length() > 6 && action.substring(0, 6).equals("SERVER"))
			client.assignServer(action.substring(7));
		else if(action.length() > 4 && action.substring(0, 4).equals("CHAT"))
			client.getChatArea().append(action.substring(5) + "\n");
		else if(action.length() == 18 && action.substring(0, 18).equals("CLOSING_CONNECTION"))
			client.shutdown();
		else if(action.length() > 7 && action.substring(0,7).equals("CONSOLE"))
			client.getConsoleLog().append(action.substring(8) + "\n");
	}

}
