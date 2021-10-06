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
public class PrepaidProvisionedOffer extends Auditable implements Serializable {
    @Id
    @Column(name = "PROVISIONED_OFFER_ID")
    private Long id;
    private Long offerProvisionId;
    private Long offerSelectionId;
    private Date offerDate;

}
