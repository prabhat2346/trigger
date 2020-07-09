package com.nec.edgedisplay.modal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AIMessage {
	@JsonProperty("msgId")
private String msgId;
	@JsonProperty("playerId")
private String playerId;
public String getMsgId() {
	return msgId;
}
public void setMsgId(String msgId) {
	this.msgId = msgId;
}
public String getPlayerId() {
	return playerId;
}
public void setPlayerId(String playerId) {
	this.playerId = playerId;
}

}
