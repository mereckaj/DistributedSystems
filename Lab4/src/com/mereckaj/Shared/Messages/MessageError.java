package com.mereckaj.Shared.Messages;

import com.mereckaj.Shared.Client;

import java.io.Serializable;

/**
 * Created by mereckaj on 11/12/15.
 */
public class MessageError extends Message implements Serializable {
	public static final int UNDETERMINED_MESSAGE_RECEIVED = 1;
	private String error;
	private int code;
	public  MessageError(String err, int code, Client c){
		super();
		this.error = err;
		this.code = code;
		message = "ERROR_CODE: " + code + "\n"
				+ "ERROR_DESCRIPTION: " + err + "\n";
		sender = c;
		receiver = new Client("SERVER");
	}
}
