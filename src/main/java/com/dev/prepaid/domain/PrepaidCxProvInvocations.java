package com.dev.prepaid.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PREPAID_CX_PROV_INVOCATIONS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxProvInvocations {
	@Id
	private String id; //request_id
	private String instanceId;
	private String status;
	private String input;
	private String output;
//	private String createdBy;
	private Date createdDate;
	private Date lastModifiedDate;
}
