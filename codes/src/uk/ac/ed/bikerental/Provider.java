package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;


public class Provider implements PricingPolicy{
	private ID id;
	private Location ShopAddress;
	private PersonalInfo personalInfo;
	private HashMap<ID, Provider> partners;
	private HashMap<BikeType, ArrayList<Bike>> Stock;
	private BigDecimal deposit_rate;
	private ValuationPolicy Calculator;
	private ArrayList<BikeRentalOrder> CurrentOrders;

	public Provider(Location shopAddress, BigDecimal deposit_rate, PersonalInfo perInfo) {
		this.id = new ID(ID_TYPE.PROVIDER);
		this.ShopAddress = shopAddress;
		this.personalInfo = perInfo;
		this.deposit_rate = deposit_rate;
		this.CurrentOrders = new ArrayList<>();
		this.partners = new HashMap<ID, Provider>();
		this.Stock = new HashMap<BikeType, ArrayList<Bike>>();
		this.Calculator = new DefaultValuationCalculator();
	}
	
	public Set<BikeType> get_BikeTypesFrom(Request req){
		return req.getBikesRequested().keySet();
	}
	
	public ArrayList<Bike> getBikes_of_same_type(BikeType bt){
		return this.Stock.get(bt);
	}

	
	public Quote GenerateQuote(Request request) {
		
		ArrayList<Bike> bikes = new ArrayList<>();
		HashMap<BikeType, Integer> bikesRequested = request.getBikesRequested();
		
		// first loop iterating through bike types from the request
		for (BikeType bt :this.get_BikeTypesFrom(request)) {
			// Variable defined here
			boolean typeFound = false;
			int num_bikes_requested = bikesRequested.get(bt);

			// nested loop to find matched bike type
			for (BikeType bktype_shop : Stock.keySet()) {
				if (bktype_shop.equals(bt)) {
					typeFound = true;
					break;
				}
			}
			if (typeFound) {

				// iterate through all bikes of a certain requested bike type
				for (Bike bike : this.getBikes_of_same_type(bt)) {
					if (bike.Check_Availabilty(request.getDateRange())) {
						bikes.add(bike);
						num_bikes_requested--;
					}
					if (num_bikes_requested == 0) {
						break;
					}
				}
				// Not enough bikes for a certain type requested
				if (num_bikes_requested != 0) {
					return null;
				}

			} // else no matched bike type found
			else {
				return null;
			}
		}
		// construct Quote object based on the HashMap<BikeType,ArrayList<Bike>> Bikes
		Quote q = new Quote(request, bikes, this);
		return q;
	}

	public void registerBikesReturn(ArrayList<Bike> bikes, ID Booking_number) {
		for (Bike bike : bikes) {
			this.registerOneBikeReturn(bike, Booking_number);
		}
	}

	public void registerOneBikeReturn(Bike bike, ID Booking_number) {
		if (bike.getOwner_id() == this.id) {
			BikeRentalOrder orderToComplete = bike.getCurrent_Order();

			assert (orderToComplete != null);
			//change order status to COMPLETED
			orderToComplete.setOrder_status(Order_Status.COMPLETED);
			// delete completed order from provider's current orders
			this.CurrentOrders.remove(orderToComplete);
			// The booking dateRange is expired, no longer needed to be recorded in bike
			// object
			bike.RemoveDatesFrom_BookingDates(orderToComplete.getDaterange());
			// Update Bike status to INSHOP
			bike.setBike_Status(Bike_Status.INSHOP);
			System.out.printf("Your Bike(ID: %s) is returned successfully", bike.getBike_ID());
		}
		// Bike from partner
		else {
			registerBikeReturnFromPartner(bike);
		}

	}

	public void registerBikeReturnFromPartner(Bike bike) {
		BikeRentalOrder orderToComplete = bike.getCurrent_Order();

		orderToComplete.setOrder_status(Order_Status.BIKERETURNEDTOPARTNERSHOP);
		bike.setBike_Status(Bike_Status.INPARTNERSHOP); // update bike's status;
		Provider partner = this.partners.get(bike.getOwner_id());

		// Set up delivery service
		
		DeliveryService DS = new MockDeliveryService();
		DS.scheduleDelivery(bike, this.ShopAddress, partner.getShopAddress(), LocalDate.now());
		System.out.printf("Delivery service delivering bike[id: %s] %nfrom %s to %s%nSuccessfully scheduled%n%n ",
				bike.getBike_ID(), this.getShopAddress(), partner.getShopAddress());
	}

	// int method: (1,0,-1) selects the methods to calculate the replacement value of bikes
	public void SwitchValuationPolicy(int method, BigDecimal depreciation_rate) {
		if (method == 1) {
			this.Calculator = new LinearDepreciationCalculator(depreciation_rate);
		} else if (method == -1) {
			this.Calculator = new DoubleDecliningDepreciationCalculator(depreciation_rate);
		} else if (method == 0) {
			this.Calculator = new DefaultValuationCalculator();
		} else {
			assert false;
		}
	}

	public BigDecimal calculateDepositofABike(Bike bike, LocalDate date) {
		BigDecimal replacmentValue = this.Calculator.calculateValue(bike, date);
		BigDecimal Actualdeposit = replacmentValue.multiply(this.deposit_rate);

		return Actualdeposit;
	}

	public void addBikeIntoStock(BikeType bktp, LocalDate date) {
		// Initialising bike object by the parameter provided
		Bike bike = new Bike(bktp, date, this.id);
		// assign deposit rate to bike object

		if (this.Stock.containsKey(bike.getType())) {
			this.Stock.get(bike.getType()).add(bike);
		} else {
			ArrayList<Bike> bikes = new ArrayList<>();
			bikes.add(bike);
			this.Stock.put(bike.getType(), bikes);
		}
	}

	public void FormPartnership(Provider p) {
		this.partners.put(p.getId(), p);
		p.AddPartner(this);
	}

	public void AddPartner(Provider p) {
		this.partners.put(p.getId(), p);
	}
	public HashMap<BikeType, ArrayList<Bike>> getStock() {
		return Stock;
	}

	public void setStock(HashMap<BikeType, ArrayList<Bike>> stock) {
		Stock = stock;
	}

	public ArrayList<BikeRentalOrder> getCurrentOrders() {
		return CurrentOrders;
	}

	public void setCurrentOrders(ArrayList<BikeRentalOrder> currentOrders) {
		CurrentOrders = currentOrders;
	}

	public ID getId() {
		return id;
	}

	public Location getShopAddress() {
		return ShopAddress;
	}

	public void setShopAddress(Location shopAddress) {
		ShopAddress = shopAddress;
	}

	public PersonalInfo getPersonalInfo() {
		// TODO Auto-generated method stub

		return this.personalInfo;
	}

	public void addCurrent_Order(BikeRentalOrder new_order) {
		CurrentOrders.add(new_order);
	}

	@Override
	public void setDailyRentalPrice(BikeType bikeType, BigDecimal dailyPrice) {
		// TODO Auto-generated method stub
		ArrayList<Bike> bikes = this.Stock.get(bikeType);
		for(Bike bike: bikes) {
			bike.setDaily_Rental_Price(dailyPrice);
		}
	}

	@Override
	public BigDecimal calculatePrice(Collection<Bike> bikes, DateRange duration) {
		// TODO Auto-generated method stub
		BigDecimal total = BigDecimal.ZERO;
		for (Bike bike: bikes) {
			total=total.add(bike.getDaily_Rental_Price().multiply(BigDecimal.valueOf(duration.toDays())));
		}
		return total;
	}
}
