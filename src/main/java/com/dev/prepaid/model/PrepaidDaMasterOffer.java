package com.dev.prepaid.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.dev.prepaid.model.PrepaidOmsMasterOffer.PrepaidOmsMasterOfferBuilder;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidDaMasterOffer {

	@SerializedName("id") private Long id;
	@SerializedName("code") private String code;
	@SerializedName("description") private String description;
	@SerializedName("createdBy") private String createdBy;
	@SerializedName("createdDate") private Timestamp createdDate;
	@SerializedName("lastModifiedBy") private String lastModifiedBy;
	@SerializedName("lastModifiedDate") private Date lastModifiedDate;
	@SerializedName("deletedBy") private String deletedBy;
	@SerializedName("deletedDate") private Date deletedDate;
}
