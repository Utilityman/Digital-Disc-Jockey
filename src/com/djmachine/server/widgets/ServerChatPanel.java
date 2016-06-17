package com.djmachine.server.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.djmachine.server.Server;

@SuppressWarnings("serial")
public class ServerChatPanel extends JPanel
{	
	private JTextArea chatArea;
	private boolean firstClick;
	
	public ServerChatPanel(Server server, JTextArea chatArea)
	{
		setLayout(new BorderLayout());
		
		this.chatArea = chatArea;
		this.chatArea.setEditable(false);
		this.chatArea.setLineWrap(true);
		this.chatArea.append(server.TITLE + "@" + "localhost" + " (" + server.VERSION + ")\nType !help for help\n");
		JScrollPane scroll = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		JTextField inputBox = new JTextField();
		firstClick = true;
		inputBox.setText("Type to send a message!");
		inputBox.setForeground(Color.GRAY);
		
	
		inputBox.addKeyListener(new KeyListener()
		{
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) 
			{
				if(e.getKeyCode() == KeyEvent.VK_ENTER && !inputBox.getText().equals(""))
				{
					if(inputBox.getText().charAt(0) == '!')
						handleCommand(inputBox.getText());
					else
					{
						chatArea.append(server.getServerName() + ": " + inputBox.getText() + "\n");
						server.broadcast("CHAT " + server.getServerName() + ": "+ inputBox.getText());
					}
					inputBox.setText("");

				}
			}
		});
		inputBox.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if(firstClick)
				{
					firstClick = false;
					inputBox.setText("");
					inputBox.setForeground(Color.BLACK);
				}
			}
			
		});
		
		add(scroll, BorderLayout.CENTER);
		add(inputBox, BorderLayout.SOUTH);
	}

	private void handleCommand(String text) 
	{
		if(text.equals("!help"))
			chatArea.append("Options are - \n"
					+ "!clear - clear the message board\n"
					+ "!save - copy the chatroom text to your clipboard \n"
					+ "!help display the help message\n");
		if(text.equals("!clear"))
			chatArea.setText("Board Cleared!");
		if(text.equals("!save"))
		{
			StringSelection selection = new StringSelection(chatArea.getText());
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(selection, selection);
			chatArea.append("Saved to clipboard!");
		}
	}
}
