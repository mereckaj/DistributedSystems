package com.mereckaj.ChatServer;

import com.mereckaj.Shared.Messages.Message;

import java.io.*;
import java.net.Socket;

/**
 * Created by mereckaj on 11/12/15.
 */
public class SocketWorkerThread implements Runnable {
	public static final int RECEIVE_BUFFER_SIZE = 4096;
	private Socket socket;
	private InputStreamReader isr;
	private OutputStreamWriter osw;
	public SocketWorkerThread(Socket socket) {
		System.out.println("Accepted new connection");
		this.socket = socket;
		try {
			isr = new InputStreamReader(socket.getInputStream());
			osw = new OutputStreamWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
//		while(true){
			String m = readMessage();
			dealWithMessage(m);
//		}
	}

	private void dealWithMessage(String m) {

	}

	private String readMessage() {
		char[] buffer = new char[RECEIVE_BUFFER_SIZE];
		char[] result;
		int read = 0;
		try {
			read = isr.read(buffer,0,0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		result = new char[read];
		System.arraycopy(buffer,0,result,0,read);
		return new String(result);
	}
}
