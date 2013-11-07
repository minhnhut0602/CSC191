package com.teamsierra.csc191.api.util;

import java.util.Date;

public class DateRange 
{
	private Date startDate;
	private Date endDate;
	
	public DateRange(Date startDate, Date endDate)
	{
		this.startDate = startDate;
		setEndDate(endDate);
	}

	public Date getStartDate() 
	{
		return startDate;
	}

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
}
