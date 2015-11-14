package com.mereckaj.ChatServer;

/**
 * Created by mereckaj on 11/12/15.
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mereckaj on 12/10/15.
 */
public class Server {
	private int port;
	public boolean running;
	private ServerSocket serverSocket;
	ThreadPool tp = ThreadPool.getInstance();

	// Maps for channels
	ConcurrentHashMap<Integer,String> channelTableByRef;
	ConcurrentHashMap<String,Integer> channelTableByName;

	// Maps for members
	ConcurrentHashMap<Integer,String> memberTableByRef;
	ConcurrentHashMap<String,Integer> memberTableByName;

	// Channel ref -> (UserRef, Socket)
	ConcurrentHashMap<Integer,ConcurrentHashMap<Integer,SocketWorkerThread>> channelMembers;



	// Constructor that creates a server on this machine listening on all interfaces at port
	public Server(int port){
		channelTableByRef = new ConcurrentHashMap<>();
		channelTableByName = new ConcurrentHashMap<>();
		memberTableByRef = new ConcurrentHashMap<>();
		memberTableByName = new ConcurrentHashMap<>();
		channelMembers = new ConcurrentHashMap<>();
		this.port = port;
		this.running = true;
		try {
			this.serverSocket = new ServerSocket(port,0, InetAddress.getByName("0.0.0.0"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Start the server
	public void start(){
		while(running){
			try {
				tp.addJobToQueue(new SocketWorkerThread(serverSocket.accept()));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	// Getter for server socket (Necessary so that server can be shutdown
	public ServerSocket getSocket(){
		return serverSocket;
	}

	// Closes the server socket, shuts down the ThreadPool
	public void terminate(){
		try {
			running = false;
			tp.terminate();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void broadcast(int channelref, String reply) {
		ConcurrentHashMap<Integer,SocketWorkerThread> usrs = channelMembers.get(channelref);
		for(Integer i : usrs.keySet()){
			usrs.get(i).addToSendQueue(reply);
			System.out.println("Sent message to: " + i);
		}
	}
}
