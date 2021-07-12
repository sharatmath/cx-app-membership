package com.dev.prepaid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "PREPAID_OFFER_MEMBERSHIP_EXCLUS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOfferMembershipExclus {
    @Id
    @Column(name = "OFFER_MEMBERSHIP_EXCL_ID")
    private Long id;
    private Long offerSelectionId;
    private Long msisdn;
    private Date offerDate;
    private Date topupDate;
    private Boolean isControlFlag;
    private Boolean isGcgFlag;
    private String holdoutGroupId;
}