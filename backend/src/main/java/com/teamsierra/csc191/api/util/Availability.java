package com.teamsierra.csc191.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class Availability 
{
	public enum Day
	{
		SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
	}
	
	private Collection<DateRange> availability;
	
	public Availability()
	{
		availability = new ArrayList<DateRange>();
	}
	
	public void addRange(Date startDate, Date endDate)
	{
		addRange(new DateRange(startDate, endDate));
	}
	
	public void addRange(DateRange dateRange)
	{		
		if(availability.isEmpty())
		{
			availability.add(dateRange);
		}
		else
		{
			boolean overlap = false;
			DateRange overlapDate = dateRange;
			ArrayList<DateRange> removeDate = new ArrayList<DateRange>();
			
			for(DateRange dr : availability)
			{
				if(overlapDate.isOverlapping(dr))
				{
					removeDate.add(dr);
					overlapDate = overlapDate.mergeDateRange(dr);
					overlap = true;
				}
			}
			for(DateRange dr : removeDate)
			{
				availability.remove(dr);
			}
			
			if(!overlap)
			{
				availability.add(dateRange);
			}
			else
			{				
				availability.add(overlapDate);
			}
		}
	}
	
	public void removeRange(DateRange dateRange)
	{
		if(!availability.isEmpty())
		{
			ArrayList<DateRange> removeList = new ArrayList<DateRange>();
			Date dateRangeStart = dateRange.getStartDate();
			Date dateRangeEnd = dateRange.getEndDate();
			
			Date drStart, drEnd;
			for(DateRange dr : availability)
			{
				drStart = dr.getStartDate();
				drEnd = dr.getEndDate();
				
				if(dateRangeStart.compareTo(drEnd) < 0)
				{
					if(dateRangeStart.compareTo(drStart) > 0)
					{
						dr.setEndDate(dateRangeStart);
						
						if(dateRangeEnd.compareTo(drEnd) < 0)
						{
							availability.add(new DateRange(dateRangeEnd, drEnd));
							break;
						}
					}
					else
					{
						if(dateRangeEnd.compareTo(drStart) > 0)
						{
							if(dateRangeEnd.compareTo(drEnd) < 0)
							{
								dr.setStartDate(dateRangeEnd);
							}
							else
							{
								removeList.add(dr);
							}
						}
					}
				}
			}
			
			for(DateRange dr : removeList)
			{
				availability.remove(dr);
			}
		}
	}
	
	public Collection<DateRange> getAvailability()
	{
		return availability;
	}
	
	public boolean isEmpty()
	{
		return availability.isEmpty();
	}
	
	@Override
	public String toString()
	{		
		String string = "{";
		Iterator<DateRange> iterator = availability.iterator();
		
		if(iterator.hasNext())
		{
			string += "availability[" + 0 + "]='" + iterator.next() + '\'';
			
			int i = 1;
			while(iterator.hasNext())
			{
				string += ", availability[" + i + "]='" + iterator.next() + '\'';
				i++;
			}
		}
		
		string += "}";
		
		return string;
	}
}
