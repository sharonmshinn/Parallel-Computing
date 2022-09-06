
/**
 * This class represents a queue using a linked list structure. It performs
 * the following functions:
 * 		run
 * 		dequeue
 * 		enqueue
 * It uses the Node class as its linked list nodes.
 * @author Sharon Shin
 * 
 * Source codes: 
 * https://www.techiedelight.com/queue-implementation-using-linked-list/#:~:text=A%20queue%20can%20be%20easily,(1)%20efficiency%20for%20insertion.
 * https://menhaj0102.medium.com/how-to-implement-a-generic-queue-data-structure-in-java-28a853bb1187
 * 
 */

public class Queue<T> extends Thread{
	
	/**
	 * This method is called upon by a thread calling the start() method call.
	 * It creates a Queue object that takes in integers and randomly creates
	 * 1000 numbers between 0-100 to either dequeue or enqueue onto the queue, also
	 * based on random numbers. 
	 */
	public void run() {
		Queue<Integer> queue = new Queue<Integer>();
		for(int i=0; i<Math.pow(10, 3); i++) {
			int rand = (int)(Math.random() * 100);
			if(rand >= 50) {
				queue.dequeue();
			} else {
				queue.enqueue((int)(Math.random() * 100));
			}
		}
	}
	
	class Node<S>{
		//Field to reference the next node after this node.
		Node<S> next;
		//Field to hold a generic data value, determined when instantiating queue
		S data;
				
		/**
		* Constructor for the Node class. It sets the generic parameter as 
		* the data it is holding and automatically sets its next node 
		* reference as null. This must be set accordingly in the code.
		* @param data
		*/
		Node(S data) {
			this.data = data;
			this.next = null;
		}
				
		@Override
		public String toString() {
			String output = "";
			output += data;
			return output;
		}
	}
	
	//Reference to the rear of the queue
	public Node<T> rear = null;
	//Reference to the front of the queue
	public Node<T> front = null;
		
	/**
	* This method removes the node from the front of the queue. 
	* If the queue is empty, it prints an error message that the method failed
	* and the reason why. If it is not empty, it will print the node value
	* that is being dequeued and updates the front accordingly. It checks
	* to see if the queue is empty after successful dequeue and updates the
	* front and rear to null if necessary.
	*/
	public void dequeue() {
		if (front == null) {
			System.out.println("Dequeue - failed (queue empty).");
		} else {
			System.out.println("Dequeue() - " + front.data);
			front = front.next;
			if(front == null) {
				rear = null;
			}
		}
	}
	
	/**
	* This method creates a new node with the value and puts it at the rear of 
	* the queue. If the queue is empty, it will set both the front and rear
	* as the new node. If it is not, it updates the rear's next to be the 
	* new node and sets the rear of the queue as the new node.  
	* @param value 
	*/
	public void enqueue(T val) {
		Node<T> node = new Node<T>(val);
		System.out.println("Enqueue(" + val + ")");
		
		if(front == null) {
			front = node;
			rear = node;
		} else {
			rear.next = node;
			rear = node;
		}
	}
}

