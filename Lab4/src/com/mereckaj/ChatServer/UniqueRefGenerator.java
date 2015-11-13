package com.mereckaj.ChatServer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mereckaj on 11/13/15 4:52 PM.
 */
public class UniqueRefGenerator {
	private static final AtomicInteger clientCounter = new AtomicInteger();
	private static final AtomicInteger channelCounter = new AtomicInteger();

	public static int nextClientRef(){
		return  clientCounter.getAndIncrement();
	}

	public static int nextChannelRef(){
		return channelCounter.getAndIncrement();
	}
}
