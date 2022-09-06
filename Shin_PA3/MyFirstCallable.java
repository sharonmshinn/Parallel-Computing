
import java.io.BufferedReader;
/**
 * This class takes in lat, lon, and a FileReader and calculates distance
 * between the input lat and lon to return the 8 closest TrashSites.
 * This class implements the Callable interface.
 * This class consists of:
 * 		Constructor
 * 		distance method to calculate distance between two TrashSites
 * 		call method that is run when executor invokes this callable
 * 
 * Selection sort code from https://www.geeksforgeeks.org/selection-sort/
 * 
 * @author Sharon Shin
 */

import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MyFirstCallable implements Callable<ArrayList<TrashSite>>{
	FileReader csvPath;
	double inputLat, inputLon, latitude, longitude, dist;
	String URL, name, address;
	
	/**
	 * Constructor to create new Callable object.
	 * @param inputLat
	 * @param inputLon
	 * @param csvName
	 */
	public MyFirstCallable(double inputLat, double inputLon, FileReader csvName) {
		this.inputLat = inputLat;
		this.inputLon = inputLon;
		this.csvPath = csvName;
	}
	
	/**
	 * Distance method to calculate distance between two TrashSites
	 * @param lat1
	 * @param long1
	 * @param lat2
	 * @param long2
	 * @return distance
	 */
	public static double distance(double lat1, double long1, double lat2, double long2) {
	    lat1 = Math.toRadians(lat1);
	    long1 = Math.toRadians(long1);
	    lat2 = Math.toRadians(lat2);
	    long2 = Math.toRadians(long2);
	    return 6371 * 2 * Math.asin(Math.sqrt(
	        Math.pow(Math.sin((lat2 - lat1) / 2), 2) +
	        Math.cos(lat1) * Math.cos(lat2) *
	        Math.pow(Math.sin((long2 - long1) / 2), 2)));
	}
	
	@Override
	public ArrayList<TrashSite> call() throws Exception {
		//ArrayList of TrashSite to return at end of method
		ArrayList<TrashSite> output = new ArrayList<TrashSite>();
		//Creating a BufferedReader to read line by line of CSV
		BufferedReader br = new BufferedReader(csvPath);
		//String line to be split by ","
		String line = "";
		//Static capacity int variable, max is 8 TrashSites
		int capacity = 0;
		//Boolean flag to account for if the list is sorted from smallest to biggest distance
		boolean sorted = false;
		//String array after line is split
		String[] parts;
		//While the line is not null
		while((line = br.readLine()) != null) {
			//Split the line by ","
            parts = line.split("\",\"");
			try {
				//Parsing information and accounting for unnecessary symbols
				latitude = Double.parseDouble(parts[26].replaceAll("^\"|\"$", ""));
				longitude = Double.parseDouble(parts[27].replaceAll("^\"|\"$", ""));
				dist = distance(inputLat, inputLon, latitude, longitude); 
				URL = parts[0].replaceAll("^\"|\"$", "");
				name = parts[2].replaceAll("^\"|\"$", "");
				address = parts[3].replaceAll("^\"|\"$", "");
				
				//Accounting for min and max dist so far to sort
				double minDist = 0;
				double maxDist = 0;
				
				//If capacity is not full yet
				if(capacity != 8) {
					//Create new TrashSite with parsed information
					TrashSite location = 
							new TrashSite(URL, name, address, latitude, longitude, dist);
					//Add location to output
					output.add(location);
					//Increase capacity by 1
					capacity ++;
				//if capacity is full and the list is not sorted
				} else if (capacity == 8 && !sorted) {
					//Sort the list using selection sort 
					for(int i=0; i<output.size()-1; i++) {
						int min_idx = i;
			            for (int j = i+1; j < output.size(); j++)
			                if (output.get(j).distance < output.get(min_idx).distance)
			                    min_idx = j;
			            TrashSite temp = output.get(min_idx);
			            output.set(min_idx, output.get(i));
			            output.set(i, temp);
					}
					//Set min and max dist to first and land indexes
					minDist = output.get(0).distance;
					maxDist = output.get(capacity-1).distance;
					//Change flag to true
					sorted = true;
				//If capacity is true, and the list is sorted
				} else if (capacity == 8 && sorted) {
					//If the dist is lower than minDis
					if(dist < minDist) {
						//Create a new TrashSite
						TrashSite location = new TrashSite
								(URL, name, address, latitude, longitude, dist);
						//Add trashsite to first location
						output.add(0, location);
						//Remove last location with highest distance
						output.remove(capacity);
						//Set minDist to new lowest distance
						minDist = output.get(0).distance;
						//Set maxDist to new highest distance
						maxDist = output.get(capacity-1).distance;
					// If dist is between minDist and maxDist
					} else if (dist > minDist && dist < maxDist) {
						//Create new TrashSite
						TrashSite location = new TrashSite
								(URL, name, address, latitude, longitude, dist);
						//Iterate over list of sorted TrashSites
						for(int i=0; i<capacity-1; i++) {
							//Add TrashSite to associated index and remove last location
							if(dist > output.get(i).distance && 
									dist < output.get(i+1).distance) {
								output.add(i+1, location);
								output.remove(capacity);
								maxDist = output.get(capacity-1).distance;
							}
						}
					}
				}
			} catch (Exception e){
				continue;
			}
		}
		//Return final output with 8 lowest distance cities
		return output;
	}
	
}

