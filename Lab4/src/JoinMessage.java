/**
 * Created by mereckaj on 11/7/15.
 */
public class JoinMessage extends Message {
	public JoinMessage(Client sender, Group receiver) {
		super(sender, receiver);
	}

	@Override
	public String toString() {
		String result = new String();
		result += "JOIN_CHATROOM: " + this.getReceiver().toString() + "\n";
		result += "CLIENT_IP: 0\n";
		result += "PORT: 0\n";
		result += "CLIENT_NAME: " + this.getSender().toString() +"\n";
		return result;
	}
}
