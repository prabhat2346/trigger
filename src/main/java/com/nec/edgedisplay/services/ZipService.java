package com.nec.edgedisplay.services;

import java.util.List;

import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.Weather;

public interface ZipService {
	
	String getZipCode();
	Weather getWeather(String zipCode);
	List <Event> getEvents(String zipCode);

}
