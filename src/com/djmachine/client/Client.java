package com.djmachine.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client 
{
	private Socket clientSocket;
	private BufferedReader fromServer;
	private PrintWriter toServer;
	private boolean running;
	
	private Scanner scan;
	
	// Threads to handle operations
	private Thread serverReader;
	
	
	public Client(int port) 
	{
		try 
		{
			clientSocket = new Socket("localhost", 8989);
			fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			toServer = new PrintWriter(clientSocket.getOutputStream());
		} catch (UnknownHostException e) {
			System.out.println("[SEVERE] Could not connect to host");
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("[SEVERE] IO error while connecting to host");
			e.printStackTrace();
			System.exit(-1);
		}
		
		System.out.println("[SUCCESS] Connected to server");
		running = true;
		
		scan = new Scanner(System.in);
		
		serverReader = new Thread(new ClientMessageReader(fromServer));
		serverReader.start();
		
		while(running)
		{
			System.out.print(">>> ");
			String input = scan.nextLine();
			sendInput(input);
		}
		
		shutdown();
	}
	
	private void sendInput(String input)
	{
		System.out.println("Sending input");
		toServer.println(input);
		toServer.flush();	}
	
	private void shutdown()
	{
		System.out.println("[INFO] Closing connection safely");
		try {
			fromServer.close();
			toServer.close();
			clientSocket.close();
			System.out.println("[SUCCESS] Connection closed!");
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
