package com.nec.edgedisplay.zmqsub.subscriber;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nec.edgedisplay.exception.MessageNotFoundException;
import com.nec.edgedisplay.exception.PlayerNotFoundException;
import com.nec.edgedisplay.exception.TriggerPercentageException;
import com.nec.edgedisplay.services.IDemographicTriggerService;

/**
 * 
 * @author Rahul Sharma
 *
 */
@Service
public class ZMQSubscriber {

	private final Logger logger = LoggerFactory.getLogger(ZMQSubscriber.class);

	@Autowired
	private IDemographicTriggerService demographicTriggerService;

	@Value("${nec.event.trigger}")
	private String trigger;

	@Value("${neceva.socket.address}")
	private String necEvaAddress;

	@PostConstruct
	public void subscribe() {

		new Thread() {
			@Override
			public void run() {
				try (Context context = ZMQ.context(1); Socket subscriber = context.socket(ZMQ.SUB)) {
//					logger.info("Receiving demographic event from nec-eva");

					subscriber.connect(necEvaAddress);
					subscriber.subscribe(trigger.getBytes(StandardCharsets.UTF_8));

					while (!Thread.currentThread().isInterrupted()) {

						String topicId = (subscriber.recvStr());
						String topicContent = (subscriber.recvStr());

//						logger.info("topicId recieved from NEC EVA :{} ", topicId);

						switch (topicId) {
						case "trigger":
							try {
								demographicTriggerService.triggerToPlayer(topicContent);

							} catch (JsonParseException  e) {
								logger.error("There is problem in processing dempographic trigger:-{}", e.getMessage());
							}
							catch (InterruptedException e) {
								Thread.currentThread().interrupt();
								logger.error("There is problem in processing dempographic trigger:-{}", e.getMessage());
							}
							catch (JsonMappingException e) {

								logger.error("Unable map json to object {}", e.getMessage());
							} catch (IOException e) {

								logger.error("IO exception occured:: {}", e.getMessage());
							}  catch (MessageNotFoundException | TriggerPercentageException | PlayerNotFoundException e) {

								logger.error("Error:{}--{}",e.getMessage(), e.getId());
							} catch (Exception e) {

								logger.error("Unexpected error::{}", e.getMessage());
							}
							break;
						default:
							break;

						}

					}
				}
			}

		}.start();

	}
}
