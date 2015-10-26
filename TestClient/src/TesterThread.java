import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.concurrent.ThreadFactory;

import static java.lang.Thread.sleep;

/**
 * Created by mereckaj on 12/10/15.
 */
public class TesterThread extends Thread {
	private int port;
	private int connectionCount;
	private OutputStreamWriter osw;
	private InputStreamReader isr;
	private BufferedReader br;
	private static final int BUFFER_SIZE = 1024;
	private static final String message = "HELO fake message here\\n";
	private static final String HOST = "188.166.36.191";
	private static final int PORT = 8000;
//	private static final String message = "KILL_SERVICE\\n";
	public TesterThread(int port, int connectionCount){
		this.port = port;
		this.connectionCount = connectionCount;
	}

	@Override
	public void run() {
		Socket s;
		Random r = new Random();
		for(int i = 0; i < connectionCount;i++){
			try {
					long n = (long)(r.nextInt()%TestClient.MAX_DELAY_BETWEEN_CONNECTIONS);
				if(n < 0){
					n = n* -1;
				}
				sleep(n);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				s = new Socket(HOST,PORT);
				osw = new OutputStreamWriter(s.getOutputStream());
				osw.write(message.toCharArray());
				osw.flush();
				isr = new InputStreamReader(s.getInputStream());

				char[] buffer = new char[BUFFER_SIZE];
				int charactersRead = isr.read(buffer,0,buffer.length);
				System.out.println(new String(buffer,0,charactersRead));
				osw.close();
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
