/**
 * 
 */
package com.dev.prepaid.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Saket
 *
 */
@Entity
@Table(name = "PREPAID_CX_OFFER_MESSAGE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferMessage extends Auditable {
	@Id
	@Column(name = "OFFER_MESSAGE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String offerConfigId;
	
	private String instanceId;
	
	private String channelType;
	
	private String offerDisplayName;
	
	private String offerDisplayDescription;
	
	private String offerDisplayMessage;
	
	private String topupcode;
	
	private String retailerCommission;
	
	private String isCTA_Active;
	
	private String payload;

}
