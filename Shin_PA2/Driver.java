
public class Driver {
    public static void main(String[] args) {
        Queue<TrashType> collection =
                new Queue<TrashType>();
        TrashType t;
        /*
         * Create 10000 pieces of trash and add them to our queue
         */
        for (int index = 0; index < 10000; index++) {
            int value = (int)(Math.random()*10000) %
                        TrashType.values().length;
            t = TrashType.values()[value];
            collection.add(t);
        }
        
        /*
         * Create one new TrashTruck instance.  Note that all threads
         * will share the data in this instance unless it is made local
         * to each thread
         */
        TrashTruck truck = new TrashTruck(collection);
        int numberOfThreads = 5;
        /*
         * Create and start five threads.
         */
        Thread[] threads = new Thread[numberOfThreads];
        for (int index = 0; index < threads.length; index++) {
            threads[index] = new Thread(truck);
            threads[index].start();
        }
        /*
         * Wait for the five threads to return from run()
         */
        try {
            for (int index = 0; index < threads.length; index++) {
                threads[index].join();
        }
        } catch(InterruptedException ie) {
            ie.printStackTrace();
        }
        /*
         * Print the results from all the threads.
         */
        truck.printAll();
    }
}
