package com.mereckaj.Shared.Messages;

import com.mereckaj.Shared.Client;
import com.mereckaj.Shared.Group;

import java.io.Serializable;

/**
 * Created by mereckaj on 11/12/15.
 */
public class Message implements Serializable{
	protected String message;
	protected Client sender;
	protected Client receiver;
	protected Group group;

	public Message(String m,Client from, Client toc, Group tog){

	}
	public Message(){
		this("null",null,null,null);
	}

	@Override
	public String toString(){
		return this.getClass().getName() + ":" +
				message + ":" +
				sender + ":" +
				receiver + ":" +
				group;
	}
}
