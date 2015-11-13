package com.mereckaj.Shared.Messages;

import com.mereckaj.Shared.Client;
import com.mereckaj.Shared.Group;

import java.io.Serializable;

/**
 * Created by mereckaj on 11/12/15.
 */
public class Message {
	public String joinMessage(String username, String channel){
		String result = "JOIN_CHATROOM: " + channel + "\n"
				+ "CLIENT_IP: " + "0" + "\n"
				+ "PORT: " + "0" + "\n"
				+ "CLIENT_NAME: " + username + "\n";
		return result;
	}
	public String joinMessageReply(String joinMessage){
		return "";
	}
}
