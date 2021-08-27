package com.dev.prepaid.repository;

import com.dev.prepaid.domain.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, String> {
    @Query(
            value = "SELECT a.promo_id, a.promo_name, 'PROMO' as offer_type \n" +
                    "FROM PREPAID_COUPON_CODE_PROMOS a \n" +
                    "JOIN prepaid_coupon_code b ON b.promo_id = a.promo_id  \n" +
                    "where issued_date is null \n" +
                    "group by a.promo_id, a.promo_name "
//                    +
//                    "union \n" +
//                    "select TO_CHAR(id) id, name from prepaid_da_campaign_offers \n" +
//                    "union \n" +
//                    "select TO_CHAR(id) id, name from prepaid_oms_campaign_offers"
            ,
            nativeQuery = true)
    List<PromoCode> findEligiblePromo();
}
