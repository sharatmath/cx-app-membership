DROP TABLE IF EXISTS request_log;
DROP TABLE IF EXISTS PREPAID_CX_PROV_INSTANCES;
DROP TABLE IF EXISTS PREPAID_CX_PROV_INVOCATIONS;
 
CREATE TABLE request_log (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  uri VARCHAR(250) DEFAULT NULL,
  request JSON DEFAULT NULL,
  response JSON DEFAULT NULL,
  created_date TIMESTAMP DEFAULT NULL
);

CREATE TABLE PREPAID_CX_PROV_INSTANCES (
  ID VARCHAR(50) PRIMARY KEY,
  SERVICE_ID VARCHAR(50) DEFAULT NULL,
  START_DATE TIMESTAMP DEFAULT NULL,
  END_DATE TIMESTAMP DEFAULT NULL,
  CAMPAIGN_OFFER_TYPE VARCHAR(20) DEFAULT NULL,
  CAMPAIGN_OFFER_ID INT DEFAULT NULL,
  INPUT_MAPPING VARCHAR(4000) DEFAULT NULL,
  OUTPUT_MAPPING VARCHAR(4000) DEFAULT NULL,
  STATUS VARCHAR(20) DEFAULT NULL,
  CREATED_BY VARCHAR(100) DEFAULT NULL,
  CREATED_DATE TIMESTAMP DEFAULT NULL,
  LAST_MODIFIED_BY VARCHAR(100) DEFAULT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL,
  DELETED_BY VARCHAR(100) DEFAULT NULL,
  DELETED_DATE TIMESTAMP DEFAULT NULL
);

INSERT INTO PREPAID_CX_PROV_INSTANCES VALUES ('inst-id-001', 'service-id-001', null, null, null, null, null, null, 'CREATED', null, null, null, null, null, null); 

CREATE TABLE PREPAID_CX_PROV_INVOCATIONS (
  ID INT AUTO_INCREMENT  PRIMARY KEY,
  INSTANCE_ID VARCHAR(50) DEFAULT NULL,
  STATUS VARCHAR(20) DEFAULT NULL,
  INPUT VARCHAR(4000) DEFAULT NULL,
  OUTPUT VARCHAR(4000) DEFAULT NULL,
  CREATED_BY VARCHAR(100) DEFAULT NULL,
  CREATED_DATE TIMESTAMP DEFAULT NULL,
  LAST_MODIFIED_BY VARCHAR(100) DEFAULT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL,
  DELETED_BY VARCHAR(100) DEFAULT NULL,
  DELETED_DATE TIMESTAMP DEFAULT NULL
);

--- DATA MODEL DROP 2 ---
DROP TABLE PREPAID_CX_OFFER_CONFIG;
CREATE TABLE PREPAID_CX_OFFER_CONFIG (
  OFFER_CONFIG_ID VARCHAR2(50) PRIMARY KEY,
  PROGRAM_ID VARCHAR2(50) NOT NULL,
  INSTANCE_ID VARCHAR2(50) NOT NULL,
  PROVISION_TYPE VARCHAR2(30) NOT NULL,
  PROGRAM_NAME VARCHAR2(100) NOT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL,
  DELETED_BY STRING DEFAULT NULL,
  DELETED_DATE DATE DEFAULT NULL
);

DROP TABLE PREPAID_CX_OFFER_SELECTION;
CREATE TABLE PREPAID_CX_OFFER_SELECTION (
  OFFER_SELECTION_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  OFFER_CONFIG_ID VARCHAR2(50) NOT NULL,
  OFFER_BUCKET_TYPE VARCHAR2(50) NOT NULL,
  OFFER_ID VARCHAR2(50) NOT NULL,
  OFFER_BUCKET_ID VARCHAR2(50) NOT NULL,
  OFFER_TYPE VARCHAR2(100) NOT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL
);
COMMENT ON COLUMN PREPAID_CX_OFFER_SELECTION.OFFER_BUCKET_TYPE IS 'DA/OMS';
ALTER TABLE PREPAID_CX_OFFER_SELECTION
ADD CONSTRAINT FK_OFFER_CONFIG_ID
  FOREIGN KEY (OFFER_CONFIG_ID)
  REFERENCES PREPAID_CX_OFFER_CONFIG(OFFER_CONFIG_ID);

