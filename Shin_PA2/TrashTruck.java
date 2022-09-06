
import java.util.concurrent.locks.ReentrantLock;

/**
 * This singleton class represents a trash truck driving around and picking up 
 * pieces of trash that are defined in the TrashType enum. This class works with 
 * its inner class, PrivateTruckData. It performs the following functions:
 * 		run
 * 		getInstance for singleton
 * 		singleton constructor
 * 		printAll
 * This class extends Thread.
 * @author Sharon Shin
 *
 */
public class TrashTruck extends Thread{
	
	//Static variable reference for singleton truck
	private static TrashTruck truck;
	//Reference to the Queue of trash passed into the constructor
	public static Queue<TrashType> collection;
	//Queue to append PrivateTruckData as each thread finishes their work
	public static Queue<PrivateTruckData> data = new Queue<>();;
	//Lock for the TrashType queue
	ReentrantLock collectionLock = new ReentrantLock();
	//Lock for the PrivateTruckData queue
	ReentrantLock privateLock = new ReentrantLock();
	/*
	 * ThreadLocal keeps a copy of the PrivateTruckData for each thread to 
	 * mofify as needed. It overrides the initialValue() and get() methods.
	 */
	ThreadLocal<PrivateTruckData> privateTruckData =
	         new ThreadLocal<PrivateTruckData>() {
	             @Override 
	             protected PrivateTruckData initialValue() {
	            	 return new PrivateTruckData();
	             }
	             
	             @Override
	             public PrivateTruckData get() {
	            	 return super.get();
	             }
	     };
	
	/**
	 * This constructor is protected so other classes in the file can access it.
	 * This create a singleton TrashTruck instance and stores the static 
	 * truck variable to the object being made. It stores the queue variable 
	 * in a field as well.
	 * @param TrashType queue
	 */
	protected TrashTruck(Queue<TrashType> queue) {
		collection = queue;
		truck = this;
	}
	
	/**
	 * This method is called upon by a thread calling the start() method call.
	 * While the collection still has Nodes to dequeue, or is not empty, 
	 * we will use the lock to stop other thread from using or modifying 
	 * the queue we are working with. Then we will use PrivateTruckData's 
	 * collectTrash method with the dequeued item to increment the associated
	 * trash type counter, then unlock, allowing other threads to use the queue
	 * again. After the queue is empty, we will enqueue each thread's 
	 * ThreadLocal data onto our PrivateTruckData queue, using a lock as well.
	 */
	public void run() { 
		while(collection.front != null) {
			collectionLock.lock();
	        try{
	        	if(collection.front != null) {
		        	TrashType trash = collection.dequeue().data;
			        privateTruckData.get().collectTrash(trash);
	        	}
	        } finally {
	        	collectionLock.unlock();
		    }
		}
		privateLock.lock(); 
        try
        {
        	data.enqueue(privateTruckData.get());
        }
        finally {
        	privateLock.unlock();	
        }
	}
	
	/**
	 * A static method to return the instance of our singleton class.
	 * @return
	 */
	public static TrashTruck getInstance() {
		return truck;
	}
	
	/**
	 * This method is called at the end of the Driver class to print 
	 * each thread/truck's count totals with the overall total printing at the
	 * bottom. 
	 */
	public void printAll() {
		int counter = 1;
		int total = 0;
		int[] totals = new int[TrashType.values().length];
		while(data.front != null) {
			System.out.println("Truck: " + counter);
			PrivateTruckData truckData = data.dequeue().data;
			truckData.print();
			for(int i=0; i<TrashType.values().length; i++) {
				totals[i] += truckData.getCounts()[i];
			}
			counter ++;
		}
		System.out.println("Overall Totals");
		for(int i=0; i<TrashType.values().length; i++) {
			System.out.println("type=" + TrashType.values()[i] + 
					" total collected=" + totals[i]);
			total += totals[i];
		}
		System.out.println("total=" + total);
		
	}
	
	/**
	 * This inner class represents the count totals of each type of trash per 
	 * truck/thread. It keeps an integer array of the totals, going in the same
	 * index as the TrashType's associated indexes in the enum.
	 * It performs the following functions:
	 * 		print
	 * 		collectTrash
	 * 		getCounts
	 * @author Sharon Shin
	 *
	 */
	class PrivateTruckData {
		//Integer array keeping track of the totals per type of trash
		private int[] nums = new int[TrashType.values().length];
		
		/*
		 * This print method prints the types of trash with the total collected
		 * for this thread/truck.
		 */
		public void print() {
			for(int i=0; i<nums.length; i++) {
				System.out.println("type=" + TrashType.values()[i] + " collected=" + nums[i]);
			}
		}
		
		/*
		 * This method takes in an argument of TrashType and increments the
		 * associated index using oridinal. 
		 */
		public void collectTrash(TrashType type) {
			nums[type.ordinal()] += 1;
		}
		
		/*
		 * This method returns the integer array of each type it has counted
		 * for the current thread/truck.
		 */
		public int[] getCounts() {
			return nums;
		}
		
	}

}
