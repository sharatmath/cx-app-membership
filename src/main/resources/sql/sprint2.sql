DROP TABLE PREPAID_OFFER_ELIGIBILITY_TX;
CREATE TABLE PREPAID_OFFER_ELIGIBILITY_TX (
  ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  INVOCATION_ID VARCHAR2(50) NOT NULL,
  INSTANCE_ID VARCHAR2(50) NOT NULL,
  BATCH_ID NUMBER NOT NULL,
  BATCH_SIZE NUMBER NOT NULL,
  TOTAL_ROW NUMBER NOT NULL,
  DATA VARCHAR2(4000) NOT NULL,
  IS_EVALUATED NUMBER(1) DEFAULT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL
);

--PREPAID_OFFER_MEMBERSHIP_EXCLUS
ALTER TABLE PREPAID_OFFER_MEMBERSHIP_EXCLUS ADD  EVALUATION_TYPE VARCHAR2(50) ;
ALTER TABLE PREPAID_OFFER_MEMBERSHIP_EXCLUS ADD  EVALUATION_STATUS VARCHAR2(1000) ;
ALTER TABLE PREPAID_OFFER_MEMBERSHIP_EXCLUS DROP COLUMN  OFFER_SELECTION_ID;

ALTER TABLE PREPAID_OFFER_MEMBERSHIP_EXCLUS ADD  INVOCATION_ID VARCHAR2(1000);
ALTER TABLE PREPAID_OFFER_MEMBERSHIP_EXCLUS ADD  OFFER_ELIGIBILITY_TX_ID NUMBER;

ALTER TABLE PREPAID_OFFER_MEMBERSHIP ADD  INVOCATION_ID VARCHAR2(1000);
ALTER TABLE PREPAID_OFFER_MEMBERSHIP ADD  OFFER_ELIGIBILITY_TX_ID NUMBER;