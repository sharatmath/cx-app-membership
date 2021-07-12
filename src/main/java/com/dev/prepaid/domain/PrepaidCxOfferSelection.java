package com.dev.prepaid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PREPAID_CX_OFFER_SELECTION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferSelection {
    @Id
    @Column(name = "OFFER_SELECTION_ID")
    private Long id;
    private String offerConfigId;
    private String offerBucketType;
    private String offerId;
    private String offerBucketId;
    private String offerType;
}
