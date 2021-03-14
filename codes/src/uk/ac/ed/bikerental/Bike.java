package uk.ac.ed.bikerental;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.HashSet;


public class Bike implements Deliverable {
	private ID Owner_id;
	private Bike_Status Bike_Status;
	private BikeType Bike_Type;
	private ID Bike_ID;
	// private Queue<String> History_Order_info;
	private BigDecimal Daily_Rental_Price;
	private LocalDate Date_of_Purchase;
	// private Deposit Deposit;
	private HashSet<DateRange> BookedDates;
	private BikeRentalOrder Current_Order;
	// private BigDecimal deposit_rate=BigDecimal.ONE;
	// private BikeRentalOrder current_order;

	public Bike(BikeType bktp, LocalDate date, ID owner_id) {
		this.Bike_Status = Bike_Status.INSHOP;
		this.Bike_Type = bktp;
		this.Bike_ID = new ID(ID_TYPE.BIKE);
		this.Date_of_Purchase = date;
		this.BookedDates = new HashSet<>();
		this.Current_Order = null;
		this.Daily_Rental_Price = bktp.getDefaultDailyRentalPrice();
		this.Owner_id = owner_id;
	}

	public void setDaily_Rental_Price(BigDecimal daily_Rental_Price) {
		Daily_Rental_Price = daily_Rental_Price;
	}

	public HashSet<DateRange> getBookedDates() {
		return BookedDates;
	}

	public void setBookedDates(HashSet<DateRange> bookedDates) {
		BookedDates = bookedDates;
	}

	public BigDecimal getRepalcementValue() {
		return this.Bike_Type.getReplacementValue();
	}

	public void setOwner_id(ID id) {
		this.Owner_id = id;
	}

	public ID getOwner_id() {
		return Owner_id;
	}

	public Bike_Status getBike_Status() {
		return Bike_Status;
	}

	public void setBike_Status(Bike_Status bike_Status) {
		Bike_Status = bike_Status;
	}

	public ID getBike_ID() {
		return Bike_ID;
	}

	public void addBookingDates(DateRange daterange) {
		this.BookedDates.add(daterange);
	}

	public BigDecimal getDaily_Rental_Price() {
		return Daily_Rental_Price;
	}

	public LocalDate getDate_of_Purchase() {
		return Date_of_Purchase;
	}

	public BikeRentalOrder getCurrent_Order() {
		return Current_Order;
	}

	public void removeCurrent_Order() {
		this.Current_Order = null;
	}

	public void setCurrent_Order(BikeRentalOrder current_Order) {
		assert this.Current_Order == null;
		Current_Order = current_Order;
	}

	public BikeType getType() {
		// TODO: Implement Bike.getType
		assert (!this.Bike_Type.equals(null));
		return this.Bike_Type;
	}

	public boolean RemoveDatesFrom_BookingDates(DateRange dr) {
		assert (this.BookedDates.contains(dr));
		return this.BookedDates.remove(dr);
	}

	// return true if it is available in given date range, else false
	public boolean Check_Availabilty(DateRange dateRange) {
		for (DateRange dt : this.BookedDates) {
			if (dateRange.overlaps(dt)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void onPickup() {
		Bike_Status = Bike_Status.DELIVERYING;
		this.Current_Order.setOrder_status(Order_Status.BIKEDELIVERING);
	}

	@Override
	public void onDropoff() {
		Bike_Status = Bike_Status.ARRIVED;
		this.Current_Order.setOrder_status(Order_Status.BIKEARRIVED);
	}

}