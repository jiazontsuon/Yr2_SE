package uk.ac.ed.bikerental;

import java.util.ArrayList;

public class Customer {
	private ID Customer_ID;
	private Location Address;
	private PaymentInfo Payment_Info;
	private PersonalInfo personalInfo;
	private ArrayList<BikeRentalOrder> Current_Order;

	public Customer(PersonalInfo pinfo, PaymentInfo pmt_info, Location address) {
		this.Customer_ID = new ID(ID_TYPE.CUSTOMER);
		this.Address = address;
		this.Payment_Info = pmt_info;
		this.personalInfo = pinfo;
		this.Current_Order = new ArrayList<>();

	}
	
	public ArrayList<Quote> QuoteRequest(Request request, ProviderManagementSystem PMS) {
		return PMS.getQuoteFromProviders(request);
	}
	
	public boolean BookQuote(Quote quote, boolean require_delivery) {
		if (Payment_Info.checkPaymentinfo(quote)) {
			BikeRentalOrder res = new BikeRentalOrder(quote, this, require_delivery);
			quote.getProvider().addCurrent_Order(res);
			addCurrent_Order(res);
			return true;
		}
		return false;
	}

	public void addCurrent_Order(BikeRentalOrder new_order) {
		this.Current_Order.add(new_order);
	}

	public ID getCustomer_ID() {
		return Customer_ID;
	}

	public Location getAddress() {
		return Address;
	}

	public PaymentInfo getPayment_Info() {
		return Payment_Info;
	}

	public ArrayList<BikeRentalOrder> getCurrent_Order() {
		return Current_Order;
	}

	public PersonalInfo getPersonalInfo() {
		return personalInfo;
	}
	

}
