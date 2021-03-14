package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;


import org.junit.jupiter.api.*;

public class ValuationPolicyTests {
	LinearDepreciationCalculator LDC;
	DoubleDecliningDepreciationCalculator DDDC;
	LocalDate dateofpurchase_bike1; //Purchase date: 2016-12-3
	BikeType biketype;
	Bike bike;
	Provider provider_a;
	ProviderManagementSystem PMS;
	LocalDate testDate1 = LocalDate.of(2019, 12, 3);// bike age: 3 years 
	LocalDate testDate2 = LocalDate.of(2018, 12, 3);// bike age: 2 years 
	LocalDate testDate3 = LocalDate.of(2017, 12, 3);// bike age: 1 years 
	LocalDate testDate4 = LocalDate.of(2023, 12, 3);// bike age: 7 years 
    // You can add attributes here

    @BeforeEach
    void setUp() throws Exception {
    	
    	// set depreciation rate to 0.1
    	this.LDC = new LinearDepreciationCalculator(BigDecimal.valueOf(0.1));
    	this.DDDC = new DoubleDecliningDepreciationCalculator(BigDecimal.valueOf(0.1));
    			
    	//create BikeType, LocalDate object
    	this.biketype = new BikeType("wsd","mo789",BigDecimal.valueOf(900),BigDecimal.valueOf(23.8));
    	this.dateofpurchase_bike1 = LocalDate.of(2016,12, 3); // Bike purchased on 2016-12-3
    			
    	//create Bike object
    	this.bike = new Bike(biketype, dateofpurchase_bike1,new ID(ID_TYPE.PROVIDER));
    			
    	//set up provider object for system_level test
    	 Location address_a = new Location("EH14 8AY","13 Church Rd");
    	 PersonalInfo pi_a = new PersonalInfo("Papa shop","3243 3435","hdhfj@gmail.com");
    	 this.provider_a= new Provider(address_a,BigDecimal.valueOf(0.8),pi_a); // The provider has set the deposit value to be 0.8
         this.provider_a.addBikeIntoStock(this.biketype, this.dateofpurchase_bike1);
		
 		
 			

    }
    @Test
	void TestCalculateValueDDDC() {

//		testDate1 => bike age: 3 years 
//		testDate2 => bike age: 2 years 
//		testDate3 => bike age: 1 years 
//		testDate4 => bike age: 7 years 

		assertEquals(0,DDDC.calculateValue(this.bike, testDate1).compareTo(BigDecimal.valueOf(460.8))); //900 * (1-2*0.1)^3 = 460.8
		assertEquals(0,DDDC.calculateValue(this.bike, testDate2).compareTo(BigDecimal.valueOf(576)));   //900 * (1-2*0.1)^2 = 576
		assertEquals(0,DDDC.calculateValue(this.bike, testDate3).compareTo(BigDecimal.valueOf(720)));   //900 * (1-2*0.1)^1 = 720
		assertEquals(0,DDDC.calculateValue(this.bike, testDate4).compareTo(BigDecimal.valueOf(188.74368)));//900 * (1-2*0.1)^7 = 188.74368
		
	}
    @Test
	void TestCalculateValueLDC() {
//		testDate1 => bike age: 3 years 
//		testDate2 => bike age: 2 years 
//		testDate3 => bike age: 1 years 
//		testDate4 => bike age: 7 years 

		
		assertEquals(0,LDC.calculateValue(this.bike, testDate1).compareTo(BigDecimal.valueOf(630))); // 900- 3*0.1*900 = 630
		assertEquals(0,LDC.calculateValue(this.bike, testDate2).compareTo(BigDecimal.valueOf(720)));// 900- 2*0.1*900 = 720
		assertEquals(0,LDC.calculateValue(this.bike, testDate3).compareTo(BigDecimal.valueOf(810)));// 900- 1*0.1*900 = 810
		assertEquals(0,LDC.calculateValue(this.bike, testDate4).compareTo(BigDecimal.valueOf(270)));// 900- 7*0.1*900 = 270
		
	}
    
    @Test
    void TestProviderCalculateDepositUseDefaultValuationPolicy() {
//		testDate1 => bike age: 3 years 
//		testDate2 => bike age: 2 years 

    	BigDecimal deposit1 = this.provider_a.calculateDepositofABike(this.bike,this.testDate1 );
    	assertEquals(0,BigDecimal.valueOf(720).compareTo(deposit1));//Expected deposit: 900 * 0.8 = 720; 
    	
    	BigDecimal deposit2 = this.provider_a.calculateDepositofABike(this.bike,this.testDate2 );
    	assertEquals(0,BigDecimal.valueOf(720).compareTo(deposit2));//Expected deposit: 900 * 0.8 = 720; 
    
    }
    @Test
    void TestProviderCalculateDepostUseLinearDepreciation() {
//		testDate1 => bike age: 3 years 
//		testDate2 => bike age: 2 years 
//		testDate3 => bike age: 1 years 
//		testDate4 => bike age: 7 years 
    	
    	this.provider_a.SwitchValuationPolicy(1, BigDecimal.valueOf(0.1));
    	
    	BigDecimal deposit1 = this.provider_a.calculateDepositofABike(this.bike,this.testDate1 );
    	assertEquals(0,BigDecimal.valueOf(504).compareTo(deposit1));//Expected deposit: 630 * 0.8 = 504; 
    	
    	BigDecimal deposit2 = this.provider_a.calculateDepositofABike(this.bike,this.testDate2 );
    	assertEquals(0,BigDecimal.valueOf(576).compareTo(deposit2));//Expected deposit: 720 * 0.8 = 576;
    	
    	BigDecimal deposit3 = this.provider_a.calculateDepositofABike(this.bike,this.testDate3 );
    	assertEquals(0,BigDecimal.valueOf(648).compareTo(deposit3));//Expected deposit: 810 * 0.8 = 648;
    	
    	BigDecimal deposit4 = this.provider_a.calculateDepositofABike(this.bike,this.testDate4 );
    	assertEquals(0,BigDecimal.valueOf(216).compareTo(deposit4));//Expected deposit: 270 * 0.8 = 216;
    }
    
