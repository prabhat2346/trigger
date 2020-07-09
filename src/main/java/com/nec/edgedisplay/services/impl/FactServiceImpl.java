package com.nec.edgedisplay.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.Weather;
import com.nec.edgedisplay.services.FactService;
import com.nec.edgedisplay.utils.Constants;

import net.sf.clipsrules.jni.Environment;

/**
 * 
 * @author Rahul Sharma
 *
 */
@Service
public class FactServiceImpl implements FactService {
	private final Logger logger = LoggerFactory.getLogger(FactServiceImpl.class);

	@Value("${rule.fileName}")
	private String fileName;
	
	private String gender=") (gender "; 
	private String player=") (player ";
	private String temp=")(temp ";
	private String age="\") (age ";

	@Override
	public void createFact(Environment engine, String person) {

		engine.load("function2.clp");

		engine.eval("(deffacts startup (A 0) (weather sunny) (age 25_40) (gender " + person + ") )");

		engine.reset();

	}

	@Override
	public void createFact(final Environment engine, final String person, final String age_group,
			final PlayerIdList playerData, final String event, final Weather weather) {
		final String TAG=FactServiceImpl.class.getSimpleName()+"##createFact()";

		try {
			engine.clear();
			engine.load(fileName);
		    engine.eval("(assert (A 0) (weather \"" + weather.getCategory() + age + age_group + gender
					+ person + player + playerData.getPlayerId() + temp + weather.getTemperatureText()
					+ ") (event \"" + event + "\")  )");
		} catch (Exception e) {
			logger.error("{} No message found for giving fact {}",TAG,e.getMessage());
			

		}
	}

	@Override
	public void createFact(final Environment engine, final String person, final String age_group,
			final PlayerIdList playerData, final List<Event> events, final Weather weather) {
		final String TAG=FactServiceImpl.class.getSimpleName()+"##createFact()";

		try {
			String event = createAssertEvents(events);
			engine.clear();
			engine.load(fileName);
			engine.eval("(assert (A 0) (weather \"" + weather.getCategory() + "\") (age " + age_group + ") (gender "
					+ person + ") (player " + playerData.getPlayerId() + ")(temp " + weather.getTemperatureText()
					+ ")  \"" + event + "\" )");
			
			String data=" (assert (A 0) (weather \"" + weather.getCategory() + age + age_group + gender
					+ person + player + playerData.getPlayerId() + temp + weather.getTemperatureText()
					+ ")  \"" + event + "\"  )";

			logger.info(TAG,data);
			logger.info("{} Current weather is {} & temperature {} ",TAG , weather.getCategory() , weather.getTemperatureText());
		} catch (Exception e) {
			logger.error("{} Error while getting message:: No message found {}",TAG,e.getMessage());
		}

	}

	private String createAssertEvents(List<Event> events) {
		String eventStr = "";
		for (Event event : events) {
			eventStr = eventStr + Constants.ASSERT_EVENT_STRING + Constants.SPACE + event.getEventName()
					+ Constants.CLOSE_STRING + Constants.SPACE;
		}
		return eventStr;
	}

}
