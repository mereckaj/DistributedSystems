package com.mereckaj.ChatServer;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class SocketWorkerThread implements Runnable {
	public static final int RECEIVE_BUFFER_SIZE = 4096;
	private Socket socket;
	private InputStreamReader isr;
	private OutputStreamWriter osw;
	private MessageQueueWorkerThread sendQueueWorkerThread;
	private static final String STUDENT_ID_TOKEN = "48ffb53659413c0ee24b09bffed47b329f7b5ac80c23d908e952e328814dfb49";
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
//		System.out.println("Starting send queue worker");
		sendQueueWorkerThread.start();
//		System.out.println("send queue worker running");
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
			if(mLines[0].contains("HELO")){
				System.out.println("Helo->" + m);
				String text = mLines[0].substring("HELO".length()+1);
				String response = "HELO " + text + "\n" +
						"IP:" + socket.getLocalAddress().toString().substring(1) + "\n" +
						"Port:" + ServerMain.PORT + "\n"+
						"StudentID:" + STUDENT_ID_TOKEN + "\n";
				addToSendQueue(response);
			}else if(mLines[0].contains("KILL_SERVICE")){
				System.out.println("Kill->" + m);
				ServerMain.server.terminate();
			}else if(mLines[0].contains("JOIN_CHATROOM:")){
				// Join message
				System.out.println("Join->" + m);
				String channelToJoin = mLines[0].substring(mLines[0].indexOf(":")+1);
				String clientName = mLines[3].substring(mLines[3].indexOf(":")+1);
				joinClientToChannel(clientName,channelToJoin);
			}else if(mLines[0].contains("JOINED_CHATROOM:")){
				System.out.println("Joined->"+m);
				// Reply to join (Don't care; drop the message)
			}else if(mLines[0].contains("ERROR_CODE:")){
				System.out.println("Error->"+m);
				// Error message (Should only come from the server, print to stderr just in case)
				System.err.println(m);
			}else if(mLines[0].contains("LEAVE_CHATROOM:")){
				// Leave message
				System.out.println("Leave->"+m);
				int roomRef = Integer.parseInt(mLines[0].substring(mLines[0].indexOf(":")+1));
				int memberRef = Integer.parseInt(mLines[1].substring(mLines[1].indexOf(":")+1).trim());
				String memberName = mLines[2].substring(mLines[2].indexOf(":"));
				removeClientFromChannel(roomRef,memberRef,memberName);
			}else if(mLines[0].contains("DISCONNECT:")){
				// Disconnect message
				System.out.println("Disconnect->"+m);
				String memberName = mLines[2].substring(mLines[2].indexOf(":"));
				disconnectUser(memberName);
			}else if(mLines[0].contains("CHAT:")){
				// Message message
				System.out.println("Message->"+m);
				int roomRef = Integer.parseInt(mLines[0].substring(mLines[0].indexOf(":")+1));
				int memberRef = Integer.parseInt(mLines[1].substring(mLines[1].indexOf(":")+1).trim());
				String clientName = mLines[2].substring(mLines[2].indexOf(":")+1);
				String message = mLines[3].substring(mLines[3].indexOf(":")+1);
				sendMessageToGroup(roomRef,memberRef,clientName,message);
//				System.out.println("Message from: " + clientName +"{"+memberRef+"} to: " + roomRef + ": " + message);

			}else if(mLines[0].contains("JOIN_BROADCAST:")){
				System.out.println("join broadcast received->"+m);
			}else {
				System.out.println("Bad msg" + m);
				// IDK message
			}
		}

	}

	private void disconnectUser(String memberName) {
//		Server s = ServerMain.server;
//		int ref;
//		if(s.memberTableByName.containsKey(memberName)){
//			ref = s.memberTableByName.get(memberName);
//		}else{
//			System.out.println("User not found, closign this conenction");
			this.terminate();
//		}
	}

	private void sendMessageToGroup(int roomRef, int memberRef, String clientName, String message) {
		Server s = ServerMain.server;
		if(s.channelMembers.containsKey(roomRef)){
			if(s.channelMembers.get(roomRef).containsKey(memberRef)){
				broadcast(roomRef,"CHAT:" + roomRef +"\nJOIN_ID:" + memberRef +"\nCLIENT_NAME:"+clientName+"\nMESSAGE:"+ message+ "\n\n");
			}else{
				createError(ErrorReporter.USER_NOT_IN_GROUP_C,ErrorReporter.USER_NOT_IN_GROUP_S);
			}
		}else{
			createError(ErrorReporter.GROUP_NOT_EXIST_C,ErrorReporter.GROUP_NOT_EXIST_S);
		}
	}

	private void removeClientFromChannel(int roomRef, int memberRef, String memberName) {
		//TODO: not leaving
		Server s = ServerMain.server;
		if(s.channelMembers.containsKey(roomRef)){
			if(s.channelMembers.get(roomRef).containsKey(memberRef)){
				s.channelMembers.get(roomRef).remove(memberRef);
				sendLeaveReply(roomRef,memberRef);
			}else{
				createError(ErrorReporter.USER_NOT_IN_GROUP_C,ErrorReporter.USER_NOT_IN_GROUP_S);
			}
		}else{
			createError(ErrorReporter.GROUP_NOT_EXIST_C,ErrorReporter.GROUP_NOT_EXIST_S);
		}
	}

	private void sendLeaveReply(int roomRef, int memeberRef) {
		String reply = "LEFT_CHATROOM:" + roomRef + "\n"
				+ "JOIN_ID:" + memeberRef;
		addToSendQueue(reply);
	}

	private void joinClientToChannel(String clientName, String channelToJoin) {
		String refs = addToChannel(clientName,channelToJoin);
		if(refs==null){
			return;
		}
		int memberRef = new Integer(refs.substring(0,refs.indexOf(":")));
		int roomRef = new Integer(refs.substring(refs.indexOf(":")+1));
		sendJoinReply(roomRef,memberRef,channelToJoin);
		broadcast(roomRef,"CHAT:" + roomRef +"\n" +"CLIENT_NAME:"+clientName+"\nMESSAGE:" +clientName+" has joined this chatroom." + "\n\n");
	}

	private String lookupMemberNameByRef(int memberRef) {
		Server s = ServerMain.server;
		if(s.memberTableByRef.containsKey(memberRef)){
			return s.memberTableByRef.get(memberRef);
		}else{
			//TODO: error instead of returning shit value
			return "ChangeToErrorReport";
		}
	}

	private void broadcast(int channelRef, String m){
		ServerMain.server.broadcast(channelRef,m);
	}

	private void createError(int code, String message){
		String reply = "ERROR_CODE:" + code +"\n"
				+ "ERROR_DESCRIPTION:" + message + "\n";
		addToSendQueue(reply);
	}
	private void sendJoinReply(int roomref, int joinref, String channelName) {
		String reply = "JOINED_CHATROOM:" + channelName +"\n"
				+ "SERVER_IP:" + socket.getLocalAddress().toString().substring(1) + "\n"
				+ "PORT:" + "0" + "\n"
				+ "ROOM_REF:" + roomref + "\n"
				+ "JOIN_ID:" + joinref + "\n";
		addToSendQueue(reply);
	}

	public void addToSendQueue(String reply) {
		System.out.println("Added to queue::\n" + reply);
		sendQueueWorkerThread.addMessageToQueue(reply);
	}

	private String addToChannel(String clientName, String channelToJoin) {
		int clientRef = getClientRefIfExistElseCreate(clientName);
		int channelRef = getChannelRefIfExistElseCreate(channelToJoin);
		if(userExistsInChannel(channelRef,clientRef)){
//			System.out.println("User already in channel");
			createError(ErrorReporter.USER_ALREADY_IN_GROUP_C,ErrorReporter.USER_ALREADY_IN_GROUP_S);
			return null;
		}else{
			addUserToChannel(channelRef,clientRef);
		}
		return clientRef+":"+channelRef;
	}

	private void addUserToChannel(int channelRef, int clientRef) {
		Server s = ServerMain.server;
		if(!s.channelMembers.containsKey(channelRef)) {
			s.channelMembers.put(channelRef, new ConcurrentHashMap<Integer, SocketWorkerThread>());
		}
		s.channelMembers.get(channelRef).put(clientRef,this);
//		System.out.println("Added to channel. Size now: " + s.channelMembers.size());
	}

	private boolean userExistsInChannel(int channelRef, int clientRef) {
//		System.out.println("Checking if " + clientRef + " is in " + channelRef);
		Server s = ServerMain.server;
		if(s.channelMembers.containsKey(channelRef)){
//			System.out.println("User: " + clientRef + " in: " + channelRef + " was " + (found==true?"found":"not found"));
			return  s.channelMembers.get(channelRef).containsKey(clientRef);
		}
		System.out.println("No channel with ref " + channelRef);
		return false;
	}

	private int getChannelRefIfExistElseCreate(String channelToJoin) {
		Server s = ServerMain.server;
		if(s.channelTableByName.containsKey(channelToJoin)){
//			int ref = s.channelTableByName.get(channelToJoin);
//			System.out.println("Found Channel: "+channelToJoin+" with ref: " + ref);
			return s.channelTableByName.get(channelToJoin);
		}else{
			int ref = UniqueRefGenerator.nextChannelRef();
//			System.out.println("Non Channel: "+channelToJoin+". Created with ref: " + ref);
			s.channelTableByName.put(channelToJoin,ref);
			s.channelTableByRef.put(ref,channelToJoin);
			s.channelMembers.put(ref,new ConcurrentHashMap<Integer, SocketWorkerThread>());
			return ref;
		}
	}

	private int getClientRefIfExistElseCreate(String clientName) {
		Server s = ServerMain.server;
		if(s.memberTableByName.containsKey(clientName)){
//			int ref = s.memberTableByName.get(clientName);
//			System.out.println("Found User: "+clientName + " with ref: " + ref);
			return s.memberTableByName.get(clientName);
		}else{
			int ref = UniqueRefGenerator.nextClientRef();
//			System.out.println("Non User: "+clientName + ". Created with ref: " + ref);
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
	private void terminate(){
		try {
			socket.close();
			sendQueueWorkerThread.join();
			Thread.currentThread().join();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
