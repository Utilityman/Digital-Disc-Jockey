package com.djmachine.test;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class PlaybackDemoMP3
{
	public static void main(String args[]) throws Exception
	{
		new PlaybackDemoMP3();
	}
	
	public PlaybackDemoMP3()
	{
		new javafx.embed.swing.JFXPanel(); // forces JavaFX init

		String uriString = new File("Bit Rush.mp3").toURI().toString();
		System.out.println(new Media(uriString).getMetadata());

		MediaPlayer player = new MediaPlayer(new Media(uriString));
		player.play(); // or stop() or pause() etc etc
		
	}
}
