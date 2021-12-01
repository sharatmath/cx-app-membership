package com.dev.prepaid.constant;

public class Constant {
  public static final String TOPIC_EXCHANGE_NAME_MEMBERSHIP = "membership.exchange";
  public static final String QUEUE_NAME_MEMBERSHIP_MONITORING = "membership.offer.monitoring.queue";
  public static final String QUEUE_NAME_MEMBERSHIP_EVENT_CONDITION = "membership.offer.event.condition.queue";
  public static final String QUEUE_NAME_MEMBERSHIP_SUBSCRIBER = "membership.offer.subscriber.queue";
  public static final String QUEUE_NAME_SINGTEL_REDEMPTION = "singtel.redemption.queue";
  public static final String QUEUE_NAME_MEMBERSHIP_ELIGIBILITY = "membership.offer.eligibility.queue";

  public static final String CONNECTION_FACTORY_NAME_MEMBERSHIP_MONITORING = "rabbitListenerContainerFactoryForOfferMonitoring";
  public static final String CONNECTION_FACTORY_NAME_MEMBERSHIP_ELIGIBILITY = "rabbitListenerContainerFactoryForOfferEligibility";
}