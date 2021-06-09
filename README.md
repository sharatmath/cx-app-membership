prepaid-cx-api.git

##  DOCKER BUILD
```sh
```

##  DEPLOYMENT
```sh
```

##  Changelog
```sh
```

##  JMS
```sh
```

## PREPAID_CX_PROV_APPLICATION ATP-DB 
```sh
CREATE TABLE ADMIN.PREPAID_CX_PROV_APPLICATION (
	ID VARCHAR2(50) NOT NULL,
	INSTALL_ID VARCHAR2(50),
	APP_ID VARCHAR2(50),
	
	CREATED_BY VARCHAR2(100),
	CREATED_DATE DATE,
	LAST_MODIFIED_BY VARCHAR2(100),
	LAST_MODIFIED_DATE DATE,
	UNINSTALL_BY VARCHAR2(100),
	UNINSTALL_DATE DATE,
	CONSTRAINT PREPAID_CX_PROV_INSTANCES_PK PRIMARY KEY (ID)
);
```

## PREPAID_CX_PROV_INSTANCES ATP-DB 
```sh
CREATE TABLE ADMIN.PREPAID_CX_PROV_INSTANCES (
	ID VARCHAR2(50) NOT NULL,
	APPLICATION_ID VARCHAR2(50) NOT NULL,
	SERVICE_ID VARCHAR2(50),
	INSTANCE_ID VARCHAR2(50),
	START_DATE DATE,
	END_DATE DATE,
	CAMPAIGN_OFFER_TYPE VARCHAR2(20),
	CAMPAIGN_OFFER_ID NUMBER(38,0),
	INPUT_MAPPING VARCHAR2(4000),
	OUTPUT_MAPPING VARCHAR2(4000),
	STATUS VARCHAR2(20),
	
	CREATED_BY VARCHAR2(100),
	CREATED_DATE DATE,
	LAST_MODIFIED_BY VARCHAR2(100),
	LAST_MODIFIED_DATE DATE,
	DELETED_BY VARCHAR2(100),
	DELETED_DATE DATE,
	CONSTRAINT PREPAID_CX_PROV_INSTANCES_PK PRIMARY KEY (ID)
);
```

##  PrepaidCxProvInvocations NoSQL
```sh
	compartment : singteloracloud, Prepaid_DEV_Comp

	CREATE TABLE PREPAID_CX_PROV_INVOCATIONS(
	ID STRING, 
	INSTANCE_ID STRING, 
	STATUS STRING, 
	INPUT JSON, 
	OUTPUT JSON, 
	
	CREATED_BY STRING, 
	CREATED_DATE TIMESTAMP(3), 
	LAST_MODIFIED_BY STRING, 
	LAST_MODIFIED_DATE TIMESTAMP(3), 
	DELETED_BY STRING, 
	DELETED_DATE TIMESTAMP(3), 
	PRIMARY KEY(SHARD(ID))) USING TTL 1 DAYS
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


