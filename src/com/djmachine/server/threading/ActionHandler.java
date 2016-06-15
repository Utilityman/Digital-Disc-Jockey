package com.djmachine.server.threading;

import com.djmachine.server.Server;

// TODO: Handle Actions
public class ActionHandler implements Runnable
{
	private String action;
	private Server server;
	
	public ActionHandler(Server server, String action)
	{
		this.server = server;
		this.action = action;
	}

	@Override
	public void run() 
	{
		if(action.substring(0, 14).equals("update playing"))
			server.setPlaying(action.substring(15, action.length()-1));
	}
}
