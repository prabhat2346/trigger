package com.nec.edgedisplay.modal;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "cmsPlayerId", "cmsMessageId","cmsType" })
public class PlayerTriggerData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("cmsPlayerId")
	private String cmsPlayerId;
	@JsonProperty("cmsMessageId")
	private String cmsMessageId;
	
	@JsonProperty("cmsType")
	private String cmsType;


	@JsonProperty("cmsPlayerId")
	public String getCmsPlayerId() {
		return cmsPlayerId;
	}

	@JsonProperty("cmsPlayerId")
	public void setCmsPlayerId(String cmsPlayerId) {
		this.cmsPlayerId = cmsPlayerId;
	}

	@JsonProperty("cmsMessageId")
	public String getCmsMessageId() {
		return cmsMessageId;
	}

	@JsonProperty("cmsMessageId")
	public void setCmsMessageId(String cmsMessageId) {
		this.cmsMessageId = cmsMessageId;
	}

	@JsonProperty("cmsType")
	public String getCmsType() {
		return cmsType;
	}

	public void setCmsType(String cmsType) {
		this.cmsType = cmsType;
	}

	@Override
	public String toString() {
		return "PlayerTriggerData [cmsPlayerId=" + cmsPlayerId + ", cmsMessageId=" + cmsMessageId + ", cmsType="
				+ cmsType + "]";
	}
	
	

}
