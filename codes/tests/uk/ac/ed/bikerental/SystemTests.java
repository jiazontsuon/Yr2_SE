package uk.ac.ed.bikerental;

import org.junit.jupiter.api.*;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;


public class SystemTests {
	// You can add attributes here
	ProviderManagementSystem PMS;
	Provider a;
	Provider b;
	Provider c;
	Customer Jane;
	Customer Harry;
	BikeType biketype1;
	BikeType biketype2;
	BikeType biketype3;
	Request req1;
	Request req2;

	@BeforeEach
	void setUp() throws Exception {
		// Setup mock delivery service before each tests
		DeliveryServiceFactory.setupMockDeliveryService();

		// -----------------------------------------------------------------
		// Locations for providers
		Location address_a = new Location("EH14 8AY", "13 Church Rd");
		Location address_b = new Location("EH13 8AY", "18 sourth bridge");
		Location address_c = new Location("EH47 M14", "Old College");

		Location address_Jane = new Location("AK34 M87", "Candy Rd");
		Location address_Harry = new Location("EH67 9UI", "JMCC Rd");
		// -----------------------------------------------------------------

		// -----------------------------------------------------------------
		// Personal information
		PersonalInfo pi_a = new PersonalInfo("Papa shop", "3243 3435", "hdhfj@gmail.com");
		PersonalInfo pi_b = new PersonalInfo("Social Bike", "5475 9274", "ur74sfs@gmail.com");
		PersonalInfo pi_c = new PersonalInfo("Hello World Biek shop", "7884 6758", "dfgyihf7237@gmail.com");
		PersonalInfo pi_Jane = new PersonalInfo("Jane", "5475 9759", "yhsjkj73r@gmail.com");
		PersonalInfo pi_Harry = new PersonalInfo("Harry", "7473 7860", "dsgfyh4y8@gmail.com");

		// -----------------------------------------------------------------
		// initialise bike provider
		this.a = new Provider(address_a, BigDecimal.valueOf(0.8), pi_a);
		this.b = new Provider(address_b, BigDecimal.valueOf(0.62), pi_b);
		this.c = new Provider(address_c, BigDecimal.valueOf(0.89), pi_c);
		// ------------------------------------------------------------------
		// ------------------------------------------------------------------
		// initialise Payment Info , constructor parameters: String card_no, String
		// cvv,LocalDate Expire_Date,Location Billing_Address
		PaymentInfo payment_Jane = new PaymentInfo("5646 4367 4536 6343", "567", LocalDate.of(2023, 9, 12),
				address_Jane);
		PaymentInfo payment_Harry = new PaymentInfo("5646 4367 4546 6343", "597", LocalDate.of(2023, 9, 12),
				address_Harry);
		// ------------------------------------------------------------------
		// Initialise customer objects
		this.Jane = new Customer(pi_Jane, payment_Jane, address_Jane);
		this.Harry = new Customer(pi_Harry, payment_Harry, address_Harry);
		// ------------------------------------------------------------------
		// bike type, constructor parameters:String name,String model,BigDecimal
		// replacementValue,BigDecimal dailyRentalPrice
		this.biketype1 = new BikeType("wsd", "mo789", BigDecimal.valueOf(289.5), BigDecimal.valueOf(23.8));
		this.biketype2 = new BikeType("tfb", "at769", BigDecimal.valueOf(233.5), BigDecimal.valueOf(19.8));
		this.biketype3 = new BikeType("rtd", "mtg889", BigDecimal.valueOf(492.5), BigDecimal.valueOf(40.5));
		// ------------------------------------------------------------------
		// LocalDate DateOfPurchase
		LocalDate dateofpurchase_bike1 = LocalDate.of(2018, 4, 8);
		LocalDate dateofpurchase_bike2 = LocalDate.of(2013, 4, 19);
		LocalDate dateofpurchase_bike3 = LocalDate.of(2015, 7, 30);
		LocalDate dateofpurchase_bike4 = LocalDate.of(2016, 7, 20);
		LocalDate dateofpurchase_bike5 = LocalDate.of(2018, 7, 30);
		// Initialise Bike object and add them into Stock of Providers, constructor
		// parameters:BikeType bktp,LocalDate date
		// provide a owns: 1 biketype1 bike, 1 biketype2 bike
		a.addBikeIntoStock(biketype1, dateofpurchase_bike1);
		a.addBikeIntoStock(biketype2, dateofpurchase_bike2);

		// provide b owns: 1 biketype2 bike, 1 biketype3 bike
		b.addBikeIntoStock(biketype2, dateofpurchase_bike2);
		b.addBikeIntoStock(biketype3, dateofpurchase_bike3);
		// provide c owns: 1 biketype1 bike, 1 biketype2 bike
		c.addBikeIntoStock(biketype1, dateofpurchase_bike4);
		c.addBikeIntoStock(biketype2, dateofpurchase_bike5);

		// create a Request object: req1
		DateRange dtrg1 = new DateRange(LocalDate.of(2019, 12, 3), LocalDate.of(2019, 12, 14));
		Location requested_location1 = this.a.getShopAddress(); // pick area around provider a's shop which is EH...
		HashMap<BikeType, Integer> requested_bikes1 = new HashMap<BikeType, Integer>();
		requested_bikes1.put(this.biketype3, 1); // request 1 bike of biketype3
		requested_bikes1.put(biketype2, 1);// request 1 bike of biketype2
		this.req1 = new Request(dtrg1, requested_location1, requested_bikes1);

		// create another Request object: req2
		DateRange dtrg2 = new DateRange(LocalDate.of(2019, 12, 3), LocalDate.of(2019, 12, 14));
		Location requested_location2 = this.a.getShopAddress(); // pick area around provider a's shop which is EH...
		HashMap<BikeType, Integer> requested_bikes2 = new HashMap<BikeType, Integer>();
		requested_bikes2.put(this.biketype1, 1); // request 1 bike of biketype1
		requested_bikes2.put(this.biketype2, 1);// request 1 bike of biketype2
		this.req2 = new Request(dtrg2, requested_location2, requested_bikes2);

		// Add Bike into Provider's stock

		// ------------------------------------------------------------------
		// Initialising provideBigDecimal.of()rMangementSystem
		this.PMS = new ProviderManagementSystem();
		PMS.addProvider(this.a);
		PMS.addProvider(this.b);
		PMS.addProvider(this.c);
	}