DROP TABLE PREPAID_CX_OFFER_ADVANCE_FILTER;
CREATE TABLE PREPAID_CX_OFFER_ADVANCE_FILTER (
  ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  OFFER_CONFIG_ID VARCHAR2(50) NOT NULL,
  OFFER_BUCKET_TYPE VARCHAR2(50) NOT NULL,
  OFFER_ID VARCHAR2(50) NOT NULL,
  OFFER_BUCKET_ID VARCHAR2(50) NOT NULL,
  OFFER_TYPE VARCHAR2(100) NOT NULL
);
COMMENT ON COLUMN PREPAID_CX_OFFER_ADVANCE_FILTER.OFFER_BUCKET_TYPE IS 'DA/OMS';

DROP TABLE PREPAID_CX_OFFER_ELIGIBILITY;
CREATE TABLE PREPAID_CX_OFFER_ELIGIBILITY (
  OFFER_ELIGIBILITY_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  OFFER_CONFIG_ID VARCHAR2(50) NOT NULL,
  IS_FREQUENCY_ONLY NUMBER(1) DEFAULT NULL,
  IS_FREQUENCY_AND_TIME NUMBER(1) DEFAULT NULL,
  FREQUENCY NUMBER DEFAULT NULL,
  NUMBER_OF_FREQUENCY NUMBER DEFAULT NULL,
  NUMBER_OF_DAYS NUMBER DEFAULT NULL,
  IS_OFFER_LEVEL_CAP_ONLY NUMBER(1) DEFAULT NULL,
  IS_OFFER_LEVEL_CAP_AND_PERIOD NUMBER(1) DEFAULT NULL,
  OFFER_LEVEL_CAP_VALUE NUMBER DEFAULT NULL,
  OFFER_LEVEL_CAP_PERIOD_VALUE NUMBER DEFAULT NULL,
  OFFER_LEVEL_CAP_PERIOD_DAYS NUMBER DEFAULT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL
);
ALTER TABLE PREPAID_CX_OFFER_ELIGIBILITY
ADD CONSTRAINT FK_OFFER_CONFIG_ID
  FOREIGN KEY (OFFER_CONFIG_ID)
  REFERENCES PREPAID_CX_OFFER_CONFIG(OFFER_CONFIG_ID);
--20210818 update UI start
ALTER TABLE PREPAID_CX_OFFER_ELIGIBILITY
    ADD  EXCLUDE_PROGRAM_ID VARCHAR2(50) ;
--20210818 update UI end
--
--DROP TABLE PREPAID_CX_OFFER_MONITORING;
--CREATE TABLE PREPAID_CX_OFFER_MONITORING (
--  OFFER_MONITORING_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
--  OFFER_CONFIG_ID VARCHAR2(50) NOT NULL,
--  EVENT_TYPE VARCHAR2(50) DEFAULT NULL,
--  USAGE_SERVICE_TYPE VARCHAR2(50) DEFAULT NULL,
--  PRODUCT_PACKAGE VARCHAR2(50) DEFAULT NULL,
--  CREDIT_METHOD VARCHAR2(50) DEFAULT NULL,
--  OPERATOR_ID VARCHAR2(50) DEFAULT NULL,
--  PERIOD VARCHAR(10) DEFAULT NULL,
--  PERIOD_DAYS NUMBER DEFAULT NULL,
--  PERIOD_START_DATE DATE DEFAULT NULL,
--  PERIOD_END_DATE DATE DEFAULT NULL,
--  TOPUP_CODE VARCHAR2(50) DEFAULT NULL,
--  USAGE_TYPE VARCHAR2(50) DEFAULT NULL,
--  TRANSACTION_VALUE NUMBER DEFAULT NULL,
--  OPERATOR_VALUE VARCHAR2(50) DEFAULT NULL,
--  PAID_ARPU_OPERATOR VARCHAR2(50) DEFAULT NULL,
--  PAID_ARPU_VALUE NUMBER DEFAULT NULL,
--  IS_MONITOR_DATE_RANGE NUMBER(1) DEFAULT NULL,
--  IS_MONITOR_SPECIFIC_PERIOD NUMBER(1) DEFAULT NULL,
--  CREATED_DATE TIMESTAMP NOT NULL,
--  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL
--);
--ALTER TABLE PREPAID_CX_OFFER_MONITORING
--ADD CONSTRAINT FK_OFFER_CONFIG_ID
--  FOREIGN KEY (OFFER_CONFIG_ID)
--  REFERENCES PREPAID_CX_OFFER_CONFIG(OFFER_CONFIG_ID);


