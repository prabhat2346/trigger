package com.nec.edgedisplay.services.test;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nec.edgedisplay.common.redis.services.AccuWeatherRedisService;
import com.nec.edgedisplay.common.redis.services.MasterPlayerListRedisService;
import com.nec.edgedisplay.model.DemographicTrigger;
import com.nec.edgedisplay.model.People;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.Weather;
import com.nec.edgedisplay.model.store.Address;
import com.nec.edgedisplay.model.store.Data;
import com.nec.edgedisplay.model.store.Store;
import com.nec.edgedisplay.services.impl.AIServiceImpl;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AIServiceTest {
	
	@InjectMocks
	AIServiceImpl aIService;
	
	@Mock
	AccuWeatherRedisService accuWeatherRedisService;

	@Mock
    MasterPlayerListRedisService masterPlayerListRedisService;
	
	
	@Before
	public void init() {

		ReflectionTestUtils.setField(aIService, "aiBaseUrl", "http://23.100.83.147:5001/trigger_message");
		ReflectionTestUtils.setField(aIService, "storeId", 2);

	}
	
	@Test
	public void getTriggringMsgId() throws JsonParseException, JsonMappingException, IOException {
		
		PlayerIdList playerData = new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Map<String, Float> messageId = new HashMap<>();
		messageId.put("1234", .9f);

		String zipCode = "10001";
		Weather weather = new Weather();
		Store store=new Store();
		Data data=new Data();
		Address address=new Address();
		address.setZipCode("10001");
		data.setAddress(address);
		store.setData(data);
		DemographicTrigger demographic = new DemographicTrigger();
		demographic.setCameraId("");
		demographic.setLocationId("2");
		List<People> peoples=new ArrayList<>();
		People people=new People();
		people.setAge(24);
		people.setGender("FEMALE");
		peoples.add(people);
		demographic.setPeople(peoples);
		
		Mockito.when(masterPlayerListRedisService.findStore(2)).thenReturn(store);
		Mockito.when(accuWeatherRedisService.find(zipCode)).thenReturn(weather);
		aIService.getTriggringMsgId(demographic, playerData);


		
		
	}
	

	
	@Test
	public void getTriggringMsgIdZipCodeEmpty() throws JsonParseException, JsonMappingException, IOException {
		
		PlayerIdList playerData = new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Map<String, Float> messageId = new HashMap<>();
		messageId.put("1234", .9f);
		String zipCode = "10001";
		Weather weather = new Weather();
		Store store=null;
		DemographicTrigger demographic = new DemographicTrigger();
		demographic.setCameraId("");
		demographic.setLocationId("2");
		List<People> peoples=new ArrayList<>();
		People people=new People();
		people.setAge(24);
		people.setGender("FEMALE");
		people.setDwellTime(8);
		people.setDataStoreId(123L);
		people.setTrackId(1243L);
		peoples.add(people);
		demographic.setPeople(peoples);
		demographic.setDataStoreId(3432L);
		
		Mockito.when(masterPlayerListRedisService.findStore(2)).thenReturn(store);
		Mockito.when(accuWeatherRedisService.find(zipCode)).thenReturn(weather);
		aIService.getTriggringMsgId(demographic, playerData);


		
		
	}
	



}
