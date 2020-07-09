package com.nec.edgedisplay.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nec.edgedisplay.common.redis.services.AccuWeatherRedisService;
import com.nec.edgedisplay.common.redis.services.EventRedisService;
import com.nec.edgedisplay.common.redis.services.MasterPlayerListRedisService;
import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.Weather;
import com.nec.edgedisplay.model.store.Store;
import com.nec.edgedisplay.services.ZipService;
import com.nec.edgedisplay.utils.Constants;
import com.nec.edgedisplay.utils.DateUtils;

@Service
public class ZipServiceImpl implements ZipService {

	private final Logger logger = LoggerFactory.getLogger(ZipServiceImpl.class);

	@Autowired
	private AccuWeatherRedisService accuWeatherRedisService;

	@Autowired
	private MasterPlayerListRedisService masterPlayerListRedisService;
	@Autowired
	EventRedisService eventRedisService;

	@Value("${store.id}")
	private int storeId;

	@Value("${current.weather}")
	private String currentWeather;

	private String temp = "chilly";

	@Override
	public String getZipCode() {
		Store store = masterPlayerListRedisService.findStore(storeId);
		return store != null ? store.getData().getAddress().getZipCode() : "";

	}

	@Override
	public Weather getWeather(final String zipCode) {
		Weather weather = null;
		weather = accuWeatherRedisService.find(zipCode);
		if (weather!=null) {
			temp = weather.getTemperatureText() == null ? temp : weather.getTemperatureText().toLowerCase();
			weather.setTemperatureText(temp);
		}else {
			weather=new Weather();
			weather.setTemperatureText(temp);  // Set default when data not available
			weather.setCategory(currentWeather);
		}
	
		
		
		return weather;
	}

	@Override
	public List<Event> getEvents(final String zipCode) {
		List<Event> events = eventRedisService.find(zipCode);
		if (events == null|| events.isEmpty() ) {
			events = createDefaultEvent(zipCode);
		}
		return events;
	}

	private List<Event> createDefaultEvent(final String zipCode) {
		List<Event> events = new ArrayList<>();
		Event event = new Event();
		event.setEventName(Constants.DEFAULT_EVENT);
		event.setEventDescription(Constants.DEFAULT_EVENT_DESCRIPTION);
		event.setZipCode(zipCode);
		event.setId(1);
		event.setEventStartTime(String.valueOf(DateUtils.getCurrentTime().getTime().getTime()));
		events.add(event);
		return events;
	}

}
