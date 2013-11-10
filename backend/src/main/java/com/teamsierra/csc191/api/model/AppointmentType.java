package com.teamsierra.csc191.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: Alex Chernyak
 * @Date: 11/5/13
 * @Project: salon-scheduler-api
 * @Package: com.teamsierra.csc191.api.model
 * @Description: place short description here
 */
@Document(collection = "appointmentTypes")
public class AppointmentType extends GenericModel
{
    private String appointmentType;
    private int durationInMinutes;
    private double basePrice;
    private String[] stylists;

    public String getAppointmentType()
    {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType)
    {
        this.appointmentType = appointmentType;
    }

    public int getDurationInMinutes()
    {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes)
    {
        this.durationInMinutes = durationInMinutes;
    }

    public double getBasePrice()
    {
        return basePrice;
    }

    public void setBasePrice(double basePrice)
    {
        this.basePrice = basePrice;
    }

    public String[] getStylists()
    {
        return stylists;
    }

    public void setStylists(String[] stylists)
    {
        this.stylists = stylists;
    }
}
