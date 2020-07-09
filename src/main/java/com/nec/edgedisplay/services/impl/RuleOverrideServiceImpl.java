package com.nec.edgedisplay.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nec.edgedisplay.enums.RuleOptions;
import com.nec.edgedisplay.model.OptionSchedule;
import com.nec.edgedisplay.model.RuleOption;
import com.nec.edgedisplay.services.RuleOverrideService;
import com.nec.edgedisplay.utils.DateUtils;

@Service
public class RuleOverrideServiceImpl implements RuleOverrideService {

	@Override
	public boolean checkRuleOption(final RuleOption ruleOption) {
		
		if(ruleOption==null) {
			return true;
		}
		if (ruleOption.getRuleOption().equals(RuleOptions.NORESTRICT.getRuleOption())) {
			return true;
		} else if (ruleOption.getRuleOption().equals(RuleOptions.RESTRICT.getRuleOption())) {
			return false;
		}
		List<OptionSchedule> optionSchedules = ruleOption.getOptionSchedules();

		for (OptionSchedule options : optionSchedules) {
			if (options.getDays().contains(DateUtils.getCurrentDay()) && DateUtils.checkCurrentTime(
					Integer.parseInt(options.getStartTimeHour()), Integer.parseInt(options.getStartTimeMin()),
					Integer.parseInt(options.getEndTimeHour()), Integer.parseInt(options.getEndTimeMin()))) {
				return true;
			}

		}
		return false;
	}

	@Override
	public boolean checkRuleOption(String playerId) {
		return false;
	}

}
