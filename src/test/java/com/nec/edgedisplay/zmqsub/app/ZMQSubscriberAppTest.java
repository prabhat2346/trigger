package com.nec.edgedisplay.zmqsub.app;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.nec.edgedisplay.model.OptionSchedule;
import com.nec.edgedisplay.model.RuleOption;

@RunWith(MockitoJUnitRunner.Silent.class)
public class ZMQSubscriberAppTest {

	@Before
	public void before() {
		System.out.println("Before");
	}

	@After
	public void after() {
		System.out.println("After");
	}

	@BeforeClass
	public static void beforeClass() {
		System.out.println("Before Class");
	}

	@AfterClass
	public static void afterClass() {
		System.out.println("After Class");
	}

	@Test
	public void demographicTriggerSaveTest() {

		RuleOption ruleOption = new RuleOption();
		ruleOption.setRuleOption("Scheduled");
		OptionSchedule options = new OptionSchedule();
		List<OptionSchedule> optionList = new ArrayList<>();
		optionList.add(options);
		ruleOption.setOptionSchedules(optionList);
//		DemographicTriggerServiceImpl.checkRuleOption(ruleOption);
	}
}
