package com.nec.edgedisplay.services;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nec.edgedisplay.modal.AIMessage;
import com.nec.edgedisplay.model.DemographicTrigger;
import com.nec.edgedisplay.model.PlayerIdList;

public interface AIService {
public AIMessage getTriggringMsgId(final DemographicTrigger demographic, final PlayerIdList playerData) throws JsonParseException, JsonMappingException, IOException;


}
