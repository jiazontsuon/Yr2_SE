package uk.ac.ed.bikerental;

import java.time.LocalDate;

public class PaymentInfo {
	private String Card_Number;
	private String CVV;
	private LocalDate Expire_Date;
	private Location Billing_Address;
	
	
	public PaymentInfo(String card_no, String cvv,LocalDate Expire_Date,Location Billing_Address) {
		this.Card_Number = card_no;
		this.CVV = cvv;
		this.Expire_Date = Expire_Date;
		this.Billing_Address =  Billing_Address;
		
	}
    
	public boolean checkPaymentinfo(Quote q) {
		if (Expire_Date.isBefore(LocalDate.now())) {
			System.out.println("Your payment has benn sucessful.");
			return false;
		}
		return true;
	}

	public String getCard_Number() {
		return Card_Number;
	}


	public String getCVV() {
		return CVV;
	}


	public LocalDate getExpire_Date() {
		return Expire_Date;
	}


	public Location getBilling_Address() {
		return Billing_Address;
	}


	public void setCard_Number(String card_Number) {
		Card_Number = card_Number;
	}


	public void setCVV(String cVV) {
		CVV = cVV;
	}


	public void setExpire_Date(LocalDate expire_Date) {
		Expire_Date = expire_Date;
	}


	public void setBilling_Address(Location billing_Address) {
		Billing_Address = billing_Address;
	}
}