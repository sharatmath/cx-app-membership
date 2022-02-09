
package com.dev.prepaid.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dev.prepaid.model.tableRequest.Group;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferAdvanceFilter extends Auditable {
	@SerializedName("id")
	private Long id;
	@SerializedName("offerConfigId")
	private String offerConfigId;
	@SerializedName("payload")
	private String payload;
	@SerializedName("queryText")
	private String queryText;
	@SerializedName("isCustomQuery")
	private boolean isCustomQuery;

	@SerializedName("payloadList")
	private List<Group> payloadList;
}
