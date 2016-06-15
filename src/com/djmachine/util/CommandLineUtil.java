package com.djmachine.util;

import com.djmachine.library.Library;
import com.djmachine.queue.MusicQueue;
import com.djmachine.track.Track;

/**
 * A utility class to hold nasty code that I don't want in the actual music player.
 * This class will probably only hold add() and find() since the other music player functions aren't too bad. 
 * @author jmackin
 *
 */
public class CommandLineUtil 
{
	
	/**
	 * This function looks through the library and adds any found songs to the queue.
	 * When the GUI is implemented this might still be useful to call when adding songs to the queue.-
	 * 
	 * @param input - the parameters for the add function "<artist> <album> <track>"
	 * @param queue - the object to add songs to if any are found
	 * @param library - library to query for params and the songs to eventually add
	 * @return
	 */
	public static boolean add(String input, MusicQueue queue, Library library)
	{
		// Entire Library
		if(input.equalsIgnoreCase("*"))
		{
			System.out.println("[INFO] Adding the entire library to the queue");
			queue.add(library.randDump(library.size()));
			System.out.println("Queue: " + queue);
			return true;
		}
		// Does the input even have quotes
		if(!input.contains("\""))
			return false;
		
		int firstQuote = input.indexOf("\"");
		int secondQuote = getNextQuotePos(firstQuote, input);
		if(secondQuote <= 0)
		{
			System.out.print("\n");
			return false;
		}
		firstQuote++;
		System.out.print(input.substring(firstQuote, secondQuote));

		if(library.hasArtist(input.substring(firstQuote, secondQuote)))
		{
			try
			{
				input = input.substring(secondQuote + 2);	// Cut out artist parameter...
			}	
			catch(StringIndexOutOfBoundsException ex)		// Unless it's the last param
			{
				System.out.print(" --> *\n");

				queue.add(library.getArtistTracks(input.substring(firstQuote, secondQuote)));
				
				System.out.println("Queue: " + queue);
				return true;
			}
			
			firstQuote = input.indexOf("\"");
			secondQuote = getNextQuotePos(firstQuote, input);
			if(secondQuote <= 0)
			{
				System.out.print("\n");
				return false;
			}
			firstQuote++;
			System.out.print(" --> " + input.substring(firstQuote, secondQuote));
			
			if(library.hasAlbum(input.substring(firstQuote, secondQuote)))
			{
				try
				{
					input = input.substring(secondQuote + 2);	// Cut out album parameter
				}
				catch(StringIndexOutOfBoundsException ex)		// Unless it's the last param
				{
					System.out.print(" --> *\n");

					queue.add(library.getAlbumTracks(input.substring(firstQuote, secondQuote)));
					
					System.out.println("Queue: " + queue);
					return true;
				}
				
				firstQuote = input.indexOf("\"");
				secondQuote = getNextQuotePos(firstQuote, input);
				if(secondQuote <= 0)
				{
					System.out.print("\n");
					return false;
				}
				firstQuote++;	
				Track track = library.getTrack(input.substring(firstQuote, secondQuote));
				if(track != null)
				{
					System.out.println(" --> " + input.substring(firstQuote, secondQuote));
					queue.add(track);
					return true;
				}
				else
					System.out.println("--> error\nCould not find track...");
			}
			else
				System.out.println("--> error \nCould not find album...");
		}
		else
			System.out.println("--> error \nCould not find artist...");
		
		return false;
	}
	
	private static int getNextQuotePos(int firstQuote, String input)
	{
		for(int i = firstQuote + 1; i < input.length() - firstQuote; i++)
		{
			if(input.charAt(i) == '"')
			{
				return i;
			}
		}	
		
		return -1;
	}

	/**
	 * This method will mostly be outclassed by the eventual gui. Why show on the command line what you can see!
	 * @param input
	 * @param library
	 * @return
	 */
	public static boolean find(String input, Library library) 
	{
		// Entire Library
		if(input.equalsIgnoreCase("*"))
		{
			System.out.println("[INFO] Adding the entire library to the queue");
			System.out.println(library);
			return true;
		}
		// Does the input even have quotes
		if(!input.contains("\""))
			return false;
		
		int firstQuote = input.indexOf("\"");
		int secondQuote = getNextQuotePos(firstQuote, input);
		if(secondQuote <= 0)
		{
			System.out.print("\n");
			return false;
		}
		firstQuote++;
		System.out.print(input.substring(firstQuote, secondQuote));

		if(library.hasArtist(input.substring(firstQuote, secondQuote)))
		{
			try
			{
				input = input.substring(secondQuote + 2);	// Cut out artist parameter...
			}	
			catch(StringIndexOutOfBoundsException ex)		// Unless it's the last param
			{
				System.out.print(" --> *\n");

				System.out.println(library.getArtistTracks(input.substring(firstQuote, secondQuote)));
				
				return true;
			}
			
			firstQuote = input.indexOf("\"");
			secondQuote = getNextQuotePos(firstQuote, input);
			if(secondQuote <= 0)
			{
				System.out.print("\n");
				return false;
			}
			firstQuote++;
			System.out.print(" --> " + input.substring(firstQuote, secondQuote));
			
			if(library.hasAlbum(input.substring(firstQuote, secondQuote)))
			{
				try
				{
					input = input.substring(secondQuote + 2);	// Cut out album parameter
				}
				catch(StringIndexOutOfBoundsException ex)		// Unless it's the last param
				{
					System.out.print(" --> *\n");

					System.out.println(library.getAlbumTracks(input.substring(firstQuote, secondQuote)));
					
					return true;
				}
				
				firstQuote = input.indexOf("\"");
				secondQuote = getNextQuotePos(firstQuote, input);
				if(secondQuote <= 0)
				{
					System.out.print("\n");
					return false;
				}
				firstQuote++;	
				Track track = library.getTrack(input.substring(firstQuote, secondQuote));
				if(track != null)
				{
					System.out.println(" --> " + input.substring(firstQuote, secondQuote));
					System.out.println(track);
					return true;
				}
				else
					System.out.println("--> error\nCould not find track...");
			}
			else
				System.out.println("--> error \nCould not find album...");
		}
		else
			System.out.println("--> error \nCould not find artist...");
		
		return false;
	}
}
