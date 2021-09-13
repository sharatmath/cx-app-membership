package com.dev.prepaid.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
	
	@Value("${pgs.queue.prefetch:1}")
	private Integer pgsQueuePrefetch;
	@Value("${pgs.queue.concurent:1}")
	private Integer pgsQueueConcurent;

	@Value("${membership.queue.prefetch:1}")
	private Integer membershipQueuePrefetch;
	@Value("${membership.queue.concurent:1}")
	private Integer membershipQueueConcurent;
	
	@Value("${redemption.queue.prefetch:1}")
	private Integer redemptionQueuePrefetch;
	@Value("${redemption.queue.concurent:1}")
	private Integer redemptionQueueConcurent;

	//============================================================================= //
	//queue
	@Bean
	public Queue queueSingtelPrepaid() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		Queue queue = new Queue(Constant.QUEUE_NAME, true, false, false, args);
		return queue;
	}
	//binding
	@Bean
	public Binding bindingSingtelPrepaidSubscriberQualificationEvaluation(Queue queueSingtelPrepaid, TopicExchange exchangeSingtel) {
		return BindingBuilder.bind(queueSingtelPrepaid).to(exchangeSingtel)
				.with(Constant.QUEUE_NAME); //route.key.name=queue.name
	}
	//container factory
	@Bean
	public RabbitListenerContainerFactory<SimpleMessageListenerContainer>
	rabbitListenerContainerFactoryForSingtelPrepaid(ConnectionFactory rabbitConnectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(rabbitConnectionFactory);
		factory.setPrefetchCount(pgsQueuePrefetch);
		factory.setConcurrentConsumers(pgsQueueConcurent);
		factory.setMessageConverter(jackson2MessageConverter());
		return factory;
	}
	//============================================================================= //
	
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
		factory.setConcurrentConsumers(redemptionQueueConcurent);
		factory.setMessageConverter(jackson2MessageConverter());
		return factory;
	}
	//============================================================================= //	
	
	//PGS
	// ============================================================================= //
	//queue
	@Bean
	public Queue queueSingtelPgs() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		Queue queue = new Queue(Constant.QUEUE_NAME_SINGTEL_PGS, true, false, false, args);
		return queue;
	}
	//binding
	@Bean
	public Binding bindingSingtelPgsAll(Queue queueSingtelPgs, TopicExchange exchangeSingtel) {
		return BindingBuilder.bind(queueSingtelPgs).to(exchangeSingtel)
				.with(Constant.QUEUE_NAME_SINGTEL_PGS); //route.key.name=queue.name
	}
	//container factory
	@Bean
	public RabbitListenerContainerFactory<SimpleMessageListenerContainer>
	rabbitListenerContainerFactoryForPgs(
			ConnectionFactory rabbitConnectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(rabbitConnectionFactory);
		factory.setPrefetchCount(pgsQueuePrefetch);
		factory.setConcurrentConsumers(pgsQueueConcurent);
		factory.setMessageConverter(jackson2MessageConverter());
		return factory;
	}
	//============================================================================= //	
	
	//PVAS
	//============================================================================= //
	//queue
	@Bean
	public Queue queueSingtelPvas() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		Queue queue = new Queue(Constant.QUEUE_NAME_SINGTEL_PVAS, true, false, false, args);
		return queue;
	}
	//binding
	@Bean
	public Binding bindingSingtelPvasAll(Queue queueSingtelPvas, TopicExchange exchangeSingtel) {
		return BindingBuilder.bind(queueSingtelPvas).to(exchangeSingtel)
				.with(Constant.QUEUE_NAME_SINGTEL_PVAS); //route.key.name=queue.name
	}
	//container factory
	@Bean
	public RabbitListenerContainerFactory<SimpleMessageListenerContainer>
	rabbitListenerContainerFactoryForMembership(
			ConnectionFactory rabbitConnectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(rabbitConnectionFactory);
		factory.setPrefetchCount(membershipQueuePrefetch);
		factory.setConcurrentConsumers(membershipQueueConcurent);
		factory.setMessageConverter(jackson2MessageConverter());
		return factory;
	}

	@Bean
	public Queue queueMembershipOfferMonitoring() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		Queue queue = new Queue(Constant.QUEUE_NAME_MEMBERSHIP_MONITORING, true, false, false, args);
		return queue;
	}

	@Bean
	public Binding bindingMembershipOfferMonitoring(Queue queueMembershipOfferMonitoring, TopicExchange exchangeMembership) {
		return BindingBuilder.bind(queueMembershipOfferMonitoring).to(exchangeMembership)
				.with(Constant.QUEUE_NAME_MEMBERSHIP_MONITORING); //route.key.name=queue.name
	}

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

	@Bean
	public TopicExchange exchangeMembership() {
		return new TopicExchange(Constant.TOPIC_EXCHANGE_NAME_MEMBERSHIP);
	}

	//============================================================================= //
	
	
	// ============================================================================= //
	@Bean
	public TopicExchange exchangeSingtel() {
		return new TopicExchange(Constant.TOPIC_EXCHANGE_NAME_SINGTEL);
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
	// ============================================================================= //
}
