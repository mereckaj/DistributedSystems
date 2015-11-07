/**
 * Created by mereckaj on 11/7/15.
 */
public class ChatServer {
	public static void main(String[] args){
		Client c = new Client("TestClient1");
		Group g = new Group("TestGroup1");
		JoinMessage jm = new JoinMessage(c,g);
		System.out.println(jm.toString());
	}
}
