package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MockMultiDayPricingPolicy implements PricingPolicy {
	HashMap<BikeType,ArrayList<Bike>> bikes;
   
	public MockMultiDayPricingPolicy() {
		this.bikes = new HashMap<BikeType,ArrayList<Bike>>();
	}
	
	public void addBike(Bike bike) {
		this.bikes.putIfAbsent(bike.getType(), new ArrayList<Bike>());
		this.bikes.get(bike.getType()).add(bike);
	}
	
	@Override
	public void setDailyRentalPrice(BikeType bikeType, BigDecimal dailyPrice) {
		// TODO Auto-generated method stub
		ArrayList<Bike> bikes = this.bikes.get(bikeType);
		for(Bike bike: bikes) {
			bike.setDaily_Rental_Price(dailyPrice);
		}
		
	}

	@Override
	public BigDecimal calculatePrice(Collection<Bike> bikes, DateRange duration) {
		// TODO Auto-generated method stub
		BigDecimal TotalPrice = BigDecimal.ZERO;
		for (Bike bike: bikes) {
			TotalPrice = TotalPrice.add(bike.getDaily_Rental_Price());
		}
		BigDecimal discount =BigDecimal.ONE;
		long numberOfDays = duration.toDays();
		if (numberOfDays<=2) {
			discount = BigDecimal.ONE;
		}
		else if(numberOfDays<=6) {
			discount = BigDecimal.valueOf(0.95);
		}
		else if (numberOfDays<=13) {
			discount = BigDecimal.valueOf(0.9);
		}
		else {
			discount = BigDecimal.valueOf(0.85);
		}
		
		BigDecimal FinalPrice = TotalPrice.multiply(discount).multiply(BigDecimal.valueOf(numberOfDays));
		return FinalPrice;
	}

	public HashMap<BikeType, ArrayList<Bike>> getBikes() {
		return bikes;
	}
}
