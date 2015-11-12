package com.mereckaj.Shared.Messages;

import com.mereckaj.ChatServer.ServerMain;

import java.io.Serializable;

/**
 * Created by mereckaj on 11/12/15.
 */
public class MessageJoinSuccess extends Message implements Serializable {
	private MessageJoin mj;
	private String ip;
	public MessageJoinSuccess(MessageJoin mj,String ip){
		super();
		this.mj = mj;
		this.ip = ip;
		//TODO: unique refs for group and joins
		message = "JOINED_CHATROOM: " + mj.group + "\n"
				+ "SERVER_IP: " + ip + "\n"
				+ "PORT: 0\n"
				+ "ROOM_REF" + "0" + "\n"
				+ "JOIN_ID" + "0" + "\n";
		sender = ServerMain.server.getServerClient();
	}
}