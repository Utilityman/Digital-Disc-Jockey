package com.djmachine.server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
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
import javax.swing.JTextArea;

import com.alee.laf.WebLookAndFeel;
import com.djmachine.library.Library;
import com.djmachine.muiscplayer.MusicPlayer;
import com.djmachine.muiscplayer.MusicPlayer.Mode;
import com.djmachine.server.threading.ServerActionHandler;
import com.djmachine.server.threading.ClientHandler;
import com.djmachine.server.widgets.ServerChatPanel;
import com.djmachine.server.widgets.DDJPrompt;
import com.djmachine.server.widgets.MusicManager;
import com.djmachine.server.widgets.UserManager;

public class Server extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final int SERVER_SIZE = 10;
	public final String TITLE = "DDJServer";
	public final String VERSION = "v0.1";
	
	private String name;
	
	private Library library;
	private ServerSocket serverSock; 
	private ArrayList<ClientHandler> clientThreads;
	private MusicPlayer musicPlayer;
	
	// Mutable components;
	private JLabel nowPlaying;
	private JTextArea chatArea;
	private JRadioButton acceptUsers;
	private JRadioButton acceptInstructions;
	
	public Server(int port, Library library) 
	{
		super("DDJ Server");
		this.setServerName("DJ the Nice Server");
		
		setupGUI();
		
		this.library = library;
		
		clientThreads = new ArrayList<ClientHandler>();
		
		musicPlayer = new MusicPlayer(library, Mode.SERVER, this);
		Thread musicThread = new Thread(musicPlayer);
		musicThread.start();
		
		try 
		{
			serverSock = new ServerSocket(port);
			System.out.println("[INFO] Server Established");
			
			while(true)
			{
				//while(acceptingUsers)
				{
					Socket clientSocket = serverSock.accept();
					ClientHandler handler = new ClientHandler(clientSocket, this, this.library, this.clientThreads);
					Thread t = new Thread(handler);
					clientThreads.add(handler);
					t.start();
					System.out.println("[SERVER] Got a new connection");
				}
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
	public void broadcast(String message)
	{
		System.out.println("Sending to clients: " + message);
		for(int i = 0; i < clientThreads.size(); i++)
		{
			clientThreads.get(i).tellClient(message);
		}
	}
	
	/**
	 * A message sent to an individual user
	 * @param userID - the ID of the user to send the message to
	 * @param text - the message to send to a single user
	 */
	public void tellUser(String user, String message) 
	{
		for(int i = 0; i < clientThreads.size(); i++)
		{
			if(clientThreads.get(i).getName().equals(user))
				clientThreads.get(i).tellClient(message);
		}
	}
	
	public void sendInstruction(String instruction)
	{
		
	}
	
	/**
	 * TODO: This needs to be done in a better way... 
	 * @param requestee - the user making the request to the music player
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
	public void addAction(String username, String action)
	{
		new Thread(new ServerActionHandler(this, username, action)).start();;
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
        acceptInstructions = new JRadioButton("<html>Accept User  <br>Instructions<html>");
        acceptInstructions.setSelected(true);
        JRadioButton declineInstructions = new JRadioButton("<html>Decline User  <br>Instructions</html>");
        ButtonGroup instructionGroup = new ButtonGroup();
        instructionGroup.add(acceptInstructions);
        instructionGroup.add(declineInstructions);
        instructionPanel.add(acceptInstructions);
        instructionPanel.add(declineInstructions);
        
        JPanel newUserPanel = new JPanel();
        newUserPanel.setLayout(new BoxLayout(newUserPanel, BoxLayout.Y_AXIS));
        acceptUsers = new JRadioButton("<html>Accept New  <br>Users</html>");
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

        chatArea = new JTextArea();
    	JTabbedPane center = new JTabbedPane();
        center.addTab("Chat Room", new ServerChatPanel(this, chatArea));
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

	public boolean verify(String user) 
	{
		if(user.equals("malware.exe"))
			return false;
		if(clientThreads.size() > SERVER_SIZE)
			return false;
		return true;
	}

	public void closeConnectionWith(String user) 
	{
		System.out.println("[WARNING] Ending session with " + user);
		for(int i = 0; i < clientThreads.size(); i++)
		{
			if(clientThreads.get(i).getName().equals(user))
				clientThreads.get(i).closeConnection();
		}
	}

	public JTextArea getChatArea()
	{
		return chatArea;
	}
}


