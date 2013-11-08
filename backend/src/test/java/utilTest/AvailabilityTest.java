package utilTest;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.teamsierra.csc191.api.util.Availability;
import com.teamsierra.csc191.api.util.DateRange;

public class AvailabilityTest 
{
	private static Calendar cal;
	private static Availability availability;
	
	@BeforeClass
	public static void beforeClass()
	{
		cal = new GregorianCalendar();
		availability = new Availability();
	}
	
	@Before
	public void beforeEachTest()
	{
		availability.clear();
	}
	
	@Test
	public void testAddAndRemoveRange()
	{
		Date sdate, testSDate;
		Date edate, testEDate;
		DateRange[] drArr;
		
		cal.set(2013, 10, 4, 5, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 7, 0, 0);
		edate = cal.getTime();
		testSDate = cal.getTime();
		availability.addRange(sdate, edate);
		assertEquals(1, availability.size());
		
		cal.set(2013, 10, 4, 6, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 8, 0, 0);
		edate = cal.getTime();
		testEDate = cal.getTime();
		availability.addRange(sdate, edate);
		assertEquals(1, availability.size());
		
		cal.set(2013, 10, 4, 5, 30, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 7, 0, 0);
		edate = cal.getTime();
		availability.removeRange(sdate, edate);
		assertEquals(2, availability.size());
		
		cal.set(2013, 10, 4, 5, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 5, 30, 0);
		edate = cal.getTime();
		availability.removeRange(sdate, edate);
		assertEquals(1, availability.size());
		
		drArr = new DateRange[availability.size()];
		availability.toArray(drArr);
		DateRange dr = drArr[0];
		Date eDate = dr.getEndDate();
		Date sDate = dr.getStartDate();
		assertEquals(0, sDate.compareTo(testSDate));
		assertEquals(0, eDate.compareTo(testEDate));
		
		availability.clear();
		assertEquals(0, availability.size());
	}
	
	@Test
	public void verboseTest()
	{
		Date sdate;
		Date edate;
		
		cal.set(2013, 10, 4, 5, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 7, 0, 0);
		edate = cal.getTime();
		availability.addRange(sdate, edate);
		assertEquals(1, availability.size());
		
		cal.set(2013, 10, 4, 4, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 8, 0, 0);
		edate = cal.getTime();
		availability.addRange(sdate, edate);
		assertEquals(1, availability.size());
		
		cal.set(2013, 10, 4, 4, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 8, 0, 0);
		edate = cal.getTime();
		availability.addRange(sdate, edate);
		assertEquals(1, availability.size());
		
		cal.set(2013, 10, 4, 1, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 2, 0, 0);
		edate = cal.getTime();
		availability.addRange(sdate, edate);
		assertEquals(2, availability.size());
		
		cal.set(2013, 10, 4, 3, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 5, 0, 0);
		edate = cal.getTime();
		availability.add(new DateRange(sdate, edate));
		assertEquals(2, availability.size());
		
		cal.set(2013, 10, 4, 8, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 10, 0, 0);
		edate = cal.getTime();
		availability.addRange(sdate, edate);
		assertEquals(2, availability.size());
		
		cal.set(2013, 10, 4, 11, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 11, 0, 0);
		edate = cal.getTime();
		availability.addRange(sdate, edate);
		assertEquals(3, availability.size());
		
		cal.set(2013, 10, 4, 10, 30, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 11, 0, 0);
		edate = cal.getTime();
		availability.add(new DateRange(sdate, edate));
		assertEquals(3, availability.size());
		
		cal.set(2013, 10, 4, 4, 30, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 6, 0, 0);
		edate = cal.getTime();
		availability.remove(new DateRange(sdate, edate));
		assertEquals(4, availability.size());
		
		cal.set(2013, 10, 4, 6, 0, 0);
		sdate = cal.getTime();
		cal.set(2013, 10, 4, 11, 0, 0);
		edate = cal.getTime();
		availability.remove(new DateRange(sdate, edate));
		assertEquals(2, availability.size());
		
		Availability avail = new Availability();
		avail.addAll(availability);
		assertEquals(avail.size(), availability.size());
	}
}
