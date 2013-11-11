package com.teamsierra.csc191.api.util;

import java.util.Date;

/**
 * A class representing a range of two {@link Date} instances.
 * 
 * @author Kyle
 *
 */
public class DateRange implements Cloneable
{
	private Date startDate;
	private Date endDate;
	
	/**
	 * Creates a DateRange with the given params. can throw an 
	 * {@link IllegalArgumentException} if the start date is after the
	 * end date.
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public DateRange(Date startDate, Date endDate)
	{
		this.startDate = startDate;
		setEndDate(endDate);
	}
	
	/**
	 * Getter for the start date.
	 * 
	 * @return the start date of this DateRange
	 */
	public Date getStartDate() 
	{
		return startDate;
	}
	
	/**
	 * Sets the start date of the DateRange. Can throw an 
	 * {@link IllegalArgumentException} if the start date is after
	 * the current end date.
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) 
	{
		if(startDate.compareTo(endDate) <= 0)
		{
			this.startDate = startDate;
		}
		else
		{
			throw new IllegalArgumentException("The start date must be before the end date.");
		}
	}
	
	/**
	 * Getter for the end date.
	 * 
	 * @return the end date of this DateRange
	 */
	public Date getEndDate() 
	{
		return endDate;
	}
	
	/**
	 * Sets the end date of the DateRange. can throw an 
	 * {@link IllegalArgumentException} if the end date is before the
	 * current start date.
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate)
	{
		if(endDate.compareTo(startDate) >= 0)
		{
			this.endDate = endDate;
		}
		else
		{
			throw new IllegalArgumentException("The end date must be later than the start date.");
		}
	}
	
	/**
	 * Sets the range for the DateRange. can throw an 
	 * {@link IllegalArgumentException} if the start date is not
	 * before the end date.
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public void setRange(Date startDate, Date endDate)
	{
		if(startDate.compareTo(endDate) <= 0)
		{
			this.startDate = startDate;
			this.endDate = endDate;
		}
		else
		{
			throw new IllegalArgumentException("The end date must be later than the start date.");
		}
	}
	
	/**
	 * Returns a new DateRange which is the concatenation of this DateRange with
	 * the param DateRange and any time that might span between them.
	 * 
	 * @param dateRange
	 * @return
	 */
	public DateRange mergeDateRange(DateRange dateRange)
	{
		Date drStart = dateRange.getStartDate();
		Date drEnd = dateRange.getEndDate();
		
		if(startDate.compareTo(drStart) < 0)
		{
			drStart = startDate;
		}
		
		if(endDate.compareTo(drEnd) > 0)
		{
			drEnd = endDate;
		}
		
		return new DateRange(drStart, drEnd);
	}
	
	/**
	 * Returns true if this DateRange overlaps with the param DateRange. This
	 * includes if one of the date's end date is the same as the other date's
	 * start date.
	 * 
	 * @param dateRange
	 * @return true if the DateRanges overlap in any manor
	 */
	public boolean isOverlapping(DateRange dateRange)
	{
		Date drStart = dateRange.getStartDate();
		Date drEnd = dateRange.getEndDate();
		
		if(startDate.compareTo(drEnd) <= 0)
		{
			if(endDate.compareTo(drStart) < 0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			return false;
		}
	}
	
	@Override
	public String toString()
	{		
		return "{" +
				"startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				"}";
	}
	
	/**
	 * Returns a new DateRange with the same start and end date
	 * as the original.
	 * 
	 */
	@Override
	public DateRange clone()
	{
		return new DateRange(startDate, endDate);
	}
	
	/**
	 * Returns true iff the param Object is an instance of a DateRange
	 * and both the start date and end date are the same for the two
	 * DateRanges. 
	 * 
	 */
	@Override
	public boolean equals(Object o)
	{
		DateRange dateRange;
		if(!(o instanceof DateRange))
		{
			return false;
		}
		else
		{
			dateRange = (DateRange) o;
			
			return (startDate.compareTo(dateRange.getStartDate()) == 0 && endDate.compareTo(dateRange.getEndDate()) == 0);
		}
	}
}
