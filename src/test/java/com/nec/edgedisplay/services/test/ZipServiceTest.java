package com.nec.edgedisplay.services.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.nec.edgedisplay.common.redis.services.AccuWeatherRedisService;
import com.nec.edgedisplay.common.redis.services.EventRedisService;
import com.nec.edgedisplay.common.redis.services.MasterPlayerListRedisService;
import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.Weather;
import com.nec.edgedisplay.model.store.Store;
import com.nec.edgedisplay.services.impl.ZipServiceImpl;
import com.nec.edgedisplay.utils.Constants;
import com.nec.edgedisplay.utils.DateUtils;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ZipServiceTest {

	@InjectMocks
	ZipServiceImpl zipService;

	@Mock
	AccuWeatherRedisService accuWeatherRedisService;

	@Mock
	MasterPlayerListRedisService masterPlayerListRedisService;

	@Mock
	EventRedisService eventRedisService;

	@Test
	public void getZipCode() {

		Store store = new Store();
		Mockito.when(masterPlayerListRedisService.findStore(2)).thenReturn(store);
		zipService.getZipCode();

	}

	@Test
	public void getWeather() {

		Weather weather = new Weather();
		Mockito.when(accuWeatherRedisService.find("10001")).thenReturn(weather);
		zipService.getWeather("10001");

	}
	
	@Test(expected = Exception.class)
	public void getWeatheTest() {

		Weather weather = new Weather();
		Mockito.when(accuWeatherRedisService.find(null)).thenReturn(weather);
		zipService.getWeather("10001");

	}
	
	@Test(expected = Exception.class)
	public void getWeatheEmpty() {

		Weather weather = new Weather();
		Mockito.when(accuWeatherRedisService.find("")).thenReturn(weather);
		zipService.getWeather("10001");

	}
	
	

	@Test
	public void getEvents() {

		List<Event> events = new ArrayList<>();
		Mockito.when(eventRedisService.find("10001")).thenReturn(events);
		zipService.getEvents("10001");

	}
	
	
	@Test
	public void createDefaultEvent() {

		List<Event> events = new ArrayList<>();
		Event event = new Event();
		event.setEventName(Constants.DEFAULT_EVENT);
		event.setEventDescription(Constants.DEFAULT_EVENT_DESCRIPTION);
		event.setZipCode("10001");
		event.setId(1);
		event.setEventStartTime(String.valueOf(DateUtils.getCurrentTime().getTime().getTime()));
		events.add(event);
		Mockito.when(eventRedisService.find("10001")).thenReturn(events);
		zipService.getEvents("10001");

	}
}
