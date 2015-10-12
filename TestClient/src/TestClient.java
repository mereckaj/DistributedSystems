/**
 * Created by mereckaj on 12/10/15.
 */
public class TestClient {
	public static final int NUMBER_OF_TEST_THREADS = 1;
	public static final int NUMBER_OF_CONNECTIONS_PER_THREAD = 1;
	public static final int MAX_DELAY_BETWEEN_CONNECTIONS = 1; //2500ms
	public static final int SERVER_PORT = 8000;

	// Spawn a number of threads that will connect to some port
	public static void main(String[] args){
		for(int i =0; i < NUMBER_OF_TEST_THREADS;i++){
			new TesterThread(SERVER_PORT,NUMBER_OF_CONNECTIONS_PER_THREAD).start();
		}
	}
}