DROP TABLE PREPAID_CX_OFFER_MONITORING;
CREATE TABLE PREPAID_CX_OFFER_MONITORING (
  OFFER_MONITORING_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  OFFER_CONFIG_ID VARCHAR2(50) NOT NULL,
  EVENT_TYPE VARCHAR2(50) DEFAULT NULL,
  USAGE_SERVICE_TYPE VARCHAR2(50) DEFAULT NULL,
  CREDIT_METHOD VARCHAR2(50) DEFAULT NULL,
  OPERATOR_ID VARCHAR2(50) DEFAULT NULL,
  TOP_UP_CODE VARCHAR2(50) DEFAULT NULL,
  TOP_UP_CUR_BALANCE_OP VARCHAR2(50) DEFAULT NULL,
  TOP_UP_CUR_BALANCE_VALUE NUMBER DEFAULT NULL,
  TOP_UP_ACC_BALANCE_BEFORE_OP VARCHAR2(50) DEFAULT NULL,
  TOP_UP_ACC_BALANCE_BEFORE_VALUE NUMBER DEFAULT NULL,
  TOP_UP_OP VARCHAR2(50) DEFAULT NULL,
  TOP_UP_TRANSACTION_VALUE NUMBER DEFAULT NULL,
  TOP_UP_DA_ID VARCHAR2(50) DEFAULT NULL,
  TOP_UP_DA_BALANCE_OP VARCHAR2(50) DEFAULT NULL,
  TOP_UP_DA_BALANCE_VALUE NUMBER DEFAULT NULL,
  TOP_UP_TEMP_SERVICE_CLASS VARCHAR2(50) DEFAULT NULL,
  USAGE_TYPE VARCHAR2(50) DEFAULT NULL,
  COUNTRY_CODE VARCHAR2(50) DEFAULT NULL,
  USAGE_OP VARCHAR2(50) DEFAULT NULL,
  USAGE_VALUE NUMBER DEFAULT NULL,
  ARPU_TYPE VARCHAR2(50) DEFAULT NULL,
  ARPU_OP VARCHAR2(50) DEFAULT NULL,
  ARPU_VALUE NUMBER DEFAULT NULL,
  ARPU_SELECTED_TOP_UP_CODE VARCHAR2(50) DEFAULT NULL,
  IS_MONITOR_DATE_RANGE NUMBER(1) DEFAULT NULL,
  PERIOD_START_DATE DATE DEFAULT NULL,
  PERIOD_END_DATE DATE DEFAULT NULL,
  IS_MONITOR_SPECIFIC_PERIOD NUMBER(1) DEFAULT NULL,
  PERIOD VARCHAR(10) DEFAULT NULL,
  PERIOD_DAYS NUMBER DEFAULT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL
);
ALTER TABLE PREPAID_CX_OFFER_MONITORING
ADD CONSTRAINT FK_OFFER_CONFIG_ID
  FOREIGN KEY (OFFER_CONFIG_ID)
  REFERENCES PREPAID_CX_OFFER_CONFIG(OFFER_CONFIG_ID);

