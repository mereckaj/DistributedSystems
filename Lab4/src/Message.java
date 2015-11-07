/**
 * Created by mereckaj on 11/7/15.
 */
public abstract class Message {
	private Client sender;
	private Group receiver;
	public Message(Client sender, Group receiver){
		this.sender = sender;
		this.receiver = receiver;
	}
	public abstract String toString();
	public Client getSender(){
		return this.sender;
	}
	public Group getReceiver(){
		return this.receiver;
	}
}
