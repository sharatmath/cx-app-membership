package com.dev.prepaid.config;
import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dev.prepaid.constant.Constant;


@Configuration
public class AmqpConfig {
	@Value("${offer.monitoring.queue.prefetch:1}")
	private Integer offerMonitoringQueuePrefetch;
	@Value("${offer.monitoring.queue.concurrent:1}")
	private Integer offerMonitoringQueueConcurrent;

	@Value("${offer.event.condition.queue.prefetch:1}")
	private Integer offerEventConditionQueuePrefetch;
	@Value("${offer.event.condition.queue.concurrent:1}")
	private Integer offerEventConditionQueueConcurrent;

	@Value("${offer.eligibility.queue.prefetch:1}")
	private Integer offerEligibilityQueuePrefetch;
	@Value("${offer.monitoring.queue.concurrent:1}")
	private Integer offerEligibilityQueueConcurrent;
	
	@Value("${redemption.queue.prefetch:1}")
	private Integer redemptionQueuePrefetch;
	@Value("${redemption.queue.concurrent:1}")
	private Integer redemptionQueueConcurrent;

	@Value("${singtel.prepaid.responsys.custom.event.queue.prefetch:1}")
	private Integer singtelPrepaidResponsysCustomEventQueuePrefetch;
	@Value("${singtel.prepaid.responsys.custom.event.queue.concurrent:1}")
	private Integer singtelPrepaidResponsysCustomEventConcurrent;


	@Bean
	public Queue queueSingtelResponsysCustomEvent() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		Queue queue = new Queue(Constant.QUEUE_NAME_SINGTEL_RESPONSYS_CUSTOM_EVENT, true, false, false, args);
		return queue;
	}
	//binding
	@Bean
	public Binding bindingSingtelResponsysCustomEvent(Queue queueSingtelResponsysCustomEvent, TopicExchange exchangeMembership) {
		return BindingBuilder.bind(queueSingtelResponsysCustomEvent).to(exchangeMembership)
				.with(Constant.QUEUE_NAME_SINGTEL_RESPONSYS_CUSTOM_EVENT); //route.key.name=queue.name
	}


	//REDEMPTION
	// ============================================================================= //
	//queue
	@Bean
	public Queue queueSingtelRedemption() {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("x-max-priority", 1);
			Queue queue = new Queue(Constant.QUEUE_NAME_SINGTEL_REDEMPTION, true, false, false, args);
			return queue;
		}
	//binding
	@Bean
	public Binding bindingSingtelRedemptionAll(Queue queueSingtelRedemption, TopicExchange exchangeSingtel) {
		return BindingBuilder.bind(queueSingtelRedemption).to(exchangeSingtel)
				.with(Constant.QUEUE_NAME_SINGTEL_REDEMPTION); //route.key.name=queue.name
	}
	//container factory
	@Bean
	public RabbitListenerContainerFactory<SimpleMessageListenerContainer>
	rabbitListenerContainerFactoryForRedemption(
			ConnectionFactory rabbitConnectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(rabbitConnectionFactory);
		factory.setPrefetchCount(redemptionQueuePrefetch);
		factory.setConcurrentConsumers(redemptionQueueConcurrent);
		factory.setMessageConverter(jackson2MessageConverter());
		return factory;
	}
	//redemption DLQ
	@Bean
	public Queue dlqSingtelRedemption() {
		return new Queue(Constant.QUEUE_NAME_DLQ_SINGTEL_REDEMPTION);
	}

	//============================================================================= //
	// offerMonitoring
	@Bean
	public Queue queueOfferMonitoring() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		Queue queue = new Queue(Constant.QUEUE_NAME_MEMBERSHIP_MONITORING, true, false, false, args);
		return queue;
	}

	@Bean
	public Queue dlqOfferMonitoring() {
		return new Queue(Constant.QUEUE_NAME_DLQ_MEMBERSHIP_MONITORING);
	}

	@Bean
	public Binding bindingOfferMonitoring(Queue queueOfferMonitoring, TopicExchange exchangeMembership) {
		return BindingBuilder.bind(queueOfferMonitoring).to(exchangeMembership)
				.with(Constant.QUEUE_NAME_MEMBERSHIP_MONITORING); //route.key.name=queue.name
	}

	// eventCondition
	@Bean
	public Queue queueOfferEventCondition() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		Queue queue = new Queue(Constant.QUEUE_NAME_MEMBERSHIP_EVENT_CONDITION, true, false, false, args);
		return queue;
	}

	@Bean
	public Binding bindingOfferEventCondition(Queue queueOfferEventCondition, TopicExchange exchangeMembership) {
		return BindingBuilder.bind(queueOfferEventCondition).to(exchangeMembership)
				.with(Constant.QUEUE_NAME_MEMBERSHIP_EVENT_CONDITION); //route.key.name=queue.name
	}
	//============================================================================= //
	// Subscriber
	@Bean
	public Queue queueMembershipOfferSubscriber() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		Queue queue = new Queue(Constant.QUEUE_NAME_MEMBERSHIP_SUBSCRIBER, true, false, false, args);
		return queue;
	}

	@Bean
	public Binding bindingMembershipOfferSubscriber(Queue queueMembershipOfferSubscriber, TopicExchange exchangeMembership) {
		return BindingBuilder.bind(queueMembershipOfferSubscriber).to(exchangeMembership)
				.with(Constant.QUEUE_NAME_MEMBERSHIP_SUBSCRIBER); //route.key.name=queue.name
	}
	//============================================================================= //
	//Eligibility
	@Bean
	public Queue queueMembershipOfferEligibility() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		Queue queue = new Queue(Constant.QUEUE_NAME_MEMBERSHIP_ELIGIBILITY, true, false, false, args);
		return queue;
	}

	@Bean
	public Binding bindingMembershipOfferEligibility(Queue queueMembershipOfferEligibility, TopicExchange exchangeMembership) {
		return BindingBuilder.bind(queueMembershipOfferEligibility).to(exchangeMembership)
				.with(Constant.QUEUE_NAME_MEMBERSHIP_ELIGIBILITY);
	}

	@Bean
	public TopicExchange exchangeMembership() {
		return new TopicExchange(Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP);
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jackson2MessageConverter());
		return rabbitTemplate;
	}
	
	@Bean
	public Jackson2JsonMessageConverter jackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}


	@Bean
	public RabbitListenerContainerFactory<SimpleMessageListenerContainer>
	rabbitListenerContainerFactoryForOfferEligibility(ConnectionFactory rabbitConnectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(rabbitConnectionFactory);
		factory.setPrefetchCount(offerEligibilityQueuePrefetch);
		factory.setConcurrentConsumers(offerEligibilityQueueConcurrent);
		factory.setMessageConverter(jackson2MessageConverter());
		return factory;
	}

}
