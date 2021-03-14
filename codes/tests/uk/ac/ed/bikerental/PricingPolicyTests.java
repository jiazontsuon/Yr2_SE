package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.*;


public class PricingPolicyTests {
    // You can add attributes here
	LocalDate dateofpurchase_bike1; //Purchase date: 2016-12-3
	BikeType biketype;
	//Bike bike;
	Provider provider_a;

    @BeforeEach
    void setUp() throws Exception {
        // Put setup here
    	this.biketype = new BikeType("wsd","mo789",BigDecimal.valueOf(900),BigDecimal.valueOf(23.8));
    	this.dateofpurchase_bike1 = LocalDate.of(2016,12, 3); // Bike purchased on 2016-12-3
    			
    	//create Bike object
    	//this.bike = new Bike(biketype, dateofpurchase_bike1,new ID(ID_TYPE.PROVIDER));
    	//create provider(PricingPolicy) object 
    	Location address_a = new Location("EH14 8AY","13 Church Rd");
   	    PersonalInfo pi_a = new PersonalInfo("Papa shop","3243 3435","hdhfj@gmail.com");
   	    this.provider_a= new Provider(address_a,BigDecimal.valueOf(0.8),pi_a); 
   	   
   	    
    }
    @Test
    void TestSetDailyRentalPrice() {
    	this.provider_a.addBikeIntoStock(biketype, this.dateofpurchase_bike1);
    	Bike bike = this.provider_a.getStock().get(this.biketype).get(0); // The only bike in the stock
    	
    	this.provider_a.setDailyRentalPrice(this.biketype, BigDecimal.valueOf(100));//set daily rental price of bike to 100
    	assertEquals(0,BigDecimal.valueOf(100).compareTo(bike.getDaily_Rental_Price())); // expected price is 100
    	

    	this.provider_a.setDailyRentalPrice(this.biketype, BigDecimal.valueOf(2346));//set daily rental price of bike to 2346
    	assertEquals(0,BigDecimal.valueOf(2346).compareTo(bike.getDaily_Rental_Price())); // expected price is 2346
    	
    	this.provider_a.setDailyRentalPrice(this.biketype, BigDecimal.valueOf(678));//set daily rental price of bike to 678
    	assertEquals(0,BigDecimal.valueOf(678).compareTo(bike.getDaily_Rental_Price())); // expected price is 678
    }
    
    @Test
    void TestcalculatePrice() {
    	DateRange DR_1 = new DateRange(LocalDate.of(2019, 12, 1),LocalDate.of(2019, 12, 4)); // duration: 3 days
    	DateRange DR_2 = new DateRange(LocalDate.of(2019, 12, 1),LocalDate.of(2019, 12, 5)); // duration: 4 days
    	DateRange DR_3 = new DateRange(LocalDate.of(2019, 12, 1),LocalDate.of(2019, 12, 6)); // duration: 5 days
    	
    	
    	//Add three bikes of the same bike type into the stock of provider_a
    	this.provider_a.addBikeIntoStock(this.biketype, this.dateofpurchase_bike1);
    	this.provider_a.addBikeIntoStock(this.biketype, this.dateofpurchase_bike1);
    	this.provider_a.addBikeIntoStock(this.biketype, this.dateofpurchase_bike1);
    	this.provider_a.setDailyRentalPrice(this.biketype, BigDecimal.valueOf(100));//set daily rental price of bikes to 100
    	
    	BigDecimal totalPrice = this.provider_a.calculatePrice(this.provider_a.getStock().get(this.biketype), DR_1);// renting 3 bikes for 3 days
    	assertEquals(0,BigDecimal.valueOf(900).compareTo(totalPrice)); // Expected price = 3*100*3= 900
    	
    	totalPrice = this.provider_a.calculatePrice(this.provider_a.getStock().get(this.biketype), DR_2);// renting 3 bikes for 4 days
    	assertEquals(0,BigDecimal.valueOf(1200).compareTo(totalPrice)); // Expected price = 3*100*4= 1200
    	
    	totalPrice = this.provider_a.calculatePrice(this.provider_a.getStock().get(this.biketype), DR_3);// renting 3 bikes for 5 days
    	assertEquals(0,BigDecimal.valueOf(1500).compareTo(totalPrice)); // Expected price = 3*100*5= 1500
    }
    @Test
    void TestMockMultiDayPricingPolicy() {
    	//Duration  = 2 days
    	DateRange dtrg1 = new DateRange(LocalDate.of(2019, 12, 3), LocalDate.of(2019, 12, 5));
    	//Duration  = 4 days
    	DateRange dtrg2 = new DateRange(LocalDate.of(2019, 12, 3), LocalDate.of(2019, 12, 7));
    	//Duration  = 8 days
    	DateRange dtrg3 = new DateRange(LocalDate.of(2019, 12, 3), LocalDate.of(2019, 12, 11));
    	//Duration  = 15 days
    	DateRange dtrg4 = new DateRange(LocalDate.of(2019, 12, 3), LocalDate.of(2019, 12, 18));
    
    	
    	Bike bike1 = new Bike(this.biketype,this.dateofpurchase_bike1,this.provider_a.getId());
    	Bike bike2 = new Bike(this.biketype,this.dateofpurchase_bike1,this.provider_a.getId());
    	
    	
    	MockMultiDayPricingPolicy mock = new MockMultiDayPricingPolicy();
    	mock.addBike(bike1);
    	mock.addBike(bike2);
    	
    	//set daily rental price of all the bikes that has the same biketype to 15
    	mock.setDailyRentalPrice(this.biketype,BigDecimal.valueOf(15));
    	
    	//renting 2 bikes of  daily rental price of 15 for 2 days should cost 15*2*2*1 = 60
    	assertEquals(0,BigDecimal.valueOf(60).compareTo(mock.calculatePrice(mock.getBikes().get(this.biketype),dtrg1)));
    	//renting 2 bikes of  daily rental price of 15 for 4 days should cost 15*2*4*0.95 = 114
    	assertEquals(0,BigDecimal.valueOf(114).compareTo(mock.calculatePrice(mock.getBikes().get(this.biketype),dtrg2)));
    	//renting 2 bikes of  daily rental price of 15 for 8 days should cost 15*2*8*0.9 = 216
    	assertEquals(0,BigDecimal.valueOf(216).compareTo(mock.calculatePrice(mock.getBikes().get(this.biketype),dtrg3)));
    	//renting 2 bikes of  daily rental price of 15 for 15 days should cost 15*2*15*0.85 = 382.5
    	assertEquals(0,BigDecimal.valueOf(382.5).compareTo(mock.calculatePrice(mock.getBikes().get(this.biketype),dtrg4)));
    }
    
}