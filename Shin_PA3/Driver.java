
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class acts as the start button once the program is ran.
 * This class uses a scanner to receive user input and creates Callables to 
 * execute certain tasks using the TrashSite, MyFirstCallable, and 
 * MySecondCallable classes. It uses the inputed lat and lon to 
 * return the 8 closest TrashSites in comparison to the inputed values.
 * 
 * Selection sort code from https://www.geeksforgeeks.org/selection-sort/
 * Pathname to data on computer: C:\Users\sharo\Downloads\Epa_Facility_Data\Epa_Facility_Data
 * 
 * @author Sharon Shin
 */

public class Driver {
	public static void main(String args[]) throws Exception{
		//Scanner to take in input
		Scanner scanner = new Scanner(System.in);
		//Executor with 8 threads
		ExecutorService executor = Executors.newFixedThreadPool(8);
		//List of Futures that hold ArrayLists of TrashSite objects
		List<Future<ArrayList<TrashSite>>> list = 
				new ArrayList<Future<ArrayList<TrashSite>>>();
		//Four static ArrayLists to hold data returned by first set of Callables.
		ArrayList<TrashSite> l1 = new ArrayList<TrashSite>();
		ArrayList<TrashSite> l2 = new ArrayList<TrashSite>();
		ArrayList<TrashSite> l3 = new ArrayList<TrashSite>();
		ArrayList<TrashSite> l4 = new ArrayList<TrashSite>();
		
		//User input of latitude, longitude, and path.
		System.out.println("Enter your latitude: ");
		double inputLat = scanner.nextDouble();
		System.out.println("Enter your longitude: ");
		double inputLon = scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter the absolute pathname of your data directory: ");
		String path = scanner.nextLine();
		//Creates a File object with the folder path
		File folder = new File(path);
		//Creates a list of String names of all csv files in folder path
		String[] contents = folder.list();
		//Iterate over each file in the folder
		for(String file : contents) {
			//Open a FileReader per csv in folder
			FileReader data = new FileReader(new File(path + "\\" + file));
			//Create a MyFirstCallable object and pass in lat, long, and file path
			Callable<ArrayList<TrashSite>> callable = 
					new MyFirstCallable(inputLat, inputLon, data);
			//Submit the Callable for execution and return data into Future
			Future<ArrayList<TrashSite>> future = executor.submit(callable);
			//add future to list of futures.
			list.add(future);
		}
		
		//Iterate over list of futures once done with all csv files
		for(int i=0; i<list.size(); i++){
			//Using modulo, add contents of each Future into the static lists
			try {
				if(i%4 == 0) {
					l1.addAll(list.get(i).get());
				} else if (i%4 == 1) {
					l2.addAll(list.get(i).get());
				} else if (i%4 == 2) {
					l3.addAll(list.get(i).get());
				} else if (i%4 == 3) {
					l4.addAll(list.get(i).get());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Create an ArrayList of Callable objects that hold ArrayLists of TrashSite
		ArrayList<Callable<ArrayList<TrashSite>>> tasks = 
				new ArrayList<Callable<ArrayList<TrashSite>>>();
		//An ArrayList of TrashSite objects for our final output of 8 nearest cities
		ArrayList<TrashSite> array = new ArrayList<TrashSite>();
		
		/*Creating four new callables of MySecondCallable and passing in 
		 * each static list and add to the list of tasks. 
		 */
		Callable<ArrayList<TrashSite>> c1 = new MySecondCallable(l1);
		tasks.add(c1);
		Callable<ArrayList<TrashSite>> c2 = new MySecondCallable(l2);
		tasks.add(c2);
		Callable<ArrayList<TrashSite>> c3 = new MySecondCallable(l3);
		tasks.add(c3);
		Callable<ArrayList<TrashSite>> c4 = new MySecondCallable(l4);
		tasks.add(c4);
		
		/*Iterate over each callable task and execute, then add contents of 
		 * each Future to final output array
		 */
		for(Callable<ArrayList<TrashSite>> callable : tasks) {
			Future<ArrayList<TrashSite>> future = executor.submit(callable);
			array.addAll(future.get());
		}
		
		//Use selection sort to sort the elements in the final output array
		for(int i=0; i<array.size()-1; i++) {
			int min_idx = i;
            for (int j = i+1; j < array.size(); j++)
                if (array.get(j).distance < array.get(min_idx).distance)
                    min_idx = j;
            TrashSite temp = array.get(min_idx);
            array.set(min_idx, array.get(i));
            array.set(i, temp);
		}
		
		//Print out top 8 TrashSites in suggested format
		for(int i=0; i<8; i++) {
			System.out.println(array.get(i));
		}

        //Shut down the executor service and close scanner
        executor.shutdown();
        scanner.close();
	}
}

