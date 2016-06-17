package com.djmachine.client.widgets;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.djmachine.client.Client;

@SuppressWarnings("serial")
public class ConsoleWindow extends JPanel
{
	private JTextArea chatArea;
	private boolean firstClick;
	
	public ConsoleWindow(Client client, JTextArea consoleArea)
	{
		setLayout(new BorderLayout());
		
		this.chatArea = consoleArea;
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		JScrollPane scroll = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		chatArea.setText("DDJ Command Prompt\n(check logs for more information)\n");
		
		JTextField inputBox = new JTextField();
		firstClick = true;
		inputBox.setText("DDJ Console");
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
					client.sendToServer("CONSOLE " + inputBox.getText());
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

}
