package com.nec.edgedisplay.enums;

public enum RuleOptions {

	SCHEDULED("Scheduled"), RESTRICT("No Trigger"), NORESTRICT("No Restriction");

	private String ruleOption;

	private RuleOptions(final String ruleOption) {
		this.ruleOption = ruleOption;
	}

	public String getRuleOption() {
		return this.ruleOption;
	}

}
