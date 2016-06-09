package com.djmachine.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TestServer 
{
	ArrayList<PrintWriter> clientOutputStreams;
	
	public static void main(String[] args)
	{
		new TestServer().go();
	}
	
	public void go()
	{
		clientOutputStreams = new ArrayList<PrintWriter>();
		ServerSocket serverSock = null;
		try
		{
			serverSock = new ServerSocket(8989);
			
			while(true)
			{
				Socket clientSocket = serverSock.accept();
				clientOutputStreams.add(new PrintWriter(clientSocket.getOutputStream()));
				
				Thread t = new Thread(new TestClientHandler(clientSocket, clientOutputStreams));
				t.start();
				System.out.println("Got a new connection");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		try {
			serverSock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
