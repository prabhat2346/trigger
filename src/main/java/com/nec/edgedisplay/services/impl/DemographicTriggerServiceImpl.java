package com.nec.edgedisplay.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nec.edgedisplay.common.DemographicTriggerVO;
import com.nec.edgedisplay.common.PeopleVO;
import com.nec.edgedisplay.common.redis.services.MasterPlayerListRedisService;
import com.nec.edgedisplay.exception.MessageNotFoundException;
import com.nec.edgedisplay.exception.PlayerNotFoundException;
import com.nec.edgedisplay.modal.AIMessage;
import com.nec.edgedisplay.model.DemographicTrigger;
import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.People;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.Weather;
import com.nec.edgedisplay.services.AIService;
import com.nec.edgedisplay.services.ClipService;
import com.nec.edgedisplay.services.IDemographicTriggerService;
import com.nec.edgedisplay.services.TriggerService;
import com.nec.edgedisplay.services.ZipService;

import net.sf.clipsrules.jni.Environment;

/**
 * 
 * @author Rahul Sharma
 *
 */
@Service
public class DemographicTriggerServiceImpl implements IDemographicTriggerService {

	private TriggerService triggerService;
	private ClipService clipService;
	private MasterPlayerListRedisService masterPlayerListRedisService;
	private AIService aiService;
	private Environment engine = new Environment();
	private final Logger logger = LoggerFactory.getLogger(DemographicTriggerServiceImpl.class);
	private ZipService zipService;
	@Value("${trigger.ai}")
	private boolean triggerAI;

	@Autowired
	public DemographicTriggerServiceImpl(final ClipService clipService, final TriggerService triggerService,
			final MasterPlayerListRedisService masterPlayerListRedisService, final AIService aiService,
			final ZipService zipService) {

		this.triggerService = triggerService;
		this.masterPlayerListRedisService = masterPlayerListRedisService;
		this.aiService = aiService;
		this.clipService = clipService;
		this.zipService = zipService;

	}

	@Override
	public synchronized void triggerToPlayer(String topicContent) throws JsonParseException, JsonMappingException, IOException,
			Exception, MessageNotFoundException, PlayerNotFoundException {

		final String TAG = DemographicTriggerServiceImpl.class.getSimpleName() + "##triggerToPlayer()";
//		logger.info("Inside {}", TAG);
		
		DemographicTrigger demographic = getDemographicDataObjectFromViewObject(
				getDemographicTriggerObjFromJsonString(topicContent));
		if (demographic != null) {

			final PlayerIdList playerData = masterPlayerListRedisService.findPlayer(demographic.getCameraId());
			
			
			logger.info("{} total number of event received from NEC_EVA:: {} ", TAG, demographic.getCount());
			if (!demographic.getPeople().isEmpty() && playerData != null) {
				logger.debug("{} topicContent recieved from NEC EVA ##{} : ", TAG, topicContent);
				logger.debug("{} Player Info:: {}", TAG, playerData);
				final String zipCode = zipService.getZipCode();
				final Weather weather = zipService.getWeather(zipCode);
				List<Event> events = zipService.getEvents(zipCode);
				if (triggerAI) {
					AIMessage aiMessage = aiService.getTriggringMsgId(demographic, playerData);
					if (aiMessage != null) {
						logger.info("{} AI Message Id :{}", TAG, aiMessage.getMsgId());
						triggerService.triggerToPlayer(playerData, aiMessage.getMsgId());
					}

				} else {

					Map<String, Float> messageWithVal = clipService.getTriggringMsgId(demographic, playerData, weather,
							events, engine);

					if (messageWithVal.size() != 0) {

						Optional<Entry<String, Float>> finalResult = calculateMaxResult(messageWithVal);
						if (finalResult.isPresent()) {
							logger.debug("{} message::{} -- and max value : {} ", TAG, finalResult.get().getKey(),
									finalResult.get().getValue());
							triggerService.triggerToPlayer(finalResult.get().getValue().floatValue(), playerData,
									finalResult.get().getKey());

						}

					} else {
						logger.info("{} No message found for given fact:", TAG);
					}

				}

				engine.clear();

			}

			demographic = null;

		} else {
			logger.error("demographic data not available");

		}

	}

	private Optional<Entry<String, Float>> calculateMaxResult(final Map<String, Float> messageWithVal) {

		Map<String, Float> finalResult = new HashMap<>();
		Optional<Entry<String, Float>> value = messageWithVal.entrySet().stream().filter(Objects::nonNull)
				.max(Map.Entry.comparingByValue());
		Entry<String, Float> finalResult1 = null;
		if (value.isPresent()) {
			finalResult1 = value.get();
		}
		if (finalResult1 != null) {

			final float matchPercentage = finalResult1.getValue().floatValue();
			Map<String, Float> randomValue = new HashMap<>();

			messageWithVal.entrySet().stream().forEach(entry -> {
				if ((float) entry.getValue() == matchPercentage) {
					randomValue.put(entry.getKey(), entry.getValue());
				}
			});

			Object[] keys = randomValue.keySet().toArray();
			finalResult.put(keys[new Random().nextInt(keys.length)].toString(), matchPercentage);
		}

		return finalResult.entrySet().stream().findFirst();
	}

	private static DemographicTriggerVO getDemographicTriggerObjFromJsonString(String jsonString)
			throws JsonParseException, JsonMappingException, IOException {
		DemographicTriggerVO demogprahicVO = null;
		ObjectMapper mapper = new ObjectMapper();

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		if (!StringUtils.isEmpty(jsonString)) {
			demogprahicVO = mapper.readValue(jsonString, DemographicTriggerVO.class);
		}
		return demogprahicVO;
	}

	private DemographicTrigger getDemographicDataObjectFromViewObject(DemographicTriggerVO demographicVo) {

		DemographicTrigger demographic = null;
		if (demographicVo != null) {
			demographic = new DemographicTrigger();

			demographic.setCameraId(demographicVo.getCameraId());
			demographic.setType("demographicTrigger");
			demographic.setLocationId(demographicVo.getLocationId());
			demographic.setEndTime(demographicVo.getStartTime());
			demographic.setStartTime(demographicVo.getEndTime());

			demographic.setCount(demographicVo.getCount());

			List<People> peopleList = new ArrayList<>();

			for (PeopleVO peopleVo : demographicVo.getPeople()) {
				People people = new People();
				people.setAge(peopleVo.getAge());
				people.setAgeConfidence(peopleVo.getAgeConfidence());
				people.setGender(peopleVo.getGender());
				people.setGenderConfidence(peopleVo.getGenderConfidence());
				people.setTrackCount(peopleVo.getTrackCount());
				people.setTrackId(people.getTrackId());
				peopleList.add(people);
			}

			demographic.setPeople(peopleList);

		}

		return demographic;
	}

	@PreDestroy
	public void methodName() {
		engine.clear();

	}

}
