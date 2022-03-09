/**
 * 
 */
package com.dev.prepaid.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name = "META_TABLE_LIST")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllTables {
	@Id
    @Column(name = "T_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String tableName;

}
