package com.mereckaj.ChatServer;

/**
 * Created by mereckaj on 11/12/15.
 */
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static java.lang.Thread.sleep;

public class ThreadPool {
	public static int THREAD_POOL_SIZE = 50;
	private static ThreadPool instance = new ThreadPool();
	private ExecutorService executorService;
	private ThreadPool(){
		this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
	}
	public static ThreadPool getInstance(){
		return instance;
	}
	public void addJobToQueue(SocketWorkerThread t){
		executorService.execute(t);
	}
	public void terminate() {
		executorService.shutdownNow();
	}
}

