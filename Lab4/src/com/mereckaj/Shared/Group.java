package com.mereckaj.Shared;

import java.io.Serializable;

/**
 * Created by mereckaj on 11/12/15.
 */
public class Group implements Serializable{
	private String groupName;
	public Group(String groupName){
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
}
