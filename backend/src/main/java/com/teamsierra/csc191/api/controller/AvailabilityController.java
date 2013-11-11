package com.teamsierra.csc191.api.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.teamsierra.csc191.api.model.Appointment;
import com.teamsierra.csc191.api.model.GenericModel.AppointmentStatus;
import com.teamsierra.csc191.api.model.StylistAvailability;
import com.teamsierra.csc191.api.model.User;
import com.teamsierra.csc191.api.repository.AppointmentRepository;
import com.teamsierra.csc191.api.repository.StylistAvailabilityRepository;
import com.teamsierra.csc191.api.repository.UserRepository;
import com.teamsierra.csc191.api.resources.ResourceHandler;
import com.teamsierra.csc191.api.util.DateRange;

@Controller
@RequestMapping("/availability")
public class AvailabilityController extends GenericController
{
	private static final Log L = LogFactory.getLog(AvailabilityController.class);
	
	private UserRepository userRepo;
	private StylistAvailabilityRepository sar;
	private AppointmentRepository apptRepo;
	
	@Autowired
	public AvailabilityController(UserRepository userRepo,
								  StylistAvailabilityRepository stylistAvailRepo,
								  AppointmentRepository apptRepo)
	{
		this.userRepo = userRepo;
		this.sar = stylistAvailRepo;
		this.apptRepo = apptRepo;
	}
	
	/**
	 * A method to get the current availability for all stylists within the given month
	 * and year. The availability returned has the time of all approved appointments
	 * removed.
	 * 
	 * Usage: Http GET call to "/availability?year=Y&month=M". where M is the month
	 * param and Y is the year param as integer values. These values wrap around if
	 * they exceed their specific ranges. 
	 * 
	 * i.e. month = 12 would be equivalent to month = 1 and year++
	 * 
	 * @param month integer in the range 0-11, values outside of this range wrap around.
	 * @param year integer value for the year
	 * @return a list of the current available times for all of the stylists along with
	 * their corresponding userID for the entire month.
	 */
	@RequestMapping(method = RequestMethod.GET, params = {"month", "year"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<StylistAvailability>>> getMonthAvailability(@RequestParam int month,
																	 		   		@RequestParam int year)
	{
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date sDate = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		Date eDate = cal.getTime();
		DateRange dr = new DateRange(sDate, eDate);
		
		List<Resource<StylistAvailability>> returnList = getAvailForCalendar(dr);
		
		return new ResponseEntity<List<Resource<StylistAvailability>>>(returnList, HttpStatus.OK);
	}
	
	/**
	 * A method to get the current availability for all stylists for a specific day.
	 * The availability returned has the time of all approved appointments
	 * removed.
	 * 
	 * Usage: Http GET call to "/availability?year=Y&month=M&day=D". where D is
	 * the day param, M is the month param, and Y is the year param as integer
	 * values. These values wrap around if they exceed their specific ranges.
	 * 
	 * i.e. month = 12 would be equivalent to month = 1 and year++
	 * 
	 * @param day integer value for the day of the month
	 * @param month integer in the range 0-11, values outside of this range wrap around.
	 * @param year integer value for the year
	 * @return a list of the current available times for all of the stylists along with
	 * their corresponding userID for a specific day.
	 */
	@RequestMapping(method = RequestMethod.GET, params = {"day", "month", "year"}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<StylistAvailability>>> getDayAvailability(@RequestParam int day,
																				  @RequestParam int month,
																				  @RequestParam int year)
	{
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date sDate = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		Date eDate = cal.getTime();
		DateRange dr = new DateRange(sDate, eDate);
		
		List<Resource<StylistAvailability>> returnList = getAvailForCalendar(dr);
		
		return new ResponseEntity<List<Resource<StylistAvailability>>>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{stylistID}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateAvailability(@PathVariable String stylistID,
								   @RequestBody StylistAvailability stylistAvailability,
								   HttpServletRequest request) throws Exception
	{
		this.setRequestControllerState(request);
		
		switch(authType)
		{
			case CLIENT: throw new Exception("Clients cannot modify stylist availability.");
			case STYLIST: User user = userRepo.findByToken(authToken);
						  if(!stylistID.equals(user.getId()))
						  {
							  throw new Exception("Stylists can only edit their own availability.");
						  }
						  break;
			case ADMIN: 
						break;
			default:
		}
		
		StylistAvailability sa = sar.findByStylistID(stylistID);
		if(sa == null)
		{
			throw new Exception("Unable to find stylist availability in the repository.");
		}
		
		sa.setAvailability(stylistAvailability.getAvailability());
		
		sar.save(sa);
	}
	
	private List<Resource<StylistAvailability>> getAvailForCalendar(DateRange dr)
	{
		List<StylistAvailability> list = sar.findByDateRange(dr);
		List<Appointment> apptList;
		Appointment appt;
		DateRange dateRange;
		for(StylistAvailability sa : list)
		{
			appt = new Appointment();
			appt.setStylistID(sa.getStylistID());
			appt.setStartTime(dr.getStartDate());
			appt.setEndTime(dr.getEndDate());
			appt.setAppointmentStatus(AppointmentStatus.APPROVED);
			
			apptList = apptRepo.findByCriteria(appt);
			
			for(Appointment appointment : apptList)
			{
				dateRange = new DateRange(appointment.getStartTime(), appointment.getEndTime());
				sa.getAvailability().remove(dateRange);
			}
		}
		
		List<Resource<StylistAvailability>> returnList = new ArrayList<Resource<StylistAvailability>>();
		for(StylistAvailability sa : list)
		{
			returnList.add(ResourceHandler.createResource(sa));
		}
		
		return returnList;
	}
}
