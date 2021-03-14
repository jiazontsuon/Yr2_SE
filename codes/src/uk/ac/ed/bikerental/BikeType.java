package uk.ac.ed.bikerental;

import java.math.BigDecimal;


public class BikeType {
	
	private String name;
	private String model;
	private BigDecimal replacementValue;
	private BigDecimal DefaultdailyRentalPrice;

	public BikeType(String name,String model,BigDecimal replacementValue,BigDecimal dailyRentalPrice) {
		this.name = name;
		this.model =model;
		this.replacementValue = replacementValue;
		this.DefaultdailyRentalPrice = dailyRentalPrice;
	}
	
 

	public BigDecimal getDefaultDailyRentalPrice() {
		return DefaultdailyRentalPrice;
	}



	public BigDecimal getReplacementValue() {
		return this.replacementValue;
	}

	
    
}