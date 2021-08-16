package com.dev.prepaid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="PREPAID_PROVISIONED_OFFER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidProvisionedOffer implements Serializable {
    @Id
    @Column(name = "PROVISIONED_OFFER_ID")
    private Long id;
    private Long offerProvisionId;
    private Long offerId;
    private String name;
    private String description;
    private Long value;
    private String valueUnit;
    private Long valueCap;
    private Long validity;
    private Long valueToDeductFromMa;
    private Date startDate;
    private Date endDate;
    private String action;
    private String counterId;
    private Long counterValue;
    private String thresholdId;
    private Long thresholdValue;
    private String thresholdValueUnit;
    private Integer day;
    private Integer hour;
    private Integer minute;
}
