package com.nec.edgedisplay.services.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import com.nec.edgedisplay.common.redis.services.BrightSignRedisService;
import com.nec.edgedisplay.common.redis.services.MasterPlayerListRedisService;
import com.nec.edgedisplay.common.redis.services.SignageLiveRedisService;
import com.nec.edgedisplay.common.redis.services.StratosMediaRedisService;
import com.nec.edgedisplay.enums.CmsPlayer;
import com.nec.edgedisplay.exception.MessageNotFoundException;
import com.nec.edgedisplay.exception.PlayerNotFoundException;
import com.nec.edgedisplay.exception.TriggerPercentageException;
import com.nec.edgedisplay.httpservices.BrightSignService;
import com.nec.edgedisplay.httpservices.NavoriService;
import com.nec.edgedisplay.httpservices.SignageLiveService;
import com.nec.edgedisplay.httpservices.StratosMedia;
import com.nec.edgedisplay.model.BrightSignPlayerMessage;
import com.nec.edgedisplay.model.MessageDistributionList;
import com.nec.edgedisplay.model.MessageList;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.RuleOption;
import com.nec.edgedisplay.model.SignageLivePlayerMessages;
import com.nec.edgedisplay.model.StratosMediaPlayerMessage;
import com.nec.edgedisplay.model.store.Player;
import com.nec.edgedisplay.network.StratosMediaMulticast;
import com.nec.edgedisplay.services.RuleOverrideService;
import com.nec.edgedisplay.services.impl.TriggerServiceImpl;
import com.nec.edgedisplay.utils.Constants;


@RunWith(MockitoJUnitRunner.Silent.class)
public class TriggerServiceTest {

	@InjectMocks
	TriggerServiceImpl triggerService;

	@Mock
	BrightSignRedisService brightSignRedisService;

	@Mock
	StratosMedia stratosMedia;

	@Mock
	BrightSignService brightSignService;

	@Mock
	NavoriService navoriService;

	@Mock
	StratosMediaMulticast stratosMediaMulticast;

	@Mock
	MasterPlayerListRedisService masterPlayerListRedisService;

	@Mock
	StratosMediaRedisService stratosMediaRedisService;

	@Mock
	RuleOverrideService ruleOverrideService;

	@Mock
	SignageLiveService signageLiveService;

	@Mock
	SignageLiveRedisService signageLiveRedisService;
	
