package com.teamsierra.csc191.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.hateoas.Identifiable;

/**
 * @Author: Alex Chernyak
 * @Date: 10/27/13
 * @Project: salon-scheduler-api
 * @Package: com.teamsierra.csc191.api.model
 * @Description: Abstract model class that defines Id field
 *               behavior for HATEOAS constraint principle
 */
public abstract class GenericModel implements Identifiable<String>
{
    public enum AppointmentStatus {NEW, CANCELED, APPROVED, REJECTED, MISSED, COMPLETED};
    public enum UserType {CLIENT, STYLIST, ADMIN};

    @Id
    private String id;

    @Override
    public String getId()
    {
        return this.id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
