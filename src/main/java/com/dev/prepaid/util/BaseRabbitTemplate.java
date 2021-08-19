package com.dev.prepaid.util;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseRabbitTemplate {

	@Autowired
	protected RabbitTemplate rabbitTemplate;
	
	protected void rabbitTemplateConvertAndSendWithPriority(String exchange, String routingKey, Object object, Integer priority){
//		log.debug("send message to exchange :{} ,topic : {}",exchange,routingKey);
		rabbitTemplate.convertAndSend(exchange, routingKey, object, new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message) throws AmqpException {
				message.getMessageProperties().setPriority(priority);
		        return message;
			}
		});
		
	}
}
