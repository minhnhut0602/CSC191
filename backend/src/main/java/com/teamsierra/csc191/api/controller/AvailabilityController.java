package com.teamsierra.csc191.api.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.teamsierra.csc191.api.repository.AppointmentRepository;
import com.teamsierra.csc191.api.repository.StylistAvailabilityRepository;
import com.teamsierra.csc191.api.repository.UserRepository;
import com.teamsierra.csc191.api.resources.ResourceHandler;
import com.teamsierra.csc191.api.util.DateRange;

@Controller
@RequestMapping("/availability")
public class AvailabilityController extends GenericController
{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StylistAvailabilityRepository sar;
	@Autowired
	private AppointmentRepository apptRepo;
	
	//TODO use map, different function for month, day, year
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Resource<StylistAvailability>>> getAvailability(@RequestParam int month,
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
		
		List<StylistAvailability> list = sar.findByDateRange(dr);
		List<Appointment> apptList;
		Appointment appt;
		DateRange dateRange;
		for(StylistAvailability sa : list)
		{
			appt = new Appointment();
			appt.setStylistID(sa.getStylistID());
			appt.setStartTime(sDate);
			appt.setEndTime(eDate);
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
		
		return new ResponseEntity<List<Resource<StylistAvailability>>>(returnList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{stylistID}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateAvailability(@PathVariable String stylistID,
								   @RequestBody StylistAvailability stylistAvailability,
								   HttpServletRequest request) throws Exception
	{
		this.setRequestControllerState(request);
		
		switch(authType)
		{
			case CLIENT: throw new Exception("Clients cannot modify stylist availability.");
			case STYLIST: 
				break;
			case ADMIN: 
				break;
			default:
		}
	}
}
