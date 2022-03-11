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
@Table(name="PREPAID_MREWARDS_OFFERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidMrewardsOffers implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4572770302021879841L;
	@Id
	private Long id;
	private String offerName;
	private Float value;
	private String bonusOfferType;
	private Long valueCap;
	private Date startDate;
	private Date endDate;
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
}
