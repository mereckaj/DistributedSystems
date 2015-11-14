package com.mereckaj.Client;

import com.mereckaj.ChatServer.MessageQueueWorkerThread;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by mereckaj on 11/14/15 2:38 PM.
 */
public class Client {
	public String ip;
	public int port;
	public Scanner scanner;
	public Socket s;
	boolean running;
	public MessageQueueWorkerThread messageQueue;
	public String username;
	public String channelName;
	public int channelRef = -1;
	public int usernameRef = -1;
	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage: <program> <ip> <port>");
		} else {
			Client c = new Client();
			c.parseArguments(args);
			System.out.println("Connecting: " + c.ip +":"+ c.port);
			c.run();
		}
	}
	public Client(){
	}
	public void run(){
		try {
			s = new Socket(ip,port);
			messageQueue = new MessageQueueWorkerThread(new OutputStreamWriter(s.getOutputStream()));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		running = true;
		scanner = new Scanner(System.in);
		int choice = 0;
		pickUsername();
		while(running){
			choice = showMenu();
			dealWithChoice(choice);
		}
	}

	private void pickUsername() {
		boolean nameNotPicked = true;
		while(nameNotPicked){
			System.out.println("Pick a username");
			username = scanner.next();
			if(username!="" && username!=null){
				nameNotPicked = false;
			}
		}
	}

	private void addToSendQueue(String m){
		messageQueue.addMessageToQueue(m);
	}

	private void dealWithChoice(int choice) {
		switch(choice){
			case 0:
				exit();
				break;
			case 1:
				joinChatroom();
				break;
			case 2:
				leaveChatroom();
				break;
			case 3:
				sendMessage();
				break;
			case 4:
				disconnect();
				break;
			default:
				System.out.println("Bad choice");
		}
	}

	private void exit() {
		running = false;
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void disconnect() {
		addToSendQueue("DISCONNECT: 0\nPORT: 0\nCLIENT_NAME: " +username);
	}

	private void sendMessage() {
		if(channelName!=null && channelRef !=-1 && usernameRef != -1) {
			System.out.println("Enter message");
			String msg = scanner.next();
			if(msg!=null){
				addToSendQueue("CHAT: "+channelRef+"\n" +
						"JOIN_ID: "+usernameRef+"\n" +
						"CLIENT_NAME: "+username+"\n" +
						"MESSAGE: "+msg+"\n\n");
			}
		}else{
			if(channelName==null) {
				System.out.println("No channel selected");
			}
			if(channelRef==-1){
				System.out.println("Wrong channel ref");
			}
			if(usernameRef==-1){
				System.out.println("Wrong username ref");
			}
		}
	}

	private void leaveChatroom() {
		if(channelName!=null && channelRef !=-1 && usernameRef != -1) {
			addToSendQueue("LEAVE_CHATROOM: "+channelRef+"\n" +
					"JOIN_ID: "+usernameRef+"\n" +
					"CLIENT_NAME: " + username);
			channelName = null;
			channelRef = -1;
			usernameRef = -1;
		}else{
			if(channelName==null) {
				System.out.println("No channel selected");
			}
			if(channelRef==-1){
				System.out.println("Wrong channel ref");
			}
			if(usernameRef==-1){
				System.out.println("Wrong username ref");
			}
		}
	}

	private void joinChatroom() {
		if(channelName==null && channelRef == -1 && usernameRef == -1) {
			System.out.println("Enter channel name");
			channelName = scanner.next();
			if (channelName != "") {
				addToSendQueue("JOIN_CHATROOM: "+channelName+"\n" +
						"CLIENT_IP: 0\n" +
						"PORT: 0\n" +
						"CLIENT_NAME: "+username);
			}
		}else{
			if(channelName!=null) {
				System.out.println("Channel already selected");
			}
			if(channelRef!=-1){
				System.out.println("Wrong channel ref");
			}
			if(usernameRef!=-1){
				System.out.println("Wrong username ref");
			}
		}
	}

	private int showMenu() {
		System.out.println("-----------------------------");
		System.out.println("[1] Join Chat room");
		System.out.println("[2] Leave Chat room");
		System.out.println("[3] Send Message");
		System.out.println("[4] Disconnect");
		System.out.println("[0] Exit (Without disconnect)");
		return Integer.parseInt(scanner.next());
	}

	public void parseArguments(String[] args) {
		ip = args[0];
		port = new Integer(args[1]);
	}
}
