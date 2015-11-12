package com.mereckaj.Shared;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;

/**
 * Created by mereckaj on 11/12/15.
 */
public class Group implements Serializable{
	private String groupName;
	private LinkedList<Client> userList;
	public Group(String groupName){
		userList = new LinkedList<Client>();
		this.groupName = groupName;
	}
	@Override
	public String toString() {
		if (groupName == null) {
			return "null";
		} else {
			return groupName;
		}
	}
	public void addClientToGroup(Client c){

	}
}
