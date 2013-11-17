package com.teamsierra.csc191.api.model;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.springframework.data.mongodb.core.mapping.Document;

import com.teamsierra.csc191.api.util.Availability;
import com.teamsierra.csc191.api.util.AvailabilityDeserializer;

@Document(collection = "stylistAvailabilities")
public class StylistAvailability extends GenericModel
{
	private String stylistID;
	private Availability availability;
	
	public String getStylistID() {
		return stylistID;
	}

	public void setStylistID(String stylistID) {
		this.stylistID = stylistID;
	}

	public Availability getAvailability() 
	{
		return availability;
	}
	
	@JsonDeserialize(using = AvailabilityDeserializer.class)
	public void setAvailability(Availability availability)
	{
		this.availability = availability;
	}
	
	@Override
	public String toString()
	{
		return "{" +
    			"id='" + this.getId() + '\'' +
    			", stylistID='" + stylistID + '\'' +
    			", availability='" + availability + '\'' +
    			'}';
	}
}
