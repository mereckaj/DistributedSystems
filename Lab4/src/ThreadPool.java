import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mereckaj on 11/10/15.
 */
public class ThreadPool {
	public static int THREAD_POOL_SIZE = 5;
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
