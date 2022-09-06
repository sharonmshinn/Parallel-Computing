
/**
 * This class represents a Trash Site with a URL, name, address, lat and lon cords,
 * and distance calculated by MyFirstCallable class. It consists of a constructor
 * and a toString method.
 * @author Sharon Shin
 *
 */
public class TrashSite {
	String URL, name, address;
	double latitude, longitude, distance;

	/**
	 * Constructor for the TrashSite that is called in MyFirstCallable class
	 * @param URL
	 * @param name
	 * @param address
	 * @param latitude
	 * @param longitude
	 * @param distance
	 */
	public TrashSite(String URL, String name, String address, double latitude, 
			double longitude, double distance) {
		this.URL = URL;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
	}
	
	/**
	 * String representation of the TrashSite class.
	 * @output String output
	 */
	public String toString() {
		String output = "URL=";
		output += URL + " name=" + name + " address=" + address + 
				" latitude=" + latitude + " longitude=" + longitude + 
				" distance=" + distance;
		return output;
	}

	
}

