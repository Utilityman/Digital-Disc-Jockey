package com.djmachine.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class TestClientHandler implements Runnable 
{
	BufferedReader reader; 
	Socket sock;
	ArrayList<PrintWriter> clientOutputWriters;
	
	public TestClientHandler(Socket clientSocket, ArrayList<PrintWriter> clientOutputWriters)
	{
		try
		{
			this.sock = clientSocket;
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			this.clientOutputWriters = clientOutputWriters;
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{
		String message;
		try
		{
			while((message = reader.readLine()) != null)
			{
				System.out.println("read " + message);
				tellEveryone(message);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void tellEveryone(String message)
	{
		Iterator<PrintWriter> it = clientOutputWriters.iterator();
		while(it.hasNext())
		{
			try
			{
				PrintWriter writer = it.next();
				writer.println(message);
				writer.flush();
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

}
