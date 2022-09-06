
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * This class implements the Callable interface.
 * This class takes in an ArrayList of TrashSites and return the top 8
 * TrashSites with the sortest distance variables. This class acts similarly
 * to MyFirstCallable but does not create TrashSite objects and instead
 * sorts the list passed in.
 * This class consists of:
 * 		constructor
 * 		call method that is run when executor invokes this callable
 * 
 * Selection sort code from https://www.geeksforgeeks.org/selection-sort/
 * 
 * @author Sharon Shin
 *
 */
public class MySecondCallable implements Callable<ArrayList<TrashSite>>{
	ArrayList<TrashSite> list;
	
	/**
	 * Constructor for this Calalble that takes in a list.
	 * @param list
	 */
	public MySecondCallable(ArrayList<TrashSite> list) {
		this.list = list;
	}

	@Override
	public ArrayList<TrashSite> call() throws Exception {
		//ArrayList of top 8 TrashSites with shortest distance variables to return
		ArrayList<TrashSite> output = new ArrayList<TrashSite>();
		
		//Static capacity int variable, max is 8 TrashSites
		int capacity = 0;
		//Accounting for min and max dist so far to sort
		double minDist = 0;
		double maxDist = 0;
		//Boolean flag to account for if the list is sorted from smallest to biggest distance
		boolean sorted = false;
		//Iterate over list variable
		for(int i=0; i<list.size(); i++) {
			//If capacity is not full yet
			if(capacity != 8) {
				//Add TrashSite to output list
				output.add(list.get(i));
				//Increment capacity
				capacity ++;
			//If capacity is full and boolean flag is false
			} else if (capacity == 8 && !sorted) {
				//Use selection sort to sort output array from smallest to biggest distance
				for(int k=0; k<output.size()-1; k++) {
					int min_idx = k;
		            for (int j = k+1; j < output.size(); j++)
		                if (output.get(j).distance < output.get(min_idx).distance)
		                    min_idx = j;
		            TrashSite temp = output.get(min_idx);
		            output.set(min_idx, output.get(k));
		            output.set(k, temp);
				}
				minDist = output.get(0).distance;
				maxDist = output.get(capacity-1).distance;
				//Change boolean flag to true
				sorted = true;
			//If capacity is full and boolean flag is true
			} else if (capacity == 8 && sorted) {
				//If the dist at list index is lower than minDis
				if(list.get(i).distance < minDist) {
					//Add TrashSite to first index
					output.add(0, list.get(i));
					//Remove highest index TrashSite with largest distance
					output.remove(capacity);
					//Set minDist to new lowest distance
					minDist = output.get(0).distance;
					//Set maxDist to new highest distance
					maxDist = output.get(capacity-1).distance;
					// If dist is between minDist and maxDist
				} else if (list.get(i).distance > minDist && list.get(i).distance < maxDist) {
					//Iterate over list of sorted TrashSites
					for(int m=0; m<capacity-1; m++) {
						//Add TrashSite to associated index and remove last location
						if(list.get(i).distance > output.get(m).distance 
								&& list.get(i).distance < output.get(m+1).distance) {
							output.add(m+1, list.get(i));
							output.remove(capacity);
							maxDist = output.get(capacity-1).distance;
						}
					}
				}
			}
		}
		//Return final output with 8 lowest distance cities
		return output;
	}

}

