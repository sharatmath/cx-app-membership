package com.dev.prepaid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PREPAID_OFFER_ELIGIBILITY_TX")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOfferEligibilityTrx extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String invocationId;
    private Long batchId;
    private Long batchSize;
    private Long totalRow;
    private String data;
    private Boolean isEvaluated;
}
