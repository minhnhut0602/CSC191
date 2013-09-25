package com.teamsierra.csc191.api.repository;

import com.teamsierra.csc191.api.model.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.teamsierra.csc191.api.model.Appointment;
import org.springframework.stereotype.Repository;

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
    public Appointment findByID(String id) {
        L.debug("Finding an appointment by _id:"+ id);
        return mongoTemplate.findOne(query(where("_id").is(id)), Appointment.class);
    }
    public List<Appointment> findAll(User user) {
        L.debug("Finding all appointments for user: "+ user);
        return mongoTemplate.find(query(where("clientID").is(user.getId())), Appointment.class);
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