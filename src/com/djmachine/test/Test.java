package com.djmachine.test;

import java.io.IOException;
import java.net.Socket;

public class Test 
{
	public static void main(String[] args)
	{
		System.out.println("TOMCAT UP: " + isServerUp(8080));
		System.out.println("MYSQL UP: " + isServerUp(3306));
		
	}
	
	private static boolean isServerUp(int port)
	{
		try
		{
			Socket socket = new Socket("127.0.0.1", port);
			socket.close();
			return true;
		}
		catch(IOException e)
		{
			//Nope...
		}
		return false;	
	}
}
