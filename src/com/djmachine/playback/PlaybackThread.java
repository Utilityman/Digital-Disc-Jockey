package com.djmachine.playback;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import net.sourceforge.jaad.aac.AACException;
import net.sourceforge.jaad.aac.Decoder;
import net.sourceforge.jaad.aac.SampleBuffer;
import net.sourceforge.jaad.mp4.api.Frame;

import com.djmachine.track.Track;

public class PlaybackThread implements Runnable
{
	private Track track;
	private boolean finished;
	private volatile boolean running = true;
	private SourceDataLine line;

	
	public PlaybackThread(Track track)
	{
		this.track = track;
		finished = false;
		line = null;
	}

	@Override
	public void run() 
	{
		try 
		{
			Playback.decodeM4A(track.location);
			finished = true;
			/*
			 final AudioFormat aufmt = new AudioFormat(track.getSampleRate(), track.getSampleSize(), track.getChannelCount(), true, true);
			line = AudioSystem.getSourceDataLine(aufmt);
			line.open();
			line.start();
			
			final Decoder dec = new Decoder(track.getDecoderSpecificInfo());
			
			Frame frame;
			final SampleBuffer buf = new SampleBuffer();
			
			while(track.hasMoreFrames())
			{
				frame = track.readNextFrame();
				try
				{
					dec.decodeFrame(frame.getData(), buf);
					b = buf.getData();
					line.write(b, 0, b.length);
				}
				catch(AACException e)
				{
					e.printStackTrace();
				}
			}
			 
			 */
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isFinished()
	{
		return finished;
	}
}
