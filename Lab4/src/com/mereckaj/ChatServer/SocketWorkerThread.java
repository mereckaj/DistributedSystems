package com.mereckaj.ChatServer;

import com.mereckaj.Shared.Messages.Message;
import com.mereckaj.Shared.Messages.MessageError;
import com.mereckaj.Shared.Messages.MessageJoin;
import com.mereckaj.Shared.Messages.MessageJoinSuccess;

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
		while(true){
			try {
				Message m = (Message) ois.readObject();
				dealWithMessage(m);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private void dealWithMessage(Message m) {
		if(m instanceof MessageJoin){
			//TODO: Join user to channel
			sendJoinReply((MessageJoin) m);
		}else if(m instanceof MessageError){
			sendErrorReply(m);
		}else {
			MessageError me = new MessageError("Cant determine message type: " + m,MessageError.UNDETERMINED_MESSAGE_RECEIVED,ServerMain.server.getServerClient());
			sendErrorReply(me);
		}
	}

	private void sendErrorReply(Message m) {
		//TODO: Add to sending queue
	}


	private void sendJoinReply(MessageJoin mj) {
		MessageJoinSuccess mjs = new MessageJoinSuccess(mj,socket.getLocalAddress().toString().substring(1));
		//TODO: Add to sending quque
	}
}
