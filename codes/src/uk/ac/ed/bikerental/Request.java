package uk.ac.ed.bikerental;

import java.time.LocalDate;
import java.util.HashMap;

public class Request {
	private DateRange dateRange;
	private Location location_of_hire;
	private HashMap<BikeType,Integer> BikesRequested;
	
	
	public Request(DateRange dateRange,Location location,HashMap<BikeType,Integer> BikesRequested) {
		assert dateRange.getStart().isAfter(LocalDate.now()) : "The Starting date should be after today";
		this.dateRange = dateRange;
		this.location_of_hire = location;
		this.BikesRequested  = BikesRequested;
	}
	public DateRange getDateRange() {
		return dateRange;
	}
	public Location getLocation_of_hire() {
		return location_of_hire;
	}
	public HashMap<BikeType, Integer> getBikesRequested() {
		return this.BikesRequested;
	}
	
}
