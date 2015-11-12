package com.mereckaj.Shared.Messages;

import com.mereckaj.Shared.Client;
import com.mereckaj.Shared.Group;

import java.io.Serializable;

/**
 * Created by mereckaj on 11/12/15.
 */
public class MessageJoin extends Message implements Serializable{
	public MessageJoin(String username, String group){
		super();
		//TODO: Change uername to clint and group to Group
		message = "JOIN_CHATROOM: " + group + "\n" +
				"CLIENT_IP: 0\n" +
				"PORT: 0\n" +
				"CLIENT_NAME: " + username + "\n";
		sender = new Client(username);
	}
}
