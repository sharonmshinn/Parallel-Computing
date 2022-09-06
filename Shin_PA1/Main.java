
/**
 * This class acts as the start button once the program is ran.
 * This class creates a single thread, then calls upon the start method call 
 * in order to start the thread's work.
 * It works in sync with the Queue and Node classes.
 * @author Sharon Shin
 *
 */
public class Main {
	public static <T> void main(String[] args) {
		Queue<Integer> thread = new Queue<Integer>();
		Thread thread1 = new Thread(thread);
		thread1.start();
	}

}
