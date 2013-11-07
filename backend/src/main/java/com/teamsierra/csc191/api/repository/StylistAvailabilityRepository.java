package com.teamsierra.csc191.api.repository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.teamsierra.csc191.api.model.StylistAvailability;
import com.teamsierra.csc191.api.util.Availability;
import com.teamsierra.csc191.api.util.DateRange;

@Repository
public class StylistAvailabilityRepository 
{
	private static final Log L = LogFactory.getLog(UserRepository.class);

    private MongoTemplate mongoTemplate;

    @Autowired
    public StylistAvailabilityRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;

        if(!mongoTemplate.collectionExists(StylistAvailability.class)) {
            L.debug("Creating the StylistAvailability collection");
            mongoTemplate.createCollection(StylistAvailability.class);
        }
    }
    
   //   /$$$$$$ /$$   /$$  /$$$$$$  /$$$$$$$$ /$$$$$$$  /$$$$$$$$
   //  |_  $$_/| $$$ | $$ /$$__  $$| $$_____/| $$__  $$|__  $$__/
   //    | $$  | $$$$| $$| $$  \__/| $$      | $$  \ $$   | $$
   //    | $$  | $$ $$ $$|  $$$$$$ | $$$$$   | $$$$$$$/   | $$
   //    | $$  | $$  $$$$ \____  $$| $$__/   | $$__  $$   | $$
   //    | $$  | $$\  $$$ /$$  \ $$| $$      | $$  \ $$   | $$
   //   /$$$$$$| $$ \  $$|  $$$$$$/| $$$$$$$$| $$  | $$   | $$
   //  |______/|__/  \__/ \______/ |________/|__/  |__/   |__/
   public void insert(StylistAvailability stylistAvailability) {
       L.info("Inserting new StylistAvailability: "+ stylistAvailability);
       mongoTemplate.insert(stylistAvailability);
   }


   //   /$$$$$$$$ /$$$$$$ /$$   /$$ /$$$$$$$
   //  | $$_____/|_  $$_/| $$$ | $$| $$__  $$
   //  | $$        | $$  | $$$$| $$| $$  \ $$
   //  | $$$$$     | $$  | $$ $$ $$| $$  | $$
   //  | $$__/     | $$  | $$  $$$$| $$  | $$
   //  | $$        | $$  | $$\  $$$| $$  | $$
   //  | $$       /$$$$$$| $$ \  $$| $$$$$$$/
   //  |__/      |______/|__/  \__/|_______/
   public StylistAvailability findById(String id) {
       L.info("Finding a StylistAvailabilty by id: "+ id);
       return mongoTemplate.findOne(query(where("_id").is(id)), StylistAvailability.class);
   }
   public List<StylistAvailability> findByStylistID(String stylistID)
   {
	   L.info("Finding a StylistAvailabilty by stylistID: "+ stylistID);
       return mongoTemplate.find(query(where("stylistID").is(stylistID)), StylistAvailability.class);
   }
   public List<StylistAvailability> findAll()
   {
	   L.info("Finding all StylistAvailability: ");
	   return mongoTemplate.findAll(StylistAvailability.class);
   }
   /**
    * Find all availability on a specific day for a specific stylist. The date 
    * passed in will only be used to determine the day and the time set for it
    * will not matter. This method creates a new {@link DateRange} where the 
    * startDate is the date param with time set to all 0, and the endDate is the
    * date param with the time set to all max values. The return value is all the
    * availabilities that over lap this DateRange.
    * 
    * Guaranteed to return a StylistAvailability, however the availability
    * field my be an empty collection.
    * 
    * Returns the same value as if the above specified DateRange and stylistID
    * were used as the params for {@link #findByDateRange(DateRange, String)}.
    * 
    * @param date
    * @param stylistID
    * @return
    */
   public StylistAvailability findByDay(Date date, String stylistID)
   {
	   L.info("Finding a StylistAvailabilty by date and stylistID: "+ date + " " + stylistID);
	   
	   Calendar cal = new GregorianCalendar();
	   cal.setTime(date);
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
	   DateRange dateRange = new DateRange(sDate, eDate);
	   
	   return findByDateRange(dateRange, stylistID);
   }
   /**
    * Find all availability on a specific day for all stylists. The date 
    * passed in will only be used to determine the day and the time set for it
    * will not matter. This method creates a new {@link DateRange} where the 
    * startDate is the date param with time set to all 0, and the endDate is the
    * date param with the time set to all max values. The return value is all the
    * availabilities that over lap this DateRange.
    * 
    * If a stylist has no availability in the DateRange, they will be excluded from the
    * return list.
    * 
    * Returns the same value as if the above specified DateRange was used
    * as the params for {@link #findByDateRange(DateRange)}.
    * 
    * @param date
    * @return
    */
   public List<StylistAvailability> findByDay(Date date)
   {
	   L.info("Finding a StylistAvailabilty by date: "+ date);
	   
	   Calendar cal = new GregorianCalendar();
	   cal.setTime(date);
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
	   DateRange dateRange = new DateRange(sDate, eDate);
	   
	   return findByDateRange(dateRange);
   }
   /**
    * Find all availability within the specified {@link DateRange} for the specified
    * stylist. 
    * 
    * Guaranteed to return a StylistAvailability, however the availability
    * field my be an empty collection. 
    * 
    * @param dateRange
    * @param stylistID
    * @return
    */
   public StylistAvailability findByDateRange(DateRange dateRange, String stylistID)
   {
	   StylistAvailability sa = mongoTemplate.findOne(query(where("stylistID").is(stylistID)), StylistAvailability.class);
	   
	   Availability newAvail = new Availability();
	   
	   for(DateRange dr : sa.getAvailability().getAvailability())
	   {
		   if(dr.isOverlapping(dateRange))
		   {
			   newAvail.addRange(dr);
		   }
	   }
	   
	   StylistAvailability returnSA = new StylistAvailability();
	   returnSA.setStylistID(stylistID);
	   returnSA.setAvailability(newAvail);	   
	   
	   return returnSA;
   }
   /**
    * Finds all the availability for all stylists within the specified {@link DateRange}.
    * 
    * If a stylist has no availability in the DateRange, they will be excluded from the
    * return list.
    * 
    * @param dateRange
    * @return
    */
   public List<StylistAvailability> findByDateRange(DateRange dateRange)
   {
	   List<StylistAvailability> stylists = mongoTemplate.findAll(StylistAvailability.class);
	   
	   Availability newAvail;
	   StylistAvailability returnSA;
	   ArrayList<StylistAvailability> returnList = new ArrayList<StylistAvailability>();
	   for(StylistAvailability sa : stylists)
	   {
		   newAvail = new Availability();
		   
		   for(DateRange dr : sa.getAvailability().getAvailability())
		   {
			   if(dr.isOverlapping(dateRange))
			   {
				   newAvail.addRange(dr);
			   }
		   }
		   
		   if(!newAvail.isEmpty())
		   {
			   returnSA = new StylistAvailability();
			   returnSA.setStylistID(sa.getStylistID());
			   returnSA.setAvailability(newAvail);
			   returnList.add(returnSA);
		   }
	   }
	   
	   return returnList;
   }


   //    /$$$$$$   /$$$$$$  /$$    /$$ /$$$$$$$$
   //   /$$__  $$ /$$__  $$| $$   | $$| $$_____/
   //  | $$  \__/| $$  \ $$| $$   | $$| $$
   //  |  $$$$$$ | $$$$$$$$|  $$ / $$/| $$$$$
   //   \____  $$| $$__  $$ \  $$ $$/ | $$__/
   //   /$$  \ $$| $$  | $$  \  $$$/  | $$
   //  |  $$$$$$/| $$  | $$   \  $/   | $$$$$$$$
   //   \______/ |__/  |__/    \_/    |________/
   public void save(StylistAvailability stylistAvailability) {
       L.info("Saving the following StylistAvailabilty object: " + stylistAvailability);
       mongoTemplate.save(stylistAvailability);
   }
}
