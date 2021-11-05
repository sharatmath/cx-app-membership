/**
 * 
 */
package com.dev.prepaid.model;

import java.io.Serializable;

import javax.persistence.*;

import com.dev.prepaid.domain.Auditable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Saket
 *
 * 
 */

@Entity
@Table(name = "META_INDICATORS_ADVANCE_FILTER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaIndicatorsAdvanceFilter extends Auditable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "INDICATOR_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String indicatorName;
}
