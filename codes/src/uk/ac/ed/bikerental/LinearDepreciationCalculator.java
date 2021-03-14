package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LinearDepreciationCalculator implements ValuationPolicy {

	private BigDecimal Depreciation_Rate_Per_year;

	public LinearDepreciationCalculator(BigDecimal d_r) {
		this.Depreciation_Rate_Per_year = d_r;

	}

	@Override
	public BigDecimal calculateValue(Bike bike, LocalDate date) {
		BigDecimal Replacement_Value = bike.getType().getReplacementValue();
		BigDecimal Rate = this.Depreciation_Rate_Per_year;
		BigDecimal Year = BigDecimal.valueOf(bike.getDate_of_Purchase().until(date, ChronoUnit.YEARS));

		BigDecimal FinalValue = Replacement_Value.subtract((Year.multiply(Rate.multiply(Replacement_Value))));
		return FinalValue;
	}

}
