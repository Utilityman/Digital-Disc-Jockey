package com.djmachine.application;

import com.djmachine.library.Library;
import com.djmachine.library.LibraryLoader;
import com.djmachine.library.LibraryOrganizer;
import com.djmachine.playback.MusicPlayer;

public class Launcher 
{

	public static void main(String[] args) 
	{
		LibraryOrganizer.organizeLibrary();
		Library library = LibraryLoader.LoadLibrary();
		
		new MusicPlayer(library).run();
		
	}

}
