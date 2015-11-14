package com.mereckaj.ChatServer;

/**
 * Created by mereckaj on 11/13/15 5:20 PM.
 */
public class ErrorReporter {
	public static final int USERNAME_ALREADY_IN_USE_C = 1;
	public static final String USERNAME_ALREADY_IN_USE_S = "Username is already registered";
	public static final int USER_ALREADY_IN_GROUP_C = 2;
	public static final String USER_ALREADY_IN_GROUP_S = "User is already in this group";
	public static final int USER_NOT_IN_GROUP_C =3;
	public static final String USER_NOT_IN_GROUP_S = "User is not in this group";
	public static final int GROUP_NOT_EXIST_C = 4;
	public static final String GROUP_NOT_EXIST_S = "Group does not exist";
}
