package com.dev.prepaid.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "STG_WHITELIST")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Whitelist implements Comparable<Whitelist> {
	
//	@Id
//	private String msisdn;
//	private Date timestamp;
//	private String whitelist_type;
//	private Date createdDate;
//	private String createdBy;
//	@Override
//	public int compareTo(Whitelist o) {
//		// TODO Auto-generated method stub
//		return this.getMsisdn().compareTo(o.getMsisdn());
//	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long whitelistId;
	String whitelistType;
	String matchKey;
	String targetOn;
	Date expiryDate;
	String operand;
	String value;
	Date createdDate;
	String createdBy;
	Date modifiedDate;
	String modifiedBy;

	@Override
	public int compareTo(Whitelist whitelist) {
		return 0;
	}
}
