/**
 * 
 */
package com.dev.prepaid.model.configuration;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Saket
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferMessage {

	@SerializedName("id")
	private Long id;

	@SerializedName("payload")
	private String payload;

	@SerializedName("instanceId")
	private String instanceId;

	@SerializedName("displayHiApp")
	private boolean displayHiApp;

	@SerializedName("displayUssd")
	private boolean displayUssd;

	@SerializedName("displayUmtu")
	private boolean displayUmtu;

	@SerializedName("crmOfferDisplayName")
	private String crmOfferDisplayName;

	@SerializedName("crmOfferDisplayDesc")
	private String crmOfferDisplayDesc;

	@SerializedName("umtuOfferDisplayName")
	private String umtuOfferDisplayName;

	@SerializedName("umtuOfferDisplayMessage")
	private String umtuOfferDisplayMessage;

	@SerializedName("umtuTopUpCode")
	private String umtuTopUpCode;

	@SerializedName("umtuRetailerCommission")
	private String umtuRetailerCommission;

	@SerializedName("ussdOfferDisplayName")
	private String ussdOfferDisplayName;

	@SerializedName("ussdOfferDisplayMessage")
	private String ussdOfferDisplayMessage;

	@SerializedName("hiAppOfferDisplayNameEnglish")
	private String hiAppOfferDisplayNameEnglish;

	@SerializedName("hiAppOfferDisplayNameIndo")
	private String hiAppOfferDisplayNameIndo;

	@SerializedName("hiAppOfferDisplayNameTamil")
	private String hiAppOfferDisplayNameTamil;

	@SerializedName("hiAppOfferDisplayNameMandarin")
	private String hiAppOfferDisplayNameMandarin;

	@SerializedName("hiAppOfferDisplayNameBengali")
	private String hiAppOfferDisplayNameBengali;

	@SerializedName("hiAppOfferDisplayNameBurmese")
	private String hiAppOfferDisplayNameBurmese;

	@SerializedName("displayCta")
	private boolean displayCta;

	@SerializedName("ctaDisplayTextEnglish")
	private String ctaDisplayTextEnglish;

	@SerializedName("ctaDisplayTextIndo")
	private String ctaDisplayTextIndo;

	@SerializedName("ctaDisplayTextTamil")
	private String ctaDisplayTextTamil;

	@SerializedName("ctaDisplayTextMandarin")
	private String ctaDisplayTextMandarin;

	@SerializedName("ctaDisplayTextBengali")
	private String ctaDisplayTextBengali;

	@SerializedName("ctaDisplayTextBurmese")
	private String ctaDisplayTextBurmese;

	@SerializedName("successRedemptionDisplayEnglish")
	private String successRedemptionDisplayEnglish;

	@SerializedName("successRedemptionDisplayIndo")
	private String successRedemptionDisplayIndo;

	@SerializedName("successRedemptionDisplayTamil")
	private String successRedemptionDisplayTamil;

	@SerializedName("successRedemptionDisplayMandarin")
	private String successRedemptionDisplayMandarin;

	@SerializedName("successRedemptionDisplayBengali")
	private String successRedemptionDisplayBengali;

	@SerializedName("successRedemptionDisplayBurmese")
	private String successRedemptionDisplayBurmese;

	@SerializedName("offerDisplayMessageEnglish")
	private String offerDisplayMessageEnglish;

	@SerializedName("offerDisplayMessageIndo")
	private String offerDisplayMessageIndo;

	@SerializedName("offerDisplayMessageTamil")
	private String offerDisplayMessageTamil;

	@SerializedName("offerDisplayMessageMandarin")
	private String offerDisplayMessageMandarin;

	@SerializedName("offerDisplayMessageBengali")
	private String offerDisplayMessageBengali;

	@SerializedName("offerDisplayMessageBurmese")
	private String offerDisplayMessageBurmese;

}
