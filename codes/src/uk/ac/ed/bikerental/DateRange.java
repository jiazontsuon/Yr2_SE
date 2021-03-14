package uk.ac.ed.bikerental;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * DateRange class created to simulate a certain time period.
 * <p>
 * This is an immutable date-to-date object that represents a period, often viewed as "from Start date to End date".
 * @author s1864509, s1857043
 *
 */
public class DateRange {
	/**
	 * the Start date of this DateRange.
	 */
    private LocalDate start;
    /**
     * the end date of this DateRange.
     */
    private LocalDate end;
    /**
     * Creates a new DateRange with 2 given dates:
     * The first Parameter is Starting date and the 2nd is End date.
     * @param start
     * @param end
     */
    public DateRange(LocalDate start, LocalDate end) {
    	assert(end.isAfter(start));
        this.start = start;
        this.end = end;
    }
    /**
     * returns Start Date
     * @return start
     */
    public LocalDate getStart() {
        return this.start;
    }
    /**
     * returns End Date
     * @return End
     */
    public LocalDate getEnd() {
        return this.end;
    }
    /**
     * return length of time period in unit of Year
     * @return length of time period.
     */
    public long toYears() {
        return ChronoUnit.YEARS.between(this.getStart(), this.getEnd());
    }
    /**
     * return length of time period in unit of Day
     * @return length of time period.
     */
    public long toDays() {
        return ChronoUnit.DAYS.between(this.getStart(), this.getEnd());
    }
    /**
     * This takes another DateRange into consideration and do 2 checks:
     * 1.Checking whether "AnotherDateRange"'s start is after this DateRange's end 
     * and 2.whether "AnotherDateRange"'s end is before this DateRange's start.
     * <p>
     * If and only if both of the comparison turns out to be false, it will return true.
     * @param DateRange AnotherDateRange
     * @return result of the comparison
     */
    public Boolean overlaps(DateRange AnotherDateRange) {
        // TODO: implement date range intersection checking
    	assert (AnotherDateRange!=null);
    	// check if the start of other is after end of this class
    	boolean b1= AnotherDateRange.getStart().isAfter(this.end);
    	// check if the end of other is before start of this class
    	boolean b2 = AnotherDateRange.getEnd().isBefore(this.start);
    	return (!b1&&!b2);
        
    }
    /** 
     * return hashCode
     * @return hashCode
     */
    @Override
    public int hashCode() {
        // hashCode method allowing use in collections
        return Objects.hash(end, start);
    }
    /**
     * check if the object is equal to this DateRange.
     * @return result of the comparison
     */
    @Override
    public boolean equals(Object obj) {
        // equals method for testing equality in tests
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DateRange other = (DateRange) obj;
        return Objects.equals(end, other.end) && Objects.equals(start, other.start);
    }
    
    // You can add your own methods here
}
