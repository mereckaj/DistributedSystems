package com.mereckaj.ChatServer;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by mereckaj on 11/13/15 12:24 PM.
 */
public class MessageQueueWorkerThread extends Thread {
	public OutputStreamWriter osw;
	public BlockingQueue<String> messageQueue;
	public boolean running;
	public MessageQueueWorkerThread(OutputStreamWriter osw){
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
		System.out.println(">>Sending->" + reply);
		if(!messageQueue.add(reply)){
			System.out.println("Failed to add message to queue");
		}
	}
}
