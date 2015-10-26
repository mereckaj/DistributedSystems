import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by mereckaj on 11/10/15.
 */
public class Main {
	public static int PORT;
	public static final int BUFFER_SIZE = 1024;
	public static final String SERVICE_MESSAGE_TERMINATE = "KILL_SERVICE\n";
	public static final String SERVICE_MESSAGE_HELO = "HELO";
	public static Server s;
	public static void main(String[] args){
		if(args.length<1){
			System.out.println("Usage: <program> <port>");
		}else{
			parseArguments(args);
		}
		s = new Server(PORT);
		s.run();
	}
	private static void parseArguments(String[] args) {
		PORT = new Integer(args[0]);
		System.out.println("New port is: " + PORT);
	}
}
