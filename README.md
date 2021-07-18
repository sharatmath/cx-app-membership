prepaid-cx-api.git

##  DOCKER BUILD
```sh
mvn clean package
docker build -f Dockerfile -t fra.ocir.io/singteloracloud/singtelomcsit/prepaid-cx-membership-api:sit .
docker push fra.ocir.io/singteloracloud/singtelomcsit/prepaid-cx-membership-api:sit
```

##  DEPLOYMENT
```sh
kubectl proxy --kubeconfig /home/opc/.kube/config_prepaid_dev

kubectl delete deployment prepaid-cx-membership-api-deployment --kubeconfig /home/opc/.kube/config_prepaid_dev
kubectl create -f services.yaml --kubeconfig /home/opc/.kube/config_prepaid_dev
```

##  Changelog
```sh
```

##  JMS
```sh
```

## PREPAID_CX_PROV_APPLICATION ATP-DB 
```sh
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

DROP TABLE PREPAID_CX_OFFER_MONITORING;
CREATE TABLE PREPAID_CX_OFFER_MONITORING (
  OFFER_MONITORING_ID NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
  OFFER_CONFIG_ID VARCHAR2(50) NOT NULL,
  EVENT_TYPE VARCHAR2(50) DEFAULT NULL,
  USAGE_SERVICE_TYPE VARCHAR2(50) DEFAULT NULL,
  PRODUCT_PACKAGE VARCHAR2(50) DEFAULT NULL,
  CREDIT_METHOD VARCHAR2(50) DEFAULT NULL,
  OPERATOR_ID VARCHAR2(50) DEFAULT NULL,
  PERIOD_DAYS NUMBER DEFAULT NULL,
  PERIOD_START_DATE DATE DEFAULT NULL,
  PERIOD_END_DATE DATE DEFAULT NULL,
  TOPUP_CODE VARCHAR2(50) DEFAULT NULL,
  USAGE_TYPE VARCHAR2(50) DEFAULT NULL,
  TRANSACTION_VALUE NUMBER DEFAULT NULL,
  OPERATOR_VALUE VARCHAR2(50) DEFAULT NULL,
  PAID_ARPU_OPERATOR VARCHAR2(50) DEFAULT NULL,
  PAID_ARPU_VALUE NUMBER DEFAULT NULL,
  IS_MONITOR_DATE_RANGE NUMBER(1) DEFAULT NULL,
  IS_MONITOR_SPECIFIC_PERIOD NUMBER(1) DEFAULT NULL,
  CREATED_DATE TIMESTAMP NOT NULL,
  LAST_MODIFIED_DATE TIMESTAMP DEFAULT NULL
);
ALTER TABLE PREPAID_CX_OFFER_MONITORING
ADD CONSTRAINT FK_OFFER_CONFIG_ID
  FOREIGN KEY (OFFER_CONFIG_ID)
  REFERENCES PREPAID_CX_OFFER_CONFIG(OFFER_CONFIG_ID);


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
  OPTIN_FLAG VARCHAR2(50) DEFAULT NULL
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
  HOLDOUT_GROUP_ID VARCHAR2(50) DEFAULT NULL
);

DROP TABLE PREPAID_OFFER_PROVISION_TRX;
CREATE TABLE PREPAID_OFFER_PROVISION_TRX (
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

```

##  Sample Invocation Request
```sh
	{
	  "dataSet": {
	    "id": "dataSet-id01",
	    "rows": [
	      [
	        "18e492fd-0c1b-4d6b-b199-62c302139380",
	        "168125468786"
	      ],
	      [
	        "28e492fd-0c1b-4d6b-b199-62c302139380",
	        "268125468786"
	      ],
	      [
	        "38e492fd-0c1b-4d6b-b199-62c302139380",
	        "368125468786"
	      ],
	      [
	        "48e492fd-0c1b-4d6b-b199-62c302139380",
	        "468125468786"
	      ],
	      [
	        "58e492fd-0c1b-4d6b-b199-62c302139380",
	        "568125468786"
	      ]
	    ],
	    "size": 0
	  },
	  "instanceContext": {
	    "appId": "97d16835-90a8-43a6-9a96-dddeacfa9362",
	    "installId": "eb2121ee-7675-4c87-9b0a-a2502892cf8f",
	    "instanceId": "d2fb3ad9-a1ab-474e-b22c-86dcde9ffc84",
	    "serviceId": "cc35bf58-049f-48cb-892b-42e1c30f899d",
	    "secret": "string",
	    "tenantId": "string",
	    "appVersion": "string",
	    "maxBatchSize": 0,
	    "maxPushBatchSize": 0,
	    "productId": "string",
	    "recordDefinition": {
	      "inputParameters": [
	        {
	          "name": "appcloud_row_correlation_id",
	          "dataType": "Text",
	          "width": 40,
	          "unique": true,
	          "required": true,
	          "readOnly": null,
	          "minimumValue": null,
	          "maximumValue": null,
	          "possibleValues": null,
	          "format": null,
	          "resources": null
	        },
	        {
	          "name": "MSISDN",
	          "dataType": "Text",
	          "width": 50,
	          "unique": null,
	          "required": null,
	          "readOnly": true,
	          "minimumValue": null,
	          "maximumValue": null,
	          "possibleValues": null,
	          "format": null,
	          "resources": null
	        }
	      ],
	      "outputParameters": [
	        {
	          "name": "appcloud_row_correlation_id",
	          "dataType": "Text",
	          "width": 40,
	          "unique": true,
	          "required": true,
	          "readOnly": null,
	          "minimumValue": null,
	          "maximumValue": null,
	          "possibleValues": null,
	          "format": null,
	          "resources": null
	        },
	        {
	          "name": "appcloud_row_status",
	          "dataType": "Text",
	          "width": 10,
	          "unique": null,
	          "required": true,
	          "readOnly": null,
	          "minimumValue": null,
	          "maximumValue": null,
	          "possibleValues": [
	            "success",
	            "warning",
	            "failure"
	          ],
	          "format": null,
	          "resources": null
	        },
	        {
	          "name": "appcloud_row_errormessage",
	          "dataType": "Text",
	          "width": 5120,
	          "unique": null,
	          "required": null,
	          "readOnly": null,
	          "minimumValue": null,
	          "maximumValue": null,
	          "possibleValues": null,
	          "format": null,
	          "resources": null
	        }
	      ]
	    }
	  },
	  "maxPullPageSize": 0,
	  "maxPushBatchSize": 0,
	  "onCompletionCallbackEndpoint": {
	    "headers": {},
	    "method": "string",
	    "url": "string"
	  },
	  "productExportEndpoint": {
	    "headers": {},
	    "method": "string",
	    "url": "string"
	  },
	  "productImportEndpoint": {
	    "headers": {},
	    "method": "string",
	    "url": "string"
	  },
	  "uuid": "string"
	}
```


