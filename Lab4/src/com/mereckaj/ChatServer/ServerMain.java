package com.mereckaj.ChatServer;

import com.mereckaj.Shared.Client;

/**
 * Created by mereckaj on 11/12/15.
 */
public class ServerMain{
	public static int PORT;
	public static Server s;

	public static void main(String[] args){
		if(args.length<1){
			System.out.println("Usage: <program> <port>");
		}else{
			parseArguments(args);
		}
		s = new Server(PORT);
		s.run();
	}
	private static void parseArguments(String[] args) {
		PORT = new Integer(args[0]);
		System.out.println("New port is: " + PORT);
	}
}
