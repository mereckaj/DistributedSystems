import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by mereckaj on 11/7/15.
 */
public class ChatWorker implements Runnable {
	private Socket socket;
	private boolean running;
	private ObjectOutputStream oos;
	private String threadInfo;
	private Client c;
	public  ChatWorker(Socket s){
		threadInfo = Thread.currentThread().getName();
		socket = s;
		c = new Client("Tester");
		running = true;
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
			System.err.println("[" + threadInfo + "]" + "Can't set up StreamReaders, it's fucked!");
		}
	}

	@Override
	public void run() {
		Message msg;
		System.out.println("Start");
		try {
			new AsyncReceiverThread(socket.getInputStream(),c).start();
			System.out.println("Async running");
			while(running){
				System.out.println("Waiting for object");
				msg =c.getMessageQueue().take();
				System.out.println("Writing object");
				oos.writeObject(msg);
			}
		} catch (IOException e) {
			//TODO: error to queue
			e.printStackTrace();
		} catch (InterruptedException e) {
			//TODO: error to queue
			e.printStackTrace();
		}
	}
}
