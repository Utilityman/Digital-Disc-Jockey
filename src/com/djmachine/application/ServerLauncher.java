package com.djmachine.application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerLauncher extends Thread
{
	private ServerSocket serverSocket; 
	
	public ServerLauncher(int port) throws IOException
	{
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				System.out.println("just connected to " + server.getRemoteSocketAddress());
				
				DataInputStream in = new DataInputStream(server.getInputStream());
				System.out.println(in.readUTF());
				
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
				
				server.close();
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		int port = 8989;
		
		try
		{
			Thread t = new ServerLauncher(port);
			t.start();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
