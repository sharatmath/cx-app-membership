package com.dev.prepaid.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="PREPAID_MA_CREDIT_OFFERS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidMaCreditOffer implements Serializable {

    @Id
    private Long id;
    private String productName;
    private String description;
    private Float value;
    private Long validity;
    private Date startDate;
    private Date endDate;
    private String createdBy;
    private Date createdDate;
    private String lastModifiedBy;
    private Date lastModifiedDate;
    private String deletedBy;
    private Date deletedDate;
    private String action;
}
