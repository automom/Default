import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	
	private JTextField user_text;
	private JTextArea chat_window;
	private ObjectOutputStream output_stream;
	private ObjectInputStream input_stream;
	private String message = "";
	private String serverIP;
	private Socket connection;
	
	/*
	 * Constructor
	 * Will not allow others to connect to you for safety
	 * But will allow you to connect to a server
	 */
	public Client(String host){
		super("Client Part");
		serverIP = host;
		user_text = new JTextField();
		user_text.setEditable(false);
		user_text.addActionListener(
			new ActionListener(){
				public void actionPerformed (ActionEvent event){
					send_message(event.getActionCommand());
					user_text.setText("");
				}
			}
				
		);
		add (user_text, BorderLayout.NORTH);
		chat_window = new JTextArea();
		add (new JScrollPane (chat_window), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	
	public void start_running(){
		try{
			connect_to_server();
			setup_streams();//Output and input streams
			while_chatting();//Allows us to send messages back and forth
		}
		//Not actually an error, but tells us when the connection
		//or stream ends
		catch(EOFException eofException){
			show_message("\n Client ended connection!");
		}
		catch (IOException ioException){
			//Print stack
			ioException.printStackTrace();
		}
		finally{
			close_stuff();//Closes down streams and sockets and does general "housekeeping"
		}
	}
	
	/*
	 * Connect to the server
	 */
	private void connect_to_server() throws IOException{
		show_message("Attempting connection... \n");
		connection = new Socket(InetAddress.getByName(serverIP),6789);
		show_message("Connected to: " + connection.getInetAddress().getHostName());
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
		show_message("\n Streams are set up and ready to go \n");			
	}
	

	/*
	 * During chat conversation
	 */
	private void while_chatting() throws IOException{
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
		}while(!message.equals("Server: END"));//While both sides are open for conversation END ends
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
			output_stream.writeObject("Client: " + message);
			output_stream.flush();
			show_message("\nClient: " + message); // Show it on the message screen
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
