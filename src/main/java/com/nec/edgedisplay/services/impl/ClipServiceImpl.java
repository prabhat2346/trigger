package com.nec.edgedisplay.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nec.edgedisplay.model.DemographicTrigger;
import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.Weather;
import com.nec.edgedisplay.services.ClipService;
import com.nec.edgedisplay.services.FactService;
import com.nec.edgedisplay.utils.AgeUtils;

import net.sf.clipsrules.jni.Environment;
import net.sf.clipsrules.jni.FloatValue;

@Service
public class ClipServiceImpl implements ClipService {

	private FactService factService;
	private final Logger logger = LoggerFactory.getLogger(ClipServiceImpl.class);

	@Autowired
	public ClipServiceImpl(final FactService factService) {
		this.factService = factService;

	}

	@Override
	public Map<String, Float> getTriggringMsgId(final DemographicTrigger demographic, final PlayerIdList playerData,
			Weather weather, List<Event> events, final Environment engine) {
		Map<String, Float> messageWithVal = new HashMap<>();
		final String TAG=ClipServiceImpl.class.getSimpleName()+"#getTriggringMsgId";
		demographic.getPeople().stream().filter(Objects::nonNull).forEach(person -> {

			logger.info(TAG+" Age received from NEC-EVA is" + person.getAge() + "age group is "
					+ AgeUtils.ageMapper(person.getAge()), " &gender is ", person.getGender());

			logger.debug(TAG+" (assert (A 0) (weather \"" + weather.getCategory() + "\") (age "
					+ AgeUtils.ageMapper(person.getAge()) + ") (gender " + person.getGender() + ") (player "
					+ playerData.getPlayerId() + ")(temp " + weather.getTemperatureText() + ")  )");

			events.stream().filter(Objects::nonNull).forEach(event -> {
				
				String message = "";
				Float maxVal = 0.0f;

				factService.createFact(engine, person.getGender(), AgeUtils.ageMapper(person.getAge()), playerData,
						event.getEventName(), weather);

				long count = engine.run();
				if (count > 0) {

					message = String.valueOf(engine.eval("?*TriggerMsg*"));

					maxVal =  (Float) ((FloatValue) engine.eval("?*MaxVal*")).floatValue();

					Optional<Float> currentMessage = Optional.ofNullable(messageWithVal.get(message));

					if (currentMessage.isPresent()) {
						messageWithVal.put(message, currentMessage.get() > maxVal ? currentMessage.get() : maxVal);

					} else {
						messageWithVal.put(message, maxVal);
					}

				}
				message = "";
				maxVal = 0.0f;
				engine.clear();					
			});

			

		});
		return messageWithVal;
	}

}
