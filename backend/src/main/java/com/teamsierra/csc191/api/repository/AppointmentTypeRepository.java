package com.teamsierra.csc191.api.repository;

import com.mongodb.WriteResult;
import com.teamsierra.csc191.api.model.Appointment;
import com.teamsierra.csc191.api.model.AppointmentType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @Author: Alex Chernyak
 * @Date: 11/5/13
 * @Project: salon-scheduler-api
 * @Package: com.teamsierra.csc191.api.repository
 * @Description: table to store appointment types and associated stylists
 */
@Repository
public class AppointmentTypeRepository
{
    private static final Log L = LogFactory.getLog(AppointmentRepository.class);

    private MongoTemplate mongoTemplate;

    @Autowired
    public AppointmentTypeRepository(MongoTemplate mongoTemplate)
    {
        this.mongoTemplate = mongoTemplate;

        if (!mongoTemplate.collectionExists(Appointment.class))
        {
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
    public AppointmentType insert(AppointmentType type)
    {
        L.debug("Insering new type");
        if (type.getStylists() == null)
        {
            String[] stylists = new String[0];
            type.setStylists(stylists);
        }
        mongoTemplate.insert(type);
        return type;
    }


    //   /$$$$$$$$ /$$$$$$ /$$   /$$ /$$$$$$$
    //  | $$_____/|_  $$_/| $$$ | $$| $$__  $$
    //  | $$        | $$  | $$$$| $$| $$  \ $$
    //  | $$$$$     | $$  | $$ $$ $$| $$  | $$
    //  | $$__/     | $$  | $$  $$$$| $$  | $$
    //  | $$        | $$  | $$\  $$$| $$  | $$
    //  | $$       /$$$$$$| $$ \  $$| $$$$$$$/
    //  |__/      |______/|__/  \__/|_______/
    public List<AppointmentType> findByCriteria(AppointmentType appointmentType)
    {
        List<AppointmentType> result;
        Query query = new Query();

        String id = appointmentType.getId();
        String type = appointmentType.getAppointmentType();
        String[] stylists = appointmentType.getStylists();

        if (id != null && !id.isEmpty())
            query.addCriteria(where("_id").is(id));

        if (type != null && !type.isEmpty())
            query.addCriteria(where("type").is(type));

        if (stylists != null && stylists.length > 0)
            query.addCriteria(where("stylists").all(stylists[0]));

        L.info("Finding appointment types by query:" + query.toString());

        result = mongoTemplate.find(query, AppointmentType.class);

        return result;
    }

    //    /$$$$$$   /$$$$$$  /$$    /$$ /$$$$$$$$
    //   /$$__  $$ /$$__  $$| $$   | $$| $$_____/
    //  | $$  \__/| $$  \ $$| $$   | $$| $$
    //  |  $$$$$$ | $$$$$$$$|  $$ / $$/| $$$$$
    //   \____  $$| $$__  $$ \  $$ $$/ | $$__/
    //   /$$  \ $$| $$  | $$  \  $$$/  | $$
    //  |  $$$$$$/| $$  | $$   \  $/   | $$$$$$$$
    //   \______/ |__/  |__/    \_/    |________/
    public String addStylistToType(String typeID, String stylistID) throws Exception
    {
        if (typeID == null || stylistID == null || typeID.isEmpty() || stylistID.isEmpty())
            return "";

        Update update = new Update();
        update.push("stylists", stylistID);

        WriteResult errors = executeUpdate(typeID, update);

        // TODO add some result handling??

        return typeID;
    }

    public String deleteStylistFromType(String typeID, String stylistID)
    {
        if ((typeID == null || stylistID == null) && (typeID.isEmpty() || stylistID.isEmpty()))
            return "";

        Update update = new Update();
        update.pull("stylists", stylistID);

        WriteResult errors = executeUpdate(typeID, update);

        // TODO add some result handling??

        return typeID;
    }

    private WriteResult executeUpdate(String typeID, Update update)
    {
        return mongoTemplate.updateFirst(new Query(where("_id").is(typeID)),
                                         update, AppointmentType.class);
    }


    // TODO finish implementation
    public String updateType(AppointmentType type)
    {
        Query query = new Query();
        Update update = new Update();

        String typeID = type.getId();
        String typeName = type.getAppointmentType();
        Double basePrice = type.getBasePrice();
        int duration = type.getDurationInMinutes();

        if (typeID != null && typeID.isEmpty())
            query.addCriteria(where("_id").is(typeID));
        else if (typeName != null && !typeName.isEmpty())
            query.addCriteria(where("type").is(type));

        return "";
    }


    /**
     * Remove appointment type from repository
     * @param appointmentTypeID
     * @return
     */
    public String deleteType(String appointmentTypeID)
    {
        Query query = new Query();

        if (appointmentTypeID != null && !appointmentTypeID.isEmpty())
        {
            query.addCriteria(where("_id").is(appointmentTypeID));

            mongoTemplate.remove(query, AppointmentType.class);
        }

        return "";
    }

}

