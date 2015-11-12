import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by mereckaj on 11/7/15.
 */
public class Client {
	private String name;
	private Socket socket;
	private BlockingQueue<Message> messageQueue;
	public Client(String name){
		this.name = name;
		messageQueue = new LinkedBlockingQueue<Message>();
	}
	@Override
	public String toString(){
		if(name ==null){
			return "";
		}else{
			return name;
		}
	}
	public void addMessageToQueue(Message m){
		messageQueue.add(m);
		System.out.println("Added message to " + this.toString() +"'s queue: " + m.toString());
	}
	public BlockingQueue<Message> getMessageQueue(){
		return messageQueue;
	}
}
