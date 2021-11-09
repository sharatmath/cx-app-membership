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

	@Value("${offer.eligibility.queue.prefetch:1}")
	private Integer offerEligibilityQueuePrefetch;
	@Value("${offer.monitoring.queue.concurrent:1}")
	private Integer offerEligibilityQueueConcurrent;
	
	@Value("${redemption.queue.prefetch:1}")
	private Integer redemptionQueuePrefetch;
	@Value("${redemption.queue.concurrent:1}")
	private Integer redemptionQueueConcurrent;


	//REDEMPTION
	// ============================================================================= //
	//queue
	@Bean
	public Queue queueSingtelRedemption() {
			Map<String, Object> args = new HashMap<String, Object>();
			args.put("x-max-priority", 1);
			args.put("x-dead-letter-exchange", "singtel.redemption.dlx");
			args.put("x-dead-letter-routing-key", "singtel.redemption.rk");
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

	@Bean
	public DirectExchange dlxSingtelRedemption() {
		return new DirectExchange("singtel.redemption.dlx");
	}

	@Bean
	public Binding singtelRedemptionDlqBinding() {
		return BindingBuilder.bind(dlqSingtelRedemption()).to(dlxSingtelRedemption()).with("singtel.redemption.rk");
	}

	//============================================================================= //
	// offerMonitoring
	@Bean
	public Queue queueOfferMonitoring() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		args.put("x-dead-letter-exchange", "offer.monitoring.dlx");
		args.put("x-dead-letter-routing-key", "offer.monitoring.rk");
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

	@Bean
	public DirectExchange dlxOfferMonitoring() {
		return new DirectExchange("offer.monitoring.dlx");
	}

	@Bean
	public Binding offerMonitoringDlqBinding() {
		return BindingBuilder.bind(dlqOfferMonitoring()).to(dlxOfferMonitoring()).with("offer.monitoring.rk");
	}
	//============================================================================= //
	// Subscriber
	@Bean
	public Queue queueMembershipOfferSubscriber() {
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-max-priority", 1);
		args.put("x-dead-letter-exchange", "offer.subscriber.dlx");
		args.put("x-dead-letter-routing-key", "offer.subscriber.rk");
		Queue queue = new Queue(Constant.QUEUE_NAME_MEMBERSHIP_SUBSCRIBER, true, false, false, args);
		return queue;
	}

	@Bean
	public Queue dlqMembershipOfferSubscriber() {
		return new Queue(Constant.QUEUE_NAME_DLQ_MEMBERSHIP_SUBSCRIBER);
	}

	@Bean
	public DirectExchange dlxOfferSubscriber() {
		return new DirectExchange("offer.subscriber.dlx");
	}

	@Bean
	public Binding offerSubscriberDlqBinding() {
		return BindingBuilder.bind(dlqMembershipOfferSubscriber()).to(dlxOfferSubscriber()).with("offer.subscriber.rk");
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
		args.put("x-dead-letter-exchange", "membership.offer.dlx");
		args.put("x-dead-letter-routing-key", "membership.offer.rk");
		Queue queue = new Queue(Constant.QUEUE_NAME_MEMBERSHIP_ELIGIBILITY, true, false, false, args);
		return queue;
	}

	@Bean
	public Queue dlqMembershipOfferEigibility() {
		return new Queue(Constant.QUEUE_NAME_DLQ_MEMBERSHIP_ELIGIBILITY);
	}

	@Bean
	public DirectExchange dlxMembershipOfferEigibility() {
		return new DirectExchange("membership.offer.dlx");
	}

	@Bean
	public Binding membershipOfferEigibilityDlqBinding() {
		return BindingBuilder.bind(dlqMembershipOfferEigibility()).to(dlxMembershipOfferEigibility()).with("membership.offer.rk");
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
	rabbitListenerContainerFactoryForOfferMonitoring(ConnectionFactory rabbitConnectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(rabbitConnectionFactory);
		factory.setPrefetchCount(offerMonitoringQueuePrefetch);
		factory.setConcurrentConsumers(offerMonitoringQueueConcurrent);
		factory.setMessageConverter(jackson2MessageConverter());
		return factory;
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
