package com.djmachine.client;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONObject;


public class NodeClient 
{
	private Socket socket;
	private String id;

	
	public NodeClient(int port)
	{	
		connectSocket();
		configSocketEvents();
	}
	
	public void connectSocket()
	{
		try
		{
			socket = IO.socket("http://localhost:8989");
			socket.connect();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void configSocketEvents()
	{
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() 
		{	
			@Override
			public void call(Object... arg0) 
			{
				System.out.println("SocketIO Connected");
			}
		}).on("socketID", new Emitter.Listener() 
		{
			@Override
			public void call(Object... args) 
			{
				// Need JSON reader to read objects that come in
				try
				{
					JSONObject data = (JSONObject)args[0];
					id = data.getString("id");
					System.out.println("My id is: " + id);
				} catch (Exception e) 
				{
					System.out.println("Could not read ID from server");
					e.printStackTrace();
				}
			}
		}).on("newUser", new Emitter.Listener() {	
			@Override
			public void call(Object... args) 
			{
				System.out.println("New User ID: " + args[0]);
			}
		});
	}
}
