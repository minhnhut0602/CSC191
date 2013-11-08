package com.teamsierra.csc191.api.util;

import java.util.Date;

public class DateRange 
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
	 * Returns true if this DateRange overlaps with the param DateRange.
	 * 
	 * @param dateRange
	 * @return
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
	
	@Override
	public DateRange clone()
	{
		return new DateRange(startDate, endDate);
	}
	
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
