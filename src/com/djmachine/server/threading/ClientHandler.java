package com.djmachine.server.threading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.djmachine.library.Library;
import com.djmachine.server.Server;

/**
 * ClientHandlers communicate directly with clients and dispatches
 * ActionHandler threads when new messages come in. This class is
 * responsible for closing the connection to an individual user as well. 
 * @author jmackin
 *
 */
public class ClientHandler implements Runnable 
{
	private String username;
	
	private PrintWriter clientOutputStream;
	
	private Server server;
	//private Library library;
	private Socket clientSocket;
	private BufferedReader fromClient;
	private boolean running;
	private ArrayList<ClientHandler> clientHandlers;
	
	// TODO: Does this class need the library
	public ClientHandler(Socket clientSocket, Server server, Library library, ArrayList<ClientHandler> clientHandlers) 
	{
		this.clientSocket = clientSocket;
		//this.library = library;
		this.running = true;
		this.server = server;
		this.clientHandlers = clientHandlers;
	}

	@Override
	public void run() 
	{
		String incomingMessage;
		try
		{
			this.fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.clientOutputStream = new PrintWriter(clientSocket.getOutputStream());

			while(running && (incomingMessage = fromClient.readLine()) != null)
			{
				System.out.println("[SERVER] Received: " + incomingMessage);
				
				if(incomingMessage.contains("LOGIN"))
					setupThread(incomingMessage);
				else
				{
					if(this.hasName())	// If a client is sending messages before it has logged in, that's weird and the client is closed. 
						server.addAction(username, incomingMessage);
					else
						this.closeConnection();
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	private void setupThread(String incomingMessage) 
	{
		if(incomingMessage.substring(0, 5).equals("LOGIN"))
		{
			if(incomingMessage.length() > 6)
			{
				username = incomingMessage.substring(6);
				if(server.verify(username))
				{
					tellClient("SERVER " + server.getServerName());
					server.getChatArea().append(username + " has joined the room.\n");
					server.broadcast("CHAT " + username + " has joined the room.");
				}
				else
					closeConnection();
			}
			else
				username = null;
		}
		else
			username = null;
	}

	/**
	 * 
	 * @param message
	 */
	public void tellClient(String message)
	{
		clientOutputStream.println(message);
		clientOutputStream.flush();
	}

	public String getName() 
	{
		return username;
	}
	
	public boolean hasName()
	{
		if(username != null && !username.equals(""))
			return true;
		return false;
	}

	public void closeConnection()
	{
		tellClient("CLOSING_CONNECTION");
		try 
		{
			clientOutputStream.close();
			fromClient.close();
			clientSocket.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("[SEVERE] Connection unable to close?!");
		}
		clientHandlers.remove(this);
		running = false;
	}
}
