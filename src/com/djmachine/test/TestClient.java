package com.djmachine.test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class TestClient 
{
	JTextArea incoming; 
	JTextField outgoing;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	
	public static void main(String[] args)
	{
		TestClient client = new TestClient();
		client.go();
	}
	
	public void go()
	{
		JFrame frame = new JFrame("Chat Client");
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15, 50);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					writer.println(outgoing.getText());
					writer.flush();
				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
				outgoing.setText("");
				outgoing.requestFocus();
			}
		});;
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		setUpNetworking();
		
		Thread readerThread = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				String message;
				try
				{
					while((message = reader.readLine()) != null)
					{
						System.out.println("read " + message);
						incoming.append(message + "\n");
					}
				}
				catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}			
		});
		readerThread.start();
		
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(400, 500);
		frame.setVisible(true);
	}
	
	private void setUpNetworking()
	{
		try
		{
			sock = new Socket("localhost", 8989);
			reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			writer = new PrintWriter(sock.getOutputStream());
			System.out.println("Networking Established");
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
}
