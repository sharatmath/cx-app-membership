package com.dev.prepaid.domain;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PREPAID_CX_OFFER_SELECTION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferSelection extends Auditable{
    @Id
    @Column(name = "OFFER_SELECTION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String offerConfigId;
    private String offerBucketType; // MA | OMS | DA
    private String offerId; // ma campaign name | OMS campaign Id | DA campaign Id
    private String offerBucketId; 
    private String offerType; // MA | OMS | DA 

    private String smsCampaignName;
    private String promoCodeList;
    private String messageText1;
    private String messageText2;
    private String messageText3;
    private String messageText4;

    @Transient
    private String overallOfferName;
}
