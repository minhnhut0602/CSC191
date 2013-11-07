package com.teamsierra.csc191.api.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

import com.teamsierra.csc191.api.util.Availability;

@Document(collection = "stylistAvailabilities")
public class StylistAvailability extends GenericModel
{
	private String stylistID;
	private Availability availability;
	
	@JsonIgnore
	public String getStylistID() {
		return stylistID;
	}

	@JsonProperty(value = "stylistID")
	public void setStylistID(String stylistID) {
		this.stylistID = stylistID;
	}

	public Availability getAvailability() 
	{
		return availability;
	}

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
