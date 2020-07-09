package com.nec.edgedisplay.services;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
/**
 * 
 * @author Rahul Sharma
 *
 */
public interface IDemographicTriggerService {
	

	
	void triggerToPlayer(String topicContent) throws JsonParseException, JsonMappingException, IOException,Exception;
	
}
