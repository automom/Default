import javax.swing.JFrame;

public class ServerTest {
	public static void main(String[] args){
		Server rob = new Server();
		rob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Closes when you hit the x
		rob.start_running();
	}
}
