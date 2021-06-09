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
@Table(name="PREPAID_OMS_BUCKET_OFFERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOmsOfferBucket implements Serializable {
	
	@Id
	private Long id;
	private String code;
	private String type;
	private String description;
	private String counterId;
	private String thresholdId;
	
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
//	private String deletedBy;
//	private Date deletedDate;

	
}
