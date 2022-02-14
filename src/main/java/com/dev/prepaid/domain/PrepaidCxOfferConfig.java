package com.dev.prepaid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * 06-Sept-2021 remove program_name         sprint1
 * 06-Sept-2021 add overall_offer_name      sprint1
 *
 */

@Entity
@Table(name = "PREPAID_CX_OFFER_CONFIG")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferConfig extends Auditable{
    @Id
    @Column(name = "OFFER_CONFIG_ID")
    private String id;
    private String programId;
    private String instanceId;
    private String provisionType;
//    private String programName;

    private String overallOfferName;
    private String deletedBy;
    private Date deletedDate;
    private String offerStatus;
}