package com.nec.edgedisplay.services;


import java.util.List;

import com.nec.edgedisplay.model.Event;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.Weather;

import net.sf.clipsrules.jni.Environment;
/**
 * 
 * @author Rahul Sharma
 *
 */
public interface FactService {

	void createFact(Environment engine,String person);
	void createFact(Environment engine,String person,String ageGroup, PlayerIdList playerData,String event,Weather weather);
	void createFact(Environment engine,String person,String ageGroup, PlayerIdList playerData,List<Event> events,Weather weather);

}
