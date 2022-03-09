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

	private String payload;

	private String instanceId;

	private boolean displayHiApp;

	private boolean displayUssd;

	private boolean displayUmtu;

	private String crmOfferDisplayName;

	private String crmOfferDisplayDesc;

	private String umtuOfferDisplayName;

	private String umtuOfferDisplayMessage;

	private String umtuTopUpCode;

	private String umtuRetailerCommission;

	private String ussdOfferDisplayName;

	private String ussdOfferDisplayMessage;

	private String hiAppOfferDisplayNameEnglish;

	private String hiAppOfferDisplayNameIndo;

	private String hiAppOfferDisplayNameTamil;

	private String hiAppOfferDisplayNameMandarin;

	private String hiAppOfferDisplayNameBengali;

	private String hiAppOfferDisplayNameBurmese;

	private boolean displayCta;

	private String ctaDisplayTextEnglish;

	private String ctaDisplayTextIndo;

	private String ctaDisplayTextTamil;

	private String ctaDisplayTextMandarin;

	private String ctaDisplayTextBengali;

	private String ctaDisplayTextBurmese;

	private String successRedemptionDisplayEnglish;

	private String successRedemptionDisplayIndo;

	private String successRedemptionDisplayTamil;

	private String successRedemptionDisplayMandarin;

	private String successRedemptionDisplayBengali;

	private String successRedemptionDisplayBurmese;

	private String offerDisplayMessageEnglish;

	private String offerDisplayMessageIndo;

	private String offerDisplayMessageTamil;

	private String offerDisplayMessageMandarin;

	private String offerDisplayMessageBengali;

	private String offerDisplayMessageBurmese;

}
