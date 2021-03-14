package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.ArrayList;


public class BikeRentalOrder{
	private ID order_id;
	private ID customer_id;
	private ID provider_id;
	private Location customer_address;
	private Location provider_address;
	private ArrayList<Bike> ordered_bikes;
	private DateRange daterange;
	private boolean Require_Delivery;
	private Order_Status order_status;
	private BigDecimal totalDeposit;

	public BikeRentalOrder(Quote quote, Customer customer, boolean Require_Delivery) {
		Provider p = quote.getProvider();

		this.order_id = new ID(ID_TYPE.ORDER);
		this.customer_id = customer.getCustomer_ID();
		this.provider_id = p.getId();
		this.customer_address = customer.getAddress();
		this.provider_address = p.getShopAddress();
		this.daterange = quote.getDateRange();								
		this.Require_Delivery = Require_Delivery;
		this.order_status = Order_Status.BOOKED;
		this.totalDeposit = quote.getTotalDeposit();
		quote.getBooked(this);
		this.totalDeposit = quote.getTotalDeposit();

		this.ordered_bikes = quote.getBikes();

		// if the customer want Delivery
		if (this.Require_Delivery) {
			DeliveryService DS = new MockDeliveryService();
			for(Bike bike: ordered_bikes) {
				DS.scheduleDelivery(bike,provider_address,this.customer_address, this.daterange.getStart());
			}
		}
	}
	
	
	
	public ArrayList<Bike> getOrdered_bikes() {
		return ordered_bikes;
	}

    

	public BigDecimal getTotalDeposit() {
		return totalDeposit;
	}



	public void setTotalDeposit(BigDecimal totalDeposit) {
		this.totalDeposit = totalDeposit;
	}



	public void setOrdered_bikes(ArrayList<Bike> ordered_bikes) {
		this.ordered_bikes = ordered_bikes;
	}



	public ID getCustomer_id() {
		return customer_id;
	}


	public void setCustomer_id(ID customer_id) {
		this.customer_id = customer_id;
	}


	public ID getProvider_id() {
		return provider_id;
	}


	public void setProvider_id(ID provider_id) {
		this.provider_id = provider_id;
	}


	public Order_Status getOrder_status() {
		return order_status;
	}


	public void setOrder_status(Order_Status order_status) {
		this.order_status = order_status;
	}


	public DateRange getDaterange() {
		return daterange;
	}

	public void setDaterange(DateRange daterange) {
		this.daterange = daterange;
	}

	public ID getId() {
		return this.order_id;
	}
	


}
