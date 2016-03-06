package SocketWork;
import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.Event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class Server extends JFrame{
	private JTextField textField;
	private JTextArea textArea;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private ServerSocket server;
	private Socket socket;
	
	//constructor, builds GUI window with textbox
	public Server(){
		super("Window");
		textField = new JTextField();
		textField.setEditable(false);
		textField.addActionListener(
			new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					//sendMessage(e.getActionCommand());
					textField.setText("");
					
				}
			}
		);
		this.add(textField, BorderLayout.NORTH);
		textArea = new JTextArea();
		this.add(new JScrollPane(textArea));
		this.setSize(300,150);
		this.setVisible(true);
		
	}
	
	//begin chat room
	public void startRunningP(){
		try{
			server = new ServerSocket(0000,100); //100 is number of people who can connect to the port
			while(true){
				try{
					waitForConnection();
					setupStreams();
					whileChatting();
				}catch(EOFException eofException){
					showMessage("\nServer ended the connection!");
				}finally{
					closeCrap();
				}
			}
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	//waiting for connection, displays information
	public void waitForConnection() throws IOException{
		showMessage(" Waiting for someone to connect...\n ");
		socket = server.accept();
		showMessage(" Now connected to " + socket.getInetAddress().getHostName()));
		
	}
	
	private void setupStreams() throws IOException{
		
	}
}
