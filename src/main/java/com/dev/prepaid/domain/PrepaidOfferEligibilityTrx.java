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
@Table(name = "PREPAID_CX_ELIGIBILITY_TX")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidOfferEligibilityTrx extends Auditable{
    @Id
    private Long id;
    private Long invocationId;
    private Long batchId;
    private Boolean isEvaluated;
}