--20210818 update UI start
DROP TABLE PREPAID_CX_OFFER_EVENT_CONDITION;
CREATE TABLE PREPAID_CX_OFFER_EVENT_CONDITION (
    OFFER_EVENT_CONDITION_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    OFFER_CONFIG_ID VARCHAR2(50) NOT NULL,
    CAMPAIGN_START_DATE DATE NULL,
    CAMPAIGN_END_DATE DATE NULL,
    EVENT_CONDITION_NAME VARCHAR2(50) DEFAULT NULL,
    EVENT_CONDITION_TYPE VARCHAR2(50) DEFAULT NULL,
    CREDIT_METHOD VARCHAR2(50) DEFAULT NULL,
    USAGE_SERVICE_TYPE VARCHAR2(50) DEFAULT NULL,
    OPERATOR_ID VARCHAR2(50) DEFAULT NULL,
    TOP_UP_CODE VARCHAR2(50) DEFAULT NULL,
    TOP_UP_CUR_BALANCE_OP VARCHAR2(50) DEFAULT NULL,
    TOP_UP_CUR_BALANCE_VALUE NUMBER DEFAULT NULL,
    TOP_UP_ACC_BALANCE_BEFORE_OP VARCHAR2(50) DEFAULT NULL,
    TOP_UP_ACC_BALANCE_BEFORE_VALUE NUMBER DEFAULT NULL,
    TOP_UP_OP VARCHAR2(50) DEFAULT NULL,
    TOP_UP_TRANSACTION_VALUE NUMBER DEFAULT NULL,
    TOP_UP_DA_ID VARCHAR2(50) DEFAULT NULL,
    TOP_UP_DA_BALANCE_OP VARCHAR2(50) DEFAULT NULL,
    TOP_UP_DA_BALANCE_VALUE NUMBER DEFAULT NULL,
    TOP_UP_TEMP_SERVICE_CLASS VARCHAR2(50) DEFAULT NULL,
    EVENT_TYPE_USAGES NUMBER DEFAULT NULL,
    EVENT_USAGES_OP VARCHAR2(50) DEFAULT NULL,
    EVENT_USAGES_VALUE NUMBER DEFAULT NULL,
    AGGREGATION_PERIOD_DAYS NUMBER DEFAULT NULL,
    COUNTRY_CODE VARCHAR2(50) DEFAULT NULL,
    ARPU_TYPE VARCHAR2(50) DEFAULT NULL,
    ARPU_OP VARCHAR2(50) DEFAULT NULL,
    ARPU_VALUE NUMBER DEFAULT NULL,
    ARPU_SELECTED_TOP_UP_CODE VARCHAR2(50) DEFAULT NULL,
    CREATED_DATE TIMESTAMP NOT NULL,
    LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL
);
ALTER TABLE PREPAID_CX_OFFER_EVENT_CONDITION
ADD CONSTRAINT FK_OFFER_CONFIG_ID
  FOREIGN KEY (OFFER_CONFIG_ID)
  REFERENCES PREPAID_CX_OFFER_CONFIG(OFFER_CONFIG_ID);
--20210818 update UI end

DROP TABLE PREPAID_CX_OFFER_REDEMPTION;
CREATE TABLE PREPAID_CX_OFFER_REDEMPTION (
  OFFER_REDEMPTION_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  OFFER_CONFIG_ID VARCHAR2(50) NOT NULL,
  IS_REDEMPTION_CAP_ONLY NUMBER(1) DEFAULT NULL,
  REDEMPTION_CAP_VALUE NUMBER DEFAULT NULL,
  IS_REDEMPTION_CAP_AND_PERIOD NUMBER(1) DEFAULT NULL,
  TOTAL_REDEMPTION_PERIOD_VALUE NUMBER DEFAULT NULL,
  IS_FREQUENCY_ONLY NUMBER(1) DEFAULT NULL,
  IS_FREQUENCY_AND_TIME NUMBER(1) DEFAULT NULL,
  FREQUENCY_VALUE NUMBER DEFAULT NULL,
  TIME_PERIOD_VALUE NUMBER DEFAULT NULL,
  TIME_PERIOD_TYPE VARCHAR2(50) DEFAULT NULL,
  TOTAL_REDEMPTION_PERIOD_TYPE VARCHAR2(50) DEFAULT NULL,
  TOTAL_RECURRING_FREQUENCY NUMBER DEFAULT NULL,
  IS_RECURRING_FREQUENCY_AND_PERIOD NUMBER(1) DEFAULT NULL,
  RECURRING_FREQUENCY_VALUE NUMBER DEFAULT NULL,
  RECURRING_FREQUENCY_PERIOD_TYPE VARCHAR2(50) DEFAULT NULL,
  RECURRING_FREQUENCY_PERIOD_VALUE NUMBER DEFAULT NULL,
  IS_RECURRING_FREQUENCY_EACH_MONTH NUMBER(1) DEFAULT NULL,
  RECURRING_FREQUENCY_DAY_OF_MONTH NUMBER DEFAULT NULL,
  REDEMPTION_METHOD VARCHAR2(40) DEFAULT NULL,
  SMS_KEYWORD VARCHAR2(50) DEFAULT NULL,
  SMS_KEYWORD_VALIDITY_DAYS NUMBER DEFAULT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL
);
ALTER TABLE PREPAID_CX_OFFER_REDEMPTION
ADD CONSTRAINT FK_OFFER_CONFIG_ID
  FOREIGN KEY (OFFER_CONFIG_ID)
  REFERENCES PREPAID_CX_OFFER_CONFIG(OFFER_CONFIG_ID);

