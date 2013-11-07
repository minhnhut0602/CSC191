package com.teamsierra.csc191.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class Availability implements Collection<DateRange>
{	
	private Collection<DateRange> availability;
	
	public Availability()
	{
		availability = new ArrayList<DateRange>();
	}
	
	public boolean addRange(Date startDate, Date endDate)
	{
		return add(new DateRange(startDate, endDate));
	}
	
	public boolean removeRange(Date startDate, Date endDate)
	{
		return remove(new DateRange(startDate, endDate));
	}
	
	public boolean remove(DateRange dateRange)
	{
		return remove(dateRange);
	}
	
	@Override
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

	@Override
	public boolean add(DateRange dateRange)
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
		
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends DateRange> c) 
	{
		for(DateRange dr : c)
		{
			this.add(dr);
		}
		
		return true;
	}

	@Override
	public void clear() 
	{
		availability = new ArrayList<DateRange>();
	}

	@Override
	public boolean contains(Object o) 
	{		
		return availability.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) 
	{
		return availability.containsAll(c);
	}

	@Override
	public Iterator<DateRange> iterator()
	{
		return availability.iterator();
	}
	
	/**
	 * ONLY works if param is a {@link DateRange}. Will return false
	 * if any other object is passed in.
	 * 
	 */
	@Override
	public boolean remove(Object o) 
	{
		DateRange dateRange;

		if(o instanceof DateRange)
		{
			dateRange = (DateRange) o; 
		}
		else
		{
			return false;
		}
		
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
		
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> c) 
	{
		for(Object o : c)
		{
			remove(o);
		}
		
		return true;
	}
	
	/**
	 * NOT IMPLEMENTED.
	 * 
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		return false;
	}

	@Override
	public int size() 
	{
		return availability.size();
	}

	@Override
	public Object[] toArray() 
	{
		return availability.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) 
	{
		return availability.toArray(a);
	}
}
