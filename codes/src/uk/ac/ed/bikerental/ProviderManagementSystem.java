package uk.ac.ed.bikerental;

import java.util.ArrayList;
import java.util.HashMap;


public class ProviderManagementSystem {
	
	//private ArrayList<PartnershipRecord> partner_records;
	private ArrayList<Provider> Providers;
	private HashMap<ID,Provider> ID_Provider_Map;
	
	public ProviderManagementSystem() {
		//this.partner_records = new ArrayList<>();
		this.Providers = new ArrayList<Provider>();
		this.ID_Provider_Map = new HashMap<ID,Provider>();
		
	}
	
	public void addProvider(Provider provider) {
		if (this.Providers.contains(provider)) {
			return;
		}
		this.Providers.add(provider);
		
		ID_Provider_Map.put(provider.getId(), provider);
	}
	
	public ArrayList<Quote> getQuoteFromProviders(Request request){
		ArrayList<Quote> quotes = new ArrayList<>();
		//iterate through ketSets to select providers from requested location
		for (Provider p: this.Providers) {
			if (request.getLocation_of_hire().isNearTo(p.getShopAddress())) {
				Quote quote = p.GenerateQuote(request);
				if (quote !=null) {
					quotes.add(p.GenerateQuote(request));
				}
				
				
			}
		}
		return quotes;
	}
	
	public void addOrder(Provider p,BikeRentalOrder o) {
		p.addCurrent_Order(o);
	}
	
	

}
