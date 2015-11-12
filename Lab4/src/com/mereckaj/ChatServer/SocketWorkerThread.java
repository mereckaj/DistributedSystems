package com.mereckaj.ChatServer;

import com.mereckaj.Shared.Messages.Message;
import com.mereckaj.Shared.Messages.MessageError;
import com.mereckaj.Shared.Messages.MessageJoin;
import com.mereckaj.Shared.Messages.MessageJoinSuccess;

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
			Message m = readMessage();
			dealWithMessage(m);
//		}
	}

	private Message readMessage() {
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
		return parseStringToMessageObject(new String(result));
	}

	private Message parseStringToMessageObject(String s) {
		Message result = null;
		if(s.contains("JOIN_CHATROOM: ")){
			result = new MessageJoin();
			result.fromString(s);
		}
	}

	private void dealWithMessage(Message m) {
		if(m instanceof MessageJoin){
			//TODO: Join user to channel
			addUserToGroup((MessageJoin) m);
			sendJoinReply((MessageJoin) m);
		}else if(m instanceof MessageError){
			sendErrorReply(m);
		}else {
			MessageError me = new MessageError("Cant determine message type: " + m,MessageError.UNDETERMINED_MESSAGE_RECEIVED,ServerMain.server.getServerClient());
			sendErrorReply(me);
		}
	}

	private void addUserToGroup(MessageJoin m) {
		//TODO: Add user to group
	}

	private void sendErrorReply(Message m) {
		//TODO: Add to sending queue
	}


	private void sendJoinReply(MessageJoin mj) {
		MessageJoinSuccess mjs = new MessageJoinSuccess(mj,socket.getLocalAddress().toString().substring(1));
		//TODO: Add to sending quque
	}
}