	@Test
	void TestGetQuoteSuccessful1() {

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case1: Get Quote1 |Customer: Harry |requested date: 2019-12-3 to
		// 2019-12-14 | bike(s)Requested: 1 bike of biketype3 & 1 bike of biketype2 |
		// Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// GetQuote
		ArrayList<Quote> quotes = this.Harry.QuoteRequest(this.req1, this.PMS);

		Quote q = quotes.get(0); // get the only quote from arraylist
		assertEquals(1, quotes.size()); // [CHECK] There should be exactly one quote generated
		// test that all the bikes in each quote are available in the date range
		// requested by customer
		assertEquals(b, quotes.get(0).getProvider());// [CHECK] The quote should be from provider b

		// test that all the bikes in each quote are available in the date range
		// requested by customer
		for (Bike bike : q.getBikes()) {
			assertTrue(bike.Check_Availabilty(req1.getDateRange())); // [CHECK] every bike in quote is available between
																		// the date range specified in Request object

			// [CHECK] total deposit
			// in this case the quote contains two bike that has replacement value 492.5 and
			// 233.5 respectively,
			// provider has deposit rate 0.62, the total deposit must be 450.12;
			assertEquals(0, BigDecimal.valueOf(450.12).compareTo(q.getTotalDeposit()));

			// [CHECK] total price
			// The bikes have daily rental price(40.5 & 19.8) and it is rented for 11 days
			// the total price should be 11*(40.5+19.8) = 663.3
			assertEquals(0, BigDecimal.valueOf(663.3).compareTo(q.getTotalprice()));
		}
		// +++++++++++++++++++++++++++++++++++++++++END1+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	}

	@Test
	void TestGetQuoteSuccessful2() {
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case2: Get Quote2 |Customer: Harry |requested date: 2019-12-3 to
		// 2019-12-14 | bike(s)Requested: 1 biketype1 bike & 1 biketype2 bike |
		// Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// GetQuote
		ArrayList<Quote> quotes = this.Harry.QuoteRequest(this.req2, PMS);

		assertEquals(2, quotes.size()); // [CHECK] There should be exactly two quote generated
		assertEquals(a, quotes.get(0).getProvider());// [CHECK] one of the quotes should be from provider a
		assertEquals(c, quotes.get(1).getProvider());// [CHECK] another quote should be from provider c

		// ****************************************************************************************************************
		// For Quote from Provider a:
		Quote QuoteFromProvider_a = quotes.get(0);
		for (Bike bike : QuoteFromProvider_a.getBikes()) {
			assertTrue(bike.Check_Availabilty(req2.getDateRange())); // [CHECK] every bike in quote from provider a is
																		// available between the date range specified in
																		// Request object
		}
		// [CHECK] total deposit
		// in this case the quote contains two bike that has replacement value 289.5 and
		// 233.5 respectively,
		// provider a has deposit rate 0.8, the total deposit must be 418.4;
		assertEquals(0, BigDecimal.valueOf(418.4).compareTo(QuoteFromProvider_a.getTotalDeposit()));

		// [CHECK] total price
		// The bikes have daily rental price(23.8 & 19.8) and it is rented for 11
		// days++++++++++++
		// the total price should be 11*(23.8+19.8) = 479.6
		assertEquals(0, BigDecimal.valueOf(479.6).compareTo(QuoteFromProvider_a.getTotalprice()));
		// ***************************************************************************************************************

		// ***************************************************************************************************************
		// For Quote from Provider c:
		Quote QuoteFromProvider_c = quotes.get(1);
		for (Bike bike : QuoteFromProvider_c.getBikes()) {
			assertTrue(bike.Check_Availabilty(req2.getDateRange())); // [CHECK] every bike in quote from provider a is
																		// available between the date range specified in
																		// Request object
		}
		// [CHECK] total deposit
		// in this case the quote contains two bike that has replacement value 289.5 and
		// 233.5 respectively,
		// provider a has deposit rate 0.89, the total deposit must be 465.47;
		assertEquals(0, BigDecimal.valueOf(465.47).compareTo(QuoteFromProvider_c.getTotalDeposit()));
		
		// [CHECK] total price
		// The bikes have daily rental price(23.8 & 19.8) and it is rented for 11 days
		// the total price should be 11*(23.8+19.8) = 479.6
		assertEquals(0, BigDecimal.valueOf(479.6).compareTo(QuoteFromProvider_c.getTotalprice()));
		// ***************************************************************************************************************
	}

	@Test
	void TestBookedQuoteSuccessful() {

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case1: Get Quote1 |Customer: Harry | requested date: 2019-12-3 to
		// 2019-12-14 | bike(s)Requested: 1 bike of biketype3 & 1 bike of biketype2 |
		// Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		ArrayList<Quote> quotes = this.Harry.QuoteRequest(this.req1, PMS);

		Quote q = quotes.get(0); // get the only quote from arraylist
		assertEquals(1, quotes.size()); // [CHECK] There should be exactly one quote generated
		// test that all the bikes in each quote are available in the date range
		// requested by customer
		assertEquals(b, quotes.get(0).getProvider());// [CHECK] The quote should be from provider b

		// test that all the bikes in each quote are available in the date range
		// requested by customer
		for (Bike bike : q.getBikes()) {
			assertTrue(bike.Check_Availabilty(req1.getDateRange())); // [CHECK] every bike in quote is available between
																		// the date range specified in Request object

			// [CHECK] total deposit
			// in this case the quote contains two bike that has replacement value 492.5 and
			// 233.5 respectively,
			// provider has deposit rate 0.62, the total deposit must be 450.12;
			assertEquals(0, BigDecimal.valueOf(450.12).compareTo(q.getTotalDeposit()));

			// [CHECK] total price
			// The bikes have daily rental price(40.5 & 19.8) and it is rented for 11 days
			// the total price should be 11*(40.5+19.8) = 663.3++++++++++++
			assertEquals(0, BigDecimal.valueOf(663.3).compareTo(q.getTotalprice()));
		}
		// +++++++++++++++++++++++++++++++++++++++++END1+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case3: Book Quote | Customer: Harry |requested date: 2019-12-3 to
		// 2019-12-14 | bike(s)Requested: 1 bike of biketype3 | Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// we will not test delivery service in this use case, so the second argument
		// (boolean requireDelivery) is false
		this.Harry.BookQuote(quotes.get(0), true);
		BikeRentalOrder order = this.Harry.getCurrent_Order().get(0);
		assertEquals(1, this.Harry.getCurrent_Order().size()); // [CHECK] there is exactly one order existing in the
																// ArrayList<BikeRentalOrder> Current_Orders in Harry
																// object
		assertEquals(order, this.b.getCurrentOrders().get(0));// [CHECK] The order is recorded in provider b object;

		assertEquals(order.getProvider_id(), this.b.getId());// [CHECK]provider b's id is stored in the order
		assertEquals(order.getCustomer_id(), this.Harry.getCustomer_ID()); // [CHECK]Customer Harry's id is stored in
																			// the order
		assertTrue(order.getOrdered_bikes().contains(this.b.getStock().get(this.biketype3).get(0))); // [CHECK] the
																										// ordered bikes
																										// contains a
																										// biketye3
																										// bike in
																										// b's Stock
		assertTrue(order.getOrdered_bikes().contains(this.b.getStock().get(this.biketype2).get(0)));// [CHECK] the
																									// ordered
																									// bikes
																									// contains a
																									// biketype2
																									// bike in
																									// b's Stock

		// -------------------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------------------
		// testing delivery service
		// -------------------------------------------------------------------------------------------------------------
		
		MockDeliveryService ds = (MockDeliveryService) DeliveryServiceFactory.getDeliveryService();
		Bike bikeToDeliver = order.getOrdered_bikes().get(0);
		LocalDate DeliveryingDate = order.getDaterange().getStart();
		ds.scheduleDelivery(bikeToDeliver, this.b.getShopAddress(), this.Harry.getAddress(), DeliveryingDate);

		// carry out pickups
		ds.carryOutPickups(DeliveryingDate);
		assertEquals(Bike_Status.DELIVERYING, bikeToDeliver.getBike_Status());// [CHECK]Bike status changes to
																				// DELIVERING
		assertEquals(Order_Status.BIKEDELIVERING, order.getOrder_status());// [CHECK] Order status changes to
																			// BIKEDELIVERING
		// carry out dropoff
		ds.carryOutDropoffs();
		assertEquals(Bike_Status.ARRIVED, bikeToDeliver.getBike_Status()); // [CHECK]Bike status changes to ARRIVED
		assertEquals(Order_Status.BIKEARRIVED, order.getOrder_status());// [CHECK] Order status changes to BIKEARRIVED
		// +++++++++++++++++++++++++++++++++++++++++END2++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	}

	@Test
	void TestGetQuoteFailDueToTimeClash() {

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case1: Get Quote1 | Customer: Harry |requested date: 2019-12-3 to
		// 2019-12-14 | bike(s)Requested: 1 bike of biketype3 & 1 bike of biketype2 |
		// Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		ArrayList<Quote> quotes = this.Harry.QuoteRequest(this.req1, PMS);

		Quote q = quotes.get(0); // get the only quote from arraylist
		assertEquals(1, quotes.size()); // [CHECK] There should be exactly one quote generated
		// test that all the bikes in each quote are available in the date range
		// requested by customer
		assertEquals(b, quotes.get(0).getProvider());// [CHECK] The quote should be from provider b

		// test that all the bikes in each quote are available in the date range
		// requested by customer
		for (Bike bike : q.getBikes()) {
			assertTrue(bike.Check_Availabilty(req1.getDateRange())); // [CHECK] every bike in quote is available between
																		// the date range specified in Request object
		}
		// [CHECK] total deposit
		// in this case the quote contains two bike that has replacement value 492.5 and
		// 233.5 respectively,
		// provider has deposit rate 0.62, the total deposit must be 450.12;
		assertEquals(0, BigDecimal.valueOf(450.12).compareTo(q.getTotalDeposit()));

		// [CHECK] total price
		// The bikes have daily rental price(40.5 & 19.8) and it is rented for 11 days
		// the total price should be 11*(40.5+19.8) = 663.3
		assertEquals(0, BigDecimal.valueOf(663.3).compareTo(q.getTotalprice()));
		// +++++++++++++++++++++++++++++++++++++++++END1+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case3: Book Quote | Customer: Harry |requested date: 2019-12-3 to
		// 2019-12-14 | bike(s)Requested: 1 bike of biketype3 | Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// we will not test delivery service in this use case, so the second argument
		// (boolean requireDelivery) is false
		this.Harry.BookQuote(quotes.get(0), true);
		BikeRentalOrder order = this.Harry.getCurrent_Order().get(0);
		assertEquals(1, this.Harry.getCurrent_Order().size()); // [CHECK] there is exactly one order existing in the
																// ArrayList<BikeRentalOrder> Current_Orders in Harry
																// object
		assertEquals(order, this.b.getCurrentOrders().get(0));// [CHECK] The order is recorded in provider b object;

		assertEquals(order.getProvider_id(), this.b.getId());// [CHECK] provider b's id is stored in the order
		assertEquals(order.getCustomer_id(), this.Harry.getCustomer_ID()); // [CHECK]Customer Harry's id is stored in
																			// the order
		assertTrue(order.getOrdered_bikes().contains(this.b.getStock().get(this.biketype3).get(0))); // [CHECK] the
																										// ordered bikes
																										// contains a
																										// bike of
																										// biketype3 in
																										// b's Stock
		assertTrue(order.getOrdered_bikes().contains(this.b.getStock().get(this.biketype2).get(0)));// [CHECK] the
																									// ordered bikes
																									// contains a bike
																									// of biketype2 in
																									// b's Stock

		// -------------------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------------------
		// testing delivery service
		// -------------------------------------------------------------------------------------------------------------
		
		MockDeliveryService ds = (MockDeliveryService) DeliveryServiceFactory.getDeliveryService();
		Bike bikeToDeliver = order.getOrdered_bikes().get(0);
		LocalDate DeliveryingDate = order.getDaterange().getStart();
		ds.scheduleDelivery(bikeToDeliver, this.b.getShopAddress(), this.Harry.getAddress(), DeliveryingDate);

		// carry out pickups
		ds.carryOutPickups(DeliveryingDate);
		assertEquals(Bike_Status.DELIVERYING, bikeToDeliver.getBike_Status());// [CHECK]Bike status changes to
																				// DELIVERING
		assertEquals(Order_Status.BIKEDELIVERING, order.getOrder_status());// [CHECK]Order status changes to
																			// BIKEDELIVERING
		// carry out dropoff
		ds.carryOutDropoffs();
		assertEquals(Bike_Status.ARRIVED, bikeToDeliver.getBike_Status()); // [CHECK]Bike status changes to ARRIVED
		assertEquals(Order_Status.BIKEARRIVED, order.getOrder_status());// [CHECK]Order status changes to BIKEARRIVED
		// +++++++++++++++++++++++++++++++++++++++++END2++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case4: get Quote | Customer: Jane |requested date: 2019-12-3 to
		// 2019-12-5 | bike(s)Requested: 1 bike of biketype3| Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		DateRange daterange = new DateRange(LocalDate.of(2019, 12, 3), LocalDate.of(2019, 12, 5));
		Location location = this.a.getShopAddress(); // pick area around provider a's shop which is EH...
		HashMap<BikeType, Integer> request_bike = new HashMap<BikeType, Integer>();
		request_bike.put(this.biketype3, 1); // request 1 bike of biketype3
		Request req2 = new Request(daterange, location, request_bike);// Initialise request object
		// We want to test the scenario that someone tries to get a bike of biketype3
		// between 2019-12-3 and 2019-12-5.
		// We expect that the system will return an empty list of quotes for the request
		// since the bike at that period is booked;
		assertTrue(this.Jane.QuoteRequest(req2, PMS).isEmpty()); // [CHECK] System returns an empty arrayList of quotes
		// +++++++++++++++++++++++++++++++++++++++++END3++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

	}

	@Test
	void TestBikeReturnToOriginalProvider() {

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case1: Get Quote1 | Customer: Harry |requested date: 2019-12-3 to
		// 2019-12-14 | bike(s)Requested: 1 bike of biketype3 & 1 bike of biketype2 |
		// Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		ArrayList<Quote> quotes = this.Harry.QuoteRequest(this.req1, PMS);

		Quote q = quotes.get(0); // get the only quote from arraylist
		assertEquals(1, quotes.size()); // [CHECK] There should be exactly one quote generated
		// test that all the bikes in each quote are available in the date range
		// requested by customer
		assertEquals(b, quotes.get(0).getProvider());// [CHECK] The quote should be from provider b

		// test that all the bikes in each quote are available in the date range
		// requested by customer
		for (Bike bike : q.getBikes()) {
			assertTrue(bike.Check_Availabilty(req1.getDateRange())); // [CHECK] every bike in quote is available between
																		// the date range specified in Request object
		}
		// [CHECK] total deposit
		// in this case the quote contains two bike that has replacement value 492.5 and
		// 233.5 respectively,
		// provider has deposit rate 0.62, the total deposit must be 450.12;
		assertEquals(0, BigDecimal.valueOf(450.12).compareTo(q.getTotalDeposit()));

		// [CHECK] total price
		// The bikes have daily rental price(40.5 & 19.8) and it is rented for 11 days
		// the total price should be 11*(40.5+19.8) = 663.3
		assertEquals(0, BigDecimal.valueOf(663.3).compareTo(q.getTotalprice()));
		// +++++++++++++++++++++++++++++++++++++++++END1+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case3: Book Quote | Customer: Harry |requested date: 2019-12-3 to
		// 2019-12-14 | bike(s)Requested: 1 bike of biketype3 | Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// we will not test delivery service in this use case, so the second argument
		// (boolean requireDelivery) is false
		this.Harry.BookQuote(quotes.get(0), true);
		BikeRentalOrder order = this.Harry.getCurrent_Order().get(0);
		assertEquals(1, this.Harry.getCurrent_Order().size()); // [CHECK] there is exactly one order existing in the
																// ArrayList<BikeRentalOrder> Current_Orders in Harry
																// object
		assertEquals(order, this.b.getCurrentOrders().get(0));// [CHECK] The order is recorded in provider b object;

		assertEquals(order.getProvider_id(), this.b.getId());// [CHECK]provider b's id is stored in the order
		assertEquals(order.getCustomer_id(), this.Harry.getCustomer_ID()); // [CHECK] Customer Harry's id is stored in
																			// the order
		assertTrue(order.getOrdered_bikes().contains(this.b.getStock().get(this.biketype3).get(0))); // [CHECK] the
																										// ordered bikes
																										// contains a
																										// bike of
																										// biketype3 in
																										// b's Stock
		assertTrue(order.getOrdered_bikes().contains(this.b.getStock().get(this.biketype2).get(0)));// [CHECK] the
																									// ordered bikes
																									// contains a bike
																									// of biketype2 in
																									// b's Stock

		// -------------------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------------------
		// testing delivery service
		// -------------------------------------------------------------------------------------------------------------
		MockDeliveryService ds = (MockDeliveryService) DeliveryServiceFactory.getDeliveryService();
		Bike bikeToDeliver = order.getOrdered_bikes().get(0);
		LocalDate DeliveryingDate = order.getDaterange().getStart();
		ds.scheduleDelivery(bikeToDeliver, this.b.getShopAddress(), this.Harry.getAddress(), DeliveryingDate);

		// carry out pickups
		ds.carryOutPickups(DeliveryingDate);
		assertEquals(Bike_Status.DELIVERYING, bikeToDeliver.getBike_Status());// [CHECK]Bike status changes to
																				// DELIVERING
		assertEquals(Order_Status.BIKEDELIVERING, order.getOrder_status());// [CHECK]Order status changes to
																			// BIKEDELIVERING
		// carry out dropoffbiketype2
		ds.carryOutDropoffs();
		assertEquals(Bike_Status.ARRIVED, bikeToDeliver.getBike_Status()); // [CHECK]Bike status changes to ARRIVED
		assertEquals(Order_Status.BIKEARRIVED, order.getOrder_status());// [CHECK]Order status changes to BIKEARRIVED
		// +++++++++++++++++++++++++++++++++++++++++END2++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case5: bike return to Original provider | Provider: b| Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		Bike bikeToReturn = order.getOrdered_bikes().get(0);// choose one of the bikes from order to register return
		this.b.registerOneBikeReturn(bikeToReturn, order.getId());

		assertFalse(bikeToReturn.getBookedDates().contains(order.getDaterange()));// [CHECK] the bike's booking dates is
																					// updated and does not include the
																					// dateRange from the order
		assertFalse(this.b.getCurrentOrders().contains(order));// [CHECK] the order is removed from b's current orders
		assertEquals(Bike_Status.INSHOP, bikeToReturn.getBike_Status()); // [CHECK] the bike's status has changed to
																			// INSHOP
		assertEquals(Order_Status.COMPLETED, order.getOrder_status());// [Check] the order status is changed to
																		// COMPLETED

	}

	@Test
	void TestBikeReturnToPartner() {

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case1: Get Quote1 | Customer: Harry |requested date: 2019-12-3 to
		// 2019-12-14 | bike(s)Requested: 1 bike of biketype3 & 1 bike of biketype2 |
		// Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		ArrayList<Quote> quotes = this.Harry.QuoteRequest(this.req1, PMS);

		Quote q = quotes.get(0); // get the only quote from arraylist
		assertEquals(1, quotes.size()); // [CHECK] There should be exactly one quote generated
		// test that all the bikes in each quote are available in the date range
		// requested by customer
		assertEquals(b, quotes.get(0).getProvider());// [CHECK] The quote should be from provider b

		// test that all the bikes in each quote are available in the date range
		// requested by customer
		for (Bike bike : q.getBikes()) {
			assertTrue(bike.Check_Availabilty(req1.getDateRange())); // [CHECK] every bike in quote is available between
																		// the date range specified in Request object
		}
		// [CHECK] total deposit
		// in this case the quote contains two bike that has replacement value 492.5 and
		// 233.5 respectively,
		// provider has deposit rate 0.62, the total deposit must be 450.12;
		assertEquals(0, BigDecimal.valueOf(450.12).compareTo(q.getTotalDeposit()));

		// [CHECK] total price
		// The bikes have daily rental price(40.5 & 19.8) and it is rented for 11 days
		// the total price should be 11*(40.5+19.8) = 663.3
		assertEquals(0, BigDecimal.valueOf(663.3).compareTo(q.getTotalprice()));
		// +++++++++++++++++++++++++++++++++++++++++END1+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case3: Book Quote | Customer: Harry |requested date: 2019-12-3 to
		// 2019-12-14 | bike(s)Requested: 1 bike of biketype3 | Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// we will not test delivery service in this use case, so the second argument
		// (boolean requireDelivery) is false
		this.Harry.BookQuote(quotes.get(0), true);
		BikeRentalOrder order = this.Harry.getCurrent_Order().get(0);
		assertEquals(1, this.Harry.getCurrent_Order().size()); // [CHECK] there is exactly one order existing in the
																// ArrayList<BikeRentalOrder> Current_Orders in Harry
																// object
		assertEquals(order, this.b.getCurrentOrders().get(0));// [CHECK] The order is recorded in provider b object;

		assertEquals(order.getProvider_id(), this.b.getId());// [CHECK] provider b's id is stored in the order
		assertEquals(order.getCustomer_id(), this.Harry.getCustomer_ID()); // [CHECK]Customer Harry's id is stored in
																			// the order
		assertTrue(order.getOrdered_bikes().contains(this.b.getStock().get(this.biketype3).get(0))); // [CHECK] the
																										// ordered bikes
																										// contains a
																										// bike of
																										// biketype3 in
																										// b's Stock
		assertTrue(order.getOrdered_bikes().contains(this.b.getStock().get(this.biketype2).get(0)));// [CHECK] the
																									// ordered bikes
																									// contains a bike
																									// of biketype2 in
																									// b's Stock

		// -------------------------------------------------------------------------------------------------------------
		// -------------------------------------------------------------------------------------------------------------
		// testing delivery service
		// -------------------------------------------------------------------------------------------------------------
		MockDeliveryService ds = (MockDeliveryService) DeliveryServiceFactory.getDeliveryService();
		Bike bikeToDeliver = order.getOrdered_bikes().get(0);
		LocalDate DeliveryingDate = order.getDaterange().getStart();
		ds.scheduleDelivery(bikeToDeliver, this.b.getShopAddress(), this.Harry.getAddress(), DeliveryingDate);

		// carry out pickups
		ds.carryOutPickups(DeliveryingDate);
		assertEquals(Bike_Status.DELIVERYING, bikeToDeliver.getBike_Status());// [CHECK] Bike status changes to
																				// DELIVERING
		assertEquals(Order_Status.BIKEDELIVERING, order.getOrder_status());// [CHECK]Order status changes to
																			// BIKEDELIVERING
		// carry out dropoff
		ds.carryOutDropoffs();
		assertEquals(Bike_Status.ARRIVED, bikeToDeliver.getBike_Status()); // [CHECK] Bike status changes to ARRIVED
		assertEquals(Order_Status.BIKEARRIVED, order.getOrder_status());// [CHECK]Order status changes to BIKEARRIVED
		// +++++++++++++++++++++++++++++++++++++++++END2++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		// Test Case6: bike return to Partner a | Provider: a| Location: EH...
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		this.b.FormPartnership(this.a); // let a be b's partner
		ArrayList<Bike> bikesToReturn = order.getOrdered_bikes();// choose one of the bikes from order to register
																	// return
		a.registerBikesReturn(bikesToReturn, order.getId());
		for (Bike bike : bikesToReturn) {
			assertEquals(Bike_Status.INPARTNERSHOP, bike.getBike_Status()); // [CHECK] all the bikes' states change to
																			// INPARTNERSHOP
		}

		assertEquals(Order_Status.BIKERETURNEDTOPARTNERSHOP, order.getOrder_status());// [CHECK] Order status is changed
																						// to BIKERETURNEDTOPARTNERSHOP

		// +++++++++++++++++++++++++++++++++++++++++END5+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	}

}
