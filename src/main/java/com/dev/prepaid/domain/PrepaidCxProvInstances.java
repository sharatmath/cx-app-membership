package com.dev.prepaid.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PREPAID_CX_PROV_INSTANCES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxProvInstances {
	@Id
	private String id; //generate UUID
	private String applicationId;
	
//	private String installId; //APP INSTALL ID
//	private String appId; //APP ID
	
	private String serviceId; //APP SERVICE ID
	private String instanceId; //INSTANCE ID
	private String programId; //PROGRAM ID
	
	private Date startDate;
	private Date endDate;
	private String campaignOfferType;
	private Long campaignOfferId;
	private String inputMapping;
	private String outputMapping;
	private String status;
	
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String deletedBy;
	private Date deletedDate;
	private Boolean notification;
//	private String uninstallBy;
//	private Date uninstallDate;

}
