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
@Table(name = "PREPAID_CX_OFFER_CONFIG")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferConfig {
    @Id
    @Column(name = "OFFER_CONFIG_ID")
    private String id;
    private String programId;
    private String instanceId;
    private String provisionType;
    private String programName;
}