package com.dev.prepaid.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidBucketDetailDTO {
	
	
	@SerializedName("bucketName") private String bucketName;
	@SerializedName("offerType") private String offerType;
	@SerializedName("counterId") private String counterId;
	@SerializedName("thresholdId") private String thresholdId;
}
