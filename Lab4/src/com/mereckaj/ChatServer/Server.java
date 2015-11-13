package com.mereckaj.ChatServer;

/**
 * Created by mereckaj on 11/12/15.
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mereckaj on 12/10/15.
 */
public class Server {
	private int port;
	public boolean running;
	private ServerSocket serverSocket;
	ThreadPool tp = ThreadPool.getInstance();

	// Channel name -> Channel Ref number
	ConcurrentHashMap<String,Integer> channelList;

	// Channel Ref number -> Member names as strings
	ConcurrentHashMap<Integer,String> channelMembersByName;

	// Member name as string -> Member Ref number
	ConcurrentHashMap<String,Integer> memberRef;

	// Constructor that creates a server on this machine listening on all interfaces at port
	public Server(int port){
		this.channelList = new ConcurrentHashMap<String,Integer>();
		this.channelMembersByName = new ConcurrentHashMap<Integer,String>();
		this.memberRef = new ConcurrentHashMap<String,Integer>();
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

}
