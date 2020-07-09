package com.nec.edgedisplay.services.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.nec.edgedisplay.model.DemographicTrigger;
import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.People;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.Weather;
import com.nec.edgedisplay.services.FactService;
import com.nec.edgedisplay.services.impl.ClipServiceImpl;

import net.sf.clipsrules.jni.Environment;
import net.sf.clipsrules.jni.PrimitiveValue;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ClipServiceTest {

	@InjectMocks
	ClipServiceImpl clipServiceImpl;

	@Mock
	FactService factService;

	@Mock
	Environment engine;

	@Test
	public void getTriggeredMessage() {

		DemographicTrigger demographic = new DemographicTrigger();

		demographic.setCameraId("");
		demographic.setLocationId("2");
		List<People> peoples = new ArrayList<>();
		People people = new People();
		people.setAge(24);
		people.setGender("FEMALE");
		peoples.add(people);
		demographic.setPeople(peoples);

		PlayerIdList playerData = new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Weather weather = new Weather();
		List<Event> events = new ArrayList<Event>();
		Event event = new Event();
		event.setEventName("First Event");
		event.setEventDescription("Event description");
		events.add(event);

		Mockito.doNothing().when(factService).createFact(Mockito.any(), Mockito.anyString(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.any());

		Mockito.when(engine.run()).thenReturn(2L);
}
	
	@Test
	public void getTriggeredMessageAsEmpty() {

		DemographicTrigger demographic = new DemographicTrigger();

		demographic.setCameraId("");
		demographic.setLocationId("2");
		List<People> peoples = new ArrayList<>();
		People people = new People();
		people.setAge(24);
		people.setGender("FEMALE");
		peoples.add(people);
		demographic.setPeople(peoples);

		PlayerIdList playerData = new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Weather weather = new Weather();
		List<Event> events = new ArrayList<Event>();
		Event event = new Event();
		event.setEventName("First Event");
		event.setEventDescription("Event description");
		events.add(event);

		Mockito.doNothing().when(factService).createFact(Mockito.any(), Mockito.anyString(), Mockito.anyString(),
				Mockito.any(), Mockito.anyString(), Mockito.any());

		Mockito.when(engine.run()).thenReturn(0L);
		PrimitiveValue maxValue = new PrimitiveValue(4.6f) {
		};
      

		PrimitiveValue resule = new PrimitiveValue("271014") {
		};
		Mockito.when(engine.eval("?*MaxVal*")).thenReturn(maxValue);
		Mockito.when(engine.eval("?*TriggerMsg*")).thenReturn(resule);

		clipServiceImpl.getTriggringMsgId(demographic, playerData, weather, events, engine);
	}
}
