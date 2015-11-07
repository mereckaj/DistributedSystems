import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by mereckaj on 11/7/15.
 */
public class Group {
	private String groupName;
	private int clientCount;
	private Client[] users;
	private Queue<Message> messageQueue;
	public Group(String name){
		groupName = name;
		messageQueue = new LinkedList<Message>();
	}
	@Override
	public String toString(){
		if(groupName==null){
			return "";
		}else{
			return groupName;
		}
	}
}
