package com.djmachine.util;

import java.io.File;
import java.io.FilenameFilter;

import com.djmachine.library.Library;
import com.djmachine.queue.MusicQueue;

/**
 * A utility class to hold nasty code that I don't want in the actual music player.
 * This class will probably only hold add() and find() since the other music player functions aren't too bad. 
 * @author jmackin
 *
 */
public class CommandLineUtil 
{
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

		if(!input.contains("\""))
			return false;
		
		int firstQuote = input.indexOf("\"");
		int secondQuote = getNextQuotePos(firstQuote, input);
		if(secondQuote <= 0)
			return false;
		firstQuote++;
		
		String resourceDirectory = System.getProperty("user.dir") + "/res/";
		System.out.print(input.substring(firstQuote, secondQuote));
		File file = new File(resourceDirectory + input.substring(firstQuote, secondQuote));
		if(file.isDirectory())
		{
			resourceDirectory = file.getPath();
			try
			{
				input = input.substring(secondQuote + 2);
			}
			catch(StringIndexOutOfBoundsException ex)
			{
				// Adding all songs from an artist 
				System.out.print(" --> *\n");
				String[] directories = file.list(new FilenameFilter()
				{
					  @Override
					  public boolean accept(File current, String name) 
					  {
					    return new File(current, name).isDirectory();
					  }
				});	
				
				for(int i = 0; i < directories.length; i++)
				{
					file = new File(resourceDirectory + "/" + directories[i]);
					String[] files = file.list(new FilenameFilter()
					{
						  @Override
						  public boolean accept(File current, String name) 
						  {
						    return new File(current, name).isFile();
						  }
					});				
					
					for(int j = 0; j < files.length; j++)
					{
						queue.add(library.getTrack(resourceDirectory + "/" + directories[i] + "/" +files[j]));
					}					
				}
				
				System.out.println("Queue: " + queue);
				return true;
			}
			
			firstQuote = input.indexOf("\"");
			secondQuote = getNextQuotePos(firstQuote, input);
			if(secondQuote <= 0)
				return false;
			firstQuote++;
			System.out.print(" --> " + input.substring(firstQuote, secondQuote));
			file = new File(resourceDirectory + "/" +input.substring(firstQuote, secondQuote));
			
			if(file.isDirectory())
			{
				resourceDirectory = file.getPath();
				try
				{
					input = input.substring(secondQuote + 2);
				}
				catch(StringIndexOutOfBoundsException ex)
				{
					//Add all of an album to the queue
					System.out.print(" --> *\n");
					String[] files = file.list(new FilenameFilter()
					{
						  @Override
						  public boolean accept(File current, String name) 
						  {
						    return new File(current, name).isFile();
						  }
					});					
					for(int i = 0; i < files.length; i++)
					{
						queue.add(library.getTrack(resourceDirectory + "/" +files[i]));
					}
					
					System.out.println("Queue: " + queue);
					return true;
				}
				
				firstQuote = input.indexOf("\"");
				secondQuote = getNextQuotePos(firstQuote, input);
				if(secondQuote <= 0)
					return false;
				firstQuote++;
				System.out.print(" --> " + input.substring(firstQuote, secondQuote) + "\n");
				file = new File(resourceDirectory + "/" +input.substring(firstQuote, secondQuote));
				if(file.isFile())
				{
					queue.add(library.getTrack(resourceDirectory + "/" +input.substring(firstQuote, secondQuote)));
					System.out.println("Queue: " + queue);
					return true;
				}
			}
		}
		
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
}
