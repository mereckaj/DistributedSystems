package com.mereckaj.Client;

import com.mereckaj.Shared.Messages.Message;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by mereckaj on 11/12/15.
 */
public class ClientMain {
	public static void main(String[] args){
		try {
			Socket s = new Socket("0.0.0.0",8000);
			OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

