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
@Table(name="PREPAID_OMS_CAMPAIGN_OFFERS_DELETED")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidMrewardsOffersDeleted implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8724044544885639186L;
	@Id
	private Long id;
	private String offerName;
	private Long value;
	private String bonusOfferType;
	private Double valueCap;
	private String startDate;
	private String endDate;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String deletedBy;
	private Date deletedDate;
	
}
