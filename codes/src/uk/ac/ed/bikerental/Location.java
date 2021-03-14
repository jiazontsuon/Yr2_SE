package uk.ac.ed.bikerental;
/**
 * Provides postcode and street address of a certain location.
 * @author s1864509, s1857043
 */
public class Location {
	/**
	 * postcode of the location
	 */
    private String postcode;
    /**
     * Street Address of the location
     */
    private String address;
    /**
     * Creates a new Location taking 2 given Strings:
     * The first Parameter is the postcode and the second parameter is the street address.
     * @param postcode
     * @param address
     */
    public Location(String postcode, String address) {
        this.postcode = postcode;
        this.address = address;
    }
    
    /**
     * This takes another location and check whether they are next to each other.
     * Two places are near to each other if and only if the first 2 digits of their postcode are the same
     * <p>
     * This will return the result of comparisons.
     * @param Location other
     * @return result of the comparison.
     */
    public boolean isNearTo(Location other) {
        // TODO: Implement Location.isNearTo
    	if (other.getPostcode().substring(0, 2).equals(this.postcode.substring(0, 2))) {
    		return true;
    	}
    	return false;
    }
    /**
     * return postcode of the location
     * @return postcode
     */
    public String getPostcode() {
        return postcode;
    }
    /**
     * return address of the location
     * @return address
     */
    public String getAddress() {
        return address;
    }
    
    // You can add your own methods here
}
