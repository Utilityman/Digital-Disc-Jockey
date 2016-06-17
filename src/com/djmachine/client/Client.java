package com.djmachine.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import com.alee.laf.WebLookAndFeel;
import com.djmachine.client.threading.ClientActionHandler;
import com.djmachine.client.threading.ServerMessageReader;
import com.djmachine.client.widgets.ChatPanel;
import com.djmachine.client.widgets.ConsoleWindow;
import com.djmachine.client.widgets.MusicManager;
import com.djmachine.client.widgets.UserPreferences;

// TODO: Login before connecting - perhaps just login with username/connectionAddress/port
public class Client extends JFrame
{
	private static final long serialVersionUID = 3128927722705969206L;
	public final String USERNAME = "DDJ Client";
	public final String VERSION = "v0.1";
	
	private Socket clientSocket;
	private BufferedReader fromServer;
	private PrintWriter toServer;
		
	// Threads to handle operations
	private Thread serverReader;
	
	// Components;
	private JLabel nowPlaying;
	private JTextArea chatArea;
	private JTextArea consoleArea;
	
	
	public Client(int port) 
	{
		super("DDJ Client");
		setupGUI();
		
		try 
		{
			clientSocket = new Socket("localhost", 8989);
			fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			toServer = new PrintWriter(clientSocket.getOutputStream());
			sendToServer("LOGIN " + USERNAME);
		} catch (UnknownHostException e) {
			System.out.println("[SEVERE] Could not connect to host");
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("[SEVERE] IO error while connecting to host");
			e.printStackTrace();
			System.exit(-1);
		}
		
		System.out.println("[SUCCESS] Connected to server");
		
		serverReader = new Thread(new ServerMessageReader(this, fromServer));
		serverReader.start();		
	}

	public void sendToServer(String message) 
	{
		System.out.println("Sending input");
		toServer.println(message);
		toServer.flush();	
	}
	

	public void addAction(String inFromServer)
	{
		new Thread(new ClientActionHandler(this, inFromServer)).start();
	}

	/**
	 *  Sets up the GUI. This might be delegated to another class.
	 */
	private void setupGUI() 
	{
        WebLookAndFeel.install();
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
    	JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            new JButton("button3"), new JButton("button4"));
        right.setResizeWeight(0.5);
        right.setEnabled(false);

        chatArea = new JTextArea();
        consoleArea = new JTextArea();
    	JTabbedPane center = new JTabbedPane();
        center.addTab("Chat Room", new ChatPanel(this, chatArea));
        center.addTab("Music Manager", new MusicManager());
        center.addTab("DDJ Console", new ConsoleWindow(this, consoleArea));
        center.addTab("User Preferences", new UserPreferences());


        JPanel south = new JPanel();
        nowPlaying = new JLabel("Now Playing: Nothing!");
        south.add(nowPlaying);
        
        this.add(center, BorderLayout.CENTER);
        this.add(right, BorderLayout.EAST);
        this.add(south, BorderLayout.SOUTH);

        this.pack();
        this.setSize(new Dimension(600,400));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
	}
	
	/**
	 * Safe shutdown where connections are actually closed
	 */
	public void shutdown()
	{
		System.out.println("[INFO] Closing connection safely");
		sendToServer("CLOSING_CONNECTION");
		try {
			fromServer.close();
			toServer.close();
			clientSocket.close();
			System.out.println("[SUCCESS] Connection closed!");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		chatArea.append("You were disconnected from the server.");
	}

	public void assignServer(String action) 
	{
		System.out.print(action);
	}

	public JTextArea getChatArea() 
	{
		return chatArea;
	}

	public JTextArea getConsoleLog() 
	{
		return consoleArea;
	}
}
