package com.nec.edgedisplay.services;

import java.util.List;
import java.util.Map;

import com.nec.edgedisplay.model.DemographicTrigger;
import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.Weather;

import net.sf.clipsrules.jni.Environment;

public interface ClipService {

	/**
	 * 
	 * @param demographic
	 * @param playerData
	 * @param events 
	 * @param weather 
	 * @return Map having message id and weightage of message  
	 */
	public Map<String, Float> getTriggringMsgId( DemographicTrigger demographic, PlayerIdList playerData,Weather weather, List<Event> events, Environment engine);
}
