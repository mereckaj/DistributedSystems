import com.sun.org.apache.bcel.internal.generic.NEW;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by mereckaj on 11/10/15.
 */
public class SocketWorkerThread implements Runnable {
	private Socket socket;
	private static final String NEW_LINE = "\\n";
	private static final String STUDENT_ID_TOKEN = "48ffb53659413c0ee24b09bffed47b329f7b5ac80c23d908e952e328814dfb49";
	private InputStreamReader isr;
	private OutputStreamWriter osw;
	char[] buffer;
	public SocketWorkerThread(Socket s){
		this.socket = s;
	}

	@Override
	public void run() {
		try {
			isr = new InputStreamReader(socket.getInputStream());
			osw = new OutputStreamWriter(socket.getOutputStream());
			// Make sure the buffer is clean;
			buffer = new char[Main.BUFFER_SIZE];
			int charactersRead = isr.read(buffer,0,buffer.length);
			doAction(new String(buffer,0,charactersRead));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void doAction(String msg){
		if(msg.contains(Main.SERVICE_MESSAGE_HELO) && msg.contains("\\n")){
			doRespondToHelo(msg);
		}else if(msg.equals(Main.SERVICE_MESSAGE_TERMINATE)){
			doTerminate();
		}else{
			System.out.println("Error");
		}
	}
	private void doTerminate(){
		Main.s.terminate();
	}

	private void doRespondToHelo(String msg){
		String text = msg.substring(Main.SERVICE_MESSAGE_HELO.length()+1,msg.length()-2);
		String repsponse = "HELO " + text + NEW_LINE +
				"IP:" + socket.getInetAddress() + NEW_LINE +
				"Port:" + Main.PORT + NEW_LINE +
				"StudentID:" + STUDENT_ID_TOKEN + NEW_LINE;
		try {
			osw.write(repsponse,0,repsponse.length());
			osw.flush();
			isr.close();
			osw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}