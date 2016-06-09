package com.djmachine.application;

import com.djmachine.library.Library;
import com.djmachine.library.LibraryLoader;
import com.djmachine.library.LibraryOrganizer;
import com.djmachine.server.Server;

public class ServerLauncher extends Thread
{
	public static void main(String[] args)
	{
		LibraryOrganizer.organizeLibrary();
		Library library = LibraryLoader.LoadLibrary();
		
		int port = 8989;
		
		new Server(port, library);

	}
}
