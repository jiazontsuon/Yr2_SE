package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DoubleDecliningDepreciationCalculator  implements ValuationPolicy {

	private BigDecimal Depreciation_Rate_Per_year;

	public DoubleDecliningDepreciationCalculator(BigDecimal d_r) {
		super();
		this.Depreciation_Rate_Per_year = d_r;

	}
	

	@Override
	public BigDecimal calculateValue(Bike bike, LocalDate date) {
		BigDecimal Replacement_Value = bike.getType().getReplacementValue();
		BigDecimal Rate = this.Depreciation_Rate_Per_year;
		int Year = (int) bike.getDate_of_Purchase().until(date, ChronoUnit.YEARS);
		
		//Apply the formulae
		BigDecimal res = BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(2).multiply(Rate)); 
        return (Replacement_Value.multiply(res.pow(Year)));
	}

}
