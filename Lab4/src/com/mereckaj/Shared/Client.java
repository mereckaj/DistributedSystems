package com.mereckaj.Shared;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by mereckaj on 11/12/15.
 */
public class Client implements Serializable {
	private String username;
	public Client(String username){
		this.username = username;
	}
	@Override
	public String toString(){
		if(username==null){
			return "null";
		}else{
			return username;
		}
	}
}
