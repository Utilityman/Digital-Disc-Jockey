package com.djmachine.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MusicConvertDemoMP3 
{
	public static void main(String[] args)
	{
		String line = null;
		String fileName = "Bit Rush.mp3";
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader("Bit Rush.mp3");

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            for(int i = 0; i < 10; i++)
            	if((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }  
 

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
	}
}
