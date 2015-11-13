package com.mereckaj.ChatServer;

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
	private MessageQueueWorkerThread sendQueueWorkerThread;
	public SocketWorkerThread(Socket socket) {
		this.socket = socket;
		try {
			isr = new InputStreamReader(socket.getInputStream());
			osw = new OutputStreamWriter(socket.getOutputStream());
			sendQueueWorkerThread = new MessageQueueWorkerThread(osw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		System.out.println("Starting send queue worker");
		sendQueueWorkerThread.start();
		System.out.println("send queue worker running");
//		while(true){
		String m = readMessage();
		dealWithMessage(m);
//		}
	}

	private void dealWithMessage(String m) {
		String[] mLines = m.split("\n");
		if(mLines.length < 1){
			System.out.println("What is this message?: " + m);
		}else{
			System.out.println("Message has : " + mLines.length +" lines");
			if(mLines[0].contains("JOIN_CHATROOM:")){
				// Join message
				String channelToJoin = mLines[0].substring(mLines[0].indexOf(":")+2);
				String clientName = mLines[3].substring(mLines[3].indexOf(":")+2);
				joinClientToChannel(clientName,channelToJoin);
			}else if(mLines[0].contains("JOINED_CHATROOM:")){
				// Reply to join
			}else if(mLines[0].contains("ERROR_CODE:")){
				// Error message
			}else if(mLines[0].contains("LEAVE_CHATROOM:")){
				// Leave message
			}else if(mLines[0].contains("DISCONNECT:")){
				// Disconnect message
			}else if(mLines[0].contains("CHAT: ")){
				// Message message
			}else{
				System.out.println("Bad msg");
				// IDK message
			}
		}

	}

	private void joinClientToChannel(String clientName, String channelToJoin) {
		String refs = addToChannel(clientName,channelToJoin);
		int roomref = new Integer(refs.substring(0,refs.indexOf(":")));
		int joinref = new Integer(refs.substring(refs.indexOf(":")+1));
		createReply(roomref,joinref,channelToJoin);
	}

	private void createReply(int roomref, int joinref, String channelName) {
		String reply = "JOINED_CHATROOM: " + channelName +"\n"
				+ "SERVER_IP: " + socket.getLocalAddress().toString().substring(1) + "\n"
				+ "PORT: " + socket.getPort() + "\n"
				+ "ROOM_REF: " + roomref + "\n"
				+ "JOIN_ID: " + joinref + "\n";
		addToSendQueue(reply);
	}

	private void addToSendQueue(String reply) {
		sendQueueWorkerThread.addMessageToQueue(reply);
	}

	private String addToChannel(String clientName, String channelToJoin) {
		int clientRef = getClientRefIfNotExist(clientName);
		if(clientRef==-1){
			//TODO: fucked user already exists
		}
		int channelRef = getChannelRefIfExistElseCreate(channelToJoin);
		return "0:1";
	}

	private int getChannelRefIfExistElseCreate(String channelToJoin) {
		if(ServerMain.server.channelList.containsKey(channelToJoin)){
			return ServerMain.server.channelList.get(channelToJoin);
		}else{
			int newChannelRef = UniqueRefGenerator.nextChannelRef();
			ServerMain.server.channelList.put(channelToJoin,newChannelRef);
			return newChannelRef;
		}
	}

	private int getClientRefIfNotExist(String clientName) {
		if(ServerMain.server.memberRef.containsKey(clientName)) {
			// User already exists with that name
			return -1;
		}else{
			int newMemberRef = UniqueRefGenerator.nextClientRef();
			ServerMain.server.memberRef.put(clientName,newMemberRef);
			return newMemberRef;
		}
	}

	private String readMessage() {
		char[] buffer = new char[RECEIVE_BUFFER_SIZE];
		char[] result;
		int read = 0;
		try {
			read = isr.read(buffer,0,buffer.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		result = new char[read];
		System.arraycopy(buffer,0,result,0,read);
		return new String(result);
	}
}