    @Test
    void TestProviderCalculateDepostUseDoubleDeclining() {
//		testDate1 => bike age: 3 years 
//		testDate2 => bike age: 2 years 
//		testDate3 => bike age: 1 years 
//		testDate4 => bike age: 7 years 
    	this.provider_a.SwitchValuationPolicy(-1, BigDecimal.valueOf(0.1));
    	
    	BigDecimal deposit1 = this.provider_a.calculateDepositofABike(this.bike,this.testDate1 );
    	assertEquals(0,BigDecimal.valueOf(368.64).compareTo(deposit1));//Expected deposit: 460.8 * 0.8 = 368.64; 
    	
    	BigDecimal deposit2 = this.provider_a.calculateDepositofABike(this.bike,this.testDate2 );
    	assertEquals(0,BigDecimal.valueOf(460.8).compareTo(deposit2));//Expected deposit: 576 * 0.8 = 460.8;
    	
    	BigDecimal deposit3 = this.provider_a.calculateDepositofABike(this.bike,this.testDate3 );
    	assertEquals(0,BigDecimal.valueOf(576).compareTo(deposit3));//Expected deposit: 720 * 0.8 = 576;
    	
    	BigDecimal deposit4 = this.provider_a.calculateDepositofABike(this.bike,this.testDate4 );
    	assertEquals(0,BigDecimal.valueOf(150.994944).compareTo(deposit4));//Expected deposit: 188.74368 * 0.8 = 150.994944;
    }
    
    @Test
    void TestDepositInQuoteUseLinearDepreciation() {
//		testDate1 => bike age: 3 years 
//		testDate2 => bike age: 2 years 
//		testDate3 => bike age: 1 years 
//		testDate4 => bike age: 7 years 
    	this.provider_a.SwitchValuationPolicy(1, BigDecimal.valueOf(0.1)); //Switch to Linear depreciation method, set depreciation rate to 0.1
    	
    	DateRange dtrg = new DateRange(LocalDate.of(2019, 12, 3),LocalDate.of(2019, 12, 14));
    	Location requested_location = this.provider_a.getShopAddress(); // pick area around provider a's shop which is EH...
    	HashMap<BikeType,Integer> requested_bikes = new HashMap<BikeType,Integer>();
    	requested_bikes.put(this.biketype, 1); //request 1 bike of the type established in setup()
        Request req1 = new Request(dtrg,requested_location,requested_bikes);
        this.provider_a.addBikeIntoStock(this.biketype, this.dateofpurchase_bike1); // add the bike purchased in 2016 into provider_a's stock
        
        Quote q = this.provider_a.GenerateQuote(req1);
        // bike age = 2019-2016 =3; deposit rate =0.8; Expected deposit = 504
        assertEquals(0,BigDecimal.valueOf(504).compareTo(q.getTotalDeposit()));
        
    }
    
    @Test
    void TestDepositInQuoteUseDoubleDecliningDepreciation() {
//		testDate1 => bike age: 3 years 
//		testDate2 => bike age: 2 years 
//		testDate3 => bike age: 1 years 
//		testDate4 => bike age: 7 years 
    	this.provider_a.SwitchValuationPolicy(-1, BigDecimal.valueOf(0.1)); //Switch to double declining depreciation method, set depreciation rate to 0.1
    	
    	DateRange dtrg = new DateRange(LocalDate.of(2019, 12, 3),LocalDate.of(2019, 12, 14));
    	Location requested_location = this.provider_a.getShopAddress(); // pick area around provider a's shop which is EH...
    	HashMap<BikeType,Integer> requested_bikes = new HashMap<BikeType,Integer>();
    	requested_bikes.put(this.biketype, 1); //request 1 bike of the type established in setup()
        Request req1 = new Request(dtrg,requested_location,requested_bikes);
        this.provider_a.addBikeIntoStock(this.biketype, this.dateofpurchase_bike1); // add the bike purchased in 2016 into provider_a's stock
        
        Quote q = this.provider_a.GenerateQuote(req1);
        // bike age = 2019-2016 =3; deposit rate =0.8; Expected deposit = 368.64
        assertEquals(0,BigDecimal.valueOf(368.64).compareTo(q.getTotalDeposit()));
        
    }
    
    
    
    
    
}
