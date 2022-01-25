
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PREPAID_CX_OFFER_ADVANCE_FILTER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferAdvanceFilter extends Auditable {
	@Id
	@Column(name = "OFFER_ADVANCE_FILTER_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String offerConfigId;
	private String payload;
	private String queryText;
	private boolean isCustomQuery;

	@Transient
	private List<Group> payloadList;
}