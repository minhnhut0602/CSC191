package com.teamsierra.csc191.api.repository;

import com.teamsierra.csc191.api.model.Appointment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * User: scott
 * Date: 9/8/13
 * Time: 8:56 PM
 */

@Repository
public class AppointmentRepository {
    private static final Log L = LogFactory.getLog(AppointmentRepository.class);

    private MongoTemplate mongoTemplate;

    @Autowired
    public AppointmentRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;

        if (!mongoTemplate.collectionExists(Appointment.class)) {
            L.debug("Creating the appointments collection");
            mongoTemplate.createCollection(Appointment.class);
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
    public Appointment insert(Appointment appointment) {
        L.debug("Inserting new appointment: "+ appointment);
        mongoTemplate.insert(appointment);
        return appointment;
    }


    //   /$$$$$$$$ /$$$$$$ /$$   /$$ /$$$$$$$
    //  | $$_____/|_  $$_/| $$$ | $$| $$__  $$
    //  | $$        | $$  | $$$$| $$| $$  \ $$
    //  | $$$$$     | $$  | $$ $$ $$| $$  | $$
    //  | $$__/     | $$  | $$  $$$$| $$  | $$
    //  | $$        | $$  | $$\  $$$| $$  | $$
    //  | $$       /$$$$$$| $$ \  $$| $$$$$$$/
    //  |__/      |______/|__/  \__/|_______/
    public Appointment findByID(String appointmentID)
    {
        L.debug("Finding an appointment by _id:"+ appointmentID);
        return mongoTemplate.findOne(query(where("_id").is(appointmentID)), Appointment.class);
    }

    /**
     * Find all appointments that match search criteria
     * by build a filter query
     * @param appointment
     * @return
     */
    public List<Appointment> findByCriteria(Appointment appointment)
    {
        L.debug("Finding appointments by filters:"+appointment.toString());
        Query query = new Query();

        //Define search keywords
        String appointmentId = appointment.getId();
        String stylistID = appointment.getStylistID();
        String clientID = appointment.getClientID();
        Appointment.AppointmentStatus appointmentStatus = appointment.getAppointmentStatus();
        Date startTime = appointment.getStartTime();
        Date endTime = appointment.getEndTime();

        if (appointmentId != null && !appointmentId.isEmpty())
            query.addCriteria(where("_id").is(appointmentId));

        if (stylistID != null && !stylistID.isEmpty())
            query.addCriteria(where("stylistID").is(stylistID));

        if (clientID != null && !clientID.isEmpty())
            query.addCriteria(where("clientID").is(clientID));

        if (appointmentStatus != null)
            query.addCriteria(where("appointmentStatus").is(appointmentStatus));

        // Get appointments in the give time range
        if (startTime != null & endTime != null)
        {
            // This basically calculates collisions
            Criteria timeRanges = new Criteria().orOperator(
                where("startTime").gte(startTime).lte(endTime),
                where("endTime").gte(startTime).lte(endTime),
                where("startTime").lte(startTime).and("endTime").gte(endTime));

            query.addCriteria(timeRanges);
        }
        else
        {
            if (startTime != null)
                query.addCriteria(where("startTime").gte(startTime));

            if (endTime != null)
                query.addCriteria(where("endTime").lte(endTime));
        }

        return mongoTemplate.find(query, Appointment.class);
    }


    public List<Appointment> findAll()
    {
        return mongoTemplate.findAll(Appointment.class);
    }


    //    /$$$$$$   /$$$$$$  /$$    /$$ /$$$$$$$$
    //   /$$__  $$ /$$__  $$| $$   | $$| $$_____/
    //  | $$  \__/| $$  \ $$| $$   | $$| $$
    //  |  $$$$$$ | $$$$$$$$|  $$ / $$/| $$$$$
    //   \____  $$| $$__  $$ \  $$ $$/ | $$__/
    //   /$$  \ $$| $$  | $$  \  $$$/  | $$
    //  |  $$$$$$/| $$  | $$   \  $/   | $$$$$$$$
    //   \______/ |__/  |__/    \_/    |________/
    public void save(Appointment appointment) {
        L.debug("Saving the following appointment object: " + appointment);
        mongoTemplate.save(appointment);
    }
}