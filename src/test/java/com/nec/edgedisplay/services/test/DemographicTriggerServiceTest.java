package com.nec.edgedisplay.services.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.annotations.VisibleForTesting;
import com.nec.edgedisplay.common.redis.services.MasterPlayerListRedisService;
import com.nec.edgedisplay.exception.MessageNotFoundException;
import com.nec.edgedisplay.exception.PlayerNotFoundException;
import com.nec.edgedisplay.exception.TriggerPercentageException;
import com.nec.edgedisplay.model.DemographicTrigger;
import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.People;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.Weather;
import com.nec.edgedisplay.services.AIService;
import com.nec.edgedisplay.services.ClipService;
import com.nec.edgedisplay.services.TriggerService;
import com.nec.edgedisplay.services.ZipService;
import com.nec.edgedisplay.services.impl.DemographicTriggerServiceImpl;

import net.sf.clipsrules.jni.Environment;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DemographicTriggerServiceTest {

	@InjectMocks
	DemographicTriggerServiceImpl demographicTriggerService;

	@Mock
	TriggerService triggerService;

	@Mock
	ClipService clipService;

	@Mock
	MasterPlayerListRedisService masterPlayerListRedisService;

	@Mock
	AIService aiService;

	@Mock
	Environment engine = new Environment();

	@Mock
	ZipService zipService;

	@Before
	public void init() {

		ReflectionTestUtils.setField(demographicTriggerService, "triggerAI", false);

	}

	@Test(expected = JsonParseException.class)
	@VisibleForTesting
	public void testTriggerToPlayerJsonParseException()
			throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		PlayerIdList playerData = new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Map<String, Float> messageId = new HashMap<>();
		messageId.put("1234", .9f);
		String topicContent = "OK sfsgfc hsdfhd";
		String zipCode = "10001";
		Weather weather = new Weather();
		List<Event> events = new ArrayList<Event>();
		DemographicTrigger demographic = new DemographicTrigger();
		
		demographic.setCameraId("");
		demographic.setLocationId("2");
		List<People> peoples=new ArrayList<>();
		People people=new People();
		people.setAge(24);
		people.setGender("FEMALE");
		peoples.add(people);
		demographic.setPeople(peoples);

		Mockito.when(zipService.getZipCode()).thenReturn(zipCode);
		Mockito.when(zipService.getWeather(zipCode)).thenReturn(weather);
		Mockito.when(zipService.getEvents(zipCode)).thenReturn(events);
		Mockito.when(clipService.getTriggringMsgId(demographic, playerData, weather, events, engine)).thenReturn(messageId);
		Mockito.doNothing().when(triggerService).triggerToPlayer(.85f, playerData, "1234");
		demographicTriggerService.triggerToPlayer(topicContent);

	}

	
	@Test()
	@VisibleForTesting
	public void testTriggerToPlayerPeopleDataEmpty()
			throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		PlayerIdList playerData = new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Map<String, Float> messageId = new HashMap<>();
		messageId.put("1234", .9f);
		String topicContent = "{\"type\":\"demographicTrigger\",\"topicId\":\"trigger\",\"locationId\":\"2\",\"cameraId\":\"device.94188201825b.3\",\"startTime\":\"2019-09-12T05:17:52Z\",\"endTime\":\"2019-09-12T05:17:53Z\",\"count\":0,\"people\":[]}";
		String zipCode = "10001";
		Weather weather = new Weather();
		List<Event> events = new ArrayList<Event>();
		DemographicTrigger demographic = new DemographicTrigger();
		
		demographic.setCameraId("");
		demographic.setLocationId("2");
		List<People> peoples=new ArrayList<>();
		People people=new People();
		people.setAge(24);
		people.setGender("FEMALE");
		peoples.add(people);
		demographic.setPeople(peoples);

		Mockito.when(zipService.getZipCode()).thenReturn(zipCode);
		Mockito.when(masterPlayerListRedisService.findPlayer(Mockito.anyString())).thenReturn(playerData);
		Mockito.when(zipService.getWeather(zipCode)).thenReturn(weather);
		Mockito.when(zipService.getEvents(zipCode)).thenReturn(events);
		Mockito.when(clipService.getTriggringMsgId(demographic, playerData, weather, events, engine)).thenReturn(messageId);
		Mockito.doNothing().when(triggerService).triggerToPlayer(.85f, playerData, "1234");
		demographicTriggerService.triggerToPlayer(topicContent);

	}

	@Test()
	@VisibleForTesting
	public void testTriggerToPlayerPeopleDataAvailable()
			throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		PlayerIdList playerData = new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		
		String topicContent = "{\"type\":\"demographicTrigger\",\"topicId\":\"trigger\",\"locationId\":\"2\",\"cameraId\":\"device.94188201825b.1\",\"startTime\":\"2019-09-12T09:37:23Z\",\"endTime\":\"2019-09-12T09:37:24Z\",\"count\":1,\"people\":[{\"gender\":\"MALE\",\"genderConfidence\":\"0.9999\",\"age\":27,\"ageConfidence\":\"0.21\",\"pan\":5,\"tilt\":-9,\"trackId\":1568184706206,\"trackCount\":3}]}";
		String zipCode = "10001";
		Weather weather = new Weather();
		List<Event> events = new ArrayList<Event>();
		DemographicTrigger demographic = new DemographicTrigger();
		
		demographic.setCameraId("");
		demographic.setLocationId("2");
		List<People> peoples=new ArrayList<>();
		People people=new People();
		people.setAge(24);
		people.setGender("FEMALE");
		peoples.add(people);
		demographic.setPeople(peoples);
		Map<String, Float> messageId = new HashMap<>();
		messageId.put("1234", .9f);
		Mockito.when(zipService.getZipCode()).thenReturn(zipCode);
		Mockito.when(masterPlayerListRedisService.findPlayer(Mockito.anyString())).thenReturn(playerData);
		Mockito.when(zipService.getWeather(zipCode)).thenReturn(weather);
		Mockito.when(zipService.getEvents(zipCode)).thenReturn(events);
//		Mockito.when(clipService.getTriggringMsgId(demographic, playerData, weather, events, engine)).thenReturn(messageId);
		Mockito.doNothing().when(triggerService).triggerToPlayer(.85f, playerData, "1234");
		demographicTriggerService.triggerToPlayer(topicContent);

	}

}
