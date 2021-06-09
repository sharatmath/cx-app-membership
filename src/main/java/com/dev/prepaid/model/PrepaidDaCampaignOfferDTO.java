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
public class PrepaidDaCampaignOfferDTO {
	@SerializedName("id") private Long id;
	@SerializedName("name") private String name;
	@SerializedName("offer_id") private Long offerId;
	@SerializedName("description") private String description;
	@SerializedName("value") private double value;
	@SerializedName("value_unit") private String valueUnit;
	@SerializedName("value_calidity_in_days") private Long valueValidityInDays;
	@SerializedName("value_to_deduct_from_ma") private Double valueToDeductFromMa;
	@SerializedName("start_date") private Date startDate;
	@SerializedName("end_date") private Date endDate;
	@SerializedName("action") private String action;
	
	@SerializedName("created_by") private String createdBy;
	@SerializedName("created_date") private Date createdDate;
	@SerializedName("last_modified_by") private String lastModifiedBy;
	@SerializedName("last_modified_date") private Date lastModifiedDate;
	@SerializedName("deleted_by") private String deletedBy;
	@SerializedName("deleted_date") private Date deletedDate;
}
