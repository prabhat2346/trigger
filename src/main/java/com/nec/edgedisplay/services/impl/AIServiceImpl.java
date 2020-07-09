package com.nec.edgedisplay.services.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nec.edgedisplay.common.redis.services.AccuWeatherRedisService;
import com.nec.edgedisplay.common.redis.services.MasterPlayerListRedisService;
import com.nec.edgedisplay.modal.AIMessage;
import com.nec.edgedisplay.model.DemographicTrigger;
import com.nec.edgedisplay.model.People;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.Weather;
import com.nec.edgedisplay.model.store.Store;
import com.nec.edgedisplay.services.AIService;
import com.necti.httpclient.http.HttpClient;

@Service
public class AIServiceImpl implements AIService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AIServiceImpl.class);
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private HttpClient httpClient;

	@Value("${ai.base.url}")
	private String aiBaseUrl;
	@Value("${store.id}")
	private int storeId;

	@Autowired
	private AccuWeatherRedisService accuWeatherRedisService;

	@Autowired
	private MasterPlayerListRedisService masterPlayerListRedisService;

	@Override
	public AIMessage getTriggringMsgId(final DemographicTrigger demographic, final PlayerIdList playerData)
			throws JsonParseException, JsonMappingException, IOException {
		
		final String TAG=AIServiceImpl.class.getSimpleName()+"##getTriggringMsgId()";
		LOGGER.info("Inside {}",TAG);
		Weather currentWeather = null;
		AIMessage aiMessage=null;
		Store store = masterPlayerListRedisService.findStore(storeId);
		final String zipcode = store != null ? store.getData().getAddress().getZipCode() : "";

		try {
			if (zipcode.equals("") && zipcode != null) {
				currentWeather = accuWeatherRedisService.find(zipcode);

			}

		} catch (Exception e) {
			LOGGER.error("{} Error while getting weather data from redis for AI:{}",TAG , e.getMessage());
		}

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		ObjectNode data = null;

		List<People> people = demographic.getPeople();
		for (People p : people) {
			if (p.getTrackId() == null) {
				p.setTrackId(0l);
			}
			if (p.getDataStoreId() == null) {
				p.setDataStoreId(0l);

			}
			if (p.getDwellTime() == null) {
				p.setDwellTime(0);
			}
		}
		if (demographic.getDataStoreId() == null)
			demographic.setDataStoreId(0l);

		data = mapper.valueToTree(demographic);

		data.set("weatherData", mapper.valueToTree(currentWeather));
		data.put("playerId", playerData.getPlayerId());

		HttpEntity<String> request = new HttpEntity<>(data.toString(), header);
		ResponseEntity<JsonNode> response = null;
		try {
			response = httpClient.callClientApiAsJson(aiBaseUrl, HttpMethod.POST, request, null);
			aiMessage=mapper.readValue(response.getBody().toString(), new TypeReference<AIMessage>() {});
			LOGGER.debug("{} Resonpse from client api ::{}",TAG , response);
			return aiMessage;
		} catch (Exception e) {
			LOGGER.error("{} Error while getting message data from AI service:{} ",TAG ,e.getMessage());
		}
		
		
		
		return aiMessage;

	}

}
