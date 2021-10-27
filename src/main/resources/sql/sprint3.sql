ALTER TABLE PREPAID_CX_OFFER_MONITORING ADD  TOP_UP_TYPE VARCHAR;
ALTER TABLE PREPAID_CX_OFFER_MONITORING ADD  DA_EXPIRY_DATE TIMESTAMP;
ALTER TABLE PREPAID_CX_OFFER_MONITORING ADD  SERVICE_CLASS VARCHAR2(25);
ALTER TABLE PREPAID_CX_OFFER_MONITORING ADD  IMEI VARCHAR2(100);
ALTER TABLE PREPAID_CX_OFFER_MONITORING ADD  DA_CHANGE NUMBER;
ALTER TABLE PREPAID_CX_OFFER_MONITORING ADD  CHARGED_AMOUNT NUMBER;
ALTER TABLE PREPAID_CX_OFFER_MONITORING ADD  ROAMING_FLAG VARCHAR2(25);
ALTER TABLE PREPAID_CX_OFFER_MONITORING ADD  RATE_PLAN_ID VARCHAR2(25);
ALTER TABLE PREPAID_CX_OFFER_MONITORING ADD  AGGREGATION_PERIOD_DAYS NUMBER;

ALTER TABLE PREPAID_CX_OFFER_EVENT_CONDITION ADD  TOP_UP_TYPE VARCHAR;


DROP TABLE PREPAID_NEW_MSISDN;
CREATE TABLE PREPAID_NEW_MSISDN (
  NEW_MSISDN_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  MSISDN NUMBER NOT NULL,
  STATUS VARCHAR2(20) DEFAULT NULL,
  CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


DROP TABLE TEMP_MONITORING_MSISDN;
CREATE TABLE TEMP_MONITORING_MSISDN (
  ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  REQUEST_ID NUMBER NOT NULL,
  OFFER_CONFIG_ID VARCHAR2(50) NOT NULL
  MSISDN NUMBER
  STATUS VARCHAR2(20) DEFAULT NULL,
  STATUS_DESC VARCHAR2(2000) DEFAULT NULL,
  CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  LAST_UPDATED_DATE TIMESTAMP DEFAULT NULL
);


DROP TABLE PREPAID_OFFER_MONITORING_TX;
CREATE TABLE PREPAID_OFFER_MONITORING_TX (
  OFFER_MONITORING_TX_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  OFFER_CONFIG_ID VARCHAR2(50) NOT NULL
  OFFER_MEMBERSHIP_ID NUMBER NOT NULL,
  OFFER_SELECTION_ID NUMBER NOT NULL,
  MSISDN NUMBER,
  FULFILLMENT_STATUS VARCHAR2(20) DEFAULT NULL,
  FULFILLMENT_DATE TIMESTAMP DEFAULT NULL,
  CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  REQUEST_ID NUMBER NOT NULL,
  LAST_UPDATED_DATE TIMESTAMP DEFAULT NULL
);
CREATE TABLE "PREPAID_DEV"."PREPAID_OFFER_MONITORING_TX"
   (
   "OFFER_MEMBERSHIP_ID" NUMBER NOT NULL ENABLE,
	"OFFER_MONITORING_ID" NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE  NOT NULL ENABLE,
	"FULFILLMENT_STATUS" VARCHAR2(100 BYTE) COLLATE "USING_NLS_COMP" NOT NULL ENABLE,
	"FULFILLMENT_DATE" DATE DEFAULT NULL,
	"OFFER_SELECTION_ID" NUMBER DEFAULT NULL,
	"CREATED_DATE" DATE DEFAULT NULL,
	"LAST_MODIFIED_DATE" DATE DEFAULT NULL,
	"MSISDN" NUMBER(19,0)

