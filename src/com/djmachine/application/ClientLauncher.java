package com.djmachine.application;


import com.djmachine.client.Client;


public class ClientLauncher 
{	
	public static void main(String[] args)
	{		
		int port = 8989;
		
		new Client(port);		
		//new NodeClient(port);
	}
}
