package com.djmachine.library;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.djmachine.util.TrackData;

/** 
 * Organizes a library by tearing down the shelves and reorganizing everything. 
 * Could be optimized to do a check of whether the files are already formatted in the 
 * 	way needed by DDJ.
 * 
 *  Currently this only organizes m4a type files. Any extra files will stop the .tmp folder from being removed.
 *  TODO: Ability to recognize and organize mp3 files as well.
 *  TODO: Dump extra files into ~fileDump/
 *  TODO: Intense error handling. 
 *  TODO: Unknown artist (--> move to 'res/UnknownArtists') or album (--> move to '~artistName/')
 *  TODO: Renaming files that are brought in with itunes "#-## <trackname>.m4a" get rid of #-##
 *  
 * @author jmackin
 *
 */
public class LibraryOrganizer 
{
	public static void organizeLibrary()
	{	
		if(checkLibrary())
		{
			System.out.println("[INFO] Existing resource folder is organized");
			//return;
		}
		
		System.out.println("[INFO] Cleaning the resource folder: " + System.getProperty("user.dir") + "/res");
		String resourceDirectory = System.getProperty("user.dir") + "/res";
		File workingDirectory = new File(resourceDirectory);
		
		File tmpDir = new File(resourceDirectory + "/.temp");
		
		File mac_specific_file = new File(resourceDirectory + "/.DS_Store");
		if(mac_specific_file.exists())
		{
			mac_specific_file.delete();
			System.out.println("[INFO] Found and deleted .DS_Store");
		}
		
		System.out.println("[INFO] Creating tmp directory inside of " + resourceDirectory);
		boolean successful = tmpDir.mkdir();
		if(successful)
		{	
			// Delete existing architecture and move everything into a temporary folder.
			purge(workingDirectory, tmpDir);
			
			String[] files = tmpDir.list(new FilenameFilter()
			{
				  @Override
				  public boolean accept(File current, String name) 
				  {
				    return new File(current, name).isFile();
				  }
			});
			
			for(int i = 0; i < files.length; i++)
			{
				if(files[i].equals(".DS_Store"))
				{
					File file = new File(resourceDirectory + "/.temp/" + files[i]);
					if(file.delete())
						System.out.println("[INFO] Safely deleted .DS_Store");
					else
						System.out.println("[SEVERE] .DS_STore got through!");
				}
				else
				{
					TrackData track = TrackData.getTrackData(tmpDir + "/" + files[i]);
					
					File artistDir = new File(resourceDirectory + "/" + track.getArtist());
					if(artistDir.exists())
						System.out.println("[WARNING] Directory for " + artistDir.getName() + " already exists");
					else 
						if(artistDir.mkdir())
							System.out.println("[INFO] Creating directory for " + track.getArtist());
					
					File albumDir = new File(artistDir + "/" + track.getAlbum());
					if(albumDir.exists())
					{
						System.out.println("[WARNING] Directory for " + albumDir.getName() + " already exists");
					}
					else
					{
						if(albumDir.mkdir())
							System.out.println("[INFO] Creating directory for " + track.getAlbum());
						else
							System.out.println("[SEVERE] Could not create directory for " + track.getAlbum());
					}
					
					try 
					{
						Files.move(Paths.get(tmpDir + "/" + files[i]), Paths.get(albumDir + "/" + files[i]), StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e) 
					{
						System.out.println("[SEVERE] Could not move " + track + "!");
						e.printStackTrace();
					}
				}

				
				
			}
			
			// Lastly
			successful = tmpDir.delete();
			if(successful)
				System.out.println("[INFO] Tmp directory removed correctly");
			else
				System.out.println("[WARNING] tmp directory was not removed! May cause errors on next organize!");
		}
		else
		{
			System.out.println("[SEVERE]: Could not create " + resourceDirectory + "/tmp");
		}
	}
	
	/**
	 * Recursive function that takes a directory and then destroys all of itself and all subdirectories and moves
	 * files to a tmpLocation to later be sorted out. 
	 * @param purgeDirectory 
	 * @param tmpDirectory
	 */
	private static void purge(File purgeDirectory, File tmpDirectory)
	{
		if(purgeDirectory.getName().equals(tmpDirectory.getName()))
		{
			return;
		}
		else
		{
			String[] directories = purgeDirectory.list(new FilenameFilter()
			{
				  @Override
				  public boolean accept(File current, String name) 
				  {
				    return new File(current, name).isDirectory();
				  }
			});	
			
			String[] files = purgeDirectory.list(new FilenameFilter()
			{
				  @Override
				  public boolean accept(File current, String name) 
				  {
				    return new File(current, name).isFile();
				  }
			});	

			for(int i = 0; i < files.length; i++)
				try 
				{
					Files.move(Paths.get(purgeDirectory + "/" + files[i]), Paths.get(tmpDirectory + "/" + files[i]), StandardCopyOption.REPLACE_EXISTING);
					System.out.println("[INFO] Moved " + purgeDirectory + "/" + files[i] + " into .tmp");
				} catch (IOException e) 
				{
					System.out.println("[SEVERE] Unable to move " + purgeDirectory + "/" + files[i] + ". Organize process will now fail.");
					e.printStackTrace();
				}

			for(int i = 0; i < directories.length; i++)
				purge(new File(purgeDirectory.getPath() + "/" + directories[i]), tmpDirectory);
			
			if(purgeDirectory.getName().equals("res"))
			{
				System.out.println("[INFO] Finished removing directories and relocating directories to .tmp");
				return;
			}
			else
			{
				if(purgeDirectory.delete())
					System.out.println("[INFO] Deleting the directory: " + purgeDirectory.getName());
				else
					System.out.println("[WARNING] Could not delete the directory: " + purgeDirectory.getName());
			}		
		}
	}
	
	
	private static boolean checkLibrary()
	{
		return true;
	}
}
