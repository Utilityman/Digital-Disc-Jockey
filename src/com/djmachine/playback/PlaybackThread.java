package com.djmachine.playback;

import java.io.RandomAccessFile;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

import net.sourceforge.jaad.aac.AACException;
import net.sourceforge.jaad.aac.Decoder;
import net.sourceforge.jaad.aac.SampleBuffer;
import net.sourceforge.jaad.mp4.MP4Container;
import net.sourceforge.jaad.mp4.api.AudioTrack;
import net.sourceforge.jaad.mp4.api.Frame;
import net.sourceforge.jaad.mp4.api.Movie;

import com.djmachine.queue.MusicQueue;
import com.djmachine.track.Track;

public class PlaybackThread implements Runnable
{
	private MusicQueue tracks;
	
	private Track currentTrack;
	private SourceDataLine line;

	private boolean finished;
	private volatile boolean running;
	private boolean paused;

	
	public PlaybackThread()
	{
		tracks = new MusicQueue();
		finished = false;
		running = false;
		line = null;
		paused = false;
	}
	
	public PlaybackThread(Track track)
	{
		super();
		tracks.add(track);
		finished = false;
		line = null;
	}
	
	public PlaybackThread(MusicQueue queue)
	{
		this.tracks = queue;
		finished = false;
		running = false;
		line = null;
		paused = false;
	}

	@Override
	public void run() 
	{
		running = true;
		while(tracks.hasTracks())
		{
			finished = false;
			currentTrack = tracks.getNext();
			
			if(currentTrack.type.equals("m4a"))
			{
				handleM4A();
			}
		}
		running = false;
	}
	
	private void handleM4A()
	{
		byte[] b;
		try 
		{
			AudioTrack audioTrack = getM4ATrack();
			if(audioTrack == null)
			{
				System.out.println("[SEVERE] Something went wrong playing " + currentTrack);
				finished = true;
				running = false;
			}
			else
			{
				AudioFormat aufmt = new AudioFormat(audioTrack.getSampleRate(), audioTrack.getSampleSize(), audioTrack.getChannelCount(), true, true);

				line = AudioSystem.getSourceDataLine(aufmt);
				line.open();
				System.out.print(">>> ");
				line.start();

				
				final Decoder dec = new Decoder(audioTrack.getDecoderSpecificInfo());
				
				Frame frame;
				final SampleBuffer buf = new SampleBuffer();
				
				while(audioTrack.hasMoreFrames() && !finished)
				{
					while(!running);
					frame = audioTrack.readNextFrame();
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
			}				
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private AudioTrack getM4ATrack() 
	{
		MP4Container cont;
		try 
		{
			cont = new MP4Container(new RandomAccessFile(currentTrack.location, "r"));

			final Movie movie = cont.getMovie();
			
			final List<net.sourceforge.jaad.mp4.api.Track> tracks = movie.getTracks(AudioTrack.AudioCodec.AAC);
			
			if(tracks.isEmpty()) 
				throw new Exception("does not contain any AAC track");
			
			final AudioTrack track = (AudioTrack)tracks.get(0);	
			return track;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		return null;
	}

	public boolean isFinished()
	{
		return finished;
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public void pause()
	{
		running = false;
		paused = true;
	}
	
	public void resume()
	{
		running = true;
		paused = false;
	}
	
	public boolean isPaused()
	{
		return paused;
	}
	
	public void stop()
	{
		running = false;
		finished = true;
	}

	public void acknowledge() 
	{
		System.out.println("The current music thread is playing " + currentTrack);
	}

	public void skip() 
	{
		finished = true;
	}
}
