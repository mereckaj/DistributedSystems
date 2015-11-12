package com.mereckaj.ChatServer;

/**
 * Created by mereckaj on 11/12/15.
 */
import com.mereckaj.Shared.Client;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

/**
 * Created by mereckaj on 12/10/15.
 */
public class Server {
	private int port;
	public boolean running;
	private ServerSocket serverSocket;
	ThreadPool tp = ThreadPool.getInstance();
	public Client serverClient;
	public Server(int port){
		this.serverClient = new Client("SERVER");
		this.port = port;
		this.running = true;
		try {
			this.serverSocket = new ServerSocket(port,0, InetAddress.getByName("0.0.0.0"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run(){
		while(running){
			try {
				tp.addJobToQueue(new SocketWorkerThread(serverSocket.accept()));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	public ServerSocket getSocket(){
		return serverSocket;
	}
	public void terminate(){
		try {
			running = false;
			System.out.println("Running = false");
			tp.terminate();
			System.out.println("TP terminated");
			serverSocket.close();
			System.out.println("Socket closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
