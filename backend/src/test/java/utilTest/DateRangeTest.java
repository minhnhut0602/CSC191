package utilTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.Test;

import com.teamsierra.csc191.api.util.DateRange;

public class DateRangeTest 
{
	private Calendar cal;
	
	@Before
	public void beforeClass()
	{
		cal = new GregorianCalendar();
	}
	
	/**
	 * Also tests setEndDate() since it is called from constructor.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructor()
	{
		Date startDate = cal.getTime();
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
		Date endDate = cal.getTime();
		new DateRange(startDate, endDate);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetStartDate()
	{
		Date startDate = cal.getTime();
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		Date endDate = cal.getTime();
		DateRange dr = new DateRange(startDate, endDate);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 5);
		endDate = cal.getTime();
		dr.setStartDate(cal.getTime());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSetRange()
	{
		Date startDate = cal.getTime();
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		Date endDate = cal.getTime();
		DateRange dr = new DateRange(startDate, endDate);
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 5);
		endDate = cal.getTime();
		dr.setRange(startDate, endDate);
	}
	
	@Test
	public void testMergeDateRange()
	{
		cal.set(Calendar.HOUR_OF_DAY, 1);
		Date startDate = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 5);
		Date endDate = cal.getTime();
		DateRange dr = new DateRange(startDate, endDate);
		
		cal.set(Calendar.HOUR_OF_DAY, 3);
		Date startDate1 = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 9);
		Date endDate1 = cal.getTime();
		DateRange dr1 = new DateRange(startDate1, endDate1);
		
		DateRange dr2 = dr.mergeDateRange(dr1);
		assertEquals(0, dr2.getStartDate().compareTo(startDate));
		assertEquals(0, dr2.getEndDate().compareTo(endDate1));
	}
	
	@Test
	public void testIsOverlapping()
	{
		cal.set(Calendar.HOUR_OF_DAY, 1);
		Date startDate = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 5);
		Date endDate = cal.getTime();
		DateRange dr = new DateRange(startDate, endDate);
		
		cal.set(Calendar.HOUR_OF_DAY, 3);
		Date startDate1 = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 9);
		Date endDate1 = cal.getTime();
		DateRange dr1 = new DateRange(startDate1, endDate1);
		
		assertTrue(dr.isOverlapping(dr1));
	}
}
