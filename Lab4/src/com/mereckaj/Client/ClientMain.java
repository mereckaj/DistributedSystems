package com.mereckaj.Client;

import com.mereckaj.Shared.Messages.MessageJoin;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by mereckaj on 11/12/15.
 */
public class ClientMain {
	public static void main(String[] args){
		try {
			Socket s = new Socket("0.0.0.0",8000);
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			MessageJoin mj = new MessageJoin("TestUser1","TestGroup1");
			oos.writeObject(mj);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
