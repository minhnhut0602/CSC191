package com.teamsierra.csc191.api.model;

/**
 * @Author: Alex Chernyak
 * @Date: 11/5/13
 * @Project: salon-scheduler-api
 * @Package: com.teamsierra.csc191.api.model
 * @Description: place short description here
 */
public class AppointmentType extends GenericModel
{
    private String type;
    private int durationInMinutes;
    private double basePrice;
    private String[] stylists;

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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