	@Mock
	private TaskExecutor executor;
	
	
	@Before
	public void init() {
		ReflectionTestUtils.setField(triggerService, "messageTriggerPercentage", 80);

	}
	@Test
	public void triggerToPlayerMessageNotAvailable() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
		triggerService.triggerToPlayer(.85f, playerData, "20002");
		
		
	}
	
	@Test(expected = PlayerNotFoundException.class)
	public void triggerToPlayerMessageNull() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(null);
		triggerService.triggerToPlayer(.85f, playerData, "20002");
		
		
	}
	
	@Test(expected = TriggerPercentageException.class)
	public void triggerToPlayerTriggerPercentage() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(null);
		triggerService.triggerToPlayer(.70f, playerData, "20002");
		
		
	}
	
	@Test
	public void triggerToPlayerMessageAvailable() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		MessageList message=new MessageList();
		message.setMsgId("1234");
		message.setCmsmsgId("1234");
		List<MessageList> messageList=new ArrayList<MessageList>();
		messageList.add(message);
		messageDistributionData.setMessageList(messageList);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
        Mockito.when( ruleOverrideService.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())).thenReturn(false);
		triggerService.triggerToPlayer(.85f, playerData, "1234");
		
		
	}
	
	
	@Test
	public void triggerToPlayerMessageNotAvailable2() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
		triggerService.triggerToPlayer(playerData, "20002");
		
		
	}
	
	@Test(expected = PlayerNotFoundException.class)
	public void triggerToPlayerMessageNull2() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(null);
		triggerService.triggerToPlayer( playerData, "20002");
		
		
	}
	
	@Test(expected = TriggerPercentageException.class)
	public void triggerToPlayerTriggerPercentage2() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(null);
		triggerService.triggerToPlayer(.70f, playerData, "20002");
		
		
	}
	
	@Test
	public void triggerToPlayerMessageAvailable2() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		MessageList message=new MessageList();
		message.setMsgId("1234");
		message.setCmsmsgId("1234");
		List<MessageList> messageList=new ArrayList<MessageList>();
		messageList.add(message);
		messageDistributionData.setMessageList(messageList);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
        Mockito.when( ruleOverrideService.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())).thenReturn(false);
		triggerService.triggerToPlayer(.85f, playerData, "1234");
		
		
	}
	
	@Test(expected = MessageNotFoundException.class)
	public void triggerToPlayerMessageAvailableTrigger() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		playerData.setCmsPlayerType(CmsPlayer.STRATOS.getPlayer());
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		MessageList message=new MessageList();
		message.setMsgId("1234");
		message.setCmsmsgId("1234");
		message.setMessageDistId("1234");
		List<MessageList> messageList=new ArrayList<MessageList>();
		messageList.add(message);
		messageDistributionData.setMessageList(messageList);
		List<StratosMediaPlayerMessage> stratosMessages = new ArrayList<>();
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
        Mockito.when( ruleOverrideService.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())).thenReturn(true);
        Mockito.when(stratosMediaRedisService.findMessagesList(playerData.getPlayerId())).thenReturn(stratosMessages);
		triggerService.triggerToPlayer(.85f,playerData, "1234");
		
		
	}
	
	@Test()
	public void triggerToPlayerStratos() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setCmsPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		playerData.setCmsPlayerType(CmsPlayer.STRATOS.getPlayer());
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		MessageList message=new MessageList();
		message.setMsgId("1234");
		message.setCmsmsgId("1234");
		message.setMessageDistId("1234");
		List<MessageList> messageList=new ArrayList<MessageList>();
		messageList.add(message);
		messageDistributionData.setMessageList(messageList);
		List<StratosMediaPlayerMessage> stratosMessages = new ArrayList<>();
		StratosMediaPlayerMessage stratosMsg=new StratosMediaPlayerMessage();
		List<String> scene=new ArrayList<>();
		scene.add("1234");
		stratosMsg.setScenes(scene);
		stratosMessages.add(stratosMsg);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
        Mockito.when( ruleOverrideService.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())).thenReturn(true);
        Mockito.when(stratosMediaRedisService.findMessagesList(playerData.getPlayerId())).thenReturn(stratosMessages);
        Mockito.doNothing().when(stratosMediaMulticast).triggerMessage("1234", playerData.getCmsPlayerId(),
				playerData.getIpAddress(), playerData.getPort());
		triggerService.triggerToPlayer(.85f,playerData, "1234");
		
		
	}
	
	@Test(expected = MessageNotFoundException.class)
	public void triggerToPlayerMessageNotFoundSignageLive() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setCmsPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		playerData.setCmsPlayerType(CmsPlayer.SIGNAGELIVE.getPlayer());
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		MessageList message=new MessageList();
		message.setMsgId("1234");
		message.setCmsmsgId("1234");
		message.setMessageDistId("1234");
		List<MessageList> messageList=new ArrayList<MessageList>();
		messageList.add(message);
		messageDistributionData.setMessageList(messageList);
		List<SignageLivePlayerMessages> signageLiveMsgList = new ArrayList<>();
		SignageLivePlayerMessages SLMsg=new SignageLivePlayerMessages();
		
		SLMsg.setMessageId("1267");
		signageLiveMsgList.add(SLMsg);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
        Mockito.when( ruleOverrideService.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())).thenReturn(true);
        Mockito.when(signageLiveRedisService.find(playerData.getPlayerId())).thenReturn(signageLiveMsgList);
		triggerService.triggerToPlayer(.85f,playerData, "1234");
		
		
	}
	
	@Test()
	public void triggerToPlayerSignageLive() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setCmsPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		playerData.setCmsPlayerType(CmsPlayer.SIGNAGELIVE.getPlayer());
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		MessageList message=new MessageList();
		message.setMsgId("1234");
		message.setCmsmsgId("1234");
		message.setMessageDistId("1234");
		List<MessageList> messageList=new ArrayList<MessageList>();
		messageList.add(message);
		messageDistributionData.setMessageList(messageList);
		List<SignageLivePlayerMessages> signageLiveMsgList = new ArrayList<>();
		SignageLivePlayerMessages SLMsg=new SignageLivePlayerMessages();
		
		SLMsg.setMessageId("1234");
		signageLiveMsgList.add(SLMsg);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
        Mockito.when( ruleOverrideService.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())).thenReturn(true);
        Mockito.when(signageLiveRedisService.find(playerData.getPlayerId())).thenReturn(signageLiveMsgList);
        Mockito.when(signageLiveService.triggerToPlayer(playerData.getPlayerId(), "1234", playerData.getUrl())).thenReturn(true);
		triggerService.triggerToPlayer(.85f,playerData, "1234");
		
		
	}

	
	@Test(expected = MessageNotFoundException.class)
	public void triggerToPlayerMessageNotFoundBrightsign() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setCmsPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		playerData.setCmsPlayerType(CmsPlayer.BRIGHTSIGN.getPlayer());
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		MessageList message=new MessageList();
		message.setMsgId("1234");
		message.setCmsmsgId("1234");
		message.setMessageDistId("1234");
		List<MessageList> messageList=new ArrayList<MessageList>();
		messageList.add(message);
		messageDistributionData.setMessageList(messageList);
		List<BrightSignPlayerMessage> brightsignMsgList = new ArrayList<>();
		BrightSignPlayerMessage brightsignMsg=new BrightSignPlayerMessage();
		
		brightsignMsg.setMessageId("1267");
		brightsignMsgList.add(brightsignMsg);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
        Mockito.when( ruleOverrideService.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())).thenReturn(true);
        Mockito.when(brightSignRedisService.findMessagesList(playerData.getPlayerId())).thenReturn(brightsignMsgList);
		triggerService.triggerToPlayer(.85f,playerData, "1234");
		
		
	}
	
	@Test()
	public void triggerToPlayerBrightsign() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setCmsPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		playerData.setCmsPlayerType(CmsPlayer.BRIGHTSIGN.getPlayer());
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		MessageList message=new MessageList();
		message.setMsgId("1234");
		message.setCmsmsgId("1234");
		message.setMessageDistId("1234");
		List<MessageList> messageList=new ArrayList<MessageList>();
		messageList.add(message);
		messageDistributionData.setMessageList(messageList);
		List<BrightSignPlayerMessage> brightsignMsgList = new ArrayList<>();
		BrightSignPlayerMessage brightsignMsg=new BrightSignPlayerMessage();
		
		brightsignMsg.setMessageId("1234");
		brightsignMsgList.add(brightsignMsg);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
        Mockito.when( ruleOverrideService.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())).thenReturn(true);
        Mockito.when(brightSignRedisService.findMessagesList(playerData.getPlayerId())).thenReturn(brightsignMsgList);
		triggerService.triggerToPlayer(.85f,playerData, "1234");
			
	}

	
	@Test()
	public void triggerToPlayerNavori() throws PlayerNotFoundException, MessageNotFoundException, TriggerPercentageException, Exception {
		
		PlayerIdList playerData=new PlayerIdList();
		playerData.setCameraId("112222.222");
		playerData.setPlayerId("30532");
		playerData.setCmsPlayerId("30532");
		playerData.setIpAddress("172.65.30.195");
		playerData.setUrl("http://172.65.30.195");
		playerData.setPort(6200);
		playerData.setCmsPlayerType(CmsPlayer.NAVORI.getPlayer());
		MessageDistributionList messageDistributionData=new MessageDistributionList();
		Player playerInfo=new Player();
		RuleOption ruleOption=new RuleOption();
		playerInfo.setRuleOption(ruleOption);
		messageDistributionData.setPlayerInfo(playerInfo);
		MessageList message=new MessageList();
		message.setMsgId("1234");
		message.setCmsmsgId("1234");
		message.setMessageDistId("1234");
		List<MessageList> messageList=new ArrayList<MessageList>();
		messageList.add(message);
		messageDistributionData.setMessageList(messageList);
		Mockito.when(masterPlayerListRedisService.findDistributionData(Mockito.anyString())).thenReturn(messageDistributionData);
        Mockito.when( ruleOverrideService.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())).thenReturn(true);
		Mockito.when(navoriService.triggerMessage("messageName", Long.parseLong("1234"), Constants.MEDIA,
						10000L, false, 1, playerData)).thenReturn(true);
        triggerService.triggerToPlayer(.85f,playerData, "1234");
		
		
	}
	
}
