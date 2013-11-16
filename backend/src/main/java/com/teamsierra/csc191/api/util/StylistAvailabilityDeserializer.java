package com.teamsierra.csc191.api.util;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.teamsierra.csc191.api.model.StylistAvailability;

public class StylistAvailabilityDeserializer extends JsonDeserializer<StylistAvailability>
{
	@Override
	public StylistAvailability deserialize(JsonParser parser, 
										   DeserializationContext dc) throws IOException, JsonProcessingException 
	{
		StylistAvailability availability = new StylistAvailability();
		// TODO Auto-generated method stub
		return availability;
	}
}
