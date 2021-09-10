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
@Table(name = "PREPAID_COUPON_CODE_PROMOS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoCode {
    @Id
    @Column(name = "PROMO_ID")
    String id;
    @Column(name = "PROMO_NAME")
    String name;
    @Column(name = "offerType")
    String offerType;
}
