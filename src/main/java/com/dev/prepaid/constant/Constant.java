package com.dev.prepaid.constant;

public class Constant {

  // AMQP
  public static final String TOPIC_EXCHANGE_NAME_SINGTEL = "singtel.exchange";
  public static final String QUEUE_NAME = "singtel.queue";
  public static final String QUEUE_NAME_SINGTEL_PGS = "singtel.pgs.queue";
  public static final String QUEUE_NAME_SINGTEL_PVAS = "singtel.pvas.queue";
  
  // NoSql table
  public static final String CX_REQUEST_LOG_TABLE = "PREPAID_CX_REQUEST_LOG";
  public static final String CX_REQUEST_DETAIL_LOG_TABLE = "PREPAID_CX_REQUEST_DETAIL_LOG";
  
}
