package com.djmachine.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.djmachine.library.Library;

public class ClientHandler implements Runnable 
{
	private ArrayList<PrintWriter> allClientOutputStreams;
	private PrintWriter clientOutputStream;
	private Library library;
	private Socket clientSocket;
	private BufferedReader fromClient;
	private boolean running;
	
	
	public ClientHandler(Socket clientSocket, ArrayList<PrintWriter> clientOutputStreams, Library library) 
	{
		this.clientSocket = clientSocket;
		this.library = library;
		this.allClientOutputStreams = clientOutputStreams;
		running = true;
	}

	@Override
	public void run() 
	{
		String incomingMessage;
		try
		{
			this.fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			this.clientOutputStream = new PrintWriter(clientSocket.getOutputStream());

			while((incomingMessage = fromClient.readLine()) != null && running)
			{
				System.out.println("[SERVER] Received: " + incomingMessage);
				tellClient("Just for you here's - " + incomingMessage);
				//tellEveryone("Here's this back "  + incomingMessage);
				if(incomingMessage.equals("add *"))
				{
					System.out.println(library);
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void tellClient(String message)
	{
		clientOutputStream.println(message);
		clientOutputStream.flush();
	}
	
	public void tellEveryone(String message)
	{
		for(int i = 0; i < allClientOutputStreams.size(); i++)
		{
			try
			{
				allClientOutputStreams.get(i).println(message);
				allClientOutputStreams.get(i).flush();
			}
			catch(Exception e)
			{
				System.out.println("Client Handler Broke when trying to write to all clients");
				e.printStackTrace();
			}
		}
	}

}
