import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by mereckaj on 11/7/15.
 */
public class Client {
	private String client_name;
	private Socket socket;
	private Queue<Message> messageQueue;
	public Client(String name){
		client_name = name;
		messageQueue = new LinkedList<Message>();
	}
	@Override
	public String toString(){
		if(client_name==null){
			return "";
		}else{
			return client_name;
		}
	}
}
