package com.nec.edgedisplay.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import com.nec.edgedisplay.common.redis.services.BrightSignRedisService;
import com.nec.edgedisplay.common.redis.services.MasterPlayerListRedisService;
import com.nec.edgedisplay.common.redis.services.SignageLiveRedisService;
import com.nec.edgedisplay.common.redis.services.StratosMediaRedisService;
import com.nec.edgedisplay.enums.CmsPlayer;
import com.nec.edgedisplay.exception.MessageNotFoundException;
import com.nec.edgedisplay.exception.PlayerNotFoundException;
import com.nec.edgedisplay.exception.TriggerPercentageException;
import com.nec.edgedisplay.httpservices.BrightSignService;
import com.nec.edgedisplay.httpservices.MasterPlayerDataService;
import com.nec.edgedisplay.httpservices.NavoriService;
import com.nec.edgedisplay.httpservices.SignageLiveService;
import com.nec.edgedisplay.httpservices.StratosMedia;
import com.nec.edgedisplay.model.BrightSignPlayerMessage;
import com.nec.edgedisplay.model.MessageDistributionList;
import com.nec.edgedisplay.model.MessageList;
import com.nec.edgedisplay.model.PlayerIdList;
import com.nec.edgedisplay.model.SignageLivePlayerMessages;
import com.nec.edgedisplay.model.StratosMediaPlayerMessage;
import com.nec.edgedisplay.model.configuration.StratosConfiguration;
import com.nec.edgedisplay.network.StratosMediaMulticast;
import com.nec.edgedisplay.services.RuleOverrideService;
import com.nec.edgedisplay.services.TriggerService;
import com.nec.edgedisplay.utils.Constants;

@Service
public class TriggerServiceImpl implements TriggerService {

	@Value("${message.trigger.percentage}")
	private float messageTriggerPercentage;

	@Value("${tenant.id}")
	private String tenantId;

	@Value("${loadHttpAtApiforStratos}")
	private boolean loadHttpAtApiforStratos;
	private final Logger logger = LoggerFactory.getLogger(TriggerServiceImpl.class);

	@Autowired
	private BrightSignRedisService brightSignRedisService;
	@Autowired
	private StratosMedia stratosMedia;
	@Autowired
	private BrightSignService brightSignService;
	@Autowired
	private NavoriService navoriService;
	@Autowired
	private StratosMediaMulticast stratosMediaMulticast;
	@Autowired
	private MasterPlayerListRedisService masterPlayerListRedisService;
	@Autowired
	private StratosMediaRedisService stratosMediaRedisService;
	@Autowired
	private RuleOverrideService ruleOverrideService;
	@Autowired
	private SignageLiveService signageLiveService;
	@Autowired
	private SignageLiveRedisService signageLiveRedisService;
	@Autowired
	private TaskExecutor executor;
	@Autowired
	private MasterPlayerDataService masterPlayerDataService;

	private String messageConstent = " Message::";

