package com.nec.edgedisplay.services.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import com.nec.edgedisplay.enums.RuleOptions;
import com.nec.edgedisplay.model.OptionSchedule;
import com.nec.edgedisplay.model.RuleOption;
import com.nec.edgedisplay.services.impl.RuleOverrideServiceImpl;
import com.nec.edgedisplay.utils.DateUtils;

@RunWith(MockitoJUnitRunner.Silent.class)
public class RuleOverrideServiceTest {

	@InjectMocks
	RuleOverrideServiceImpl ruleOverrideService;

	@Test
	public void checkRuleOptionNORESTRICT() {

		List<OptionSchedule> optionSchedules = new ArrayList<>();
		OptionSchedule optionSchedule = new OptionSchedule();
		optionSchedule.setId("2");
		optionSchedule.setEndTimeHour("02");
		optionSchedule.setStartTimeHour("01");
		optionSchedule.setStartTimeMin("20");
		optionSchedule.setEndTimeMin("33");
		List<String> days = new ArrayList<>();
		days.add("MON");
		optionSchedule.setDays(days);
		RuleOption ruleOption = new RuleOption();
		ruleOption.setId(1);
		ruleOption.setRuleOption(RuleOptions.NORESTRICT.getRuleOption());
		ruleOption.setOptionSchedules(optionSchedules);
		boolean value = ruleOverrideService.checkRuleOption(ruleOption);
		assertEquals(true, value);

	}

	@Test
	public void checkRuleOptionRESTRICT() {

		List<OptionSchedule> optionSchedules = new ArrayList<>();
		OptionSchedule optionSchedule = new OptionSchedule();
		optionSchedule.setId("2");
		optionSchedule.setEndTimeHour("02");
		optionSchedule.setStartTimeHour("01");
		optionSchedule.setStartTimeMin("20");
		optionSchedule.setEndTimeMin("33");
		List<String> days = new ArrayList<>();
		days.add("MON");
		optionSchedule.setDays(days);
		RuleOption ruleOption = new RuleOption();
		ruleOption.setId(1);
		ruleOption.setRuleOption(RuleOptions.RESTRICT.getRuleOption());
		ruleOption.setOptionSchedules(optionSchedules);
		boolean value = ruleOverrideService.checkRuleOption(ruleOption);
		assertEquals(false, value);

	}

	@Test
	public void checkRuleOptionSCHEDULED() {

		List<OptionSchedule> optionSchedules = new ArrayList<>();
		OptionSchedule optionSchedule = new OptionSchedule();
		optionSchedule.setId("2");
		optionSchedule.setEndTimeHour("02");
		optionSchedule.setStartTimeHour("01");
		optionSchedule.setStartTimeMin("20");
		optionSchedule.setEndTimeMin("33");
		List<String> days = new ArrayList<>();
		days.add(DateUtils.getCurrentDay());
		optionSchedule.setDays(days);
		RuleOption ruleOption = new RuleOption();
		ruleOption.setId(1);
		ruleOption.setRuleOption(RuleOptions.SCHEDULED.getRuleOption());
		ruleOption.setOptionSchedules(optionSchedules);
		boolean value = ruleOverrideService.checkRuleOption(ruleOption);
		assertEquals(false, value);

	}

	@Test
	public void checkRuleOptionSCHEDULED2() {

		List<OptionSchedule> optionSchedules = new ArrayList<>();
		OptionSchedule optionSchedule = new OptionSchedule();
		optionSchedule.setId("2");
		optionSchedule.setEndTimeHour("02");
		optionSchedule.setStartTimeHour("01");
		optionSchedule.setStartTimeMin("20");
		optionSchedule.setEndTimeMin("33");
		List<String> days = new ArrayList<>();
		days.add(DateUtils.getCurrentDay());
		optionSchedule.setDays(days);
		optionSchedules.add(optionSchedule);
		RuleOption ruleOption = new RuleOption();
		ruleOption.setId(1);
		ruleOption.setRuleOption(RuleOptions.SCHEDULED.getRuleOption());
		ruleOption.setOptionSchedules(optionSchedules);
		boolean value = ruleOverrideService.checkRuleOption(ruleOption);
		assertEquals(false, value);

	}

	@Test
	public void checkRuleOptionSCHEDULED3() {

		List<OptionSchedule> optionSchedules = new ArrayList<>();
		OptionSchedule optionSchedule = new OptionSchedule();
		Calendar cal = Calendar.getInstance();
		optionSchedule.setId("2");
		optionSchedule.setStartTimeHour(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
		optionSchedule.setStartTimeMin(String.valueOf(cal.get(Calendar.MINUTE)));
		cal.add(Calendar.HOUR_OF_DAY, 3);
		cal.add(Calendar.MINUTE, 10);
		optionSchedule.setEndTimeHour(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)));
		optionSchedule.setEndTimeMin(String.valueOf(cal.get(Calendar.MINUTE)));
		List<String> days = new ArrayList<>();
		days.add(DateUtils.getCurrentDay());
		optionSchedule.setDays(days);
		optionSchedules.add(optionSchedule);
		RuleOption ruleOption = new RuleOption();
		ruleOption.setId(1);
		ruleOption.setRuleOption(RuleOptions.SCHEDULED.getRuleOption());
		ruleOption.setOptionSchedules(optionSchedules);
		boolean value = ruleOverrideService.checkRuleOption(ruleOption);
		assertEquals(true, value);

	}

	@Test
	public void checkRuleOption() {
		boolean value = ruleOverrideService.checkRuleOption("1235");
		assertEquals(false, value);

	}

}