DROP TABLE PREPAID_OFFER_MEMBERSHIP;
CREATE TABLE PREPAID_OFFER_MEMBERSHIP (
  OFFER_MEMBERSHIP_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  OFFER_CONFIG_ID VARCHAR2(50) NOT NULL,
  OFFER_SELECTION_ID NUMBER DEFAULT NULL,
  MSISDN NUMBER DEFAULT NULL,
  OFFER_DATE DATE DEFAULT NULL,
  REDEMPTION_DATE DATE DEFAULT NULL,
  TOPUP_DATE DATE DEFAULT NULL,
  COUPON_CODE_ID NUMBER DEFAULT NULL,
  COUPON_CODE_STATUS NUMBER DEFAULT NULL,
  NOTIFICATION_CHANNEL VARCHAR2(50) DEFAULT NULL,
  OPID_TAKEUP_CHANNEL VARCHAR2(50) DEFAULT NULL,
  NOTIFICATION_MESSAGE VARCHAR2(50) DEFAULT NULL,
  REDEEM_FLAG VARCHAR2(50) DEFAULT NULL,
  MONITORING_START_DATE DATE DEFAULT NULL,
  MONITORING_END_DATE DATE DEFAULT NULL,
  FULFILLMENT_STATUS VARCHAR2(50) DEFAULT NULL,
  FULFILLMENT_DATE DATE DEFAULT NULL,
  OPTIN_FLAG VARCHAR2(50) DEFAULT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL
);

DROP TABLE PREPAID_OFFER_MEMBERSHIP_EXCLUS;
CREATE TABLE PREPAID_OFFER_MEMBERSHIP_EXCLUS (
  OFFER_MEMBERSHIP_EXCL_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  OFFER_SELECTION_ID NUMBER NOT NULL,
  MSISDN NUMBER DEFAULT NULL,
  OFFER_DATE DATE DEFAULT NULL,
  TOPUP_DATE DATE DEFAULT NULL,
  IS_CONTROL_FLAG NUMBER(1) DEFAULT NULL,
  IS_GCG_FLAG NUMBER(1) DEFAULT NULL,
  HOLDOUT_GROUP_ID VARCHAR2(50) DEFAULT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL
);

DROP TABLE PREPAID_OFFER_PROVISION_TX;
CREATE TABLE PREPAID_OFFER_PROVISION_TX (
  OFFER_PROVISION_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  OFFER_MEMBERSHIP_ID NUMBER NOT NULL,
  MSISDN NUMBER DEFAULT NULL,
  PROVISION_DATE DATE DEFAULT NULL,
  PROVISION_STATUS VARCHAR2(50) DEFAULT NULL
);

DROP TABLE PREPAID_PROVISIONED_OFFER;
CREATE TABLE PREPAID_PROVISIONED_OFFER(
   PROVISIONED_OFFER_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
   OFFER_PROVISION_ID NUMBER NOT NULL,
   OFFER_ID NUMBER DEFAULT NULL,
   NAME VARCHAR2(50) DEFAULT NULL,
   DESCRIPTION VARCHAR2(50) DEFAULT NULL,
   VALUE NUMBER DEFAULT NULL,
   VALUE_UNIT VARCHAR2(50) DEFAULT NULL,
   VALUE_CAP NUMBER DEFAULT NULL,
   VALIDITY NUMBER DEFAULT NULL,
   VALUE_TO_DEDUCT_FROM_MA NUMBER DEFAULT NULL,
   START_DATE DATE DEFAULT NULL,
   END_DATE DATE DEFAULT NULL,
   ACTION VARCHAR2(50) DEFAULT NULL,
   COUNTER_ID VARCHAR2(50) DEFAULT NULL,
   COUNTER_VALUE NUMBER DEFAULT NULL,
   THRESHOLD_ID VARCHAR2(50) DEFAULT NULL,
   THRESHOLD_VALUE NUMBER DEFAULT NULL,
   THRESHOLD_VALUE_UNIT VARCHAR2(50) DEFAULT NULL,
   DAY NUMBER DEFAULT NULL,
   HOUR NUMBER DEFAULT NULL,
   MINUTE NUMBER DEFAULT NULL
);

