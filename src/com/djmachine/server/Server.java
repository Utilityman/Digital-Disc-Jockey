package com.djmachine.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import com.alee.laf.WebLookAndFeel;
import com.djmachine.library.Library;
import com.djmachine.muiscplayer.MusicPlayer;
import com.djmachine.muiscplayer.MusicPlayer.Mode;
import com.djmachine.server.threading.ActionHandler;
import com.djmachine.server.widgets.ChatPanel;
import com.djmachine.server.widgets.DDJPrompt;
import com.djmachine.server.widgets.MusicManager;
import com.djmachine.server.widgets.UserManager;

public class Server extends JFrame
{
	private static final long serialVersionUID = 1L;
	public final String TITLE = "DDJServer";
	public final String VERSION = "v0.1";
	
	private String name;
	
	private Library library;
	private ServerSocket serverSock; 
	private ArrayList<String> clientIDs;
	private ArrayList<PrintWriter> clientOutputStreams;
	private MusicPlayer musicPlayer;
	
	// Mutable components;
	private JLabel nowPlaying;
	
	public Server(int port, Library library) 
	{
		super("DDJ Server Application");
		this.setServerName("DJ the Nice Server");
		
		setupGUI();
		
		this.library = library;
		clientIDs = new ArrayList<String>();
		clientOutputStreams = new ArrayList<PrintWriter>();
		
		musicPlayer = new MusicPlayer(library, Mode.SERVER, this);
		Thread musicThread = new Thread(musicPlayer);
		musicThread.start();
		
		try 
		{
			serverSock = new ServerSocket(port);
			System.out.println("[INFO] Server Established");
			
			while(true)
			{
				Socket clientSocket = serverSock.accept();
				clientOutputStreams.add(new PrintWriter(clientSocket.getOutputStream()));
				
				Thread t = new Thread(new ClientHandler(clientSocket, clientOutputStreams, this.library));
				t.start();
				System.out.println("[SERVER] Got a new connection");
			}

		} catch (IOException e1)
		{
			System.out.println("[SEVERE] Server could not be created!");
			e1.printStackTrace();
		}
	}

	/**
	 * Broadcast sends a string to all clients connected. For example when the song switches, a trigger will send 
	 * everybody a message saying what song is now playing. 
	 * @param text the message sent to all users
	 */
	public void broadcast(String text)
	{
		for(int i = 0; i < clientOutputStreams.size(); i++)
		{
			clientOutputStreams.get(i).println(name + ": " + text + "\n");
		}
	}
	
	/**
	 * A message sent to an individual user, usually an error message
	 * @param userID - the ID of the user to send the message to
	 * @param text - the message to send to a single user
	 */
	public void send(String userID, String text)
	{
		for(int i = 0; i < clientIDs.size(); i++)
		{
			if(clientIDs.get(i).equals(userID))
				clientOutputStreams.get(i).println(name + ": " + text + "(private message)\n");
		}
	}
	
	public void sendInstruction(String instruction)
	{
		
	}
	
	/**
	 * 
	 * @param request - the request sent from clients or widgets 
	 */
	public String processRequest(String requestee, String request)
	{
		return musicPlayer.parseInput(request);	
	}
	
	/**
	 * If the music player gets angry it'll add an action to the server which will then be handled.
	 * @param action
	 */
	public void addAction(String action)
	{
		new Thread(new ActionHandler(this, action)).start();;
	}
	
	/**
	 * This label has public access so that widgets can interact with it
	 * @param nowPlaying
	 */
	public void setPlaying(String nowPlaying)
	{
		this.nowPlaying.setText("Now Playing: " + nowPlaying);
	}
	
	/**
	 *  Sets up the GUI. This might be delegated to another class.
	 */
	private void setupGUI() 
	{
        WebLookAndFeel.install();
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(new BoxLayout(instructionPanel, BoxLayout.Y_AXIS));
        JRadioButton acceptInstructions = new JRadioButton("<html>Accept User  <br>Instructions<html>");
        acceptInstructions.setSelected(true);
        JRadioButton declineInstructions = new JRadioButton("<html>Decline User  <br>Instructions</html>");
        ButtonGroup instructionGroup = new ButtonGroup();
        instructionGroup.add(acceptInstructions);
        instructionGroup.add(declineInstructions);
        instructionPanel.add(acceptInstructions);
        instructionPanel.add(declineInstructions);
        
        JPanel newUserPanel = new JPanel();
        newUserPanel.setLayout(new BoxLayout(newUserPanel, BoxLayout.Y_AXIS));
        JRadioButton acceptUsers = new JRadioButton("<html>Accept New  <br>Users</html>");
        acceptUsers.setSelected(true);
        JRadioButton declineUsers = new JRadioButton("<html>Decline New  <br>User</html>");
        ButtonGroup userGroup = new ButtonGroup();
        userGroup.add(acceptUsers);
        userGroup.add(declineUsers);
        newUserPanel.add(acceptUsers);
        newUserPanel.add(declineUsers);
        
        JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
        		instructionPanel, newUserPanel);
        left.setResizeWeight(0.5);
        left.setEnabled(false);

    	JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            new JButton("button3"), new JButton("button4"));
        right.setResizeWeight(0.5);
        right.setEnabled(false);

        
    	JTabbedPane center = new JTabbedPane();
        center.addTab("Chat Room", new ChatPanel(this));
        center.addTab("User Manager", new UserManager());
        center.addTab("DDJPrompt", new DDJPrompt(this));
        center.addTab("Music Manager", new MusicManager());

        JPanel south = new JPanel();
        nowPlaying = new JLabel("Now Playing: Nothing!");
        south.add(nowPlaying);
        
        this.add(left, BorderLayout.WEST);
        this.add(center, BorderLayout.CENTER);
        this.add(right, BorderLayout.EAST);
        this.add(south, BorderLayout.SOUTH);

        this.pack();
        this.setSize(new Dimension(600,400));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
	}
	
	private void setServerName(String name)
	{
		this.name = name;
	}

	public String getServerName() 
	{
		return name;
	}
}


