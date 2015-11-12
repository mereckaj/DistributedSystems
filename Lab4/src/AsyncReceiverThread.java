import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;

/**
 * Created by mereckaj on 11/12/15.
 */
public class AsyncReceiverThread extends Thread {
	public static int READ_BUFFER_SIZE = 4069;
	private InputStream is;
	private ObjectInputStream ois;
	private Client c;
	private boolean running;
	public AsyncReceiverThread(InputStream is,Client c){
		this.is=is;
		this.c = c;
		try {
			this.ois = new ObjectInputStream(is);
		} catch (IOException e) {
			//TODO: put to error queue
			e.printStackTrace();
		}
	}
	public void run(){
		while(running){
			try {
				Message m = (Message) ois.readObject();
				addToQueue(m);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void addToQueue(Message m) {
		c.addMessageToQueue(m);
	}

	public void terminate(){
		try {
			ois.close();
			running = false;
			Thread.currentThread().join();
		} catch (IOException e) {
			//TODO: put to error queue
			e.printStackTrace();
		} catch (InterruptedException e) {
			//TODO: put to error queue
			e.printStackTrace();
		}
	}
}
