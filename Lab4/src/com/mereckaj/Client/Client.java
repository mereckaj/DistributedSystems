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
	public static void main(String[] args) {
		Client c = new Client();
		if (args.length < 1) {
			System.out.println("Usage: <program> <ip> <port>");
		} else {
			c.parseArguments(args);
			System.out.println("Connecting: " + c.ip +":"+ c.port);
			c.run();
		}
	}
	public Client(){
		try {
			s = new Socket(ip,port);
			messageQueue = new MessageQueueWorkerThread(new OutputStreamWriter(s.getOutputStream()));
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		running = true;
		scanner = new Scanner(System.in);
	}
	public void run(){
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
		addToSendQueue("");
	}

	private void sendMessage() {

	}

	private void leaveChatroom() {

	}

	private void joinChatroom() {

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
