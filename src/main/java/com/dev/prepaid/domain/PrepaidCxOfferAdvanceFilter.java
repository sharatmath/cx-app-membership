
package com.dev.prepaid.domain;

        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import javax.persistence.*;
        import java.util.Date;


@Entity
@Table(name = "PREPAID_CX_OFFER_ADVANCE_FILTER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrepaidCxOfferAdvanceFilter extends Auditable{
    @Id
    @Column(name = "OFFER_ADVANCE_FILTER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String offerConfigId;
    private String payload;
    private String queryText;
    private boolean isCustomQuery;
}