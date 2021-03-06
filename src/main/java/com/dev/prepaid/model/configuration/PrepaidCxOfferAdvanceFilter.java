/**
 * 
 */
package com.dev.prepaid.model.configuration;

import com.dev.prepaid.model.tableRequest.Group;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Saket
 *
 * 
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferAdvanceFilter {

	@SerializedName("id")
	private Long id;
	@SerializedName("offerConfigId")
	private String offerConfigId;
	@SerializedName("instanceId")
	private String instanceId;
	@SerializedName("payload")
	private String payload;
	@SerializedName("queryText")
	private String queryText;
	@SerializedName("isCustomQuery")
	private boolean isCustomQuery;

	private List<Group> payloadList;

}
