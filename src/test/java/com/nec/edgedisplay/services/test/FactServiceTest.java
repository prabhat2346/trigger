package com.nec.edgedisplay.services.test;


import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.Weather;
import com.nec.edgedisplay.services.impl.FactServiceImpl;
import com.nec.edgedisplay.utils.AgeUtils;

import net.sf.clipsrules.jni.Environment;
import net.sf.clipsrules.jni.PrimitiveValue;


@RunWith(MockitoJUnitRunner.Silent.class)
public class FactServiceTest {

	@InjectMocks
	FactServiceImpl factService;
	
	@Mock
	Environment engine;
	
	@Before
	public void init() {
		ReflectionTestUtils.setField(factService, "fileName", "RuleTrigger.clp");

	}
	
	

	@Test
	public void createFactTestFromClp()  {
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Weather weather=new Weather();
		weather.setCategory("Sunny");
		weather.setTemperatureText("warm");
		PrimitiveValue value=new PrimitiveValue(4) {
		};
		Mockito.doNothing().when(engine).load("RuleTrigger.clp");
		Mockito.when(engine.eval("(assert (A 0) (weather \"" + "Sunny" + "\") (age " + "22-27" + ") (gender "
				+ "MALE" + ") (player " + "30532" + ")(temp " + "warm"
				+ ") (event \"" + "None" + "\")  )")).thenReturn(value);
		
		factService.createFact(engine, "MALE", AgeUtils.ageMapper(24), playerData,
				"", weather);

		

	}
	
	@Test()
	public void createFactTestFromEmptyClp () {
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Weather weather=new Weather();
		weather.setCategory("Sunny");
		weather.setTemperatureText("warm");
		List<Event> events=new ArrayList<>();
		Event event=new Event();
		event.setEventName("First event");
		event.setEventDescription("Shore description");
		events.add(event);
		PrimitiveValue value=new PrimitiveValue(4) {
		};
		
		Mockito.doNothing().when(engine).load("RuleTrigger.clp");
		Mockito.when(engine.eval("(assert (A 0) (weather \"" + "Sunny" + "\") (age " + "22-27" + ") (gender "
				+ "MALE" + ") (player " + "30532" + ")(temp " + "warm"
				+ ") (event \"" + "None" + "\")  )")).thenReturn(value);
		
		factService.createFact( engine,  "MALE",  "22-27",playerData,  events,  weather);

		

	}
}
