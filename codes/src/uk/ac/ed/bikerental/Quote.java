package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.ArrayList;


public class Quote implements Comparable<Quote> {
    private Provider provider;
	private Location location;
	private DateRange dateRange;
	private ArrayList<Bike> bikes;
	private BigDecimal Totalprice=BigDecimal.ZERO;
	private BigDecimal Totaldeposit = BigDecimal.ZERO;
	
	public Quote(Request request,ArrayList<Bike> bikes,Provider p) {
		this.location = request.getLocation_of_hire();
		this.dateRange= request.getDateRange();
		this.bikes = bikes;
		this.provider=p;
		
		// calculate total rental price
		this.Totalprice=p.calculatePrice(bikes, this.dateRange);
		for (Bike bike:this.bikes) {
			this.Totaldeposit=this.Totaldeposit.add(p.calculateDepositofABike(bike, this.dateRange.getStart()));
		}
		
		
	}

	public void getBooked(BikeRentalOrder order) {
		for(Bike bike: this.bikes) {
			bike.addBookingDates(this.dateRange);
			bike.setCurrent_Order(order);
		}
	}
	public ArrayList<Bike> getBikes() {
		return bikes;
	}


	public void setBikes(ArrayList<Bike> bikes) {
		this.bikes = bikes;
	}
    public BigDecimal getTotalDeposit() {
    	return this.Totaldeposit;
    }

	public Provider getProvider() {
		return provider;
	}

	public Location getLocation() {
		return location;
	}

	public DateRange getDateRange() {
		return dateRange;
	}


	public BigDecimal getTotalprice() {
		return Totalprice;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}
	
	
	public void setTotalprice(BigDecimal totalprice) {
		Totalprice = totalprice;
	}

	@Override
	public int compareTo(Quote q) {
		// TODO Auto-generated method stub
		return this.getTotalprice().compareTo(q.getTotalprice());
	}

}
