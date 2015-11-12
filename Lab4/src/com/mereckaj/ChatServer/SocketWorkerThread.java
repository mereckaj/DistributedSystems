package com.mereckaj.ChatServer;

import com.mereckaj.Shared.Messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by mereckaj on 11/12/15.
 */
public class SocketWorkerThread implements Runnable {
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	public SocketWorkerThread(Socket socket) {
		System.out.println("Accepted new connection");
		this.socket = socket;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
//		while(true){
			try {
				Message m = (Message) ois.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
//		}
	}
}
