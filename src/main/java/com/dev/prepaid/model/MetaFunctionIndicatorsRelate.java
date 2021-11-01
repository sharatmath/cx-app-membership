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
@Table(name = "META_FUNCTION_INDICATORS_RELATE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaFunctionIndicatorsRelate extends Auditable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "RELATE_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long functionId;
	private Long indicatorId;
}
