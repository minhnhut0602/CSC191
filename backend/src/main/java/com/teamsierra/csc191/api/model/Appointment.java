package com.teamsierra.csc191.api.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


/**
 * @Author: Alex Chernyak
 * @Date: 10/29/13
 * @Project: salon-scheduler-api
 * @Package: com.teamsierra.csc191.api.model
 * @Description: model representing a single appointment. Extends GenericModel
 */
@Document(collection = "appointments")
public class Appointment extends GenericModel
{
    private String clientID;
    private String stylistID;
    private Date startTime;
    private Date endTime;
    private AppointmentStatus appointmentStatus;

    @JsonIgnore
    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    @JsonIgnore
    public String getStylistID() {
        return stylistID;
    }

    public void setStylistID(String stylistID) {
        this.stylistID = stylistID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public AppointmentStatus getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

}
