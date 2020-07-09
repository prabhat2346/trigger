package com.nec.edgedisplay.services;

import com.nec.edgedisplay.model.RuleOption;

public interface RuleOverrideService {
	
	public boolean checkRuleOption(final RuleOption ruleOption);
	public boolean checkRuleOption(final String playerId);

}
