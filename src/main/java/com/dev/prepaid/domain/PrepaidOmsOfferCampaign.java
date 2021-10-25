package com.dev.prepaid.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="PREPAID_OMS_CAMPAIGN_OFFERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOmsOfferCampaign implements Serializable {

	@Id
	private Long id;
	private String name;
	private Long offerId;
	private String description;
	private Double value;
	private String valueUnit;
	private String counterId;
	private Long counterValue;
	private String thresholdId;
	private Long thresholdValue;
	private String thresholdValueUnit;
	private Double valueToDeductFromMa;
	private Date startDate;
	private Date endDate;
	private String action;
	private Long day;
	private Long hour;
	private Long minute;
	
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
//	private String deletedBy;
//	private Date deletedDate;

	
}
