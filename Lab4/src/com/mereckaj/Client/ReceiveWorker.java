package com.mereckaj.Client;

/**
 * Created by mereckaj on 11/14/15 3:43 PM.
 */
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
public class ReceiveWorker extends Thread{

	/*

	//TODO: CHANGE TO RECEIVING

	 */
	private OutputStreamWriter osw;
	private BlockingQueue<String> messageQueue;
	private boolean running;
	public ReceiveWorker(OutputStreamWriter osw){
		this.osw = osw;
		this.messageQueue = new LinkedBlockingQueue<String>();
		this.running = true;
	}
	@Override
	public void run(){
		while(running){
			try {
				String m = messageQueue.take();
				osw.write(m,0,m.length());
				osw.flush();
			} catch (InterruptedException e) {
				//TODO: error report
				e.printStackTrace();
			} catch (IOException e) {
				//TODO: error report
				e.printStackTrace();
			}
		}
	}

	public void addMessageToQueue(String reply) {
		if (!messageQueue.add(reply)) {
			System.out.println("Failed to add message to queue");
		}
	}
}
