import javax.swing.JFrame;

public class ClientTest {
	public static void main(String[] args){
		Client not_rob;
		not_rob = new Client("127.0.0.1");
		not_rob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Closes when you click the x
		not_rob.start_running();
	}
}
