package com.djmachine.library;

import java.io.File;
import java.io.FilenameFilter;

import com.djmachine.track.Track;

/**
 * Constructs a Library object to access during runtime. 
 * @author jmackin
 *
 */
public class LibraryLoader
{
	
	private static int count;
	/**
	 * Recursively searches within the res directory for tracks
	 * @return Filled up Library
	 */
	public static Library LoadLibrary()
	{
		System.out.println("[INFO] Getting resources from " + System.getProperty("user.dir") + "/res");
		Library library = new Library();
		
		findTracks(new File(System.getProperty("user.dir") + "/res"), library);
		System.out.println("[INFO] Found " + count + " tracks in " + System.getProperty("user.dir") + "/res");
		
		return library;
	}
	
	private static void findTracks(File searchDirectory, Library library)
	{
		String[] directories = searchDirectory.list(new FilenameFilter()
		{
			  @Override
			  public boolean accept(File current, String name) 
			  {
			    return new File(current, name).isDirectory();
			  }
		});	
		
		String[] files = searchDirectory.list(new FilenameFilter()
		{
			  @Override
			  public boolean accept(File current, String name) 
			  {
			    return new File(current, name).isFile();
			  }
		});	
	
		
		// Add files
		if(files != null)
		{
			for(int i = 0; i < files.length; i++)
			{
				library.addTrack(new Track(searchDirectory.getPath() + "/" + files[i]));
				count++;
			}
		}
		// Find deeper directories
		if(directories != null)
		{

			for(int i = 0; i < directories.length; i++)
			{
				findTracks(new File(searchDirectory.getPath() + "/" + directories[i]), library);

			}
		}


	}
}
