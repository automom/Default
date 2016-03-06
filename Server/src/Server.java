import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{
	
	private JTextField user_text;
	private JTextArea chat_window;
	private ObjectOutputStream output_stream;
	private ObjectInputStream input_stream;
	private ServerSocket server;
	private Socket connection;
	
	/*
	 * Constructor Sets up GUI and actionlistener for message sending
	 */
	public Server(){
		//Message at top
		super("Instant Messenger Socket Test");
		user_text = new JTextField();
		//You cannot type anything in the message box before
		//you are connected to anyone. Error prevention.
		user_text.setEditable(false);
		
		//Simple listener
		user_text.addActionListener(
				new ActionListener(){
					//When the user types and hits enter, this gets called
					public void actionPerformed(ActionEvent event){
						//Sends whatever message is typed in
						send_message(event.getActionCommand());
						//Clears the message box
						user_text.setText("");
						
					}
				}
		);
		//Adds everything to the GUI view Screen
		add(user_text, BorderLayout.NORTH);
		chat_window = new JTextArea();
		add(new JScrollPane(chat_window));
		setSize(300,150);
		setVisible(true);
	}
	
	
	/*
	 * Server setup
	 */
	public void start_running(){
		try{
			//6789 is port number, where clients must connect to
			//100 is "backlog", which means how many people can access 
			//    it at a time (controls floods of people)
			server = new ServerSocket(6789, 100);
			//Forever loop for connection
			while(true){
				//Connect and have conversation
				try{
					wait_for_connection();
					setup_streams();//Output and input streams
					while_chatting();//Allows us to send messages back and forth
				}
				//Not actually an error, but tells us when the connection
				//or stream ends
				catch(EOFException eofException){
					show_message("\n Server ended the connection!");
				}
				finally{
					close_stuff();//Closes down streams and sockets and does general "housekeeping"
				}
			}
		}
		catch (IOException ioException){
			//Print stack
			ioException.printStackTrace();
		}
	}
	
	/*
	 * wait for connection, then display connection info
	 */
	private void wait_for_connection() throws IOException{
		show_message(" Waiting for someone to connect... \n");
		//Waits for someone to connect every loop
		//server.accept() method blocks until a connection is made, 
		//so no empty sockets are made
		connection = server.accept();
		// Gets IP address and then display host name of connection
		show_message(" Now connected to " + connection.getInetAddress().getHostName());
		
	}
	
	/*
	 * Sets up the Streams, allow streams to send and receive data
	 */
	private void setup_streams() throws IOException{
		//Pipes the "connection" socket to the output stream
		output_stream = new ObjectOutputStream(connection.getOutputStream());
		output_stream.flush();//Prevents data from being leftover in buffer
		//Pipes the "connection" socket to the input stream
		input_stream = new ObjectInputStream(connection.getInputStream());
		//* you cannot flush input because they pipe to you
		//They have to flush to you
		show_message("\n Streams are set up \n");
				
	}
	
	/*
	 * During chat conversation
	 */
	private void while_chatting() throws IOException{
		String message = " You are now connected. ";
		send_message(message);
		able_to_type(true);//User can type now
		
		//Have a conversation
		do{
			try{
				//Reads the input stream from the socket and reads what they sent
				message = (String) input_stream.readObject();
				show_message( "\n" + message);
			}
			catch(ClassNotFoundException classNotFoundException){
				show_message(" \n user did not send a string or something");
			}
		}while(!message.equals("Client: END"));//While both sides are open for conversation END ends
	}
	
	/*
	 * Close streams and sockets after chat ends
	 */
	private void close_stuff() {
		show_message("\n Closing connections... \n");
		able_to_type(false);//Prevents user from typing after conversation ends
		
		try{
			output_stream.close();//closes output stream
			input_stream.close();//closes input stream
			connection.close();//closes main "connection" socket
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	/*
	 * Sends message to client
	 */
	private void send_message(String message){
		try{
			//Creates an object and send it to output stream, to whoever you 
			output_stream.writeObject("Server: " + message);
			output_stream.flush();
			show_message("\nServer: " + message); // Show it on the message screen
		}
		catch(IOException ioException){
			//Sticks it into the chat window so you can see the error
			chat_window.append("\n ERROR: cannot send message");
		}
	}
	
	/*
	 * updates chat window
	 */
	private void show_message(final String text){
		//Updates the chat window instead of rebuilding it
		//Starts a Thread that will update the GUI
		SwingUtilities.invokeLater(
			//Runnable updates GUI
			new Runnable (){
				public void run(){
					chat_window.append(text);// adds it to the end of the window
				}
			}
				
		);
	}
	
	/*
	 * Allows the user to type text into their own text box
	 * If able is true, the user can type into the box, and vice versa
	 */
	private void able_to_type(final boolean able){
		//Updates the GUI to allow or not allow user to type
		SwingUtilities.invokeLater(
				//Runnable updates GUI
				new Runnable (){
					public void run(){
						user_text.setEditable(able);//allows or disallows user to type in the user text area
					}
				}
					
			);
	}
}
