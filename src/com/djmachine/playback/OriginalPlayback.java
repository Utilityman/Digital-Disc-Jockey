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
import net.sourceforge.jaad.mp4.api.Track;

public class OriginalPlayback 
{
	public static void decodeM4A(String in) throws Exception
	{
		SourceDataLine line = null;
		byte[] b;
		try
		{
			final MP4Container cont = new MP4Container(new RandomAccessFile(in, "r"));
			final Movie movie = cont.getMovie();
			
			final List<Track> tracks = movie.getTracks(AudioTrack.AudioCodec.AAC);
			
			if(tracks.isEmpty()) 
				throw new Exception("does not contain any AAC track");
			
			final AudioTrack track = (AudioTrack)tracks.get(0);
			
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
			
		}
		finally
		{
			if(line != null)
			{
				line.stop();
				line.close();
			}
		}
		System.out.println("song-finished");
	}
}
