package com.djmachine.client;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class NodeClient 
{
	private Socket socket;
	
	public NodeClient(int port)
	{
		try 
		{
			socket = IO.socket("localhost:8989");
			socket.connect();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
