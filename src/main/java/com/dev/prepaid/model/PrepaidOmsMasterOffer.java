package com.dev.prepaid.model;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOmsMasterOffer {
	@SerializedName("id") private Long id;
	@SerializedName("code") private String code;
	@SerializedName("type") private String type;
	@SerializedName("description") private String description;
	
	@SerializedName("created_by") private String createdBy;
	@SerializedName("created_date") private Date createdDate;
	@SerializedName("last_modified_by") private String lastModifiedBy;
	@SerializedName("last_modified_date") private Date lastModifiedDate;
	@SerializedName("deleted_by") private String deletedBy;
	@SerializedName("deleted_date") private Date deletedDate;

}
