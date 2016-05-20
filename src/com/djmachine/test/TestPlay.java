package com.djmachine.test;

import java.io.FileInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import net.sourceforge.jaad.aac.Decoder;
import net.sourceforge.jaad.aac.SampleBuffer;
import net.sourceforge.jaad.adts.ADTSDemultiplexer;

import com.djmachine.playback.Playback;

public class TestPlay 
{
	public static void main(String[] args)
	{
		String in = "res/Intro.m4a";
		try
		{
			Playback.decodeM4A(in);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("error while decoding: "+ e.toString());
		}
	}
	
	// M4A can use the method intended for MP4. 
	@SuppressWarnings("unused")
	private static void decodeAAC(String in) throws Exception 
	{ 
		SourceDataLine line = null; 
		byte[] b; 
		try 
		{ 
			final ADTSDemultiplexer adts = new ADTSDemultiplexer(new FileInputStream(in)); 
			final Decoder dec = new Decoder(adts.getDecoderSpecificInfo()); 
			final SampleBuffer buf = new SampleBuffer(); 
			while(true) 
			{ 
				b = adts.readNextFrame(); 
				dec.decodeFrame(b, buf); 
		 
				if(line==null) 
				{ 
						final AudioFormat aufmt = new AudioFormat(buf.getSampleRate(), buf.getBitsPerSample(), buf.getChannels(), true, true); 
						line = AudioSystem.getSourceDataLine(aufmt); 
						line.open(); 
						line.start(); 
				} 
				b = buf.getData(); 
				line.write(b, 0, b.length); 
			} 
		} 
		finally 
		{ 
		   if(line!=null) 
		   { 
			   line.stop(); 
			   line.close(); 
		   } 
		 }
	 }
}
