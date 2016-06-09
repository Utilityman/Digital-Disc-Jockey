package com.djmachine.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.djmachine.library.Library;

public class Server 
{
	private Library library;
	private ServerSocket serverSock; 
	private ArrayList<PrintWriter> clientOutputStreams;
	
	public Server(int port, Library library) 
	{
		this.library = library;
		clientOutputStreams = new ArrayList<PrintWriter>();
		
		try 
		{
			serverSock = new ServerSocket(port);
			System.out.println("[INFO] Server Established");
			
			while(true)
			{
				Socket clientSocket = serverSock.accept();
				clientOutputStreams.add(new PrintWriter(clientSocket.getOutputStream()));
				
				Thread t = new Thread(new ClientHandler(clientSocket, clientOutputStreams, this.library));
				t.start();
				System.out.println("[SERVER] Got a new connection");
			}

		} catch (IOException e1)
		{
			System.out.println("[SEVERE] Server could not be created!");
			e1.printStackTrace();
		}
	}

}
