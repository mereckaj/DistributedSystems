package com.mereckaj.ChatServer;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

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
				// Reply to join (Don't care; drop the message)
			}else if(mLines[0].contains("ERROR_CODE:")){
				// Error message (Should only come from the server, print to stderr just in case)
				System.err.println(m);
			}else if(mLines[0].contains("LEAVE_CHATROOM:")){
				// Leave message
				int roomRef = Integer.parseInt(mLines[0].substring(mLines[0].indexOf(":")));
				int memeberRef = Integer.parseInt(mLines[1].substring(mLines[1].indexOf(":")));
				String memberName = mLines[2].substring(mLines[2].indexOf(":"));
				removeClientFromChannel(roomRef,memeberRef,memberName);
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

	private void removeClientFromChannel(int roomRef, int memeberRef, String memberName) {
//		ServerMain.server.channelMembersByName.get(roomRef);
	}

	private void joinClientToChannel(String clientName, String channelToJoin) {
		String refs = addToChannel(clientName,channelToJoin);
		if(refs==null){
			System.out.println("User already exists");
			createError(ErrorReporter.USERNAME_ALREADY_IN_USE_C,ErrorReporter.USERNAME_ALREADY_IN_USE_S);
			return;
		}
		int memberRef = new Integer(refs.substring(0,refs.indexOf(":")));
		int channelRef = new Integer(refs.substring(refs.indexOf(":")+1));
		createReply(channelRef,memberRef,channelToJoin);
	}
	private void createError(int code, String message){
		String reply = "ERROR_CODE: " + code +"\n"
				+ "ERROR_DESCRIPTION: " + message + "\n";
		addToSendQueue(reply);
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
		System.out.println("Added to queue::\n" + reply);
		sendQueueWorkerThread.addMessageToQueue(reply);
	}

	private String addToChannel(String clientName, String channelToJoin) {
		int clientRef = getClientRefIfExistElseCreate(clientName);
		int channelRef = getChannelRefIfExistElseCreate(channelToJoin);
		if(userExistsInChannel(channelRef,clientRef)){
			System.out.println("User already in channel");
			createError(ErrorReporter.USER_ALREADY_IN_GROUP_C,ErrorReporter.USER_ALREADY_IN_GROUP_S);
		}else{
			//TODO: Add to channel
			addUserToChannel(channelRef,clientRef);
		}
		return clientRef+":"+channelRef;
	}

	private void addUserToChannel(int channelRef, int clientRef) {
		Server s = ServerMain.server;
		if(!s.channelMembers.containsKey(channelRef)) {
			s.channelMembers.put(channelRef, new ConcurrentHashMap<Integer, Socket>());
		}
		s.channelMembers.get(channelRef).put(clientRef,socket);
	}

	private boolean userExistsInChannel(int channelRef, int clientRef) {
		System.out.println("Checking if " + clientRef + " is in " + channelRef);
		Server s = ServerMain.server;
		if(s.channelMembers.containsKey(channelRef)){
			boolean found = s.channelMembers.get(channelRef).containsKey(clientRef);
			System.out.println("User: " + clientRef + " in: " + channelRef + " was " + (found==true?"found":"not found"));
			return found;
		}
		System.out.println("No channel with ref " + channelRef);
		return false;
	}

	private int getChannelRefIfExistElseCreate(String channelToJoin) {
		Server s = ServerMain.server;
		if(s.channelTableByName.containsKey(channelToJoin)){
			int ref = s.channelTableByName.get(channelToJoin);
			System.out.println("Found Channel: "+channelToJoin+" with ref: " + ref);
			return ref;
		}else{
			int ref = UniqueRefGenerator.nextChannelRef();
			System.out.println("Non Channel: "+channelToJoin+". Created with ref: " + ref);
			s.channelTableByName.put(channelToJoin,ref);
			s.channelTableByRef.put(ref,channelToJoin);
			s.channelMembers.put(ref,new ConcurrentHashMap<Integer, Socket>());
			return ref;
		}
	}

	private int getClientRefIfExistElseCreate(String clientName) {
		Server s = ServerMain.server;
		if(s.memberTableByName.containsKey(clientName)){
			int ref = s.memberTableByName.get(clientName);
			System.out.println("Found User: "+clientName + " with ref: " + ref);
			return ref;
		}else{
			int ref = UniqueRefGenerator.nextClientRef();
			System.out.println("Non User: "+clientName + ". Created with ref: " + ref);
			s.memberTableByName.put(clientName,ref);
			s.memberTableByRef.put(ref,clientName);
			return ref;
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
