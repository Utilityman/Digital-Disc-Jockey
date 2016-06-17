package com.djmachine.server.threading;

import com.djmachine.server.Server;

// TODO: Handle Actions
public class ServerActionHandler implements Runnable
{
	private String user; 
	private String action;
	private Server server;
	
	public ServerActionHandler(Server server, String username, String action)
	{
		this.server = server;
		this.user = username;
		this.action = action;
	}

	@Override
	public void run() 
	{
		if(action.length() > 14 && action.substring(0, 14).equals("update playing"))
		{
			server.setPlaying(action.substring(15, action.length()-1));
			server.broadcast("\"" + server.getServerName() + "\"UPDATE_PLAYING  \"" + action.substring(15, action.length()-1) + "\"");
		}
		else if(action.length() > 9 && action.substring(0, 9).equals("BROADCAST"))
		{
			server.getChatArea().append(user + ": " + action.substring(10) + "\n");
			server.broadcast("CHAT " + user + ": " + action.substring(10));
		}
		else if(action.length() == 18 && action.substring(0, 18).equals("CLOSING_CONNECTION"))
			server.closeConnectionWith(user);
		else if(action.length() > 7 && action.substring(0,7).equals("CONSOLE"))
		{
			String response = server.processRequest(user, action.substring(8));
			server.tellUser(user, "CONSOLE " + response);
		}
	}
}
