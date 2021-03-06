package com.teamsierra.csc191.api.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
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
    private String appointmentType;
    private String appointmentTypeID;
    private String comment;

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

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

    public String getAppointmentType()
    {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType)
    {
        this.appointmentType = appointmentType;
    }

    public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@JsonIgnore
    public String getAppointmentTypeID()
    {
        return appointmentTypeID;
    }

    @JsonProperty(value = "appointmentTypeID")
    public void setAppointmentTypeID(String appointmentTypeID)
    {
        this.appointmentTypeID = appointmentTypeID;
    }
}
