package com.teamsierra.csc191.api.util;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

public class AvailabilityDeserializer extends JsonDeserializer<Availability> 
{

	@Override
	public Availability deserialize(JsonParser parser,
									DeserializationContext dc) throws IOException, JsonProcessingException
	{
		Availability availability = new Availability();
		
		String token;		
		parser.nextToken();
		while(parser.hasCurrentToken())
		{
			token = parser.getText();
			
			if(token.equals("startDate"))
			{
				Date startDate = new Date(parser.getLongValue());
				
				token = parser.getText();
				validate(parser, token, "endDate");
				Date endDate = new Date(parser.getLongValue());
				
				availability.addRange(startDate, endDate);
			}
			
			parser.nextToken();
		}	
		
		return availability;
	}
	
	private void validate(JsonParser parser, String input, String expected) throws JsonParseException
	{
		if(!input.equals(expected))
		{
			throw new JsonParseException("Syntax error: input = " + input +
					", expected = " + expected, parser.getTokenLocation());
		}
	}
}
