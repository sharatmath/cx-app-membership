package com.dev.prepaid.model.invocation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvocationRequest {	
	private String uuid;  // invocation ID
	private Integer batchId; // batch Id
	private Long offerEligibilityTxId; //ID prepaid_offer_eligibility_tx
	private InstanceContext instanceContext;
	private DataSet dataSet;
	private ProductExpImpEndpoint productExportEndpoint;
	private ProductExpImpEndpoint productImportEndpoint;
	private OnCompletionCallbackEndpoint onCompletionCallbackEndpoint;
	private Integer maxPullPageSize;
	private Integer maxPushBatchSize;
	private String trnLogId; // row from ODI unique record
}
