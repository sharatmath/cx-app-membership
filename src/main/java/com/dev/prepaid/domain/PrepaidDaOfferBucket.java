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
@Table(name="PREPAID_DA_BUCKET_OFFERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidDaOfferBucket implements Serializable {
	
	@Id
	private Long id;
	private String code;
	private String description;
	
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
//	private String deletedBy;
//	private Date deletedDate;
}
