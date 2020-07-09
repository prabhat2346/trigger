package com.nec.edgedisplay.services;

import com.nec.edgedisplay.exception.MessageNotFoundException;
import com.nec.edgedisplay.exception.PlayerNotFoundException;
import com.nec.edgedisplay.exception.TriggerPercentageException;
import com.nec.edgedisplay.model.MessageList;
import com.nec.edgedisplay.model.PlayerIdList;

public interface TriggerService {

	public void triggerToPlayer(PlayerIdList playerData, MessageList msg)
			throws MessageNotFoundException, PlayerNotFoundException,Exception;

	public void triggerToPlayer(float matchPercentage, PlayerIdList playerData, String finalResult)
			throws Exception, PlayerNotFoundException, MessageNotFoundException,TriggerPercentageException;

	public void triggerToPlayer(PlayerIdList playerData, String messageId)
			throws MessageNotFoundException, PlayerNotFoundException,Exception;

}