	@Override
	public void triggerToPlayer(final float matchPercentage, final PlayerIdList playerData, final String finalResult)
			throws Exception {

		final String TAG = TriggerServiceImpl.class.getSimpleName() + "##triggerToPlayer()";
		if (matchPercentage * 100 >= messageTriggerPercentage) {

			logger.info("Inside triggerToPlayer");

			MessageDistributionList messageDistributionData = masterPlayerListRedisService
					.findDistributionData(playerData.getPlayerId());
			if (messageDistributionData != null) {

				Optional<MessageList> msg = messageDistributionData.getMessageList().stream()
						.filter(msgList -> msgList.getCmsmsgId().trim().equals(finalResult.trim())).findFirst();

				logger.debug("{} Camera id::{} has player : {}", TAG, playerData.getCameraId(),
						playerData.getPlayerId());

				if (msg.isPresent() && ruleOverrideService
						.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())) {
					triggerToPlayer(playerData, msg.get());

				} else {
					logger.info("{} Either Message not available or rule override did not match::{} :for player {}", TAG,
							messageDistributionData.getPlayerInfo().getRuleOption().getRuleOption(),
							playerData.getPlayerId());
				}

			} else {
				throw new PlayerNotFoundException(TAG + " Player or message  not found on camera: ",
						playerData.getCameraId());

			}

		} else {
			throw new TriggerPercentageException(
					TAG + " Matching percentage of message is less than rule trigger percentage");

		}

	}

	@Override
	public void triggerToPlayer(PlayerIdList playerData, MessageList msg) throws Exception {
		final String TAG = TriggerServiceImpl.class.getSimpleName() + "##triggerToPlayer()";
		String cmsMessageId = msg.getCmsmsgId().trim();
		if (playerData.getCmsPlayerType().equalsIgnoreCase(CmsPlayer.STRATOS.getPlayer())) {

			List<StratosMediaPlayerMessage> messages = stratosMediaRedisService
					.findMessagesList(playerData.getPlayerId());

			boolean isMessageAvailable = messages != null? messages.stream().anyMatch(playerMessage -> playerMessage.getScenes().contains(cmsMessageId)): false;
			if (isMessageAvailable) {

				StratosConfiguration stratosConfig=Constants.stratosConfigMap.get(Constants.STRATOS_KEY);	
				if(stratosConfig!=null) {
					CompletableFuture.runAsync(() -> stratosMedia.triggerMessage(cmsMessageId, playerData,stratosConfig)
							, executor);
				}
				


			} else {
				throw new MessageNotFoundException(TAG + messageConstent + cmsMessageId,
						"is not matching with player: " + playerData.getPlayerId() + " on Stratos ");
			}

		} else if (playerData.getCmsPlayerType().equalsIgnoreCase(CmsPlayer.BRIGHTSIGN.getPlayer())) {

			List<BrightSignPlayerMessage> messages = brightSignRedisService.findMessagesList(playerData.getPlayerId());

			boolean isMessageAvailable = messages != null? messages.stream().anyMatch(playerMsg -> playerMsg.getMessageId().equals(cmsMessageId)): false;

			if (isMessageAvailable) {

				CompletableFuture.runAsync(
						() -> brightSignService.triggerMessage(cmsMessageId, playerData.getUrl(), playerData),
						executor);

			} else {
				throw new MessageNotFoundException(TAG + messageConstent + cmsMessageId,
						"is not matching with player:: " + playerData.getPlayerId() + " on Brightsign");
			}

		} else if (playerData.getCmsPlayerType().equalsIgnoreCase(CmsPlayer.SIGNAGELIVE.getPlayer())) {

			List<SignageLivePlayerMessages> messages = signageLiveRedisService.find(playerData.getPlayerId());
			boolean isMessageAvailable = messages != null? messages.stream().anyMatch(playerMsg -> playerMsg.getMessageId().equals(cmsMessageId)): false;
			if (isMessageAvailable) {

				CompletableFuture.runAsync(() -> signageLiveService.triggerToPlayer(playerData.getPlayerId(),
						cmsMessageId, playerData.getUrl()), executor);

			} else {
				throw new MessageNotFoundException(TAG + messageConstent + cmsMessageId,
						" is not matching with player:: " + playerData.getPlayerId() + " on SignageLive");
			}

		} else if (playerData.getCmsPlayerType().equalsIgnoreCase(CmsPlayer.NAVORI.getPlayer())) {

			CompletableFuture.runAsync(() -> navoriService.triggerMessage(msg.getMsgName(),
					Long.parseLong(cmsMessageId), Constants.MEDIA, msg.getDuration() * 1000, false, 1, playerData),
					executor);

		}

	}

	@Override
	public void triggerToPlayer(PlayerIdList playerData, String messageId) throws Exception {
		final String TAG = TriggerServiceImpl.class.getSimpleName() + "##triggerToPlayer()";

		MessageDistributionList messageDistributionData = masterPlayerListRedisService
				.findDistributionData(playerData.getPlayerId());
		if (messageDistributionData != null) {

			Optional<MessageList> msg = messageDistributionData.getMessageList().stream()
					.filter(msgList -> msgList.getMessageDistId().equals(messageId)).findFirst();

			logger.debug("{} Camera id::{} has player : {}", TAG, playerData.getCameraId(), playerData.getPlayerId());

			if (msg.isPresent()
					&& ruleOverrideService.checkRuleOption(messageDistributionData.getPlayerInfo().getRuleOption())) {
				triggerToPlayer(playerData, msg.get());

			} else {
				logger.info("{} Message not available on : {}", TAG, playerData.getPlayerId());
			}

		} else {
			throw new PlayerNotFoundException(TAG + " Player or message  not found : ", playerData.getCameraId());

		}

	}
}