DROP TABLE PREPAID_EVALUATION_LOG;
CREATE TABLE PREPAID_EVALUATION_LOG(
   ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
   REQUEST_ID VARCHAR2(100) NOT NULL,
   OFFER_CONFIG_ID VARCHAR2(50) DEFAULT NULL,
   INSTANCE_ID  VARCHAR2(50)  DEFAULT NULL,
   MSISDN NUMBER NOT NULL,
   PROVISION_TYPE VARCHAR2(100) DEFAULT NULL,
   EVENT_TYPE VARCHAR2(50) DEFAULT NULL ,
   DATA_TRX VARCHAR2(1000) DEFAULT NULL,
   DATA_CONFIG VARCHAR2(1000) DEFAULT NULL,
   STATUS VARCHAR2(50) DEFAULT NULL,
   STATUS_CODE NUMBER NOT NULL,
   ERROR_MSG VARCHAR2(500) DEFAULT NULL,
   CREATED_DATE DATE DEFAULT NULL,
   LAST_MODIFIED_DATE DATE DEFAULT NULL
);


drop PROCEDURE prepaid_evaluation_policy;
CREATE PROCEDURE prepaid_evaluation_policy
(
    in_msisdn IN NUMBER
)
IS
 trx_prepaid f_tbl_topup%ROWTYPE;
BEGIN
  insert into
  prepaid_evaluation_log(msisdn, result, payload)
  values(in_msisdn, 'sucess', '1');

  SELECT *
  INTO trx_prepaid
  FROM f_tbl_topup
  WHERE msisdn = in_msisdn;


EXCEPTION
   WHEN OTHERS THEN
      dbms_output.put_line( SQLERRM );


END;


DROP PROCEDURE prepaid_evaluation_monitoring_arpu_policy;
CREATE PROCEDURE prepaid_evaluation_monitoring_arpu_policy
(
    in_request_id in NUMBER,
    in_instance_id IN VARCHAR2,
    in_offer_config_id IN VARCHAR2,
    in_msisdn IN NUMBER,
    out_result OUT NUMBER
)
IS
 trx_prepaid f_tbl_topup%ROWTYPE;
BEGIN
  insert into
  prepaid_evaluation_log(request_id, msisdn, instance_id, offer_config_id, result, payload)
  values(in_request_id, in_msisdn, in_instance_id, in_offer_config_id, 'SUCCESS', '1');

    out_result := 1;

  SELECT *
  INTO trx_prepaid
  FROM f_tbl_topup
  WHERE msisdn = in_msisdn;


EXCEPTION
   WHEN OTHERS THEN
      dbms_output.put_line( SQLERRM );


END;



DROP PROCEDURE prepaid_evaluation_monitoring_usage_policy;
CREATE PROCEDURE prepaid_evaluation_monitoring_usage_policy
(
    in_request_id in VARCHAR2,
    in_instance_id IN VARCHAR2,
    in_offer_config_id IN VARCHAR2,
    in_msisdn IN NUMBER,
    out_result OUT NUMBER
)
IS
 trx_prepaid f_tbl_topup%ROWTYPE;
BEGIN
  insert into
  prepaid_evaluation_log(request_id, msisdn, instance_id, offer_config_id, result, payload)
  values(in_request_id, in_msisdn, in_instance_id, in_offer_config_id, 'SUCCESS', '1');

  SELECT *
   INTO trx_prepaid
   FROM f_tbl_topup
   WHERE msisdn = in_msisdn;

      IF f_tbl_topup.o
       out_result := 1;


EXCEPTION
   WHEN OTHERS THEN
      dbms_output.put_line( SQLERRM );


END;