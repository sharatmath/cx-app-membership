package com.dev.prepaid.domain.nosql;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxProvInvocations {
	@SerializedName("ID")
	private String id; //request_id
	@SerializedName("INSTANCE_ID")
	private String instanceId;
	@SerializedName("STATUS")
	private String status;
	@SerializedName("INPUT")
	private String input;
	@SerializedName("OUTPUT")
	private String output;

	@SerializedName("CREATED_BY")
	private String createdBy;
	@SerializedName("CREATED_DATE")
	private Date createdDate;
	@SerializedName("LAST_MODIFIED_BY")
	private String lastModifiedBy;
	@SerializedName("LAST_MODIFIED_DATE")
	private Date lastModifiedDate;
	@SerializedName("DELETED_BY")
	private String deletedBy;
	@SerializedName("DELETED_DATE")
	private Date deletedDate;
}
